package ar.edu.um.temira.service;

import ar.edu.um.temira.domain.Vehicle;
import ar.edu.um.temira.repository.VehicleRepository;
import ar.edu.um.temira.service.dto.VehicleDTO;
import ar.edu.um.temira.service.mapper.VehicleMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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
public class VehicleService {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleService.class);

    private final VehicleRepository vehicleRepository;

    private final VehicleMapper vehicleMapper;

    public VehicleService(VehicleRepository vehicleRepository, VehicleMapper vehicleMapper) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
    }

    /**
     * Save a vehicle.
     *
     * @param vehicleDTO the entity to save.
     * @return the persisted entity.
     */
    public VehicleDTO save(VehicleDTO vehicleDTO) {
        LOG.debug("Request to save Vehicle : {}", vehicleDTO);
        Vehicle vehicle = vehicleMapper.toEntity(vehicleDTO);
        vehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toDto(vehicle);
    }

    /**
     * Update a vehicle.
     *
     * @param vehicleDTO the entity to save.
     * @return the persisted entity.
     */
    public VehicleDTO update(VehicleDTO vehicleDTO) {
        LOG.debug("Request to update Vehicle : {}", vehicleDTO);
        Vehicle vehicle = vehicleMapper.toEntity(vehicleDTO);
        vehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toDto(vehicle);
    }

    /**
     * Partially update a vehicle.
     *
     * @param vehicleDTO the entity to update partially.
     * @return the persisted entity.
     */
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

    /**
     * Get all the vehicles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VehicleDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Vehicles");
        return vehicleRepository.findAll(pageable).map(vehicleMapper::toDto);
    }

    /**
     * Get all the vehicles with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<VehicleDTO> findAllWithEagerRelationships(Pageable pageable) {
        return vehicleRepository.findAllWithEagerRelationships(pageable).map(vehicleMapper::toDto);
    }

    /**
     *  Get all the vehicles where SleepDetector is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<VehicleDTO> findAllWhereSleepDetectorIsNull() {
        LOG.debug("Request to get all vehicles where SleepDetector is null");
        return StreamSupport.stream(vehicleRepository.findAll().spliterator(), false)
            .filter(vehicle -> vehicle.getSleepDetector() == null)
            .map(vehicleMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one vehicle by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VehicleDTO> findOne(Long id) {
        LOG.debug("Request to get Vehicle : {}", id);
        return vehicleRepository.findOneWithEagerRelationships(id).map(vehicleMapper::toDto);
    }

    /**
     * Delete the vehicle by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Vehicle : {}", id);
        vehicleRepository.deleteById(id);
    }
}
