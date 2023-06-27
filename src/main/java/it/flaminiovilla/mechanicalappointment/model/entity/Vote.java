package it.flaminiovilla.mechanicalappointment.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "votes")
public class Vote {

    //region Fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vote")
    private Integer rating;

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JsonBackReference(value="user-vote")
    private User user;

    @ManyToOne
    @JsonBackReference(value="garage-vote")
    private Garage garage;

    @ManyToOne
    @JsonBackReference(value="appointment-vote")
    private Appointment appointment;
    //endregion

    //region Constructors
    public Vote() {
    }

    public Vote(Long id, Integer rating, String comment, User user, Garage garage, Appointment appointment) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.user = user;
        this.garage = garage;
        this.appointment = appointment;
    }
    //endregion

    //region Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Garage getGarage() {
        return garage;
    }

    public void setGarage(Garage garage) {
        this.garage = garage;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    //endregion
}
