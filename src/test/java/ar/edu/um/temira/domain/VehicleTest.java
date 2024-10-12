package ar.edu.um.temira.domain;

import static ar.edu.um.temira.domain.VehicleBrandTestSamples.*;
import static ar.edu.um.temira.domain.VehicleTestSamples.*;
import static ar.edu.um.temira.domain.VehicleTypeTestSamples.*;
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
    void typeTest() {
        Vehicle vehicle = getVehicleRandomSampleGenerator();
        VehicleType vehicleTypeBack = getVehicleTypeRandomSampleGenerator();

        vehicle.setType(vehicleTypeBack);
        assertThat(vehicle.getType()).isEqualTo(vehicleTypeBack);

        vehicle.type(null);
        assertThat(vehicle.getType()).isNull();
    }

    @Test
    void brandTest() {
        Vehicle vehicle = getVehicleRandomSampleGenerator();
        VehicleBrand vehicleBrandBack = getVehicleBrandRandomSampleGenerator();

        vehicle.setBrand(vehicleBrandBack);
        assertThat(vehicle.getBrand()).isEqualTo(vehicleBrandBack);

        vehicle.brand(null);
        assertThat(vehicle.getBrand()).isNull();
    }
}
