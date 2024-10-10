package ar.edu.um.temira.service.mapper;

import static ar.edu.um.temira.domain.StatementAsserts.*;
import static ar.edu.um.temira.domain.StatementTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StatementMapperTest {

    private StatementMapper statementMapper;

    @BeforeEach
    void setUp() {
        statementMapper = new StatementMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStatementSample1();
        var actual = statementMapper.toEntity(statementMapper.toDto(expected));
        assertStatementAllPropertiesEquals(expected, actual);
    }
}
