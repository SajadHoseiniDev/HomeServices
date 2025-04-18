package nycto.homeservices.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import nycto.homeservices.base.BaseEntity;
import nycto.homeservices.entity.enums.UserStatus;
import nycto.homeservices.entity.enums.UserType;

import java.time.LocalDateTime;
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
@Getter
@Setter
@ToString
@FieldDefaults( level = AccessLevel.PRIVATE)
public class User extends BaseEntity {

    @Column(nullable = false)
    String firstName;

    @Column(nullable = false)
    String lastName;

    @Column(nullable = false, unique = true)
    String email;

    @Column(nullable = false)
    String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    UserStatus status;

    @Column(nullable = false)
    LocalDateTime registrationDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType userType;


}
