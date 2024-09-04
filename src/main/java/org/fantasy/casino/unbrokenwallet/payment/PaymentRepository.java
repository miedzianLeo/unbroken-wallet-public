package org.fantasy.casino.unbrokenwallet.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByUuid(String uid);
    List<Payment> findByPlayerUuid(String playerUuid);
}
