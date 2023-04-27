package it.mtempobono.mechanicalappointment.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link it.mtempobono.mechanicalappointment.model.entity.Place} entity
 */
@Data
public class PlaceDto implements Serializable {
    private final Long id;

    @Schema(description = "The ISTAT code of the place", example = "001001", required = true)
    private final Integer istat;

    @Schema(description = "The municipality name of the place", example = "Roma", required = true)
    private final String municipality;

    @Schema(description = "The province initials of the place", example = "RM", required = true)
    private final String province;

    @Schema(description = "The region of the place", example = "Lazio", required = true)
    private final String region;
}