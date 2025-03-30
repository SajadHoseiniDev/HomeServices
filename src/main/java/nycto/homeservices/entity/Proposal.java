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
@Table(name = "proposals")
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Proposal extends BaseEntity {

    @Column(nullable = false)
    LocalDateTime proposalDate;

    @Column(nullable = false)
    Long proposedPrice;

    @Column(nullable = false)
    String duration;

    @Column(nullable = false)
    LocalDateTime startTime;



    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    Order order ;

    @ManyToOne
    @JoinColumn(name = "specialist_id", nullable = false)
    Specialist specialist;
}