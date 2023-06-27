package it.flaminiovilla.mechanicalappointment.model.dto;

import it.flaminiovilla.mechanicalappointment.model.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;

/**
 * This class represents the response of the authentication.
 */
@Data
@Builder
public class AuthResponseDto {
    //region Fields
    public long id;
    public String email;
    public String name;
    public Collection<Role> role;
    public String token;
    public String refreshToken;
    public String duration;
    //endregion

    //region Constructors
    public AuthResponseDto() {
    }

    public AuthResponseDto(long id, String email, String name, Collection<Role> role, String token, String refreshToken, String duration) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
        this.token = token;
        this.refreshToken = refreshToken;
        this.duration = duration;
    }
    //endregion

    //region Getters & Setters methods


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Role> getRole() {
        return role;
    }

    public void setRole(Collection<Role> role
    ) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
    //endregion
}
