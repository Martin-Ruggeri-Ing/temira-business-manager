package ar.edu.um.temira.web.rest;

import ar.edu.um.temira.repository.StatementRepository;
import ar.edu.um.temira.service.StatementService;
import ar.edu.um.temira.service.dto.StatementDTO;
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
 * REST controller for managing {@link ar.edu.um.temira.domain.Statement}.
 */
@RestController
@RequestMapping("/api/statements")
public class StatementResource {

    private static final Logger LOG = LoggerFactory.getLogger(StatementResource.class);

    private static final String ENTITY_NAME = "statement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StatementService statementService;

    private final StatementRepository statementRepository;

    public StatementResource(StatementService statementService, StatementRepository statementRepository) {
        this.statementService = statementService;
        this.statementRepository = statementRepository;
    }

    /**
     * {@code POST  /statements} : Create a new statement.
     *
     * @param statementDTO the statementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new statementDTO, or with status {@code 400 (Bad Request)} if the statement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StatementDTO> createStatement(@Valid @RequestBody StatementDTO statementDTO) throws URISyntaxException {
        LOG.debug("REST request to save Statement : {}", statementDTO);
        if (statementDTO.getId() != null) {
            throw new BadRequestAlertException("A new statement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        statementDTO = statementService.save(statementDTO);
        return ResponseEntity.created(new URI("/api/statements/" + statementDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, statementDTO.getId().toString()))
            .body(statementDTO);
    }

    /**
     * {@code PUT  /statements/:id} : Updates an existing statement.
     *
     * @param id the id of the statementDTO to save.
     * @param statementDTO the statementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated statementDTO,
     * or with status {@code 400 (Bad Request)} if the statementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the statementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StatementDTO> updateStatement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StatementDTO statementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Statement : {}, {}", id, statementDTO);
        if (statementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, statementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!statementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        statementDTO = statementService.update(statementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, statementDTO.getId().toString()))
            .body(statementDTO);
    }

    /**
     * {@code PATCH  /statements/:id} : Partial updates given fields of an existing statement, field will ignore if it is null
     *
     * @param id the id of the statementDTO to save.
     * @param statementDTO the statementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated statementDTO,
     * or with status {@code 400 (Bad Request)} if the statementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the statementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the statementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StatementDTO> partialUpdateStatement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StatementDTO statementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Statement partially : {}, {}", id, statementDTO);
        if (statementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, statementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!statementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StatementDTO> result = statementService.partialUpdate(statementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, statementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /statements} : get all the statements.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of statements in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StatementDTO>> getAllStatements(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Statements");
        Page<StatementDTO> page;
        if (eagerload) {
            page = statementService.findAllWithEagerRelationships(pageable);
        } else {
            page = statementService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /statements/:id} : get the "id" statement.
     *
     * @param id the id of the statementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the statementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StatementDTO> getStatement(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Statement : {}", id);
        Optional<StatementDTO> statementDTO = statementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(statementDTO);
    }

    /**
     * {@code DELETE  /statements/:id} : delete the "id" statement.
     *
     * @param id the id of the statementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatement(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Statement : {}", id);
        statementService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
