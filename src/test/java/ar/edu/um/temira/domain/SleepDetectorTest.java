package ar.edu.um.temira.domain;

import static ar.edu.um.temira.domain.SleepDetectorTestSamples.*;
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
}
