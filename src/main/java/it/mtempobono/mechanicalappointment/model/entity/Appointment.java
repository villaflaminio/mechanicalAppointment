package it.mtempobono.mechanicalappointment.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import it.mtempobono.mechanicalappointment.model.TimePeriod;
import it.mtempobono.mechanicalappointment.util.converters.TimePeriodConverter;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "appointment")
public class Appointment implements Comparable<Appointment> {

    //region Constructors
    public Appointment() {

    }

    public Appointment(Long id, OpenDay openDay, MechanicalAction mechanicalAction, String comment, Vehicle vehicle, AppointmentStatus status, TimePeriod externalTime, TimePeriod internalTime, Double price, String idCalendarEvent, List<Vote> votes, Boolean isMechanicalActionCustom) {
        this.id = id;
        this.openDay = openDay;
        this.mechanicalAction = mechanicalAction;
        this.comment = comment;
        this.vehicle = vehicle;
        this.status = status;
        this.externalTime = externalTime;
        this.internalTime = internalTime;
        this.price = price;
        this.idCalendarEvent = idCalendarEvent;
        this.votes = votes;
        this.isMechanicalActionCustom = isMechanicalActionCustom;
    }
    //endregion

    //region Fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    private OpenDay openDay;

    @ManyToOne
    private MechanicalAction mechanicalAction;

    @Column(name = "comment")
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

    @OneToMany(mappedBy = "appointment")
    @JsonManagedReference(value="appointment-vote")
    private List<Vote> votes;

    private Boolean isMechanicalActionCustom;


    //get start time from internal time
    public LocalTime getStartTime() {
        return internalTime.getStart().getLocalTime();
    }

    @Override
    public int compareTo(Appointment o) {
        return this.getStartTime().compareTo(o.getStartTime());
    }
    //endregion

    //region Getter and Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OpenDay getOpenDay() {
        return openDay;
    }

    public void setOpenDay(OpenDay openDay) {
        this.openDay = openDay;
    }

    public MechanicalAction getMechanicalAction() {
        return mechanicalAction;
    }

    public void setMechanicalAction(MechanicalAction mechanicalAction) {
        this.mechanicalAction = mechanicalAction;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public TimePeriod getExternalTime() {
        return externalTime;
    }

    public void setExternalTime(TimePeriod externalTime) {
        this.externalTime = externalTime;
    }

    public TimePeriod getInternalTime() {
        return internalTime;
    }

    public void setInternalTime(TimePeriod internalTime) {
        this.internalTime = internalTime;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getIdCalendarEvent() {
        return idCalendarEvent;
    }

    public void setIdCalendarEvent(String idCalendarEvent) {
        this.idCalendarEvent = idCalendarEvent;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public Boolean getIsMechanicalActionCustom() {
        return isMechanicalActionCustom;
    }

    public void setIsMechanicalActionCustom(Boolean mechanicalActionCustom) {
        isMechanicalActionCustom = mechanicalActionCustom;
    }
    //endregion
}
