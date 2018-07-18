package com.total.newappli.web.rest;

import com.total.newappli.OffreContratApp;

import com.total.newappli.domain.TypeDeContenu;
import com.total.newappli.repository.TypeDeContenuRepository;
import com.total.newappli.service.TypeDeContenuService;
import com.total.newappli.service.dto.TypeDeContenuDTO;
import com.total.newappli.service.mapper.TypeDeContenuMapper;
import com.total.newappli.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static com.total.newappli.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TypeDeContenuResource REST controller.
 *
 * @see TypeDeContenuResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OffreContratApp.class)
public class TypeDeContenuResourceIntTest {

    private static final String DEFAULT_DENOMINATION_AR = "AAAAAAAAAA";
    private static final String UPDATED_DENOMINATION_AR = "BBBBBBBBBB";

    private static final String DEFAULT_DENOMINATION_FR = "AAAAAAAAAA";
    private static final String UPDATED_DENOMINATION_FR = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    @Autowired
    private TypeDeContenuRepository typeDeContenuRepository;


    @Autowired
    private TypeDeContenuMapper typeDeContenuMapper;
    

    @Autowired
    private TypeDeContenuService typeDeContenuService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTypeDeContenuMockMvc;

    private TypeDeContenu typeDeContenu;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TypeDeContenuResource typeDeContenuResource = new TypeDeContenuResource(typeDeContenuService);
        this.restTypeDeContenuMockMvc = MockMvcBuilders.standaloneSetup(typeDeContenuResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeDeContenu createEntity(EntityManager em) {
        TypeDeContenu typeDeContenu = new TypeDeContenu()
            .denominationAr(DEFAULT_DENOMINATION_AR)
            .denominationFr(DEFAULT_DENOMINATION_FR)
            .reference(DEFAULT_REFERENCE)
            .code(DEFAULT_CODE);
        return typeDeContenu;
    }

    @Before
    public void initTest() {
        typeDeContenu = createEntity(em);
    }

    @Test
    @Transactional
    public void createTypeDeContenu() throws Exception {
        int databaseSizeBeforeCreate = typeDeContenuRepository.findAll().size();

        // Create the TypeDeContenu
        TypeDeContenuDTO typeDeContenuDTO = typeDeContenuMapper.toDto(typeDeContenu);
        restTypeDeContenuMockMvc.perform(post("/api/type-de-contenus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeDeContenuDTO)))
            .andExpect(status().isCreated());

        // Validate the TypeDeContenu in the database
        List<TypeDeContenu> typeDeContenuList = typeDeContenuRepository.findAll();
        assertThat(typeDeContenuList).hasSize(databaseSizeBeforeCreate + 1);
        TypeDeContenu testTypeDeContenu = typeDeContenuList.get(typeDeContenuList.size() - 1);
        assertThat(testTypeDeContenu.getDenominationAr()).isEqualTo(DEFAULT_DENOMINATION_AR);
        assertThat(testTypeDeContenu.getDenominationFr()).isEqualTo(DEFAULT_DENOMINATION_FR);
        assertThat(testTypeDeContenu.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testTypeDeContenu.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    public void createTypeDeContenuWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = typeDeContenuRepository.findAll().size();

        // Create the TypeDeContenu with an existing ID
        typeDeContenu.setId(1L);
        TypeDeContenuDTO typeDeContenuDTO = typeDeContenuMapper.toDto(typeDeContenu);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeDeContenuMockMvc.perform(post("/api/type-de-contenus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeDeContenuDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeDeContenu in the database
        List<TypeDeContenu> typeDeContenuList = typeDeContenuRepository.findAll();
        assertThat(typeDeContenuList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkReferenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeDeContenuRepository.findAll().size();
        // set the field null
        typeDeContenu.setReference(null);

        // Create the TypeDeContenu, which fails.
        TypeDeContenuDTO typeDeContenuDTO = typeDeContenuMapper.toDto(typeDeContenu);

        restTypeDeContenuMockMvc.perform(post("/api/type-de-contenus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeDeContenuDTO)))
            .andExpect(status().isBadRequest());

        List<TypeDeContenu> typeDeContenuList = typeDeContenuRepository.findAll();
        assertThat(typeDeContenuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeDeContenuRepository.findAll().size();
        // set the field null
        typeDeContenu.setCode(null);

        // Create the TypeDeContenu, which fails.
        TypeDeContenuDTO typeDeContenuDTO = typeDeContenuMapper.toDto(typeDeContenu);

        restTypeDeContenuMockMvc.perform(post("/api/type-de-contenus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeDeContenuDTO)))
            .andExpect(status().isBadRequest());

        List<TypeDeContenu> typeDeContenuList = typeDeContenuRepository.findAll();
        assertThat(typeDeContenuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTypeDeContenus() throws Exception {
        // Initialize the database
        typeDeContenuRepository.saveAndFlush(typeDeContenu);

        // Get all the typeDeContenuList
        restTypeDeContenuMockMvc.perform(get("/api/type-de-contenus?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeDeContenu.getId().intValue())))
            .andExpect(jsonPath("$.[*].denominationAr").value(hasItem(DEFAULT_DENOMINATION_AR.toString())))
            .andExpect(jsonPath("$.[*].denominationFr").value(hasItem(DEFAULT_DENOMINATION_FR.toString())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())));
    }
    

    @Test
    @Transactional
    public void getTypeDeContenu() throws Exception {
        // Initialize the database
        typeDeContenuRepository.saveAndFlush(typeDeContenu);

        // Get the typeDeContenu
        restTypeDeContenuMockMvc.perform(get("/api/type-de-contenus/{id}", typeDeContenu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(typeDeContenu.getId().intValue()))
            .andExpect(jsonPath("$.denominationAr").value(DEFAULT_DENOMINATION_AR.toString()))
            .andExpect(jsonPath("$.denominationFr").value(DEFAULT_DENOMINATION_FR.toString()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingTypeDeContenu() throws Exception {
        // Get the typeDeContenu
        restTypeDeContenuMockMvc.perform(get("/api/type-de-contenus/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypeDeContenu() throws Exception {
        // Initialize the database
        typeDeContenuRepository.saveAndFlush(typeDeContenu);

        int databaseSizeBeforeUpdate = typeDeContenuRepository.findAll().size();

        // Update the typeDeContenu
        TypeDeContenu updatedTypeDeContenu = typeDeContenuRepository.findById(typeDeContenu.getId()).get();
        // Disconnect from session so that the updates on updatedTypeDeContenu are not directly saved in db
        em.detach(updatedTypeDeContenu);
        updatedTypeDeContenu
            .denominationAr(UPDATED_DENOMINATION_AR)
            .denominationFr(UPDATED_DENOMINATION_FR)
            .reference(UPDATED_REFERENCE)
            .code(UPDATED_CODE);
        TypeDeContenuDTO typeDeContenuDTO = typeDeContenuMapper.toDto(updatedTypeDeContenu);

        restTypeDeContenuMockMvc.perform(put("/api/type-de-contenus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeDeContenuDTO)))
            .andExpect(status().isOk());

        // Validate the TypeDeContenu in the database
        List<TypeDeContenu> typeDeContenuList = typeDeContenuRepository.findAll();
        assertThat(typeDeContenuList).hasSize(databaseSizeBeforeUpdate);
        TypeDeContenu testTypeDeContenu = typeDeContenuList.get(typeDeContenuList.size() - 1);
        assertThat(testTypeDeContenu.getDenominationAr()).isEqualTo(UPDATED_DENOMINATION_AR);
        assertThat(testTypeDeContenu.getDenominationFr()).isEqualTo(UPDATED_DENOMINATION_FR);
        assertThat(testTypeDeContenu.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testTypeDeContenu.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingTypeDeContenu() throws Exception {
        int databaseSizeBeforeUpdate = typeDeContenuRepository.findAll().size();

        // Create the TypeDeContenu
        TypeDeContenuDTO typeDeContenuDTO = typeDeContenuMapper.toDto(typeDeContenu);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTypeDeContenuMockMvc.perform(put("/api/type-de-contenus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeDeContenuDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeDeContenu in the database
        List<TypeDeContenu> typeDeContenuList = typeDeContenuRepository.findAll();
        assertThat(typeDeContenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTypeDeContenu() throws Exception {
        // Initialize the database
        typeDeContenuRepository.saveAndFlush(typeDeContenu);

        int databaseSizeBeforeDelete = typeDeContenuRepository.findAll().size();

        // Get the typeDeContenu
        restTypeDeContenuMockMvc.perform(delete("/api/type-de-contenus/{id}", typeDeContenu.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TypeDeContenu> typeDeContenuList = typeDeContenuRepository.findAll();
        assertThat(typeDeContenuList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeDeContenu.class);
        TypeDeContenu typeDeContenu1 = new TypeDeContenu();
        typeDeContenu1.setId(1L);
        TypeDeContenu typeDeContenu2 = new TypeDeContenu();
        typeDeContenu2.setId(typeDeContenu1.getId());
        assertThat(typeDeContenu1).isEqualTo(typeDeContenu2);
        typeDeContenu2.setId(2L);
        assertThat(typeDeContenu1).isNotEqualTo(typeDeContenu2);
        typeDeContenu1.setId(null);
        assertThat(typeDeContenu1).isNotEqualTo(typeDeContenu2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeDeContenuDTO.class);
        TypeDeContenuDTO typeDeContenuDTO1 = new TypeDeContenuDTO();
        typeDeContenuDTO1.setId(1L);
        TypeDeContenuDTO typeDeContenuDTO2 = new TypeDeContenuDTO();
        assertThat(typeDeContenuDTO1).isNotEqualTo(typeDeContenuDTO2);
        typeDeContenuDTO2.setId(typeDeContenuDTO1.getId());
        assertThat(typeDeContenuDTO1).isEqualTo(typeDeContenuDTO2);
        typeDeContenuDTO2.setId(2L);
        assertThat(typeDeContenuDTO1).isNotEqualTo(typeDeContenuDTO2);
        typeDeContenuDTO1.setId(null);
        assertThat(typeDeContenuDTO1).isNotEqualTo(typeDeContenuDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(typeDeContenuMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(typeDeContenuMapper.fromId(null)).isNull();
    }
}
