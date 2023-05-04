package it.mtempobono.mechanicalappointment.model.builder;

import it.mtempobono.mechanicalappointment.model.entity.Appointment;
import it.mtempobono.mechanicalappointment.model.entity.Garage;
import it.mtempobono.mechanicalappointment.model.entity.User;
import it.mtempobono.mechanicalappointment.model.entity.Vote;

public final class VoteBuilder {
    private Long id;
    private Integer rating;
    private String comment;
    private User user;
    private Garage garage;
    private Appointment appointment;

    private VoteBuilder() {
    }

    public static VoteBuilder aVote() {
        return new VoteBuilder();
    }

    public VoteBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public VoteBuilder rating(Integer rating) {
        this.rating = rating;
        return this;
    }

    public VoteBuilder comment(String comment) {
        this.comment = comment;
        return this;
    }

    public VoteBuilder user(User user) {
        this.user = user;
        return this;
    }

    public VoteBuilder garage(Garage garage) {
        this.garage = garage;
        return this;
    }

    public VoteBuilder appointment(Appointment appointment) {
        this.appointment = appointment;
        return this;
    }

    public Vote build() {
        Vote vote = new Vote();
        vote.setId(id);
        vote.setRating(rating);
        vote.setComment(comment);
        vote.setUser(user);
        vote.setGarage(garage);
        vote.setAppointment(appointment);
        return vote;
    }
}
