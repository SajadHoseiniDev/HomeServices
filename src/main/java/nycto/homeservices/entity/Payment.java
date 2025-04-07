package nycto.homeservices.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import nycto.homeservices.base.BaseEntity;
import nycto.homeservices.entity.enums.PaymentMethod;
import nycto.homeservices.entity.enums.PaymentStatus;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Payment extends BaseEntity {

    @Column(nullable = false)
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(unique = true)
    private String transactionId;

    @Column(nullable = false)
    private LocalDateTime paymentTime = LocalDateTime.now();
}