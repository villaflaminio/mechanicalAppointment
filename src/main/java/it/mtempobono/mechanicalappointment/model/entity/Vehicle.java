package it.mtempobono.mechanicalappointment.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plate")
    private String plate;

    @Column(name = "model")
    private String model;

    @Column(name = "brand")
    private String brand;

    @Column(name = "year_car")
    private Integer year;

    @Column(name = "fuel")
    private String fuel;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "vehicle")
    @JsonBackReference
    private List<Appointment> reservation;

}
