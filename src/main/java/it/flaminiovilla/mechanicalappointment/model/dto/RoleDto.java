package it.flaminiovilla.mechanicalappointment.model.dto;

import it.flaminiovilla.mechanicalappointment.model.entity.Role;

import java.io.Serializable;

/**
 * A DTO for the {@link Role} entity
 */
public class RoleDto implements Serializable {
    //region Fields
    private Long id;
    private String name;
    //endregion

    //region Constructors
    public RoleDto() {
    }

    public RoleDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    //endregion

    //region Getters & Setters methods

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //endregion
}