package ar.edu.um.temira.domain;

import static ar.edu.um.temira.domain.DriverTestSamples.*;
import static ar.edu.um.temira.domain.SleepDetectorTestSamples.*;
import static ar.edu.um.temira.domain.StatementTestSamples.*;
import static ar.edu.um.temira.domain.VehicleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.temira.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StatementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Statement.class);
        Statement statement1 = getStatementSample1();
        Statement statement2 = new Statement();
        assertThat(statement1).isNotEqualTo(statement2);

        statement2.setId(statement1.getId());
        assertThat(statement1).isEqualTo(statement2);

        statement2 = getStatementSample2();
        assertThat(statement1).isNotEqualTo(statement2);
    }

    @Test
    void sleepDetectorTest() {
        Statement statement = getStatementRandomSampleGenerator();
        SleepDetector sleepDetectorBack = getSleepDetectorRandomSampleGenerator();

        statement.setSleepDetector(sleepDetectorBack);
        assertThat(statement.getSleepDetector()).isEqualTo(sleepDetectorBack);

        statement.sleepDetector(null);
        assertThat(statement.getSleepDetector()).isNull();
    }

    @Test
    void vehicleTest() {
        Statement statement = getStatementRandomSampleGenerator();
        Vehicle vehicleBack = getVehicleRandomSampleGenerator();

        statement.setVehicle(vehicleBack);
        assertThat(statement.getVehicle()).isEqualTo(vehicleBack);

        statement.vehicle(null);
        assertThat(statement.getVehicle()).isNull();
    }

    @Test
    void driverTest() {
        Statement statement = getStatementRandomSampleGenerator();
        Driver driverBack = getDriverRandomSampleGenerator();

        statement.setDriver(driverBack);
        assertThat(statement.getDriver()).isEqualTo(driverBack);

        statement.driver(null);
        assertThat(statement.getDriver()).isNull();
    }
}
