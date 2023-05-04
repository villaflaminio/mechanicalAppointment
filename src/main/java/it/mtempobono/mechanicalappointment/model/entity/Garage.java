package it.mtempobono.mechanicalappointment.model.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "garages")
public class Garage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name="place_id")
    private Place place;

    @Column(name = "address")
    private String address;

    @Column(name = "cap")
    private String cap;

    @Column(name = "link_google_maps")
    private String linkGoogleMaps;

    @Column (name = "latitude")
    private Double latitude;

    @Column (name = "longitude")
    private Double longitude;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "website")
    private String website;

    @Column(name = "logo")
    private String logo;

    @OneToMany(mappedBy = "garage")
    private List<OpenDay> openDay;

    @OneToMany(mappedBy = "garage")
    @JsonManagedReference(value="garage-vote")
    private List<Vote> votes;

}
