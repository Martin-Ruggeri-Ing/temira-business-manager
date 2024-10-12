package ar.edu.um.temira.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Statement.
 */
@Entity
@Table(name = "statement")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Statement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date_creation", nullable = false)
    private Instant dateCreation;

    @NotNull
    @Size(min = 3)
    @Column(name = "destination", nullable = false)
    private String destination;

    @NotNull
    @Size(min = 3)
    @Column(name = "path_csv", nullable = false)
    private String pathCsv;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private SleepDetector sleepDetector;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "type", "brand" }, allowSetters = true)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Statement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateCreation() {
        return this.dateCreation;
    }

    public Statement dateCreation(Instant dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getDestination() {
        return this.destination;
    }

    public Statement destination(String destination) {
        this.setDestination(destination);
        return this;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getPathCsv() {
        return this.pathCsv;
    }

    public Statement pathCsv(String pathCsv) {
        this.setPathCsv(pathCsv);
        return this;
    }

    public void setPathCsv(String pathCsv) {
        this.pathCsv = pathCsv;
    }

    public SleepDetector getSleepDetector() {
        return this.sleepDetector;
    }

    public void setSleepDetector(SleepDetector sleepDetector) {
        this.sleepDetector = sleepDetector;
    }

    public Statement sleepDetector(SleepDetector sleepDetector) {
        this.setSleepDetector(sleepDetector);
        return this;
    }

    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Statement vehicle(Vehicle vehicle) {
        this.setVehicle(vehicle);
        return this;
    }

    public Driver getDriver() {
        return this.driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Statement driver(Driver driver) {
        this.setDriver(driver);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Statement user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Statement)) {
            return false;
        }
        return getId() != null && getId().equals(((Statement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Statement{" +
            "id=" + getId() +
            ", dateCreation='" + getDateCreation() + "'" +
            ", destination='" + getDestination() + "'" +
            ", pathCsv='" + getPathCsv() + "'" +
            "}";
    }
}
