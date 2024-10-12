package ar.edu.um.temira.service.impl;

import ar.edu.um.temira.domain.Driver;
import ar.edu.um.temira.repository.DriverRepository;
import ar.edu.um.temira.service.DriverService;
import ar.edu.um.temira.service.dto.DriverDTO;
import ar.edu.um.temira.service.mapper.DriverMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.temira.domain.Driver}.
 */
@Service
@Transactional
public class DriverServiceImpl implements DriverService {

    private static final Logger LOG = LoggerFactory.getLogger(DriverServiceImpl.class);

    private final DriverRepository driverRepository;

    private final DriverMapper driverMapper;

    public DriverServiceImpl(DriverRepository driverRepository, DriverMapper driverMapper) {
        this.driverRepository = driverRepository;
        this.driverMapper = driverMapper;
    }

    @Override
    public DriverDTO save(DriverDTO driverDTO) {
        LOG.debug("Request to save Driver : {}", driverDTO);
        Driver driver = driverMapper.toEntity(driverDTO);
        driver = driverRepository.save(driver);
        return driverMapper.toDto(driver);
    }

    @Override
    public DriverDTO update(DriverDTO driverDTO) {
        LOG.debug("Request to update Driver : {}", driverDTO);
        Driver driver = driverMapper.toEntity(driverDTO);
        driver = driverRepository.save(driver);
        return driverMapper.toDto(driver);
    }

    @Override
    public Optional<DriverDTO> partialUpdate(DriverDTO driverDTO) {
        LOG.debug("Request to partially update Driver : {}", driverDTO);

        return driverRepository
            .findById(driverDTO.getId())
            .map(existingDriver -> {
                driverMapper.partialUpdate(existingDriver, driverDTO);

                return existingDriver;
            })
            .map(driverRepository::save)
            .map(driverMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DriverDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Drivers");
        return driverRepository.findAll(pageable).map(driverMapper::toDto);
    }

    public Page<DriverDTO> findAllWithEagerRelationships(Pageable pageable) {
        return driverRepository.findAllWithEagerRelationships(pageable).map(driverMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DriverDTO> findOne(Long id) {
        LOG.debug("Request to get Driver : {}", id);
        return driverRepository.findOneWithEagerRelationships(id).map(driverMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Driver : {}", id);
        driverRepository.deleteById(id);
    }
}
