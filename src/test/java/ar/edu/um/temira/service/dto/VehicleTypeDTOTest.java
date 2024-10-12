package ar.edu.um.temira.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.temira.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleTypeDTO.class);
        VehicleTypeDTO vehicleTypeDTO1 = new VehicleTypeDTO();
        vehicleTypeDTO1.setId(1L);
        VehicleTypeDTO vehicleTypeDTO2 = new VehicleTypeDTO();
        assertThat(vehicleTypeDTO1).isNotEqualTo(vehicleTypeDTO2);
        vehicleTypeDTO2.setId(vehicleTypeDTO1.getId());
        assertThat(vehicleTypeDTO1).isEqualTo(vehicleTypeDTO2);
        vehicleTypeDTO2.setId(2L);
        assertThat(vehicleTypeDTO1).isNotEqualTo(vehicleTypeDTO2);
        vehicleTypeDTO1.setId(null);
        assertThat(vehicleTypeDTO1).isNotEqualTo(vehicleTypeDTO2);
    }
}
