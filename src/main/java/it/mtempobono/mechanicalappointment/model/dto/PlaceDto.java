package it.mtempobono.mechanicalappointment.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link it.mtempobono.mechanicalappointment.model.entity.Place} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceDto implements Serializable {
    private  Long id;

    @Schema(description = "The ISTAT code of the place", example = "001001", required = true)
    private  Integer istat;

    @Schema(description = "The municipality name of the place", example = "Roma", required = true)
    private  String municipality;

    @Schema(description = "The province initials of the place", example = "RM", required = true)
    private  String province;

    @Schema(description = "The region of the place", example = "Lazio", required = true)
    private  String region;
}