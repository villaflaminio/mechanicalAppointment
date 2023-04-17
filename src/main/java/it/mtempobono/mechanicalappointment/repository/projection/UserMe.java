package it.mtempobono.mechanicalappointment.repository.projection;


import it.mtempobono.mechanicalappointment.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * A Projection for the {@link User} entity
 */
public interface UserMe {
    Long getId();

    String getName();

    String getEmail();

    String getImageUrl();

    Collection<RoleInfo> getRoles();

    /**
     * A Projection for the {@link Role} entity
     */
    interface RoleInfo {
        Long getId();

        String getName();
    }
}