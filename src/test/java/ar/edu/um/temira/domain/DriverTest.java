package ar.edu.um.temira.domain;

import static ar.edu.um.temira.domain.DriverTestSamples.*;
import static ar.edu.um.temira.domain.SleepDetectorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.temira.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DriverTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Driver.class);
        Driver driver1 = getDriverSample1();
        Driver driver2 = new Driver();
        assertThat(driver1).isNotEqualTo(driver2);

        driver2.setId(driver1.getId());
        assertThat(driver1).isEqualTo(driver2);

        driver2 = getDriverSample2();
        assertThat(driver1).isNotEqualTo(driver2);
    }

    @Test
    void sleepDetectorTest() {
        Driver driver = getDriverRandomSampleGenerator();
        SleepDetector sleepDetectorBack = getSleepDetectorRandomSampleGenerator();

        driver.setSleepDetector(sleepDetectorBack);
        assertThat(driver.getSleepDetector()).isEqualTo(sleepDetectorBack);
        assertThat(sleepDetectorBack.getDriver()).isEqualTo(driver);

        driver.sleepDetector(null);
        assertThat(driver.getSleepDetector()).isNull();
        assertThat(sleepDetectorBack.getDriver()).isNull();
    }
}
