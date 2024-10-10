package ar.edu.um.temira.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.temira.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StatementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StatementDTO.class);
        StatementDTO statementDTO1 = new StatementDTO();
        statementDTO1.setId(1L);
        StatementDTO statementDTO2 = new StatementDTO();
        assertThat(statementDTO1).isNotEqualTo(statementDTO2);
        statementDTO2.setId(statementDTO1.getId());
        assertThat(statementDTO1).isEqualTo(statementDTO2);
        statementDTO2.setId(2L);
        assertThat(statementDTO1).isNotEqualTo(statementDTO2);
        statementDTO1.setId(null);
        assertThat(statementDTO1).isNotEqualTo(statementDTO2);
    }
}
