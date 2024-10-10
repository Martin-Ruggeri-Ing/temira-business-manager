package ar.edu.um.temira.domain;

import ar.edu.um.temira.domain.enumeration.VehicleBrand;
import ar.edu.um.temira.domain.enumeration.VehicleType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Vehicle.
 */
@Entity
@Table(name = "vehicle")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private VehicleType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "brand")
    private VehicleBrand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JsonIgnoreProperties(value = { "vehicle", "driver", "user" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "vehicle")
    private SleepDetector sleepDetector;

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

    public VehicleType getType() {
        return this.type;
    }

    public Vehicle type(VehicleType type) {
        this.setType(type);
        return this;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public VehicleBrand getBrand() {
        return this.brand;
    }

    public Vehicle brand(VehicleBrand brand) {
        this.setBrand(brand);
        return this;
    }

    public void setBrand(VehicleBrand brand) {
        this.brand = brand;
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

    public SleepDetector getSleepDetector() {
        return this.sleepDetector;
    }

    public void setSleepDetector(SleepDetector sleepDetector) {
        if (this.sleepDetector != null) {
            this.sleepDetector.setVehicle(null);
        }
        if (sleepDetector != null) {
            sleepDetector.setVehicle(this);
        }
        this.sleepDetector = sleepDetector;
    }

    public Vehicle sleepDetector(SleepDetector sleepDetector) {
        this.setSleepDetector(sleepDetector);
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
            ", type='" + getType() + "'" +
            ", brand='" + getBrand() + "'" +
            "}";
    }
}
