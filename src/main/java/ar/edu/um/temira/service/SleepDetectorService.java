package ar.edu.um.temira.service;

import ar.edu.um.temira.service.dto.SleepDetectorDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ar.edu.um.temira.domain.SleepDetector}.
 */
public interface SleepDetectorService {
    /**
     * Save a sleepDetector.
     *
     * @param sleepDetectorDTO the entity to save.
     * @return the persisted entity.
     */
    SleepDetectorDTO save(SleepDetectorDTO sleepDetectorDTO);

    /**
     * Updates a sleepDetector.
     *
     * @param sleepDetectorDTO the entity to update.
     * @return the persisted entity.
     */
    SleepDetectorDTO update(SleepDetectorDTO sleepDetectorDTO);

    /**
     * Partially updates a sleepDetector.
     *
     * @param sleepDetectorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SleepDetectorDTO> partialUpdate(SleepDetectorDTO sleepDetectorDTO);

    /**
     * Get all the sleepDetectors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SleepDetectorDTO> findAll(Pageable pageable);

    /**
     * Get all the sleepDetectors with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SleepDetectorDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" sleepDetector.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SleepDetectorDTO> findOne(Long id);

    /**
     * Delete the "id" sleepDetector.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
