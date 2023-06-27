package it.flaminiovilla.mechanicalappointment.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import it.flaminiovilla.mechanicalappointment.model.entity.Garage;

import javax.validation.constraints.Email;
import java.io.Serializable;

/**
 * A DTO for the {@link Garage} entity
 */

public class GarageDto implements Serializable {

    //region Fields
    @Schema(description = "The garage name", example = "APs Garage", required = true)
    private String name;

    @Schema(description = "The linked Place", example = "5908", required = true)
    private Long placeId;

    @Schema(description = "The address of the garage", example = "Strada Giacomo Leopardi, 7", required = true)
    private String address;

    @Schema(description = "The CAP of the garage address", example = "00159", required = true)
    private String cap;

    @Schema(description = "Link to Google Maps position", example = "https://www.google.com/maps/d/u/0/viewer?mid=1aGnEr9JQwyMqLL6twqbHYllvzBw&hl=it&ll=41.889890999999984%2C12.491658999999986&z=17", required = true)
    private String linkGoogleMaps;

    @Schema(description = "The latitude of the garage", example = "41.889890999999984", required = true)
    private Double latitude;

    @Schema(description = "The longitude of the garage", example = "12.491658999999986", required = true)
    private Double longitude;

    @Schema(description = "The phone number of the garage", example = "06 4546 4545", required = true)
    private String phone;

    @Email
    @Schema(description = "The email of the garage", example = "ap@gmail.com", required = true)
    private String email;

    @Schema(description = "The website of the garage", example = "https://www.apgarage.com", required = false)
    private String website;

    @Schema(description = "The logo of the garage", example = "https://www.apgarage.com/logo.png", required = false)
    private String logo;
    //endregion

    // region Constructors
    public GarageDto() {
    }

    public GarageDto(String name, Long placeId, String address, String cap, String linkGoogleMaps, Double latitude, Double longitude, String phone, String email, String website, String logo) {
        this.name = name;
        this.placeId = placeId;
        this.address = address;
        this.cap = cap;
        this.linkGoogleMaps = linkGoogleMaps;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.logo = logo;
    }
    // endregion

    //region Getters & Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
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

    //endregion
}