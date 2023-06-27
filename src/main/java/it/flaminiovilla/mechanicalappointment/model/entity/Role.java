package it.flaminiovilla.mechanicalappointment.model.entity;

import lombok.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * The type Role.
 */
@Entity
@EnableAutoConfiguration
@ToString(includeFieldNames = true)
@EntityListeners(AuditingEntityListener.class)
public class Role {

    //region Fields
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    //endregion

    //region Constructors
    public Role() {
    }

    public Role(String name) {
        this.name=name;
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