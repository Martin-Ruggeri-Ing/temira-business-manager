package ar.edu.um.temira.service.mapper;

import static ar.edu.um.temira.domain.VehicleBrandAsserts.*;
import static ar.edu.um.temira.domain.VehicleBrandTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VehicleBrandMapperTest {

    private VehicleBrandMapper vehicleBrandMapper;

    @BeforeEach
    void setUp() {
        vehicleBrandMapper = new VehicleBrandMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVehicleBrandSample1();
        var actual = vehicleBrandMapper.toEntity(vehicleBrandMapper.toDto(expected));
        assertVehicleBrandAllPropertiesEquals(expected, actual);
    }
}
