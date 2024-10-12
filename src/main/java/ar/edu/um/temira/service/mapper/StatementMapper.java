package ar.edu.um.temira.service.mapper;

import ar.edu.um.temira.domain.Driver;
import ar.edu.um.temira.domain.SleepDetector;
import ar.edu.um.temira.domain.Statement;
import ar.edu.um.temira.domain.User;
import ar.edu.um.temira.domain.Vehicle;
import ar.edu.um.temira.service.dto.DriverDTO;
import ar.edu.um.temira.service.dto.SleepDetectorDTO;
import ar.edu.um.temira.service.dto.StatementDTO;
import ar.edu.um.temira.service.dto.UserDTO;
import ar.edu.um.temira.service.dto.VehicleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Statement} and its DTO {@link StatementDTO}.
 */
@Mapper(componentModel = "spring")
public interface StatementMapper extends EntityMapper<StatementDTO, Statement> {
    @Mapping(target = "sleepDetector", source = "sleepDetector", qualifiedByName = "sleepDetectorName")
    @Mapping(target = "vehicle", source = "vehicle", qualifiedByName = "vehicleName")
    @Mapping(target = "driver", source = "driver", qualifiedByName = "driverFirstName")
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    StatementDTO toDto(Statement s);

    @Named("sleepDetectorName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SleepDetectorDTO toDtoSleepDetectorName(SleepDetector sleepDetector);

    @Named("vehicleName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    VehicleDTO toDtoVehicleName(Vehicle vehicle);

    @Named("driverFirstName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    DriverDTO toDtoDriverFirstName(Driver driver);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
