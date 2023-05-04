package it.mtempobono.mechanicalappointment.model.builder;

import it.mtempobono.mechanicalappointment.model.entity.Garage;
import it.mtempobono.mechanicalappointment.model.entity.OpenDay;
import it.mtempobono.mechanicalappointment.model.entity.Place;

import java.util.List;

public final class GarageBuilder {
    private Long id;
    private String name;
    private Place place;
    private String address;
    private String cap;
    private String linkGoogleMaps;
    private Double latitude;
    private Double longitude;
    private String phone;
    private String email;
    private String website;
    private String logo;
    private List<OpenDay> openDay;

    private GarageBuilder() {
    }

    public static GarageBuilder aGarage() {
        return new GarageBuilder();
    }

    public GarageBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public GarageBuilder name(String name) {
        this.name = name;
        return this;
    }

    public GarageBuilder place(Place place) {
        this.place = place;
        return this;
    }

    public GarageBuilder address(String address) {
        this.address = address;
        return this;
    }

    public GarageBuilder cap(String cap) {
        this.cap = cap;
        return this;
    }

    public GarageBuilder linkGoogleMaps(String linkGoogleMaps) {
        this.linkGoogleMaps = linkGoogleMaps;
        return this;
    }

    public GarageBuilder latitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public GarageBuilder longitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public GarageBuilder phone(String phone) {
        this.phone = phone;
        return this;
    }

    public GarageBuilder email(String email) {
        this.email = email;
        return this;
    }

    public GarageBuilder website(String website) {
        this.website = website;
        return this;
    }

    public GarageBuilder logo(String logo) {
        this.logo = logo;
        return this;
    }

    public GarageBuilder openDay(List<OpenDay> openDay) {
        this.openDay = openDay;
        return this;
    }

    public Garage build() {
        Garage garage = new Garage();
        garage.setId(id);
        garage.setName(name);
        garage.setPlace(place);
        garage.setAddress(address);
        garage.setCap(cap);
        garage.setLinkGoogleMaps(linkGoogleMaps);
        garage.setLatitude(latitude);
        garage.setLongitude(longitude);
        garage.setPhone(phone);
        garage.setEmail(email);
        garage.setWebsite(website);
        garage.setLogo(logo);
        garage.setOpenDay(openDay);
        return garage;
    }
}
