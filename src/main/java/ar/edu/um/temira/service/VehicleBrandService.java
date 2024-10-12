package ar.edu.um.temira.service;

import ar.edu.um.temira.service.dto.VehicleBrandDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ar.edu.um.temira.domain.VehicleBrand}.
 */
public interface VehicleBrandService {
    /**
     * Save a vehicleBrand.
     *
     * @param vehicleBrandDTO the entity to save.
     * @return the persisted entity.
     */
    VehicleBrandDTO save(VehicleBrandDTO vehicleBrandDTO);

    /**
     * Updates a vehicleBrand.
     *
     * @param vehicleBrandDTO the entity to update.
     * @return the persisted entity.
     */
    VehicleBrandDTO update(VehicleBrandDTO vehicleBrandDTO);

    /**
     * Partially updates a vehicleBrand.
     *
     * @param vehicleBrandDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VehicleBrandDTO> partialUpdate(VehicleBrandDTO vehicleBrandDTO);

    /**
     * Get all the vehicleBrands.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VehicleBrandDTO> findAll(Pageable pageable);

    /**
     * Get the "id" vehicleBrand.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VehicleBrandDTO> findOne(Long id);

    /**
     * Delete the "id" vehicleBrand.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
