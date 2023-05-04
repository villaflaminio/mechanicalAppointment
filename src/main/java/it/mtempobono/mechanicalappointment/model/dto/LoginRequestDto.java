package it.mtempobono.mechanicalappointment.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * This class represents the login request.
 */
public class LoginRequestDto {
    //region Fields
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
    //endregion

    //region Constructors
    public LoginRequestDto() {
    }

    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
    //endregion

    //region Getters & Setters methods

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    //endregion
}
