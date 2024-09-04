package org.fantasy.casino.unbrokenwallet.account;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fantasy.casino.unbrokenwallet.common.BaseEntity;
import org.fantasy.casino.unbrokenwallet.common.Currency;
import org.fantasy.casino.unbrokenwallet.payment.Payment;
import org.fantasy.casino.unbrokenwallet.player.Player;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "account")
@NoArgsConstructor
@Getter
@Setter
public class Account extends BaseEntity {

    private Double balance;

    @Enumerated
    private Currency currency;

    @OneToMany(mappedBy = "account")
    private Set<Payment> payments;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false, updatable = false)
    private Player player;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
