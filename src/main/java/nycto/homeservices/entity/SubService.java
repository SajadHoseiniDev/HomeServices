package nycto.homeservices.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.experimental.FieldDefaults;
import nycto.homeservices.base.BaseEntity;
import lombok.*;

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

}
