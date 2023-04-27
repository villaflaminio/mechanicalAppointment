package it.mtempobono.mechanicalappointment.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import it.mtempobono.mechanicalappointment.model.entity.Garage;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import java.io.Serializable;

/**
 * A DTO for the {@link Garage} entity
 */
@Data
@Builder
public class GarageDto implements Serializable {

    private final Long id;

    @Schema(description = "The garage name", example = "APs Garage", required = true)
    private final String name;

    @Schema(description = "The linked Place", required = true)
    private final PlaceDto place;

    @Schema(description = "The address of the garage", example = "Strada Giacomo Leopardi, 7", required = true)
    private final String address;

    @Schema(description = "The CAP of the garage address", example = "00159", required = true)
    private final String cap;

    @Schema(description = "Link to Google Maps position", example = "https://www.google.com/maps/d/u/0/viewer?mid=1aGnEr9JQwyMqLL6twqbHYllvzBw&hl=it&ll=41.889890999999984%2C12.491658999999986&z=17", required = true)
    private final String linkGoogleMaps;

    @Schema(description = "The latitude of the garage", example = "41.889890999999984", required = true)
    private final Double latitude;

    @Schema(description = "The longitude of the garage", example = "12.491658999999986", required = true)
    private final Double longitude;

    @Schema(description = "The phone number of the garage", example = "06 4546 4545", required = true)
    private final String phone;

    @Email
    @Schema(description = "The email of the garage", example = "ap@gmail.com", required = true)
    private final String email;

    @Schema(description = "The website of the garage", example = "https://www.apgarage.com", required = false)
    private final String website;

    @Schema(description = "The logo of the garage", example = "https://www.apgarage.com/logo.png", required = false)
    private final String logo;
}