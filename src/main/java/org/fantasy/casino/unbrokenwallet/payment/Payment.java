package org.fantasy.casino.unbrokenwallet.payment;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fantasy.casino.unbrokenwallet.account.Account;
import org.fantasy.casino.unbrokenwallet.common.BaseEntity;
import org.fantasy.casino.unbrokenwallet.common.Currency;
import org.fantasy.casino.unbrokenwallet.player.Player;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@NoArgsConstructor
@Getter
@Setter
public class Payment extends BaseEntity {

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private PaymentState state = PaymentState.INITIATED;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id", nullable = false, updatable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false, updatable = false)
    private Player player;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Payment(BigDecimal amount, Currency currency, PaymentType paymentType, Player player, Account account) {
        this.amount = amount;
        this.currency = currency;
        this.paymentType = paymentType;
        this.player = player;
        this.account = account;
    }
}
