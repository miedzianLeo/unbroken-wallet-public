package org.fantasy.casino.unbrokenwallet.common;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
@Getter
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String uuid = UUID.randomUUID().toString();

    @Override
    public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() && Objects.equals(uuid, ((BaseEntity) o).uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
