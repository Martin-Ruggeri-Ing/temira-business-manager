package ar.edu.um.temira.service;

import ar.edu.um.temira.service.dto.VehicleDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ar.edu.um.temira.domain.Vehicle}.
 */
public interface VehicleService {
    /**
     * Save a vehicle.
     *
     * @param vehicleDTO the entity to save.
     * @return the persisted entity.
     */
    VehicleDTO save(VehicleDTO vehicleDTO);

    /**
     * Updates a vehicle.
     *
     * @param vehicleDTO the entity to update.
     * @return the persisted entity.
     */
    VehicleDTO update(VehicleDTO vehicleDTO);

    /**
     * Partially updates a vehicle.
     *
     * @param vehicleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VehicleDTO> partialUpdate(VehicleDTO vehicleDTO);

    /**
     * Get all the vehicles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VehicleDTO> findAll(Pageable pageable);

    /**
     * Get all the vehicles with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VehicleDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" vehicle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VehicleDTO> findOne(Long id);

    /**
     * Delete the "id" vehicle.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
