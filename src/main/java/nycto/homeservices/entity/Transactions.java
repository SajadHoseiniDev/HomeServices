package nycto.homeservices.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import nycto.homeservices.base.BaseEntity;

import java.time.LocalDateTime;
@Entity
@Table(name = "transactions")
@Getter
@Setter
public class Transactions extends BaseEntity {

    @Column
    private Long creditAdded;

    @Column
    private Long creditDeducted;

    @Column(nullable = false)
    private LocalDateTime transactionDate;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "specialist_id")
    private Specialist specialist;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column
    private String description;
}
