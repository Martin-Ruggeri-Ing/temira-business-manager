package ar.edu.um.temira.web.rest;

import ar.edu.um.temira.repository.SleepDetectorRepository;
import ar.edu.um.temira.service.SleepDetectorService;
import ar.edu.um.temira.service.dto.SleepDetectorDTO;
import ar.edu.um.temira.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link ar.edu.um.temira.domain.SleepDetector}.
 */
@RestController
@RequestMapping("/api/sleep-detectors")
public class SleepDetectorResource {

    private static final Logger LOG = LoggerFactory.getLogger(SleepDetectorResource.class);

    private static final String ENTITY_NAME = "sleepDetector";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SleepDetectorService sleepDetectorService;

    private final SleepDetectorRepository sleepDetectorRepository;

    public SleepDetectorResource(SleepDetectorService sleepDetectorService, SleepDetectorRepository sleepDetectorRepository) {
        this.sleepDetectorService = sleepDetectorService;
        this.sleepDetectorRepository = sleepDetectorRepository;
    }

    /**
     * {@code POST  /sleep-detectors} : Create a new sleepDetector.
     *
     * @param sleepDetectorDTO the sleepDetectorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sleepDetectorDTO, or with status {@code 400 (Bad Request)} if the sleepDetector has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SleepDetectorDTO> createSleepDetector(@RequestBody SleepDetectorDTO sleepDetectorDTO) throws URISyntaxException {
        LOG.debug("REST request to save SleepDetector : {}", sleepDetectorDTO);
        if (sleepDetectorDTO.getId() != null) {
            throw new BadRequestAlertException("A new sleepDetector cannot already have an ID", ENTITY_NAME, "idexists");
        }
        sleepDetectorDTO = sleepDetectorService.save(sleepDetectorDTO);
        return ResponseEntity.created(new URI("/api/sleep-detectors/" + sleepDetectorDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, sleepDetectorDTO.getId().toString()))
            .body(sleepDetectorDTO);
    }

    /**
     * {@code PUT  /sleep-detectors/:id} : Updates an existing sleepDetector.
     *
     * @param id the id of the sleepDetectorDTO to save.
     * @param sleepDetectorDTO the sleepDetectorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sleepDetectorDTO,
     * or with status {@code 400 (Bad Request)} if the sleepDetectorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sleepDetectorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SleepDetectorDTO> updateSleepDetector(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SleepDetectorDTO sleepDetectorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SleepDetector : {}, {}", id, sleepDetectorDTO);
        if (sleepDetectorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sleepDetectorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sleepDetectorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        sleepDetectorDTO = sleepDetectorService.update(sleepDetectorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sleepDetectorDTO.getId().toString()))
            .body(sleepDetectorDTO);
    }

    /**
     * {@code PATCH  /sleep-detectors/:id} : Partial updates given fields of an existing sleepDetector, field will ignore if it is null
     *
     * @param id the id of the sleepDetectorDTO to save.
     * @param sleepDetectorDTO the sleepDetectorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sleepDetectorDTO,
     * or with status {@code 400 (Bad Request)} if the sleepDetectorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sleepDetectorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sleepDetectorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SleepDetectorDTO> partialUpdateSleepDetector(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SleepDetectorDTO sleepDetectorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SleepDetector partially : {}, {}", id, sleepDetectorDTO);
        if (sleepDetectorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sleepDetectorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sleepDetectorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SleepDetectorDTO> result = sleepDetectorService.partialUpdate(sleepDetectorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sleepDetectorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sleep-detectors} : get all the sleepDetectors.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sleepDetectors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SleepDetectorDTO>> getAllSleepDetectors(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of SleepDetectors");
        Page<SleepDetectorDTO> page;
        if (eagerload) {
            page = sleepDetectorService.findAllWithEagerRelationships(pageable);
        } else {
            page = sleepDetectorService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sleep-detectors/:id} : get the "id" sleepDetector.
     *
     * @param id the id of the sleepDetectorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sleepDetectorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SleepDetectorDTO> getSleepDetector(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SleepDetector : {}", id);
        Optional<SleepDetectorDTO> sleepDetectorDTO = sleepDetectorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sleepDetectorDTO);
    }

    /**
     * {@code DELETE  /sleep-detectors/:id} : delete the "id" sleepDetector.
     *
     * @param id the id of the sleepDetectorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSleepDetector(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SleepDetector : {}", id);
        sleepDetectorService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
