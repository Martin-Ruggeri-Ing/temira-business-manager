package ar.edu.um.temira.service;

import ar.edu.um.temira.service.dto.StatementDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ar.edu.um.temira.domain.Statement}.
 */
public interface StatementService {
    /**
     * Save a statement.
     *
     * @param statementDTO the entity to save.
     * @return the persisted entity.
     */
    StatementDTO save(StatementDTO statementDTO);

    /**
     * Updates a statement.
     *
     * @param statementDTO the entity to update.
     * @return the persisted entity.
     */
    StatementDTO update(StatementDTO statementDTO);

    /**
     * Partially updates a statement.
     *
     * @param statementDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StatementDTO> partialUpdate(StatementDTO statementDTO);

    /**
     * Get all the statements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StatementDTO> findAll(Pageable pageable);

    /**
     * Get all the statements with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StatementDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" statement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StatementDTO> findOne(Long id);

    /**
     * Delete the "id" statement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
