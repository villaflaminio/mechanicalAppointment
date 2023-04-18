package it.mtempobono.mechanicalappointment.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import it.mtempobono.mechanicalappointment.model.DayPlan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "open_days")
public class OpenDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JsonBackReference
    private Garage garage;

    @Column(name = "date")
    private LocalDate date;

    private Integer maxParallelAppointments;

    @OneToMany(mappedBy = "openDay")
    private List<Reservation> reservation;

    @Type(type = "json")
    @Column(columnDefinition = "json", name = "workPlan")
    private DayPlan workPlan;

}
