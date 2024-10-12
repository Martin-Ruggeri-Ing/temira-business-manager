package ar.edu.um.temira.service;

import ar.edu.um.temira.service.dto.VehicleTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ar.edu.um.temira.domain.VehicleType}.
 */
public interface VehicleTypeService {
    /**
     * Save a vehicleType.
     *
     * @param vehicleTypeDTO the entity to save.
     * @return the persisted entity.
     */
    VehicleTypeDTO save(VehicleTypeDTO vehicleTypeDTO);

    /**
     * Updates a vehicleType.
     *
     * @param vehicleTypeDTO the entity to update.
     * @return the persisted entity.
     */
    VehicleTypeDTO update(VehicleTypeDTO vehicleTypeDTO);

    /**
     * Partially updates a vehicleType.
     *
     * @param vehicleTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VehicleTypeDTO> partialUpdate(VehicleTypeDTO vehicleTypeDTO);

    /**
     * Get all the vehicleTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VehicleTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" vehicleType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VehicleTypeDTO> findOne(Long id);

    /**
     * Delete the "id" vehicleType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
