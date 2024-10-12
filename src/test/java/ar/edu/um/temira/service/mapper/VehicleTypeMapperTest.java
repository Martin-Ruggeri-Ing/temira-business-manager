package ar.edu.um.temira.service.mapper;

import static ar.edu.um.temira.domain.VehicleTypeAsserts.*;
import static ar.edu.um.temira.domain.VehicleTypeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VehicleTypeMapperTest {

    private VehicleTypeMapper vehicleTypeMapper;

    @BeforeEach
    void setUp() {
        vehicleTypeMapper = new VehicleTypeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVehicleTypeSample1();
        var actual = vehicleTypeMapper.toEntity(vehicleTypeMapper.toDto(expected));
        assertVehicleTypeAllPropertiesEquals(expected, actual);
    }
}
