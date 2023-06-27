package it.flaminiovilla.mechanicalappointment.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * This class represents the sign up request.
 */
public class SignUpRequestDto {
    //region Fields
    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
    //endregion

    //region Constructors
    public SignUpRequestDto() {
    }

    public SignUpRequestDto(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
    //endregion

    //region Getters & Setters methods

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
