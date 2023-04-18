package it.mtempobono.mechanicalappointment.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    private OpenDay openDay;

    @ManyToOne
    private MechanicalAction mechanicalAction;

    @Column(name = "start_time")
    private String startTime;

    @ManyToOne
    @JsonManagedReference
    private Vehicle vehicle;


}
