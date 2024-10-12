package ar.edu.um.temira.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Vehicle.
 */
@Entity
@Table(name = "vehicle")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 1900)
    @Max(value = 2100)
    @Column(name = "model", nullable = false)
    private Integer model;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private VehicleType type;

    @ManyToOne(fetch = FetchType.LAZY)
    private VehicleBrand brand;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vehicle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getModel() {
        return this.model;
    }

    public Vehicle model(Integer model) {
        this.setModel(model);
        return this;
    }

    public void setModel(Integer model) {
        this.model = model;
    }

    public String getName() {
        return this.name;
    }

    public Vehicle name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Vehicle user(User user) {
        this.setUser(user);
        return this;
    }

    public VehicleType getType() {
        return this.type;
    }

    public void setType(VehicleType vehicleType) {
        this.type = vehicleType;
    }

    public Vehicle type(VehicleType vehicleType) {
        this.setType(vehicleType);
        return this;
    }

    public VehicleBrand getBrand() {
        return this.brand;
    }

    public void setBrand(VehicleBrand vehicleBrand) {
        this.brand = vehicleBrand;
    }

    public Vehicle brand(VehicleBrand vehicleBrand) {
        this.setBrand(vehicleBrand);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vehicle)) {
            return false;
        }
        return getId() != null && getId().equals(((Vehicle) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vehicle{" +
            "id=" + getId() +
            ", model=" + getModel() +
            ", name='" + getName() + "'" +
            "}";
    }
}
