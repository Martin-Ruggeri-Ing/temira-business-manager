package ar.edu.um.temira.service.mapper;

import ar.edu.um.temira.domain.VehicleType;
import ar.edu.um.temira.service.dto.VehicleTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VehicleType} and its DTO {@link VehicleTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface VehicleTypeMapper extends EntityMapper<VehicleTypeDTO, VehicleType> {}
