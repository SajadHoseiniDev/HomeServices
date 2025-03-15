package nycto.homeservices.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import nycto.homeservices.base.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Proposal extends BaseEntity {

    @Column(nullable = false)
    Long proposedPrice;

    @Column(nullable = false)
    String duration;

    @Column(nullable = false)
    LocalDateTime startTime;
}