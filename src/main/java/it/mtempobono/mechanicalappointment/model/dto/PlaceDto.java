package it.mtempobono.mechanicalappointment.model.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * A DTO for the {@link it.mtempobono.mechanicalappointment.model.entity.Place} entity
 */
@Data
@Builder
public class PlaceDto implements Serializable {
    private final Long id;
    private final Long istat;
    private final String municipality;
    private final String province;
    private final String region;
    private final List<GarageDto> garage;
}