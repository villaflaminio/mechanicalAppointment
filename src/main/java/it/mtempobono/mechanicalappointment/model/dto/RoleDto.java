package it.mtempobono.mechanicalappointment.model.dto;

import lombok.*;

import java.io.Serializable;

/**
 * A DTO for the {@link it.mtempobono.mechanicalappointment.model.entity.Role} entity
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto implements Serializable {
    private Long id;
    private String name;
}