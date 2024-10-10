package ar.edu.um.temira.domain;

import static ar.edu.um.temira.domain.DriverTestSamples.*;
import static ar.edu.um.temira.domain.SleepDetectorTestSamples.*;
import static ar.edu.um.temira.domain.VehicleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.temira.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SleepDetectorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SleepDetector.class);
        SleepDetector sleepDetector1 = getSleepDetectorSample1();
        SleepDetector sleepDetector2 = new SleepDetector();
        assertThat(sleepDetector1).isNotEqualTo(sleepDetector2);

        sleepDetector2.setId(sleepDetector1.getId());
        assertThat(sleepDetector1).isEqualTo(sleepDetector2);

        sleepDetector2 = getSleepDetectorSample2();
        assertThat(sleepDetector1).isNotEqualTo(sleepDetector2);
    }

    @Test
    void vehicleTest() {
        SleepDetector sleepDetector = getSleepDetectorRandomSampleGenerator();
        Vehicle vehicleBack = getVehicleRandomSampleGenerator();

        sleepDetector.setVehicle(vehicleBack);
        assertThat(sleepDetector.getVehicle()).isEqualTo(vehicleBack);

        sleepDetector.vehicle(null);
        assertThat(sleepDetector.getVehicle()).isNull();
    }

    @Test
    void driverTest() {
        SleepDetector sleepDetector = getSleepDetectorRandomSampleGenerator();
        Driver driverBack = getDriverRandomSampleGenerator();

        sleepDetector.setDriver(driverBack);
        assertThat(sleepDetector.getDriver()).isEqualTo(driverBack);

        sleepDetector.driver(null);
        assertThat(sleepDetector.getDriver()).isNull();
    }
}
