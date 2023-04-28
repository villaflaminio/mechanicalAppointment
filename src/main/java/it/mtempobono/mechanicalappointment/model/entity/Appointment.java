package it.mtempobono.mechanicalappointment.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import it.mtempobono.mechanicalappointment.util.converters.DayPlanConverter;
import it.mtempobono.mechanicalappointment.model.TimePeriod;
import it.mtempobono.mechanicalappointment.util.converters.TimePeriodConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    @Column(name = "external_time")
    @Convert(converter = TimePeriodConverter.class)
    private TimePeriod externalTime;

    @Column(name = "internal_time")
    @Convert(converter = TimePeriodConverter.class)
    private TimePeriod internalTime;

    @Column(name = "price")
    private Double price;

    @Column(name = "id_calendar_event")
    private String idCalendarEvent;


    //get start time from internal time
    public LocalTime getStartTime() {
        return internalTime.getStart().getLocalTime();
    }



    @Override
    public int compareTo(Appointment o) {
        return this.getStartTime().compareTo(o.getStartTime());
    }
}
