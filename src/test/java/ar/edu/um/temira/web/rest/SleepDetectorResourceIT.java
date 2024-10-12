package ar.edu.um.temira.web.rest;

import static ar.edu.um.temira.domain.SleepDetectorAsserts.*;
import static ar.edu.um.temira.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.temira.IntegrationTest;
import ar.edu.um.temira.domain.SleepDetector;
import ar.edu.um.temira.repository.SleepDetectorRepository;
import ar.edu.um.temira.repository.UserRepository;
import ar.edu.um.temira.service.SleepDetectorService;
import ar.edu.um.temira.service.dto.SleepDetectorDTO;
import ar.edu.um.temira.service.mapper.SleepDetectorMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link SleepDetectorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SleepDetectorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sleep-detectors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SleepDetectorRepository sleepDetectorRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private SleepDetectorRepository sleepDetectorRepositoryMock;

    @Autowired
    private SleepDetectorMapper sleepDetectorMapper;

    @Mock
    private SleepDetectorService sleepDetectorServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSleepDetectorMockMvc;

    private SleepDetector sleepDetector;

    private SleepDetector insertedSleepDetector;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SleepDetector createEntity() {
        return new SleepDetector().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SleepDetector createUpdatedEntity() {
        return new SleepDetector().name(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        sleepDetector = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSleepDetector != null) {
            sleepDetectorRepository.delete(insertedSleepDetector);
            insertedSleepDetector = null;
        }
    }

    @Test
    @Transactional
    void createSleepDetector() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SleepDetector
        SleepDetectorDTO sleepDetectorDTO = sleepDetectorMapper.toDto(sleepDetector);
        var returnedSleepDetectorDTO = om.readValue(
            restSleepDetectorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sleepDetectorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SleepDetectorDTO.class
        );

        // Validate the SleepDetector in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSleepDetector = sleepDetectorMapper.toEntity(returnedSleepDetectorDTO);
        assertSleepDetectorUpdatableFieldsEquals(returnedSleepDetector, getPersistedSleepDetector(returnedSleepDetector));

        insertedSleepDetector = returnedSleepDetector;
    }

    @Test
    @Transactional
    void createSleepDetectorWithExistingId() throws Exception {
        // Create the SleepDetector with an existing ID
        sleepDetector.setId(1L);
        SleepDetectorDTO sleepDetectorDTO = sleepDetectorMapper.toDto(sleepDetector);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSleepDetectorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sleepDetectorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SleepDetector in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sleepDetector.setName(null);

        // Create the SleepDetector, which fails.
        SleepDetectorDTO sleepDetectorDTO = sleepDetectorMapper.toDto(sleepDetector);

        restSleepDetectorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sleepDetectorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSleepDetectors() throws Exception {
        // Initialize the database
        insertedSleepDetector = sleepDetectorRepository.saveAndFlush(sleepDetector);

        // Get all the sleepDetectorList
        restSleepDetectorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sleepDetector.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSleepDetectorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(sleepDetectorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSleepDetectorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(sleepDetectorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSleepDetectorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(sleepDetectorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSleepDetectorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(sleepDetectorRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSleepDetector() throws Exception {
        // Initialize the database
        insertedSleepDetector = sleepDetectorRepository.saveAndFlush(sleepDetector);

        // Get the sleepDetector
        restSleepDetectorMockMvc
            .perform(get(ENTITY_API_URL_ID, sleepDetector.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sleepDetector.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSleepDetector() throws Exception {
        // Get the sleepDetector
        restSleepDetectorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSleepDetector() throws Exception {
        // Initialize the database
        insertedSleepDetector = sleepDetectorRepository.saveAndFlush(sleepDetector);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sleepDetector
        SleepDetector updatedSleepDetector = sleepDetectorRepository.findById(sleepDetector.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSleepDetector are not directly saved in db
        em.detach(updatedSleepDetector);
        updatedSleepDetector.name(UPDATED_NAME);
        SleepDetectorDTO sleepDetectorDTO = sleepDetectorMapper.toDto(updatedSleepDetector);

        restSleepDetectorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sleepDetectorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sleepDetectorDTO))
            )
            .andExpect(status().isOk());

        // Validate the SleepDetector in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSleepDetectorToMatchAllProperties(updatedSleepDetector);
    }

    @Test
    @Transactional
    void putNonExistingSleepDetector() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sleepDetector.setId(longCount.incrementAndGet());

        // Create the SleepDetector
        SleepDetectorDTO sleepDetectorDTO = sleepDetectorMapper.toDto(sleepDetector);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSleepDetectorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sleepDetectorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sleepDetectorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SleepDetector in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSleepDetector() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sleepDetector.setId(longCount.incrementAndGet());

        // Create the SleepDetector
        SleepDetectorDTO sleepDetectorDTO = sleepDetectorMapper.toDto(sleepDetector);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSleepDetectorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sleepDetectorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SleepDetector in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSleepDetector() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sleepDetector.setId(longCount.incrementAndGet());

        // Create the SleepDetector
        SleepDetectorDTO sleepDetectorDTO = sleepDetectorMapper.toDto(sleepDetector);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSleepDetectorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sleepDetectorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SleepDetector in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSleepDetectorWithPatch() throws Exception {
        // Initialize the database
        insertedSleepDetector = sleepDetectorRepository.saveAndFlush(sleepDetector);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sleepDetector using partial update
        SleepDetector partialUpdatedSleepDetector = new SleepDetector();
        partialUpdatedSleepDetector.setId(sleepDetector.getId());

        restSleepDetectorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSleepDetector.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSleepDetector))
            )
            .andExpect(status().isOk());

        // Validate the SleepDetector in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSleepDetectorUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSleepDetector, sleepDetector),
            getPersistedSleepDetector(sleepDetector)
        );
    }

    @Test
    @Transactional
    void fullUpdateSleepDetectorWithPatch() throws Exception {
        // Initialize the database
        insertedSleepDetector = sleepDetectorRepository.saveAndFlush(sleepDetector);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sleepDetector using partial update
        SleepDetector partialUpdatedSleepDetector = new SleepDetector();
        partialUpdatedSleepDetector.setId(sleepDetector.getId());

        partialUpdatedSleepDetector.name(UPDATED_NAME);

        restSleepDetectorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSleepDetector.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSleepDetector))
            )
            .andExpect(status().isOk());

        // Validate the SleepDetector in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSleepDetectorUpdatableFieldsEquals(partialUpdatedSleepDetector, getPersistedSleepDetector(partialUpdatedSleepDetector));
    }

    @Test
    @Transactional
    void patchNonExistingSleepDetector() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sleepDetector.setId(longCount.incrementAndGet());

        // Create the SleepDetector
        SleepDetectorDTO sleepDetectorDTO = sleepDetectorMapper.toDto(sleepDetector);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSleepDetectorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sleepDetectorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sleepDetectorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SleepDetector in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSleepDetector() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sleepDetector.setId(longCount.incrementAndGet());

        // Create the SleepDetector
        SleepDetectorDTO sleepDetectorDTO = sleepDetectorMapper.toDto(sleepDetector);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSleepDetectorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sleepDetectorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SleepDetector in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSleepDetector() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sleepDetector.setId(longCount.incrementAndGet());

        // Create the SleepDetector
        SleepDetectorDTO sleepDetectorDTO = sleepDetectorMapper.toDto(sleepDetector);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSleepDetectorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(sleepDetectorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SleepDetector in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSleepDetector() throws Exception {
        // Initialize the database
        insertedSleepDetector = sleepDetectorRepository.saveAndFlush(sleepDetector);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sleepDetector
        restSleepDetectorMockMvc
            .perform(delete(ENTITY_API_URL_ID, sleepDetector.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sleepDetectorRepository.count();
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

    protected SleepDetector getPersistedSleepDetector(SleepDetector sleepDetector) {
        return sleepDetectorRepository.findById(sleepDetector.getId()).orElseThrow();
    }

    protected void assertPersistedSleepDetectorToMatchAllProperties(SleepDetector expectedSleepDetector) {
        assertSleepDetectorAllPropertiesEquals(expectedSleepDetector, getPersistedSleepDetector(expectedSleepDetector));
    }

    protected void assertPersistedSleepDetectorToMatchUpdatableProperties(SleepDetector expectedSleepDetector) {
        assertSleepDetectorAllUpdatablePropertiesEquals(expectedSleepDetector, getPersistedSleepDetector(expectedSleepDetector));
    }
}
