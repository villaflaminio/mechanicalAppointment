package it.mtempobono.mechanicalappointment.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Duration;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mechanical_actions")
public class MechanicalAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

//    @Column(name = "internal_duration")
//    private Integer internalDuration;
//
//    @Column(name = "external_duration")
//    private Integer externalDuration;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "internal_duration")
    Duration internalDuration ;

    @Column(name = "external_duration")
    Duration externalDuration;
}
