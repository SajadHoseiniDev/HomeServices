package nycto.homeservices.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Entity

@Getter
@Setter
@ToString

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer extends User{

}
