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
@Table(name = "specialists")
@Getter
@Setter
@ToString

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Specialist extends User {
    @Column
    int rating;

    @Column(nullable = false)
    String profilePicUrl;

    @OneToMany(mappedBy = "specialist",fetch = FetchType.LAZY)
    List<SpecialistCredit> specialistCredits = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "specialist_service",
            joinColumns = @JoinColumn(name = "specialist_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    List<Service> services = new ArrayList<>();


    @OneToMany(mappedBy = "specialist",fetch = FetchType.LAZY)
    List<Proposal > proposals = new ArrayList<>();


}
