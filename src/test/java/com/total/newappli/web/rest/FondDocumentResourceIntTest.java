package com.total.newappli.web.rest;

import com.total.newappli.OffreContratApp;

import com.total.newappli.domain.FondDocument;
import com.total.newappli.repository.FondDocumentRepository;
import com.total.newappli.service.FondDocumentService;
import com.total.newappli.service.dto.FondDocumentDTO;
import com.total.newappli.service.mapper.FondDocumentMapper;
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
 * Test class for the FondDocumentResource REST controller.
 *
 * @see FondDocumentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OffreContratApp.class)
public class FondDocumentResourceIntTest {

    private static final String DEFAULT_DENOMINATION_AR = "AAAAAAAAAA";
    private static final String UPDATED_DENOMINATION_AR = "BBBBBBBBBB";

    private static final String DEFAULT_DENOMINATION_FR = "AAAAAAAAAA";
    private static final String UPDATED_DENOMINATION_FR = "BBBBBBBBBB";

    private static final String DEFAULT_FORMAT_PJ = "AAAAAAAAAA";
    private static final String UPDATED_FORMAT_PJ = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    @Autowired
    private FondDocumentRepository fondDocumentRepository;


    @Autowired
    private FondDocumentMapper fondDocumentMapper;
    

    @Autowired
    private FondDocumentService fondDocumentService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFondDocumentMockMvc;

    private FondDocument fondDocument;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FondDocumentResource fondDocumentResource = new FondDocumentResource(fondDocumentService);
        this.restFondDocumentMockMvc = MockMvcBuilders.standaloneSetup(fondDocumentResource)
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
    public static FondDocument createEntity(EntityManager em) {
        FondDocument fondDocument = new FondDocument()
            .denominationAr(DEFAULT_DENOMINATION_AR)
            .denominationFr(DEFAULT_DENOMINATION_FR)
            .formatPj(DEFAULT_FORMAT_PJ)
            .reference(DEFAULT_REFERENCE);
        return fondDocument;
    }

    @Before
    public void initTest() {
        fondDocument = createEntity(em);
    }

    @Test
    @Transactional
    public void createFondDocument() throws Exception {
        int databaseSizeBeforeCreate = fondDocumentRepository.findAll().size();

        // Create the FondDocument
        FondDocumentDTO fondDocumentDTO = fondDocumentMapper.toDto(fondDocument);
        restFondDocumentMockMvc.perform(post("/api/fond-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fondDocumentDTO)))
            .andExpect(status().isCreated());

        // Validate the FondDocument in the database
        List<FondDocument> fondDocumentList = fondDocumentRepository.findAll();
        assertThat(fondDocumentList).hasSize(databaseSizeBeforeCreate + 1);
        FondDocument testFondDocument = fondDocumentList.get(fondDocumentList.size() - 1);
        assertThat(testFondDocument.getDenominationAr()).isEqualTo(DEFAULT_DENOMINATION_AR);
        assertThat(testFondDocument.getDenominationFr()).isEqualTo(DEFAULT_DENOMINATION_FR);
        assertThat(testFondDocument.getFormatPj()).isEqualTo(DEFAULT_FORMAT_PJ);
        assertThat(testFondDocument.getReference()).isEqualTo(DEFAULT_REFERENCE);
    }

    @Test
    @Transactional
    public void createFondDocumentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fondDocumentRepository.findAll().size();

        // Create the FondDocument with an existing ID
        fondDocument.setId(1L);
        FondDocumentDTO fondDocumentDTO = fondDocumentMapper.toDto(fondDocument);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFondDocumentMockMvc.perform(post("/api/fond-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fondDocumentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FondDocument in the database
        List<FondDocument> fondDocumentList = fondDocumentRepository.findAll();
        assertThat(fondDocumentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFondDocuments() throws Exception {
        // Initialize the database
        fondDocumentRepository.saveAndFlush(fondDocument);

        // Get all the fondDocumentList
        restFondDocumentMockMvc.perform(get("/api/fond-documents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fondDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].denominationAr").value(hasItem(DEFAULT_DENOMINATION_AR.toString())))
            .andExpect(jsonPath("$.[*].denominationFr").value(hasItem(DEFAULT_DENOMINATION_FR.toString())))
            .andExpect(jsonPath("$.[*].formatPj").value(hasItem(DEFAULT_FORMAT_PJ.toString())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE.toString())));
    }
    

    @Test
    @Transactional
    public void getFondDocument() throws Exception {
        // Initialize the database
        fondDocumentRepository.saveAndFlush(fondDocument);

        // Get the fondDocument
        restFondDocumentMockMvc.perform(get("/api/fond-documents/{id}", fondDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fondDocument.getId().intValue()))
            .andExpect(jsonPath("$.denominationAr").value(DEFAULT_DENOMINATION_AR.toString()))
            .andExpect(jsonPath("$.denominationFr").value(DEFAULT_DENOMINATION_FR.toString()))
            .andExpect(jsonPath("$.formatPj").value(DEFAULT_FORMAT_PJ.toString()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingFondDocument() throws Exception {
        // Get the fondDocument
        restFondDocumentMockMvc.perform(get("/api/fond-documents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFondDocument() throws Exception {
        // Initialize the database
        fondDocumentRepository.saveAndFlush(fondDocument);

        int databaseSizeBeforeUpdate = fondDocumentRepository.findAll().size();

        // Update the fondDocument
        FondDocument updatedFondDocument = fondDocumentRepository.findById(fondDocument.getId()).get();
        // Disconnect from session so that the updates on updatedFondDocument are not directly saved in db
        em.detach(updatedFondDocument);
        updatedFondDocument
            .denominationAr(UPDATED_DENOMINATION_AR)
            .denominationFr(UPDATED_DENOMINATION_FR)
            .formatPj(UPDATED_FORMAT_PJ)
            .reference(UPDATED_REFERENCE);
        FondDocumentDTO fondDocumentDTO = fondDocumentMapper.toDto(updatedFondDocument);

        restFondDocumentMockMvc.perform(put("/api/fond-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fondDocumentDTO)))
            .andExpect(status().isOk());

        // Validate the FondDocument in the database
        List<FondDocument> fondDocumentList = fondDocumentRepository.findAll();
        assertThat(fondDocumentList).hasSize(databaseSizeBeforeUpdate);
        FondDocument testFondDocument = fondDocumentList.get(fondDocumentList.size() - 1);
        assertThat(testFondDocument.getDenominationAr()).isEqualTo(UPDATED_DENOMINATION_AR);
        assertThat(testFondDocument.getDenominationFr()).isEqualTo(UPDATED_DENOMINATION_FR);
        assertThat(testFondDocument.getFormatPj()).isEqualTo(UPDATED_FORMAT_PJ);
        assertThat(testFondDocument.getReference()).isEqualTo(UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    public void updateNonExistingFondDocument() throws Exception {
        int databaseSizeBeforeUpdate = fondDocumentRepository.findAll().size();

        // Create the FondDocument
        FondDocumentDTO fondDocumentDTO = fondDocumentMapper.toDto(fondDocument);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFondDocumentMockMvc.perform(put("/api/fond-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fondDocumentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FondDocument in the database
        List<FondDocument> fondDocumentList = fondDocumentRepository.findAll();
        assertThat(fondDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFondDocument() throws Exception {
        // Initialize the database
        fondDocumentRepository.saveAndFlush(fondDocument);

        int databaseSizeBeforeDelete = fondDocumentRepository.findAll().size();

        // Get the fondDocument
        restFondDocumentMockMvc.perform(delete("/api/fond-documents/{id}", fondDocument.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FondDocument> fondDocumentList = fondDocumentRepository.findAll();
        assertThat(fondDocumentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FondDocument.class);
        FondDocument fondDocument1 = new FondDocument();
        fondDocument1.setId(1L);
        FondDocument fondDocument2 = new FondDocument();
        fondDocument2.setId(fondDocument1.getId());
        assertThat(fondDocument1).isEqualTo(fondDocument2);
        fondDocument2.setId(2L);
        assertThat(fondDocument1).isNotEqualTo(fondDocument2);
        fondDocument1.setId(null);
        assertThat(fondDocument1).isNotEqualTo(fondDocument2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FondDocumentDTO.class);
        FondDocumentDTO fondDocumentDTO1 = new FondDocumentDTO();
        fondDocumentDTO1.setId(1L);
        FondDocumentDTO fondDocumentDTO2 = new FondDocumentDTO();
        assertThat(fondDocumentDTO1).isNotEqualTo(fondDocumentDTO2);
        fondDocumentDTO2.setId(fondDocumentDTO1.getId());
        assertThat(fondDocumentDTO1).isEqualTo(fondDocumentDTO2);
        fondDocumentDTO2.setId(2L);
        assertThat(fondDocumentDTO1).isNotEqualTo(fondDocumentDTO2);
        fondDocumentDTO1.setId(null);
        assertThat(fondDocumentDTO1).isNotEqualTo(fondDocumentDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(fondDocumentMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(fondDocumentMapper.fromId(null)).isNull();
    }
}
