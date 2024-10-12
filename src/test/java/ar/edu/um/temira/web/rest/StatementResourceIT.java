package ar.edu.um.temira.web.rest;

import static ar.edu.um.temira.domain.StatementAsserts.*;
import static ar.edu.um.temira.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.temira.IntegrationTest;
import ar.edu.um.temira.domain.Statement;
import ar.edu.um.temira.repository.StatementRepository;
import ar.edu.um.temira.repository.UserRepository;
import ar.edu.um.temira.service.StatementService;
import ar.edu.um.temira.service.dto.StatementDTO;
import ar.edu.um.temira.service.mapper.StatementMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link StatementResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class StatementResourceIT {

    private static final Instant DEFAULT_DATE_CREATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESTINATION = "AAAAAAAAAA";
    private static final String UPDATED_DESTINATION = "BBBBBBBBBB";

    private static final String DEFAULT_PATH_CSV = "AAAAAAAAAA";
    private static final String UPDATED_PATH_CSV = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/statements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StatementRepository statementRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private StatementRepository statementRepositoryMock;

    @Autowired
    private StatementMapper statementMapper;

    @Mock
    private StatementService statementServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStatementMockMvc;

    private Statement statement;

    private Statement insertedStatement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Statement createEntity() {
        return new Statement().dateCreation(DEFAULT_DATE_CREATION).destination(DEFAULT_DESTINATION).pathCsv(DEFAULT_PATH_CSV);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Statement createUpdatedEntity() {
        return new Statement().dateCreation(UPDATED_DATE_CREATION).destination(UPDATED_DESTINATION).pathCsv(UPDATED_PATH_CSV);
    }

    @BeforeEach
    public void initTest() {
        statement = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedStatement != null) {
            statementRepository.delete(insertedStatement);
            insertedStatement = null;
        }
    }

    @Test
    @Transactional
    void createStatement() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Statement
        StatementDTO statementDTO = statementMapper.toDto(statement);
        var returnedStatementDTO = om.readValue(
            restStatementMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(statementDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StatementDTO.class
        );

        // Validate the Statement in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStatement = statementMapper.toEntity(returnedStatementDTO);
        assertStatementUpdatableFieldsEquals(returnedStatement, getPersistedStatement(returnedStatement));

        insertedStatement = returnedStatement;
    }

    @Test
    @Transactional
    void createStatementWithExistingId() throws Exception {
        // Create the Statement with an existing ID
        statement.setId(1L);
        StatementDTO statementDTO = statementMapper.toDto(statement);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStatementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(statementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Statement in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateCreationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        statement.setDateCreation(null);

        // Create the Statement, which fails.
        StatementDTO statementDTO = statementMapper.toDto(statement);

        restStatementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(statementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDestinationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        statement.setDestination(null);

        // Create the Statement, which fails.
        StatementDTO statementDTO = statementMapper.toDto(statement);

        restStatementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(statementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPathCsvIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        statement.setPathCsv(null);

        // Create the Statement, which fails.
        StatementDTO statementDTO = statementMapper.toDto(statement);

        restStatementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(statementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStatements() throws Exception {
        // Initialize the database
        insertedStatement = statementRepository.saveAndFlush(statement);

        // Get all the statementList
        restStatementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(statement.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].destination").value(hasItem(DEFAULT_DESTINATION)))
            .andExpect(jsonPath("$.[*].pathCsv").value(hasItem(DEFAULT_PATH_CSV)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStatementsWithEagerRelationshipsIsEnabled() throws Exception {
        when(statementServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStatementMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(statementServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStatementsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(statementServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStatementMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(statementRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getStatement() throws Exception {
        // Initialize the database
        insertedStatement = statementRepository.saveAndFlush(statement);

        // Get the statement
        restStatementMockMvc
            .perform(get(ENTITY_API_URL_ID, statement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(statement.getId().intValue()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.destination").value(DEFAULT_DESTINATION))
            .andExpect(jsonPath("$.pathCsv").value(DEFAULT_PATH_CSV));
    }

    @Test
    @Transactional
    void getNonExistingStatement() throws Exception {
        // Get the statement
        restStatementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStatement() throws Exception {
        // Initialize the database
        insertedStatement = statementRepository.saveAndFlush(statement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the statement
        Statement updatedStatement = statementRepository.findById(statement.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStatement are not directly saved in db
        em.detach(updatedStatement);
        updatedStatement.dateCreation(UPDATED_DATE_CREATION).destination(UPDATED_DESTINATION).pathCsv(UPDATED_PATH_CSV);
        StatementDTO statementDTO = statementMapper.toDto(updatedStatement);

        restStatementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, statementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(statementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Statement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStatementToMatchAllProperties(updatedStatement);
    }

    @Test
    @Transactional
    void putNonExistingStatement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        statement.setId(longCount.incrementAndGet());

        // Create the Statement
        StatementDTO statementDTO = statementMapper.toDto(statement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, statementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(statementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Statement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStatement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        statement.setId(longCount.incrementAndGet());

        // Create the Statement
        StatementDTO statementDTO = statementMapper.toDto(statement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(statementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Statement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStatement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        statement.setId(longCount.incrementAndGet());

        // Create the Statement
        StatementDTO statementDTO = statementMapper.toDto(statement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(statementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Statement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStatementWithPatch() throws Exception {
        // Initialize the database
        insertedStatement = statementRepository.saveAndFlush(statement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the statement using partial update
        Statement partialUpdatedStatement = new Statement();
        partialUpdatedStatement.setId(statement.getId());

        partialUpdatedStatement.destination(UPDATED_DESTINATION).pathCsv(UPDATED_PATH_CSV);

        restStatementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStatement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStatement))
            )
            .andExpect(status().isOk());

        // Validate the Statement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStatementUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStatement, statement),
            getPersistedStatement(statement)
        );
    }

    @Test
    @Transactional
    void fullUpdateStatementWithPatch() throws Exception {
        // Initialize the database
        insertedStatement = statementRepository.saveAndFlush(statement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the statement using partial update
        Statement partialUpdatedStatement = new Statement();
        partialUpdatedStatement.setId(statement.getId());

        partialUpdatedStatement.dateCreation(UPDATED_DATE_CREATION).destination(UPDATED_DESTINATION).pathCsv(UPDATED_PATH_CSV);

        restStatementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStatement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStatement))
            )
            .andExpect(status().isOk());

        // Validate the Statement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStatementUpdatableFieldsEquals(partialUpdatedStatement, getPersistedStatement(partialUpdatedStatement));
    }

    @Test
    @Transactional
    void patchNonExistingStatement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        statement.setId(longCount.incrementAndGet());

        // Create the Statement
        StatementDTO statementDTO = statementMapper.toDto(statement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, statementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(statementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Statement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStatement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        statement.setId(longCount.incrementAndGet());

        // Create the Statement
        StatementDTO statementDTO = statementMapper.toDto(statement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(statementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Statement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStatement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        statement.setId(longCount.incrementAndGet());

        // Create the Statement
        StatementDTO statementDTO = statementMapper.toDto(statement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatementMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(statementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Statement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStatement() throws Exception {
        // Initialize the database
        insertedStatement = statementRepository.saveAndFlush(statement);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the statement
        restStatementMockMvc
            .perform(delete(ENTITY_API_URL_ID, statement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return statementRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Statement getPersistedStatement(Statement statement) {
        return statementRepository.findById(statement.getId()).orElseThrow();
    }

    protected void assertPersistedStatementToMatchAllProperties(Statement expectedStatement) {
        assertStatementAllPropertiesEquals(expectedStatement, getPersistedStatement(expectedStatement));
    }

    protected void assertPersistedStatementToMatchUpdatableProperties(Statement expectedStatement) {
        assertStatementAllUpdatablePropertiesEquals(expectedStatement, getPersistedStatement(expectedStatement));
    }
}
