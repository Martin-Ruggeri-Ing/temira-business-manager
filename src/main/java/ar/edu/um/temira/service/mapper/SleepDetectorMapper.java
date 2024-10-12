package ar.edu.um.temira.service.mapper;

import ar.edu.um.temira.domain.SleepDetector;
import ar.edu.um.temira.domain.User;
import ar.edu.um.temira.service.dto.SleepDetectorDTO;
import ar.edu.um.temira.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SleepDetector} and its DTO {@link SleepDetectorDTO}.
 */
@Mapper(componentModel = "spring")
public interface SleepDetectorMapper extends EntityMapper<SleepDetectorDTO, SleepDetector> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    SleepDetectorDTO toDto(SleepDetector s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
