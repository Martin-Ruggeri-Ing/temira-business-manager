package ar.edu.um.temira.service.mapper;

import ar.edu.um.temira.domain.VehicleBrand;
import ar.edu.um.temira.service.dto.VehicleBrandDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VehicleBrand} and its DTO {@link VehicleBrandDTO}.
 */
@Mapper(componentModel = "spring")
public interface VehicleBrandMapper extends EntityMapper<VehicleBrandDTO, VehicleBrand> {}
