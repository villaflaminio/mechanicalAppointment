package it.flaminiovilla.mechanicalappointment.model.builder;

import it.flaminiovilla.mechanicalappointment.model.dto.AuthResponseDto;
import it.flaminiovilla.mechanicalappointment.model.entity.Role;

import java.util.Collection;

public final class AuthResponseDtoBuilder {
    private long id;
    private String email;
    private String name;
    private Collection<Role> role;
    private String token;
    private String refreshToken;
    private String duration;

    private AuthResponseDtoBuilder() {
    }

    public static AuthResponseDtoBuilder anAuthResponseDto() {
        return new AuthResponseDtoBuilder();
    }

    public AuthResponseDtoBuilder id(long id) {
        this.id = id;
        return this;
    }

    public AuthResponseDtoBuilder email(String email) {
        this.email = email;
        return this;
    }

    public AuthResponseDtoBuilder name(String name) {
        this.name = name;
        return this;
    }

    public AuthResponseDtoBuilder role(Collection<Role> role) {
        this.role = role;
        return this;
    }

    public AuthResponseDtoBuilder token(String token) {
        this.token = token;
        return this;
    }

    public AuthResponseDtoBuilder refreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public AuthResponseDtoBuilder duration(String duration) {
        this.duration = duration;
        return this;
    }

    public AuthResponseDto build() {
        AuthResponseDto authResponseDto = new AuthResponseDto();
        authResponseDto.setId(id);
        authResponseDto.setEmail(email);
        authResponseDto.setName(name);
        authResponseDto.setRole(role);
        authResponseDto.setToken(token);
        authResponseDto.setRefreshToken(refreshToken);
        authResponseDto.setDuration(duration);
        return authResponseDto;
    }
}
