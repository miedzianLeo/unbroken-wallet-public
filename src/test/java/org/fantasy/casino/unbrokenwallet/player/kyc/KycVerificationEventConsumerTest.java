package org.fantasy.casino.unbrokenwallet.player.kyc;


import org.fantasy.casino.unbrokenwallet.player.Player;
import org.fantasy.casino.unbrokenwallet.player.PlayerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Optional;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(
        properties = {
                "spring.kafka.consumer.auto-offset-reset=latest",
                "spring.datasource.url=jdbc:h2:mem:testdb",
        }
)
@Testcontainers
class KycVerificationEventConsumerTest {

    @Container
    static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.3.3")
    );

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    private KafkaTemplate<String, KycVerificationEvent> kafkaTemplate;

    @Autowired
    private PlayerRepository playerRepository;

    @BeforeEach
    void setUp() {
        var player = new Player();
        player.setUUID("1234:1234");
        player.setKycVerified(false);
        player.setFirstName("John");
        player.setLastName("Paul");
        player.setAccount(null);
        playerRepository.saveAndFlush(player);
    }

    @AfterEach
    void tearDown() {
        playerRepository.deleteAll();
    }

    @Test
    void shouldUpdatePlayerKycVerification() {
        KycVerificationEvent event = new KycVerificationEvent(
                "1234:1234",
                true
        );

        kafkaTemplate.send("unbroken-wallet.pub.kyc-verification", "1", event);

        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    Optional<Player> player = playerRepository.findByUuid(
                            "1234:1234"
                    );
                    assertEquals(true, player.get().getKycVerified());
                });
    }

}
