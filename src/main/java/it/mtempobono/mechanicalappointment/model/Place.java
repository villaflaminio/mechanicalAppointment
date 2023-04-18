package it.mtempobono.mechanicalappointment.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    private Long istat;
    private String municipality;
    private String province;
    private String region;

    //join to garage
    @OneToMany(mappedBy = "place")
    @JsonBackReference
    private List<Garage> garage;


}
