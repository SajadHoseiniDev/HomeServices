package nycto.homeservices.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import nycto.homeservices.base.BaseEntity;

import java.time.LocalDateTime;
@Entity
@Inheritance(strategy = InheritanceType.JOINED)

@Getter
@Setter

@FieldDefaults( level = AccessLevel.PRIVATE)
public class User extends BaseEntity {

    @Column(nullable = false)
    String firstName;

    @Column(nullable = false)
    String lastName;

    @Column(nullable = false)
    String email;

    @Column(nullable = false)
    String password;

    @Column(nullable = false)
    String status;

    @Column(nullable = false)
    LocalDateTime registrationDate;



}
