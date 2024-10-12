package ar.edu.um.temira.web.rest;

import static ar.edu.um.temira.domain.VehicleBrandAsserts.*;
import static ar.edu.um.temira.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.temira.IntegrationTest;
import ar.edu.um.temira.domain.VehicleBrand;
import ar.edu.um.temira.repository.VehicleBrandRepository;
import ar.edu.um.temira.service.dto.VehicleBrandDTO;
import ar.edu.um.temira.service.mapper.VehicleBrandMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VehicleBrandResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VehicleBrandResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vehicle-brands";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VehicleBrandRepository vehicleBrandRepository;

    @Autowired
    private VehicleBrandMapper vehicleBrandMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehicleBrandMockMvc;

    private VehicleBrand vehicleBrand;

    private VehicleBrand insertedVehicleBrand;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleBrand createEntity() {
        return new VehicleBrand().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleBrand createUpdatedEntity() {
        return new VehicleBrand().name(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        vehicleBrand = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedVehicleBrand != null) {
            vehicleBrandRepository.delete(insertedVehicleBrand);
            insertedVehicleBrand = null;
        }
    }

    @Test
    @Transactional
    void createVehicleBrand() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the VehicleBrand
        VehicleBrandDTO vehicleBrandDTO = vehicleBrandMapper.toDto(vehicleBrand);
        var returnedVehicleBrandDTO = om.readValue(
            restVehicleBrandMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleBrandDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VehicleBrandDTO.class
        );

        // Validate the VehicleBrand in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVehicleBrand = vehicleBrandMapper.toEntity(returnedVehicleBrandDTO);
        assertVehicleBrandUpdatableFieldsEquals(returnedVehicleBrand, getPersistedVehicleBrand(returnedVehicleBrand));

        insertedVehicleBrand = returnedVehicleBrand;
    }

    @Test
    @Transactional
    void createVehicleBrandWithExistingId() throws Exception {
        // Create the VehicleBrand with an existing ID
        vehicleBrand.setId(1L);
        VehicleBrandDTO vehicleBrandDTO = vehicleBrandMapper.toDto(vehicleBrand);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleBrandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleBrandDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VehicleBrand in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicleBrand.setName(null);

        // Create the VehicleBrand, which fails.
        VehicleBrandDTO vehicleBrandDTO = vehicleBrandMapper.toDto(vehicleBrand);

        restVehicleBrandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleBrandDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVehicleBrands() throws Exception {
        // Initialize the database
        insertedVehicleBrand = vehicleBrandRepository.saveAndFlush(vehicleBrand);

        // Get all the vehicleBrandList
        restVehicleBrandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleBrand.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getVehicleBrand() throws Exception {
        // Initialize the database
        insertedVehicleBrand = vehicleBrandRepository.saveAndFlush(vehicleBrand);

        // Get the vehicleBrand
        restVehicleBrandMockMvc
            .perform(get(ENTITY_API_URL_ID, vehicleBrand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicleBrand.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingVehicleBrand() throws Exception {
        // Get the vehicleBrand
        restVehicleBrandMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVehicleBrand() throws Exception {
        // Initialize the database
        insertedVehicleBrand = vehicleBrandRepository.saveAndFlush(vehicleBrand);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleBrand
        VehicleBrand updatedVehicleBrand = vehicleBrandRepository.findById(vehicleBrand.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVehicleBrand are not directly saved in db
        em.detach(updatedVehicleBrand);
        updatedVehicleBrand.name(UPDATED_NAME);
        VehicleBrandDTO vehicleBrandDTO = vehicleBrandMapper.toDto(updatedVehicleBrand);

        restVehicleBrandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleBrandDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleBrandDTO))
            )
            .andExpect(status().isOk());

        // Validate the VehicleBrand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVehicleBrandToMatchAllProperties(updatedVehicleBrand);
    }

    @Test
    @Transactional
    void putNonExistingVehicleBrand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleBrand.setId(longCount.incrementAndGet());

        // Create the VehicleBrand
        VehicleBrandDTO vehicleBrandDTO = vehicleBrandMapper.toDto(vehicleBrand);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleBrandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleBrandDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleBrandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleBrand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehicleBrand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleBrand.setId(longCount.incrementAndGet());

        // Create the VehicleBrand
        VehicleBrandDTO vehicleBrandDTO = vehicleBrandMapper.toDto(vehicleBrand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleBrandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleBrandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleBrand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehicleBrand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleBrand.setId(longCount.incrementAndGet());

        // Create the VehicleBrand
        VehicleBrandDTO vehicleBrandDTO = vehicleBrandMapper.toDto(vehicleBrand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleBrandMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleBrandDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleBrand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVehicleBrandWithPatch() throws Exception {
        // Initialize the database
        insertedVehicleBrand = vehicleBrandRepository.saveAndFlush(vehicleBrand);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleBrand using partial update
        VehicleBrand partialUpdatedVehicleBrand = new VehicleBrand();
        partialUpdatedVehicleBrand.setId(vehicleBrand.getId());

        partialUpdatedVehicleBrand.name(UPDATED_NAME);

        restVehicleBrandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleBrand.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleBrand))
            )
            .andExpect(status().isOk());

        // Validate the VehicleBrand in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleBrandUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedVehicleBrand, vehicleBrand),
            getPersistedVehicleBrand(vehicleBrand)
        );
    }

    @Test
    @Transactional
    void fullUpdateVehicleBrandWithPatch() throws Exception {
        // Initialize the database
        insertedVehicleBrand = vehicleBrandRepository.saveAndFlush(vehicleBrand);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleBrand using partial update
        VehicleBrand partialUpdatedVehicleBrand = new VehicleBrand();
        partialUpdatedVehicleBrand.setId(vehicleBrand.getId());

        partialUpdatedVehicleBrand.name(UPDATED_NAME);

        restVehicleBrandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleBrand.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleBrand))
            )
            .andExpect(status().isOk());

        // Validate the VehicleBrand in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleBrandUpdatableFieldsEquals(partialUpdatedVehicleBrand, getPersistedVehicleBrand(partialUpdatedVehicleBrand));
    }

    @Test
    @Transactional
    void patchNonExistingVehicleBrand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleBrand.setId(longCount.incrementAndGet());

        // Create the VehicleBrand
        VehicleBrandDTO vehicleBrandDTO = vehicleBrandMapper.toDto(vehicleBrand);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleBrandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehicleBrandDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleBrandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleBrand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehicleBrand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleBrand.setId(longCount.incrementAndGet());

        // Create the VehicleBrand
        VehicleBrandDTO vehicleBrandDTO = vehicleBrandMapper.toDto(vehicleBrand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleBrandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleBrandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleBrand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehicleBrand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleBrand.setId(longCount.incrementAndGet());

        // Create the VehicleBrand
        VehicleBrandDTO vehicleBrandDTO = vehicleBrandMapper.toDto(vehicleBrand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleBrandMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vehicleBrandDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleBrand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVehicleBrand() throws Exception {
        // Initialize the database
        insertedVehicleBrand = vehicleBrandRepository.saveAndFlush(vehicleBrand);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vehicleBrand
        restVehicleBrandMockMvc
            .perform(delete(ENTITY_API_URL_ID, vehicleBrand.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vehicleBrandRepository.count();
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

    protected VehicleBrand getPersistedVehicleBrand(VehicleBrand vehicleBrand) {
        return vehicleBrandRepository.findById(vehicleBrand.getId()).orElseThrow();
    }

    protected void assertPersistedVehicleBrandToMatchAllProperties(VehicleBrand expectedVehicleBrand) {
        assertVehicleBrandAllPropertiesEquals(expectedVehicleBrand, getPersistedVehicleBrand(expectedVehicleBrand));
    }

    protected void assertPersistedVehicleBrandToMatchUpdatableProperties(VehicleBrand expectedVehicleBrand) {
        assertVehicleBrandAllUpdatablePropertiesEquals(expectedVehicleBrand, getPersistedVehicleBrand(expectedVehicleBrand));
    }
}
