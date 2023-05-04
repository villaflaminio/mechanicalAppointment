package it.mtempobono.mechanicalappointment.model.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "garages")
public class Garage {
    //region Fields
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
    //endregion

    //region Constructors
    public Garage() {

    }

    public Garage(Long id, String name, Place place, String address, String cap, String linkGoogleMaps, Double latitude, Double longitude, String phone, String email, String website, String logo, List<OpenDay> openDay, List<Vote> votes) {
        this.id = id;
        this.name = name;
        this.place = place;
        this.address = address;
        this.cap = cap;
        this.linkGoogleMaps = linkGoogleMaps;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.logo = logo;
        this.openDay = openDay;
        this.votes = votes;
    }

    // endregion

    //region Geters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getLinkGoogleMaps() {
        return linkGoogleMaps;
    }

    public void setLinkGoogleMaps(String linkGoogleMaps) {
        this.linkGoogleMaps = linkGoogleMaps;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<OpenDay> getOpenDay() {
        return openDay;
    }

    public void setOpenDay(List<OpenDay> openDay) {
        this.openDay = openDay;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    //endregion
}

