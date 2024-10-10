package ar.edu.um.temira.service.mapper;

import ar.edu.um.temira.domain.Driver;
import ar.edu.um.temira.domain.SleepDetector;
import ar.edu.um.temira.domain.User;
import ar.edu.um.temira.domain.Vehicle;
import ar.edu.um.temira.service.dto.DriverDTO;
import ar.edu.um.temira.service.dto.SleepDetectorDTO;
import ar.edu.um.temira.service.dto.UserDTO;
import ar.edu.um.temira.service.dto.VehicleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SleepDetector} and its DTO {@link SleepDetectorDTO}.
 */
@Mapper(componentModel = "spring")
public interface SleepDetectorMapper extends EntityMapper<SleepDetectorDTO, SleepDetector> {
    @Mapping(target = "vehicle", source = "vehicle", qualifiedByName = "vehicleId")
    @Mapping(target = "driver", source = "driver", qualifiedByName = "driverFirstName")
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    SleepDetectorDTO toDto(SleepDetector s);

    @Named("vehicleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VehicleDTO toDtoVehicleId(Vehicle vehicle);

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
