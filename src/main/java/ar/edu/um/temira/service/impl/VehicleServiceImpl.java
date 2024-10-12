package ar.edu.um.temira.service.impl;

import ar.edu.um.temira.domain.Vehicle;
import ar.edu.um.temira.repository.VehicleRepository;
import ar.edu.um.temira.service.VehicleService;
import ar.edu.um.temira.service.dto.VehicleDTO;
import ar.edu.um.temira.service.mapper.VehicleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.temira.domain.Vehicle}.
 */
@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleServiceImpl.class);

    private final VehicleRepository vehicleRepository;

    private final VehicleMapper vehicleMapper;

    public VehicleServiceImpl(VehicleRepository vehicleRepository, VehicleMapper vehicleMapper) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
    }

    @Override
    public VehicleDTO save(VehicleDTO vehicleDTO) {
        LOG.debug("Request to save Vehicle : {}", vehicleDTO);
        Vehicle vehicle = vehicleMapper.toEntity(vehicleDTO);
        vehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toDto(vehicle);
    }

    @Override
    public VehicleDTO update(VehicleDTO vehicleDTO) {
        LOG.debug("Request to update Vehicle : {}", vehicleDTO);
        Vehicle vehicle = vehicleMapper.toEntity(vehicleDTO);
        vehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toDto(vehicle);
    }

    @Override
    public Optional<VehicleDTO> partialUpdate(VehicleDTO vehicleDTO) {
        LOG.debug("Request to partially update Vehicle : {}", vehicleDTO);

        return vehicleRepository
            .findById(vehicleDTO.getId())
            .map(existingVehicle -> {
                vehicleMapper.partialUpdate(existingVehicle, vehicleDTO);

                return existingVehicle;
            })
            .map(vehicleRepository::save)
            .map(vehicleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VehicleDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Vehicles");
        return vehicleRepository.findAll(pageable).map(vehicleMapper::toDto);
    }

    public Page<VehicleDTO> findAllWithEagerRelationships(Pageable pageable) {
        return vehicleRepository.findAllWithEagerRelationships(pageable).map(vehicleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VehicleDTO> findOne(Long id) {
        LOG.debug("Request to get Vehicle : {}", id);
        return vehicleRepository.findOneWithEagerRelationships(id).map(vehicleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Vehicle : {}", id);
        vehicleRepository.deleteById(id);
    }
}
