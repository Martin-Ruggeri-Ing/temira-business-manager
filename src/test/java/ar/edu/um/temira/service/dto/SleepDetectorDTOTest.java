package ar.edu.um.temira.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.temira.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SleepDetectorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SleepDetectorDTO.class);
        SleepDetectorDTO sleepDetectorDTO1 = new SleepDetectorDTO();
        sleepDetectorDTO1.setId(1L);
        SleepDetectorDTO sleepDetectorDTO2 = new SleepDetectorDTO();
        assertThat(sleepDetectorDTO1).isNotEqualTo(sleepDetectorDTO2);
        sleepDetectorDTO2.setId(sleepDetectorDTO1.getId());
        assertThat(sleepDetectorDTO1).isEqualTo(sleepDetectorDTO2);
        sleepDetectorDTO2.setId(2L);
        assertThat(sleepDetectorDTO1).isNotEqualTo(sleepDetectorDTO2);
        sleepDetectorDTO1.setId(null);
        assertThat(sleepDetectorDTO1).isNotEqualTo(sleepDetectorDTO2);
    }
}
