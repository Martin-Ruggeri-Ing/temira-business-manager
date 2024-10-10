package ar.edu.um.temira.service.mapper;

import ar.edu.um.temira.domain.Driver;
import ar.edu.um.temira.domain.SleepDetector;
import ar.edu.um.temira.domain.Statement;
import ar.edu.um.temira.domain.Vehicle;
import ar.edu.um.temira.service.dto.DriverDTO;
import ar.edu.um.temira.service.dto.SleepDetectorDTO;
import ar.edu.um.temira.service.dto.StatementDTO;
import ar.edu.um.temira.service.dto.VehicleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Statement} and its DTO {@link StatementDTO}.
 */
@Mapper(componentModel = "spring")
public interface StatementMapper extends EntityMapper<StatementDTO, Statement> {
    @Mapping(target = "sleepDetector", source = "sleepDetector", qualifiedByName = "sleepDetectorId")
    @Mapping(target = "vehicle", source = "vehicle", qualifiedByName = "vehicleId")
    @Mapping(target = "driver", source = "driver", qualifiedByName = "driverId")
    StatementDTO toDto(Statement s);

    @Named("sleepDetectorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SleepDetectorDTO toDtoSleepDetectorId(SleepDetector sleepDetector);

    @Named("vehicleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VehicleDTO toDtoVehicleId(Vehicle vehicle);

    @Named("driverId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DriverDTO toDtoDriverId(Driver driver);
}
