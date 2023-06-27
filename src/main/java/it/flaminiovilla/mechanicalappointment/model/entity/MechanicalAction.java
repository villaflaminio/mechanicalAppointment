package it.flaminiovilla.mechanicalappointment.model.entity;

import it.flaminiovilla.mechanicalappointment.util.converters.DurationConverter;

import javax.persistence.*;
import java.time.Duration;

@Entity
@Table(name = "mechanical_actions")
public class MechanicalAction {

    //region Fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "internal_duration")
    @Convert(converter = DurationConverter.class)
    private Duration internalDuration;

    @Column(name = "external_duration")
    @Convert(converter = DurationConverter.class)
    private Duration externalDuration;
    //endregion

    //region Constructors

    public MechanicalAction() {
    }

    public MechanicalAction(Long id, String name, String description, Double price, Boolean isActive, Duration internalDuration, Duration externalDuration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.isActive = isActive;
        this.internalDuration = internalDuration;
        this.externalDuration = externalDuration;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Duration getInternalDuration() {
        return internalDuration;
    }

    public void setInternalDuration(Duration internalDuration) {
        this.internalDuration = internalDuration;
    }

    public Duration getExternalDuration() {
        return externalDuration;
    }

    public void setExternalDuration(Duration externalDuration) {
        this.externalDuration = externalDuration;
    }
    // endregion
}
