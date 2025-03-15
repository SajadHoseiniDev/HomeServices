package nycto.homeservices.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import nycto.homeservices.base.BaseEntity;
import nycto.homeservices.entity.enums.OrderStatus;

import java.time.LocalDateTime;

@Entity

@Getter
@Setter
@ToString

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order extends BaseEntity {

    @Column
    Long proposedPrice;

    @Column
    String description;

    @Column(nullable = false)
    LocalDateTime orderDate;

    @Column
    LocalDateTime executionDate;

    @Column(nullable = false)
    String address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    OrderStatus status;


}
