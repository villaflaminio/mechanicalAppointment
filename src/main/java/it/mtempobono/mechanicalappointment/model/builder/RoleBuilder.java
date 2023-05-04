package it.mtempobono.mechanicalappointment.model.builder;

import it.mtempobono.mechanicalappointment.model.entity.Role;

public final class RoleBuilder {
    private Long id;
    private String name;

    private RoleBuilder() {
    }

    public static RoleBuilder aRole() {
        return new RoleBuilder();
    }

    public RoleBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public RoleBuilder name(String name) {
        this.name = name;
        return this;
    }

    public Role build() {
        Role role = new Role();
        role.setId(id);
        role.setName(name);
        return role;
    }
}
