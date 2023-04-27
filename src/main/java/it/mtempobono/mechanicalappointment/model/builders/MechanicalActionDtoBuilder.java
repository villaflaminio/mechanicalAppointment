package it.mtempobono.mechanicalappointment.model.builders;

import it.mtempobono.mechanicalappointment.model.dto.MechanicalActionDto;

import java.time.Duration;

public final class MechanicalActionDtoBuilder {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Boolean isActive;
    private Duration internalDuration;
    private Duration externalDuration;

    private MechanicalActionDtoBuilder() {
    }

    public static MechanicalActionDtoBuilder aMechanicalActionDto() {
        return new MechanicalActionDtoBuilder();
    }

    public MechanicalActionDtoBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public MechanicalActionDtoBuilder name(String name) {
        this.name = name;
        return this;
    }

    public MechanicalActionDtoBuilder description(String description) {
        this.description = description;
        return this;
    }

    public MechanicalActionDtoBuilder price(Double price) {
        this.price = price;
        return this;
    }

    public MechanicalActionDtoBuilder isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public MechanicalActionDtoBuilder internalDuration(Duration internalDuration) {
        this.internalDuration = internalDuration;
        return this;
    }

    public MechanicalActionDtoBuilder externalDuration(Duration externalDuration) {
        this.externalDuration = externalDuration;
        return this;
    }

    public MechanicalActionDto build() {
        return new MechanicalActionDto(id, name, description, price, isActive, internalDuration, externalDuration);
    }
}
