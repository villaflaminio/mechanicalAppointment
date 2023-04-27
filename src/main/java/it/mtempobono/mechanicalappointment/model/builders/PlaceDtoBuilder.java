package it.mtempobono.mechanicalappointment.model.builders;

import it.mtempobono.mechanicalappointment.model.dto.PlaceDto;

public final class PlaceDtoBuilder {
    private Long id;
    private Integer istat;
    private String municipality;
    private String province;
    private String region;

    private PlaceDtoBuilder() {
    }

    public static PlaceDtoBuilder aPlaceDto() {
        return new PlaceDtoBuilder();
    }

    public PlaceDtoBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public PlaceDtoBuilder istat(Integer istat) {
        this.istat = istat;
        return this;
    }

    public PlaceDtoBuilder municipality(String municipality) {
        this.municipality = municipality;
        return this;
    }

    public PlaceDtoBuilder province(String province) {
        this.province = province;
        return this;
    }

    public PlaceDtoBuilder region(String region) {
        this.region = region;
        return this;
    }

    public PlaceDto build() {
        return new PlaceDto(id, istat, municipality, province, region);
    }
}
