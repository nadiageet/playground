package com.example.playground.gift.model;


import com.example.playground.user.model.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Gift {

    @Id
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private GiftType type;

    private boolean isUsed = false;


    @CreatedDate
    private Instant createdAt;

    private Instant usedAt;


    public void use(Instant useInstant) {
        if (!isUsed) {
            this.isUsed = true;
            this.usedAt = useInstant;
        }
    }

}
