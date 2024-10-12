package ar.edu.um.temira.service.mapper;

import ar.edu.um.temira.domain.User;
import ar.edu.um.temira.domain.Vehicle;
import ar.edu.um.temira.domain.VehicleBrand;
import ar.edu.um.temira.domain.VehicleType;
import ar.edu.um.temira.service.dto.UserDTO;
import ar.edu.um.temira.service.dto.VehicleBrandDTO;
import ar.edu.um.temira.service.dto.VehicleDTO;
import ar.edu.um.temira.service.dto.VehicleTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vehicle} and its DTO {@link VehicleDTO}.
 */
@Mapper(componentModel = "spring")
public interface VehicleMapper extends EntityMapper<VehicleDTO, Vehicle> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "type", source = "type", qualifiedByName = "vehicleTypeName")
    @Mapping(target = "brand", source = "brand", qualifiedByName = "vehicleBrandName")
    VehicleDTO toDto(Vehicle s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("vehicleTypeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    VehicleTypeDTO toDtoVehicleTypeName(VehicleType vehicleType);

    @Named("vehicleBrandName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    VehicleBrandDTO toDtoVehicleBrandName(VehicleBrand vehicleBrand);
}
