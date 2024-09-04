package org.fantasy.casino.unbrokenwallet.player;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fantasy.casino.unbrokenwallet.account.Account;
import org.fantasy.casino.unbrokenwallet.common.BaseEntity;

@Entity
@Table(name = "player")
@NoArgsConstructor
@Getter
@Setter
public class Player extends BaseEntity {

    private String firstName;

    private String lastName;

    private Boolean kycVerified;

    @OneToOne(mappedBy = "player", fetch = FetchType.EAGER)
    private Account account;

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }
}
