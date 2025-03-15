package nycto.homeservices.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import nycto.homeservices.base.BaseEntity;

import java.time.LocalDateTime;
@Entity
@Inheritance(strategy = InheritanceType.JOINED)

@Getter
@Setter
@ToString
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
    @Enumerated(EnumType.STRING)
    String status;

    @Column(nullable = false)
    LocalDateTime registrationDate;



}
