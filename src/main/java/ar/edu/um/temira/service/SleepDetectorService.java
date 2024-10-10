package ar.edu.um.temira.service;

import ar.edu.um.temira.domain.SleepDetector;
import ar.edu.um.temira.repository.SleepDetectorRepository;
import ar.edu.um.temira.service.dto.SleepDetectorDTO;
import ar.edu.um.temira.service.mapper.SleepDetectorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.temira.domain.SleepDetector}.
 */
@Service
@Transactional
public class SleepDetectorService {

    private static final Logger LOG = LoggerFactory.getLogger(SleepDetectorService.class);

    private final SleepDetectorRepository sleepDetectorRepository;

    private final SleepDetectorMapper sleepDetectorMapper;

    public SleepDetectorService(SleepDetectorRepository sleepDetectorRepository, SleepDetectorMapper sleepDetectorMapper) {
        this.sleepDetectorRepository = sleepDetectorRepository;
        this.sleepDetectorMapper = sleepDetectorMapper;
    }

    /**
     * Save a sleepDetector.
     *
     * @param sleepDetectorDTO the entity to save.
     * @return the persisted entity.
     */
    public SleepDetectorDTO save(SleepDetectorDTO sleepDetectorDTO) {
        LOG.debug("Request to save SleepDetector : {}", sleepDetectorDTO);
        SleepDetector sleepDetector = sleepDetectorMapper.toEntity(sleepDetectorDTO);
        sleepDetector = sleepDetectorRepository.save(sleepDetector);
        return sleepDetectorMapper.toDto(sleepDetector);
    }

    /**
     * Update a sleepDetector.
     *
     * @param sleepDetectorDTO the entity to save.
     * @return the persisted entity.
     */
    public SleepDetectorDTO update(SleepDetectorDTO sleepDetectorDTO) {
        LOG.debug("Request to update SleepDetector : {}", sleepDetectorDTO);
        SleepDetector sleepDetector = sleepDetectorMapper.toEntity(sleepDetectorDTO);
        sleepDetector = sleepDetectorRepository.save(sleepDetector);
        return sleepDetectorMapper.toDto(sleepDetector);
    }

    /**
     * Partially update a sleepDetector.
     *
     * @param sleepDetectorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SleepDetectorDTO> partialUpdate(SleepDetectorDTO sleepDetectorDTO) {
        LOG.debug("Request to partially update SleepDetector : {}", sleepDetectorDTO);

        return sleepDetectorRepository
            .findById(sleepDetectorDTO.getId())
            .map(existingSleepDetector -> {
                sleepDetectorMapper.partialUpdate(existingSleepDetector, sleepDetectorDTO);

                return existingSleepDetector;
            })
            .map(sleepDetectorRepository::save)
            .map(sleepDetectorMapper::toDto);
    }

    /**
     * Get all the sleepDetectors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SleepDetectorDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SleepDetectors");
        return sleepDetectorRepository.findAll(pageable).map(sleepDetectorMapper::toDto);
    }

    /**
     * Get all the sleepDetectors with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SleepDetectorDTO> findAllWithEagerRelationships(Pageable pageable) {
        return sleepDetectorRepository.findAllWithEagerRelationships(pageable).map(sleepDetectorMapper::toDto);
    }

    /**
     * Get one sleepDetector by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SleepDetectorDTO> findOne(Long id) {
        LOG.debug("Request to get SleepDetector : {}", id);
        return sleepDetectorRepository.findOneWithEagerRelationships(id).map(sleepDetectorMapper::toDto);
    }

    /**
     * Delete the sleepDetector by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SleepDetector : {}", id);
        sleepDetectorRepository.deleteById(id);
    }
}
