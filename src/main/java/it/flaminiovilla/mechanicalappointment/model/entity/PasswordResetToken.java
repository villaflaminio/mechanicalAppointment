package it.flaminiovilla.mechanicalappointment.model.entity;

import javax.persistence.*;
import java.time.Instant;

/**
 * The type Password reset token.
 */
@Entity
public class PasswordResetToken {

    //region Fields
    // The constant EXPIRATION.
    private static final long EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private Instant expiryDate;
    //endregion

    //region Constructors
    public PasswordResetToken() {
    }

    /**
     * Instantiates a new Password reset token.
     * @param token the token to be set
     * @param user  the user
     */
    public PasswordResetToken(String token, User user, Instant date) {
        this.token = token;
        this.user = user;
        expiryDate = date;
    }
    //endregion

    //region Getters & Setters methods

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    //endregion
}