package ar.edu.um.temira.service.impl;

import ar.edu.um.temira.domain.VehicleType;
import ar.edu.um.temira.repository.VehicleTypeRepository;
import ar.edu.um.temira.service.VehicleTypeService;
import ar.edu.um.temira.service.dto.VehicleTypeDTO;
import ar.edu.um.temira.service.mapper.VehicleTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.temira.domain.VehicleType}.
 */
@Service
@Transactional
public class VehicleTypeServiceImpl implements VehicleTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleTypeServiceImpl.class);

    private final VehicleTypeRepository vehicleTypeRepository;

    private final VehicleTypeMapper vehicleTypeMapper;

    public VehicleTypeServiceImpl(VehicleTypeRepository vehicleTypeRepository, VehicleTypeMapper vehicleTypeMapper) {
        this.vehicleTypeRepository = vehicleTypeRepository;
        this.vehicleTypeMapper = vehicleTypeMapper;
    }

    @Override
    public VehicleTypeDTO save(VehicleTypeDTO vehicleTypeDTO) {
        LOG.debug("Request to save VehicleType : {}", vehicleTypeDTO);
        VehicleType vehicleType = vehicleTypeMapper.toEntity(vehicleTypeDTO);
        vehicleType = vehicleTypeRepository.save(vehicleType);
        return vehicleTypeMapper.toDto(vehicleType);
    }

    @Override
    public VehicleTypeDTO update(VehicleTypeDTO vehicleTypeDTO) {
        LOG.debug("Request to update VehicleType : {}", vehicleTypeDTO);
        VehicleType vehicleType = vehicleTypeMapper.toEntity(vehicleTypeDTO);
        vehicleType = vehicleTypeRepository.save(vehicleType);
        return vehicleTypeMapper.toDto(vehicleType);
    }

    @Override
    public Optional<VehicleTypeDTO> partialUpdate(VehicleTypeDTO vehicleTypeDTO) {
        LOG.debug("Request to partially update VehicleType : {}", vehicleTypeDTO);

        return vehicleTypeRepository
            .findById(vehicleTypeDTO.getId())
            .map(existingVehicleType -> {
                vehicleTypeMapper.partialUpdate(existingVehicleType, vehicleTypeDTO);

                return existingVehicleType;
            })
            .map(vehicleTypeRepository::save)
            .map(vehicleTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VehicleTypeDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all VehicleTypes");
        return vehicleTypeRepository.findAll(pageable).map(vehicleTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VehicleTypeDTO> findOne(Long id) {
        LOG.debug("Request to get VehicleType : {}", id);
        return vehicleTypeRepository.findById(id).map(vehicleTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete VehicleType : {}", id);
        vehicleTypeRepository.deleteById(id);
    }
}
