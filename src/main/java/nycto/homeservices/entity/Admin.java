package nycto.homeservices.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "admin")
@Getter
@Setter
@ToString

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Admin extends User {

}
