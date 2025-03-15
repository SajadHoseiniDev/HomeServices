package nycto.homeservices.entity;

import jakarta.persistence.*;
import lombok.experimental.FieldDefaults;
import nycto.homeservices.base.BaseEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity

@Getter
@Setter
@ToString

@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubService extends BaseEntity {

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    Long basePrice;

    @Column(nullable = false)
    String description;


    @ManyToOne
    @JoinColumn(name = "service_id",nullable = false)
    Service service;

    @OneToMany(mappedBy = "subService",fetch = FetchType.LAZY)
    List<Order> orders = new ArrayList<>();

}
