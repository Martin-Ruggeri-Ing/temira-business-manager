package ar.edu.um.temira.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.temira.domain.Vehicle} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 1900)
    @Max(value = 2100)
    private Integer model;

    @NotNull
    private String name;

    private UserDTO user;

    private VehicleTypeDTO type;

    private VehicleBrandDTO brand;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getModel() {
        return model;
    }

    public void setModel(Integer model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public VehicleTypeDTO getType() {
        return type;
    }

    public void setType(VehicleTypeDTO type) {
        this.type = type;
    }

    public VehicleBrandDTO getBrand() {
        return brand;
    }

    public void setBrand(VehicleBrandDTO brand) {
        this.brand = brand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleDTO)) {
            return false;
        }

        VehicleDTO vehicleDTO = (VehicleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vehicleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleDTO{" +
            "id=" + getId() +
            ", model=" + getModel() +
            ", name='" + getName() + "'" +
            ", user=" + getUser() +
            ", type=" + getType() +
            ", brand=" + getBrand() +
            "}";
    }
}
