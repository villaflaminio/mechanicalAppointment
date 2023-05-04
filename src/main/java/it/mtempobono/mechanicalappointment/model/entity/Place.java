package it.mtempobono.mechanicalappointment.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "places")
public class Place {
    //region Fields
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
    //endregion

    //region Constructors

    public Place() {
    }

    public Place(Long id, Integer istat, String municipality, String province, String region, List<Garage> garage) {
        this.id = id;
        this.istat = istat;
        this.municipality = municipality;
        this.province = province;
        this.region = region;
        this.garage = garage;
    }
    //endregion

    //region Getters & Setters methods

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIstat() {
        return istat;
    }

    public void setIstat(Integer istat) {
        this.istat = istat;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public List<Garage> getGarage() {
        return garage;
    }

    public void setGarage(List<Garage> garage) {
        this.garage = garage;
    }

    //endregion
}
