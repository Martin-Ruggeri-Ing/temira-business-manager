package ar.edu.um.temira.service.mapper;

import ar.edu.um.temira.domain.User;
import ar.edu.um.temira.domain.Vehicle;
import ar.edu.um.temira.service.dto.UserDTO;
import ar.edu.um.temira.service.dto.VehicleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vehicle} and its DTO {@link VehicleDTO}.
 */
@Mapper(componentModel = "spring")
public interface VehicleMapper extends EntityMapper<VehicleDTO, Vehicle> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    VehicleDTO toDto(Vehicle s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
