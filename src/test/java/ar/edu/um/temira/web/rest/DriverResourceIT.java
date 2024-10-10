package ar.edu.um.temira.web.rest;

import static ar.edu.um.temira.domain.DriverAsserts.*;
import static ar.edu.um.temira.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.temira.IntegrationTest;
import ar.edu.um.temira.domain.Driver;
import ar.edu.um.temira.repository.DriverRepository;
import ar.edu.um.temira.repository.UserRepository;
import ar.edu.um.temira.service.DriverService;
import ar.edu.um.temira.service.dto.DriverDTO;
import ar.edu.um.temira.service.mapper.DriverMapper;
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
 * Integration tests for the {@link DriverResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DriverResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/drivers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private DriverRepository driverRepositoryMock;

    @Autowired
    private DriverMapper driverMapper;

    @Mock
    private DriverService driverServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDriverMockMvc;

    private Driver driver;

    private Driver insertedDriver;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Driver createEntity() {
        return new Driver().firstName(DEFAULT_FIRST_NAME).lastName(DEFAULT_LAST_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Driver createUpdatedEntity() {
        return new Driver().firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME);
    }

    @BeforeEach
    public void initTest() {
        driver = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDriver != null) {
            driverRepository.delete(insertedDriver);
            insertedDriver = null;
        }
    }

    @Test
    @Transactional
    void createDriver() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);
        var returnedDriverDTO = om.readValue(
            restDriverMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(driverDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DriverDTO.class
        );

        // Validate the Driver in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDriver = driverMapper.toEntity(returnedDriverDTO);
        assertDriverUpdatableFieldsEquals(returnedDriver, getPersistedDriver(returnedDriver));

        insertedDriver = returnedDriver;
    }

    @Test
    @Transactional
    void createDriverWithExistingId() throws Exception {
        // Create the Driver with an existing ID
        driver.setId(1L);
        DriverDTO driverDTO = driverMapper.toDto(driver);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDriverMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(driverDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Driver in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        driver.setFirstName(null);

        // Create the Driver, which fails.
        DriverDTO driverDTO = driverMapper.toDto(driver);

        restDriverMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(driverDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        driver.setLastName(null);

        // Create the Driver, which fails.
        DriverDTO driverDTO = driverMapper.toDto(driver);

        restDriverMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(driverDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDrivers() throws Exception {
        // Initialize the database
        insertedDriver = driverRepository.saveAndFlush(driver);

        // Get all the driverList
        restDriverMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(driver.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDriversWithEagerRelationshipsIsEnabled() throws Exception {
        when(driverServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDriverMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(driverServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDriversWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(driverServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDriverMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(driverRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDriver() throws Exception {
        // Initialize the database
        insertedDriver = driverRepository.saveAndFlush(driver);

        // Get the driver
        restDriverMockMvc
            .perform(get(ENTITY_API_URL_ID, driver.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(driver.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME));
    }

    @Test
    @Transactional
    void getNonExistingDriver() throws Exception {
        // Get the driver
        restDriverMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDriver() throws Exception {
        // Initialize the database
        insertedDriver = driverRepository.saveAndFlush(driver);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the driver
        Driver updatedDriver = driverRepository.findById(driver.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDriver are not directly saved in db
        em.detach(updatedDriver);
        updatedDriver.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME);
        DriverDTO driverDTO = driverMapper.toDto(updatedDriver);

        restDriverMockMvc
            .perform(
                put(ENTITY_API_URL_ID, driverDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(driverDTO))
            )
            .andExpect(status().isOk());

        // Validate the Driver in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDriverToMatchAllProperties(updatedDriver);
    }

    @Test
    @Transactional
    void putNonExistingDriver() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        driver.setId(longCount.incrementAndGet());

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDriverMockMvc
            .perform(
                put(ENTITY_API_URL_ID, driverDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(driverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Driver in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDriver() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        driver.setId(longCount.incrementAndGet());

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDriverMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(driverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Driver in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDriver() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        driver.setId(longCount.incrementAndGet());

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDriverMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(driverDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Driver in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDriverWithPatch() throws Exception {
        // Initialize the database
        insertedDriver = driverRepository.saveAndFlush(driver);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the driver using partial update
        Driver partialUpdatedDriver = new Driver();
        partialUpdatedDriver.setId(driver.getId());

        restDriverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDriver.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDriver))
            )
            .andExpect(status().isOk());

        // Validate the Driver in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDriverUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDriver, driver), getPersistedDriver(driver));
    }

    @Test
    @Transactional
    void fullUpdateDriverWithPatch() throws Exception {
        // Initialize the database
        insertedDriver = driverRepository.saveAndFlush(driver);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the driver using partial update
        Driver partialUpdatedDriver = new Driver();
        partialUpdatedDriver.setId(driver.getId());

        partialUpdatedDriver.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME);

        restDriverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDriver.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDriver))
            )
            .andExpect(status().isOk());

        // Validate the Driver in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDriverUpdatableFieldsEquals(partialUpdatedDriver, getPersistedDriver(partialUpdatedDriver));
    }

    @Test
    @Transactional
    void patchNonExistingDriver() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        driver.setId(longCount.incrementAndGet());

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDriverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, driverDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(driverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Driver in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDriver() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        driver.setId(longCount.incrementAndGet());

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDriverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(driverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Driver in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDriver() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        driver.setId(longCount.incrementAndGet());

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDriverMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(driverDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Driver in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDriver() throws Exception {
        // Initialize the database
        insertedDriver = driverRepository.saveAndFlush(driver);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the driver
        restDriverMockMvc
            .perform(delete(ENTITY_API_URL_ID, driver.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return driverRepository.count();
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

    protected Driver getPersistedDriver(Driver driver) {
        return driverRepository.findById(driver.getId()).orElseThrow();
    }

    protected void assertPersistedDriverToMatchAllProperties(Driver expectedDriver) {
        assertDriverAllPropertiesEquals(expectedDriver, getPersistedDriver(expectedDriver));
    }

    protected void assertPersistedDriverToMatchUpdatableProperties(Driver expectedDriver) {
        assertDriverAllUpdatablePropertiesEquals(expectedDriver, getPersistedDriver(expectedDriver));
    }
}
