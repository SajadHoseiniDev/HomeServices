package nycto.homeservices.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")

@Getter
@Setter
@ToString

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer extends User{


    @OneToMany(mappedBy ="customer",fetch = FetchType.LAZY )
    List<CustomerCredit> credits = new ArrayList<>();

    @OneToMany(mappedBy = "customer",fetch = FetchType.LAZY)
    List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "customer",fetch = FetchType.LAZY)
    List<Comment> comments = new ArrayList<>();


}
