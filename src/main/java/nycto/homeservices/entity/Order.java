package nycto.homeservices.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import nycto.homeservices.base.BaseEntity;
import nycto.homeservices.entity.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")

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

    @OneToMany(mappedBy ="order" )
    List<Proposal> proposals =new ArrayList<>();


    @ManyToOne
            @JoinColumn(name="customer_id", nullable=false)
    Customer customer;


    @ManyToOne
    @JoinColumn(name="sub_service_id", nullable=false)
    SubService subService;


}
