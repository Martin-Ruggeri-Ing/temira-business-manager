package ar.edu.um.temira.service.mapper;

import static ar.edu.um.temira.domain.SleepDetectorAsserts.*;
import static ar.edu.um.temira.domain.SleepDetectorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SleepDetectorMapperTest {

    private SleepDetectorMapper sleepDetectorMapper;

    @BeforeEach
    void setUp() {
        sleepDetectorMapper = new SleepDetectorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSleepDetectorSample1();
        var actual = sleepDetectorMapper.toEntity(sleepDetectorMapper.toDto(expected));
        assertSleepDetectorAllPropertiesEquals(expected, actual);
    }
}
