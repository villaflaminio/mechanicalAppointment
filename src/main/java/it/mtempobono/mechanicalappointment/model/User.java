package it.mtempobono.mechanicalappointment.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import it.mtempobono.mechanicalappointment.model.dto.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * The type User.
 */
@Entity

/**
 * Define table name and unique constraints.
 */
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false)
    private String email;

    private String imageUrl;

    @Column(nullable = false)
    private Boolean emailVerified = false;

    @JsonIgnore
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    //relation with vehicle
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Vehicle> vehicle;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    @JsonIgnore
    private Collection<Role> roles = new ArrayList<Role>();

    // The authorities of the user.
    @Transient
    private Set<GrantedAuthority> authorities = new HashSet<>();

    /**
     * Method to get the authorities of the user.
     * @return
     */
    public Collection<? extends SimpleGrantedAuthority> getAuthorities() {

        List<SimpleGrantedAuthority> list = new ArrayList<SimpleGrantedAuthority>();
        for (Role role: roles) {
            list.add(new SimpleGrantedAuthority(role.getName()));
        }
        return list;
    }

}
