package it.mtempobono.mechanicalappointment.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import it.mtempobono.mechanicalappointment.model.JpaConverterJson;
import it.mtempobono.mechanicalappointment.model.TimePeriod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointment")
public class Appointment implements Comparable<Appointment> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    private OpenDay openDay;

    @ManyToOne
    private MechanicalAction mechanicalAction;

    private String comment;

    @ManyToOne
    @JsonManagedReference
    private Vehicle vehicle;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AppointmentStatus status;

    @Convert(converter = JpaConverterJson.class)
    private TimePeriod externalTime;

    @Convert(converter = JpaConverterJson.class)
    private TimePeriod internalTime;


    //get start time from internal time
    public LocalTime getStartTime() {
        return internalTime.getStart();
    }



    @Override
    public int compareTo(Appointment o) {
        return this.getStartTime().compareTo(o.getStartTime());
    }
}
