package nycto.homeservices.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import nycto.homeservices.base.BaseEntity;

@Entity
@Table(name = "comments")

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment extends BaseEntity {

    @Column(nullable = false)
    Double rating;

    @Column
    String comment;

    @ManyToOne
            @JoinColumn(name = "customer_id", nullable = false)
    Customer customer;

    @ManyToOne
            @JoinColumn(name = "order_id", nullable = false)

    Order order;
}