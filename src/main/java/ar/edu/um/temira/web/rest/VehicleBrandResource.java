package ar.edu.um.temira.web.rest;

import ar.edu.um.temira.repository.VehicleBrandRepository;
import ar.edu.um.temira.service.VehicleBrandService;
import ar.edu.um.temira.service.dto.VehicleBrandDTO;
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
 * REST controller for managing {@link ar.edu.um.temira.domain.VehicleBrand}.
 */
@RestController
@RequestMapping("/api/vehicle-brands")
public class VehicleBrandResource {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleBrandResource.class);

    private static final String ENTITY_NAME = "vehicleBrand";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VehicleBrandService vehicleBrandService;

    private final VehicleBrandRepository vehicleBrandRepository;

    public VehicleBrandResource(VehicleBrandService vehicleBrandService, VehicleBrandRepository vehicleBrandRepository) {
        this.vehicleBrandService = vehicleBrandService;
        this.vehicleBrandRepository = vehicleBrandRepository;
    }

    /**
     * {@code POST  /vehicle-brands} : Create a new vehicleBrand.
     *
     * @param vehicleBrandDTO the vehicleBrandDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vehicleBrandDTO, or with status {@code 400 (Bad Request)} if the vehicleBrand has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VehicleBrandDTO> createVehicleBrand(@Valid @RequestBody VehicleBrandDTO vehicleBrandDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save VehicleBrand : {}", vehicleBrandDTO);
        if (vehicleBrandDTO.getId() != null) {
            throw new BadRequestAlertException("A new vehicleBrand cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vehicleBrandDTO = vehicleBrandService.save(vehicleBrandDTO);
        return ResponseEntity.created(new URI("/api/vehicle-brands/" + vehicleBrandDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, vehicleBrandDTO.getId().toString()))
            .body(vehicleBrandDTO);
    }

    /**
     * {@code PUT  /vehicle-brands/:id} : Updates an existing vehicleBrand.
     *
     * @param id the id of the vehicleBrandDTO to save.
     * @param vehicleBrandDTO the vehicleBrandDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleBrandDTO,
     * or with status {@code 400 (Bad Request)} if the vehicleBrandDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vehicleBrandDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleBrandDTO> updateVehicleBrand(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VehicleBrandDTO vehicleBrandDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update VehicleBrand : {}, {}", id, vehicleBrandDTO);
        if (vehicleBrandDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleBrandDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleBrandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vehicleBrandDTO = vehicleBrandService.update(vehicleBrandDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicleBrandDTO.getId().toString()))
            .body(vehicleBrandDTO);
    }

    /**
     * {@code PATCH  /vehicle-brands/:id} : Partial updates given fields of an existing vehicleBrand, field will ignore if it is null
     *
     * @param id the id of the vehicleBrandDTO to save.
     * @param vehicleBrandDTO the vehicleBrandDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleBrandDTO,
     * or with status {@code 400 (Bad Request)} if the vehicleBrandDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vehicleBrandDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vehicleBrandDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VehicleBrandDTO> partialUpdateVehicleBrand(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VehicleBrandDTO vehicleBrandDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update VehicleBrand partially : {}, {}", id, vehicleBrandDTO);
        if (vehicleBrandDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleBrandDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleBrandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VehicleBrandDTO> result = vehicleBrandService.partialUpdate(vehicleBrandDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicleBrandDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vehicle-brands} : get all the vehicleBrands.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vehicleBrands in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VehicleBrandDTO>> getAllVehicleBrands(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of VehicleBrands");
        Page<VehicleBrandDTO> page = vehicleBrandService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vehicle-brands/:id} : get the "id" vehicleBrand.
     *
     * @param id the id of the vehicleBrandDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vehicleBrandDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleBrandDTO> getVehicleBrand(@PathVariable("id") Long id) {
        LOG.debug("REST request to get VehicleBrand : {}", id);
        Optional<VehicleBrandDTO> vehicleBrandDTO = vehicleBrandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vehicleBrandDTO);
    }

    /**
     * {@code DELETE  /vehicle-brands/:id} : delete the "id" vehicleBrand.
     *
     * @param id the id of the vehicleBrandDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicleBrand(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete VehicleBrand : {}", id);
        vehicleBrandService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
