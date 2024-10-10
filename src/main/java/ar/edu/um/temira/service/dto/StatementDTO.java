package ar.edu.um.temira.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.temira.domain.Statement} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StatementDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant dateCreation;

    @NotNull
    @Size(min = 3)
    private String destination;

    @NotNull
    @Size(min = 3)
    private String pathCsv;

    private SleepDetectorDTO sleepDetector;

    private VehicleDTO vehicle;

    private DriverDTO driver;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getPathCsv() {
        return pathCsv;
    }

    public void setPathCsv(String pathCsv) {
        this.pathCsv = pathCsv;
    }

    public SleepDetectorDTO getSleepDetector() {
        return sleepDetector;
    }

    public void setSleepDetector(SleepDetectorDTO sleepDetector) {
        this.sleepDetector = sleepDetector;
    }

    public VehicleDTO getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleDTO vehicle) {
        this.vehicle = vehicle;
    }

    public DriverDTO getDriver() {
        return driver;
    }

    public void setDriver(DriverDTO driver) {
        this.driver = driver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StatementDTO)) {
            return false;
        }

        StatementDTO statementDTO = (StatementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, statementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StatementDTO{" +
            "id=" + getId() +
            ", dateCreation='" + getDateCreation() + "'" +
            ", destination='" + getDestination() + "'" +
            ", pathCsv='" + getPathCsv() + "'" +
            ", sleepDetector=" + getSleepDetector() +
            ", vehicle=" + getVehicle() +
            ", driver=" + getDriver() +
            "}";
    }
}
