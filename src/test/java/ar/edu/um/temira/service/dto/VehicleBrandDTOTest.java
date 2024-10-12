package ar.edu.um.temira.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.temira.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleBrandDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleBrandDTO.class);
        VehicleBrandDTO vehicleBrandDTO1 = new VehicleBrandDTO();
        vehicleBrandDTO1.setId(1L);
        VehicleBrandDTO vehicleBrandDTO2 = new VehicleBrandDTO();
        assertThat(vehicleBrandDTO1).isNotEqualTo(vehicleBrandDTO2);
        vehicleBrandDTO2.setId(vehicleBrandDTO1.getId());
        assertThat(vehicleBrandDTO1).isEqualTo(vehicleBrandDTO2);
        vehicleBrandDTO2.setId(2L);
        assertThat(vehicleBrandDTO1).isNotEqualTo(vehicleBrandDTO2);
        vehicleBrandDTO1.setId(null);
        assertThat(vehicleBrandDTO1).isNotEqualTo(vehicleBrandDTO2);
    }
}
