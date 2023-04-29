package it.mtempobono.mechanicalappointment.model.entity;

import it.mtempobono.mechanicalappointment.util.converters.DurationConverter;
import lombok.*;

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

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "internal_duration")
    @Convert(converter = DurationConverter.class)
    private Duration internalDuration;

    @Column(name = "external_duration")
    @Convert(converter = DurationConverter.class)
    private Duration externalDuration;


}
