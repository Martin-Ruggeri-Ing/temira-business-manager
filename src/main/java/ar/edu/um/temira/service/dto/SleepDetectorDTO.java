package ar.edu.um.temira.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.temira.domain.SleepDetector} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SleepDetectorDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private UserDTO user;

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

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SleepDetectorDTO)) {
            return false;
        }

        SleepDetectorDTO sleepDetectorDTO = (SleepDetectorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sleepDetectorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SleepDetectorDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
