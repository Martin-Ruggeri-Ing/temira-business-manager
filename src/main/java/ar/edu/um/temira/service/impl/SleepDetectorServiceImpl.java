package ar.edu.um.temira.service.impl;

import ar.edu.um.temira.domain.SleepDetector;
import ar.edu.um.temira.repository.SleepDetectorRepository;
import ar.edu.um.temira.service.SleepDetectorService;
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
public class SleepDetectorServiceImpl implements SleepDetectorService {

    private static final Logger LOG = LoggerFactory.getLogger(SleepDetectorServiceImpl.class);

    private final SleepDetectorRepository sleepDetectorRepository;

    private final SleepDetectorMapper sleepDetectorMapper;

    public SleepDetectorServiceImpl(SleepDetectorRepository sleepDetectorRepository, SleepDetectorMapper sleepDetectorMapper) {
        this.sleepDetectorRepository = sleepDetectorRepository;
        this.sleepDetectorMapper = sleepDetectorMapper;
    }

    @Override
    public SleepDetectorDTO save(SleepDetectorDTO sleepDetectorDTO) {
        LOG.debug("Request to save SleepDetector : {}", sleepDetectorDTO);
        SleepDetector sleepDetector = sleepDetectorMapper.toEntity(sleepDetectorDTO);
        sleepDetector = sleepDetectorRepository.save(sleepDetector);
        return sleepDetectorMapper.toDto(sleepDetector);
    }

    @Override
    public SleepDetectorDTO update(SleepDetectorDTO sleepDetectorDTO) {
        LOG.debug("Request to update SleepDetector : {}", sleepDetectorDTO);
        SleepDetector sleepDetector = sleepDetectorMapper.toEntity(sleepDetectorDTO);
        sleepDetector = sleepDetectorRepository.save(sleepDetector);
        return sleepDetectorMapper.toDto(sleepDetector);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public Page<SleepDetectorDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SleepDetectors");
        return sleepDetectorRepository.findAll(pageable).map(sleepDetectorMapper::toDto);
    }

    public Page<SleepDetectorDTO> findAllWithEagerRelationships(Pageable pageable) {
        return sleepDetectorRepository.findAllWithEagerRelationships(pageable).map(sleepDetectorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SleepDetectorDTO> findOne(Long id) {
        LOG.debug("Request to get SleepDetector : {}", id);
        return sleepDetectorRepository.findOneWithEagerRelationships(id).map(sleepDetectorMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SleepDetector : {}", id);
        sleepDetectorRepository.deleteById(id);
    }
}
