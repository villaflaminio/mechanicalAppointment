package it.mtempobono.mechanicalappointment.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "votes")
public class Vote {

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

}
