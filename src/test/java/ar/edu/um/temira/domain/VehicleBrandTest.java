package ar.edu.um.temira.domain;

import static ar.edu.um.temira.domain.VehicleBrandTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.temira.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleBrandTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleBrand.class);
        VehicleBrand vehicleBrand1 = getVehicleBrandSample1();
        VehicleBrand vehicleBrand2 = new VehicleBrand();
        assertThat(vehicleBrand1).isNotEqualTo(vehicleBrand2);

        vehicleBrand2.setId(vehicleBrand1.getId());
        assertThat(vehicleBrand1).isEqualTo(vehicleBrand2);

        vehicleBrand2 = getVehicleBrandSample2();
        assertThat(vehicleBrand1).isNotEqualTo(vehicleBrand2);
    }
}
