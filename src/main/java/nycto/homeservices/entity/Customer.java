package nycto.homeservices.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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


    @OneToMany(mappedBy ="customer" )
    List<CustomerCredit> credits = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    List<Comment> comments = new ArrayList<>();


}
