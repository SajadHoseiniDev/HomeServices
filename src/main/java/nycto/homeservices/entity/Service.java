package nycto.homeservices.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import nycto.homeservices.base.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "services")

@Getter
@Setter
@ToString

@FieldDefaults(level = AccessLevel.PRIVATE)

public class Service extends BaseEntity {

    @Column(nullable = false)
    String name;

    @ManyToMany(mappedBy = "services")
    List<Specialist> specialists = new ArrayList<>();
}
