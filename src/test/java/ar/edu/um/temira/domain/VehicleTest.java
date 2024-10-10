package ar.edu.um.temira.domain;

import static ar.edu.um.temira.domain.SleepDetectorTestSamples.*;
import static ar.edu.um.temira.domain.VehicleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.temira.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vehicle.class);
        Vehicle vehicle1 = getVehicleSample1();
        Vehicle vehicle2 = new Vehicle();
        assertThat(vehicle1).isNotEqualTo(vehicle2);

        vehicle2.setId(vehicle1.getId());
        assertThat(vehicle1).isEqualTo(vehicle2);

        vehicle2 = getVehicleSample2();
        assertThat(vehicle1).isNotEqualTo(vehicle2);
    }

    @Test
    void sleepDetectorTest() {
        Vehicle vehicle = getVehicleRandomSampleGenerator();
        SleepDetector sleepDetectorBack = getSleepDetectorRandomSampleGenerator();

        vehicle.setSleepDetector(sleepDetectorBack);
        assertThat(vehicle.getSleepDetector()).isEqualTo(sleepDetectorBack);
        assertThat(sleepDetectorBack.getVehicle()).isEqualTo(vehicle);

        vehicle.sleepDetector(null);
        assertThat(vehicle.getSleepDetector()).isNull();
        assertThat(sleepDetectorBack.getVehicle()).isNull();
    }
}
