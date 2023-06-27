package it.flaminiovilla.mechanicalappointment.model.builder;

import it.flaminiovilla.mechanicalappointment.model.entity.Garage;
import it.flaminiovilla.mechanicalappointment.model.entity.Place;

import java.util.List;

public final class PlaceBuilder {
    private Long id;
    private Integer istat;
    private String municipality;
    private String province;
    private String region;
    private List<Garage> garage;

    private PlaceBuilder() {
    }
    public static PlaceBuilder aPlace() {
        return new PlaceBuilder();
    }

    public PlaceBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public PlaceBuilder istat(Integer istat) {
        this.istat = istat;
        return this;
    }

    public PlaceBuilder municipality(String municipality) {
        this.municipality = municipality;
        return this;
    }

    public PlaceBuilder province(String province) {
        this.province = province;
        return this;
    }

    public PlaceBuilder region(String region) {
        this.region = region;
        return this;
    }

    public PlaceBuilder garage(List<Garage> garage) {
        this.garage = garage;
        return this;
    }

    public Place build() {
        Place place = new Place();
        place.setId(id);
        place.setIstat(istat);
        place.setMunicipality(municipality);
        place.setProvince(province);
        place.setRegion(region);
        place.setGarage(garage);
        return place;
    }
}
