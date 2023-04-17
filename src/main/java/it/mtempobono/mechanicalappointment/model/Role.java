package it.mtempobono.mechanicalappointment.model;

import lombok.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * The type Role.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EnableAutoConfiguration
@ToString(includeFieldNames = true)
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    public Role(String name) {
        this.name=name;
    }
}