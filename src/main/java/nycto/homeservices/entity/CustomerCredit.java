package nycto.homeservices.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import nycto.homeservices.base.BaseEntity;


@Entity
@Getter
@Setter
@ToString

@FieldDefaults(level = AccessLevel.PRIVATE)


public class CustomerCredit extends BaseEntity {

    @Column(nullable = false)
    long amount;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    Customer customer;
}
