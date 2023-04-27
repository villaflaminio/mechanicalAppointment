package it.mtempobono.mechanicalappointment.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "places")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column (name = "istat")
    private Integer istat;

    @Column (name = "municipality")
    private String municipality;

    @Column (name = "province")
    private String province;

    @Column (name = "region")
    private String region;

    //join to garage
    @OneToMany(mappedBy = "place")
    @JsonBackReference
    private List<Garage> garage;
}
