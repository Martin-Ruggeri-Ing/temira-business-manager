package ar.edu.um.temira.service.impl;

import ar.edu.um.temira.domain.VehicleBrand;
import ar.edu.um.temira.repository.VehicleBrandRepository;
import ar.edu.um.temira.service.VehicleBrandService;
import ar.edu.um.temira.service.dto.VehicleBrandDTO;
import ar.edu.um.temira.service.mapper.VehicleBrandMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.temira.domain.VehicleBrand}.
 */
@Service
@Transactional
public class VehicleBrandServiceImpl implements VehicleBrandService {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleBrandServiceImpl.class);

    private final VehicleBrandRepository vehicleBrandRepository;

    private final VehicleBrandMapper vehicleBrandMapper;

    public VehicleBrandServiceImpl(VehicleBrandRepository vehicleBrandRepository, VehicleBrandMapper vehicleBrandMapper) {
        this.vehicleBrandRepository = vehicleBrandRepository;
        this.vehicleBrandMapper = vehicleBrandMapper;
    }

    @Override
    public VehicleBrandDTO save(VehicleBrandDTO vehicleBrandDTO) {
        LOG.debug("Request to save VehicleBrand : {}", vehicleBrandDTO);
        VehicleBrand vehicleBrand = vehicleBrandMapper.toEntity(vehicleBrandDTO);
        vehicleBrand = vehicleBrandRepository.save(vehicleBrand);
        return vehicleBrandMapper.toDto(vehicleBrand);
    }

    @Override
    public VehicleBrandDTO update(VehicleBrandDTO vehicleBrandDTO) {
        LOG.debug("Request to update VehicleBrand : {}", vehicleBrandDTO);
        VehicleBrand vehicleBrand = vehicleBrandMapper.toEntity(vehicleBrandDTO);
        vehicleBrand = vehicleBrandRepository.save(vehicleBrand);
        return vehicleBrandMapper.toDto(vehicleBrand);
    }

    @Override
    public Optional<VehicleBrandDTO> partialUpdate(VehicleBrandDTO vehicleBrandDTO) {
        LOG.debug("Request to partially update VehicleBrand : {}", vehicleBrandDTO);

        return vehicleBrandRepository
            .findById(vehicleBrandDTO.getId())
            .map(existingVehicleBrand -> {
                vehicleBrandMapper.partialUpdate(existingVehicleBrand, vehicleBrandDTO);

                return existingVehicleBrand;
            })
            .map(vehicleBrandRepository::save)
            .map(vehicleBrandMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VehicleBrandDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all VehicleBrands");
        return vehicleBrandRepository.findAll(pageable).map(vehicleBrandMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VehicleBrandDTO> findOne(Long id) {
        LOG.debug("Request to get VehicleBrand : {}", id);
        return vehicleBrandRepository.findById(id).map(vehicleBrandMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete VehicleBrand : {}", id);
        vehicleBrandRepository.deleteById(id);
    }
}
