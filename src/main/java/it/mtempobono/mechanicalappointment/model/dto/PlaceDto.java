package it.mtempobono.mechanicalappointment.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * A DTO for the {@link it.mtempobono.mechanicalappointment.model.entity.Place} entity
 */
public class PlaceDto implements Serializable {
    //region Fields
    private  Long id;

    @Schema(description = "The ISTAT code of the place", example = "001001", required = true)
    private  Integer istat;

    @Schema(description = "The municipality name of the place", example = "Roma", required = true)
    private  String municipality;

    @Schema(description = "The province initials of the place", example = "RM", required = true)
    private  String province;

    @Schema(description = "The region of the place", example = "Lazio", required = true)
    private  String region;
    //endregion

    //region Constructors
    public PlaceDto() {
    }

    public PlaceDto(Long id, Integer istat, String municipality, String province, String region) {
        this.id = id;
        this.istat = istat;
        this.municipality = municipality;
        this.province = province;
        this.region = region;
    }
    //endregion

    //region Getters and Setters

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
    //endregion
}