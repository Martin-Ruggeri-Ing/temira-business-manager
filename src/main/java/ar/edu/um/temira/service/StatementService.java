package ar.edu.um.temira.service;

import ar.edu.um.temira.domain.Statement;
import ar.edu.um.temira.repository.StatementRepository;
import ar.edu.um.temira.service.dto.StatementDTO;
import ar.edu.um.temira.service.mapper.StatementMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.temira.domain.Statement}.
 */
@Service
@Transactional
public class StatementService {

    private static final Logger LOG = LoggerFactory.getLogger(StatementService.class);

    private final StatementRepository statementRepository;

    private final StatementMapper statementMapper;

    public StatementService(StatementRepository statementRepository, StatementMapper statementMapper) {
        this.statementRepository = statementRepository;
        this.statementMapper = statementMapper;
    }

    /**
     * Save a statement.
     *
     * @param statementDTO the entity to save.
     * @return the persisted entity.
     */
    public StatementDTO save(StatementDTO statementDTO) {
        LOG.debug("Request to save Statement : {}", statementDTO);
        Statement statement = statementMapper.toEntity(statementDTO);
        statement = statementRepository.save(statement);
        return statementMapper.toDto(statement);
    }

    /**
     * Update a statement.
     *
     * @param statementDTO the entity to save.
     * @return the persisted entity.
     */
    public StatementDTO update(StatementDTO statementDTO) {
        LOG.debug("Request to update Statement : {}", statementDTO);
        Statement statement = statementMapper.toEntity(statementDTO);
        statement = statementRepository.save(statement);
        return statementMapper.toDto(statement);
    }

    /**
     * Partially update a statement.
     *
     * @param statementDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StatementDTO> partialUpdate(StatementDTO statementDTO) {
        LOG.debug("Request to partially update Statement : {}", statementDTO);

        return statementRepository
            .findById(statementDTO.getId())
            .map(existingStatement -> {
                statementMapper.partialUpdate(existingStatement, statementDTO);

                return existingStatement;
            })
            .map(statementRepository::save)
            .map(statementMapper::toDto);
    }

    /**
     * Get all the statements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StatementDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Statements");
        return statementRepository.findAll(pageable).map(statementMapper::toDto);
    }

    /**
     * Get one statement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StatementDTO> findOne(Long id) {
        LOG.debug("Request to get Statement : {}", id);
        return statementRepository.findById(id).map(statementMapper::toDto);
    }

    /**
     * Delete the statement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Statement : {}", id);
        statementRepository.deleteById(id);
    }
}
