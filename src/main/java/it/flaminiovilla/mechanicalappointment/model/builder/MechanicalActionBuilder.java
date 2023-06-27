package it.flaminiovilla.mechanicalappointment.model.builder;

import it.flaminiovilla.mechanicalappointment.model.entity.MechanicalAction;

import java.time.Duration;

public final class MechanicalActionBuilder {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Boolean isActive;
    private Duration internalDuration;
    private Duration externalDuration;

    private MechanicalActionBuilder() {
    }

    public static MechanicalActionBuilder aMechanicalAction() {
        return new MechanicalActionBuilder();
    }

    public MechanicalActionBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public MechanicalActionBuilder name(String name) {
        this.name = name;
        return this;
    }

    public MechanicalActionBuilder description(String description) {
        this.description = description;
        return this;
    }

    public MechanicalActionBuilder price(Double price) {
        this.price = price;
        return this;
    }

    public MechanicalActionBuilder isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public MechanicalActionBuilder internalDuration(Duration internalDuration) {
        this.internalDuration = internalDuration;
        return this;
    }

    public MechanicalActionBuilder externalDuration(Duration externalDuration) {
        this.externalDuration = externalDuration;
        return this;
    }

    public MechanicalAction build() {
        MechanicalAction mechanicalAction = new MechanicalAction();
        mechanicalAction.setId(id);
        mechanicalAction.setName(name);
        mechanicalAction.setDescription(description);
        mechanicalAction.setPrice(price);
        mechanicalAction.setIsActive(isActive);
        mechanicalAction.setInternalDuration(internalDuration);
        mechanicalAction.setExternalDuration(externalDuration);
        return mechanicalAction;
    }
}
