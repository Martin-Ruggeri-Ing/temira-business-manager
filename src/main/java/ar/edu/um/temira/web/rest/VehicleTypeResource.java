package ar.edu.um.temira.web.rest;

import ar.edu.um.temira.repository.VehicleTypeRepository;
import ar.edu.um.temira.service.VehicleTypeService;
import ar.edu.um.temira.service.dto.VehicleTypeDTO;
import ar.edu.um.temira.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ar.edu.um.temira.domain.VehicleType}.
 */
@RestController
@RequestMapping("/api/vehicle-types")
public class VehicleTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleTypeResource.class);

    private static final String ENTITY_NAME = "vehicleType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VehicleTypeService vehicleTypeService;

    private final VehicleTypeRepository vehicleTypeRepository;

    public VehicleTypeResource(VehicleTypeService vehicleTypeService, VehicleTypeRepository vehicleTypeRepository) {
        this.vehicleTypeService = vehicleTypeService;
        this.vehicleTypeRepository = vehicleTypeRepository;
    }

    /**
     * {@code POST  /vehicle-types} : Create a new vehicleType.
     *
     * @param vehicleTypeDTO the vehicleTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vehicleTypeDTO, or with status {@code 400 (Bad Request)} if the vehicleType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VehicleTypeDTO> createVehicleType(@Valid @RequestBody VehicleTypeDTO vehicleTypeDTO) throws URISyntaxException {
        LOG.debug("REST request to save VehicleType : {}", vehicleTypeDTO);
        if (vehicleTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new vehicleType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vehicleTypeDTO = vehicleTypeService.save(vehicleTypeDTO);
        return ResponseEntity.created(new URI("/api/vehicle-types/" + vehicleTypeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, vehicleTypeDTO.getId().toString()))
            .body(vehicleTypeDTO);
    }

    /**
     * {@code PUT  /vehicle-types/:id} : Updates an existing vehicleType.
     *
     * @param id the id of the vehicleTypeDTO to save.
     * @param vehicleTypeDTO the vehicleTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleTypeDTO,
     * or with status {@code 400 (Bad Request)} if the vehicleTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vehicleTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleTypeDTO> updateVehicleType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VehicleTypeDTO vehicleTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update VehicleType : {}, {}", id, vehicleTypeDTO);
        if (vehicleTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vehicleTypeDTO = vehicleTypeService.update(vehicleTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicleTypeDTO.getId().toString()))
            .body(vehicleTypeDTO);
    }

    /**
     * {@code PATCH  /vehicle-types/:id} : Partial updates given fields of an existing vehicleType, field will ignore if it is null
     *
     * @param id the id of the vehicleTypeDTO to save.
     * @param vehicleTypeDTO the vehicleTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleTypeDTO,
     * or with status {@code 400 (Bad Request)} if the vehicleTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vehicleTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vehicleTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VehicleTypeDTO> partialUpdateVehicleType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VehicleTypeDTO vehicleTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update VehicleType partially : {}, {}", id, vehicleTypeDTO);
        if (vehicleTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VehicleTypeDTO> result = vehicleTypeService.partialUpdate(vehicleTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicleTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vehicle-types} : get all the vehicleTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vehicleTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VehicleTypeDTO>> getAllVehicleTypes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of VehicleTypes");
        Page<VehicleTypeDTO> page = vehicleTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vehicle-types/:id} : get the "id" vehicleType.
     *
     * @param id the id of the vehicleTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vehicleTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleTypeDTO> getVehicleType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get VehicleType : {}", id);
        Optional<VehicleTypeDTO> vehicleTypeDTO = vehicleTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vehicleTypeDTO);
    }

    /**
     * {@code DELETE  /vehicle-types/:id} : delete the "id" vehicleType.
     *
     * @param id the id of the vehicleTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicleType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete VehicleType : {}", id);
        vehicleTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
