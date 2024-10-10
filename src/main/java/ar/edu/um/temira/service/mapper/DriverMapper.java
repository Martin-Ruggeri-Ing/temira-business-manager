package ar.edu.um.temira.service.mapper;

import ar.edu.um.temira.domain.Driver;
import ar.edu.um.temira.domain.User;
import ar.edu.um.temira.service.dto.DriverDTO;
import ar.edu.um.temira.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Driver} and its DTO {@link DriverDTO}.
 */
@Mapper(componentModel = "spring")
public interface DriverMapper extends EntityMapper<DriverDTO, Driver> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    DriverDTO toDto(Driver s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
