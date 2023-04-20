package it.mtempobono.mechanicalappointment.model.dto;

import it.mtempobono.mechanicalappointment.model.entity.Garage;
import lombok.*;

import java.io.Serializable;

/**
 * A DTO for the {@link it.mtempobono.mechanicalappointment.model.entity.Garage} entity
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GarageDto implements Serializable {
    private Long id;
    private String name;
    private String address;
    private String cap;
    private String linkGoogleMaps;
    private Double latitude;
    private Double longitude;
    private String phone;
    private String email;
    private String website;
    private String logo;

    public GarageDto(Garage garage) {
        this.id = garage.getId();
        this.name = garage.getName();
        this.address = garage.getAddress();
        this.cap = garage.getCap();
        this.linkGoogleMaps = garage.getLinkGoogleMaps();
        this.latitude = garage.getLatitude();
        this.longitude = garage.getLongitude();
        this.phone = garage.getPhone();
        this.email = garage.getEmail();
        this.website = garage.getWebsite();
        this.logo = garage.getLogo();
    }
}