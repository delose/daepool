package ph.talas.daepool.web.rest;

import ph.talas.daepool.Application;
import ph.talas.daepool.domain.TargetAudience;
import ph.talas.daepool.repository.TargetAudienceRepository;
import ph.talas.daepool.repository.search.TargetAudienceSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TargetAudienceResource REST controller.
 *
 * @see TargetAudienceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TargetAudienceResourceIntTest {

    private static final String DEFAULT_NAME = "A";
    private static final String UPDATED_NAME = "B";
    private static final String DEFAULT_DESCRIPTION = "A";
    private static final String UPDATED_DESCRIPTION = "B";

    private static final Integer DEFAULT_SIZE = 1;
    private static final Integer UPDATED_SIZE = 2;

    private static final LocalDate DEFAULT_CREATETIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATETIME = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_REFRESHTIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REFRESHTIME = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_CRITERIA = "AAAAA";
    private static final String UPDATED_CRITERIA = "BBBBB";

    @Inject
    private TargetAudienceRepository targetAudienceRepository;

    @Inject
    private TargetAudienceSearchRepository targetAudienceSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTargetAudienceMockMvc;

    private TargetAudience targetAudience;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TargetAudienceResource targetAudienceResource = new TargetAudienceResource();
        ReflectionTestUtils.setField(targetAudienceResource, "targetAudienceRepository", targetAudienceRepository);
        ReflectionTestUtils.setField(targetAudienceResource, "targetAudienceSearchRepository", targetAudienceSearchRepository);
        this.restTargetAudienceMockMvc = MockMvcBuilders.standaloneSetup(targetAudienceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        targetAudience = new TargetAudience();
        targetAudience.setName(DEFAULT_NAME);
        targetAudience.setDescription(DEFAULT_DESCRIPTION);
        targetAudience.setSize(DEFAULT_SIZE);
        targetAudience.setCreatetime(DEFAULT_CREATETIME);
        targetAudience.setRefreshtime(DEFAULT_REFRESHTIME);
        targetAudience.setCriteria(DEFAULT_CRITERIA);
    }

    @Test
    @Transactional
    public void createTargetAudience() throws Exception {
        int databaseSizeBeforeCreate = targetAudienceRepository.findAll().size();

        // Create the TargetAudience

        restTargetAudienceMockMvc.perform(post("/api/targetAudiences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(targetAudience)))
                .andExpect(status().isCreated());

        // Validate the TargetAudience in the database
        List<TargetAudience> targetAudiences = targetAudienceRepository.findAll();
        assertThat(targetAudiences).hasSize(databaseSizeBeforeCreate + 1);
        TargetAudience testTargetAudience = targetAudiences.get(targetAudiences.size() - 1);
        assertThat(testTargetAudience.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTargetAudience.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTargetAudience.getSize()).isEqualTo(DEFAULT_SIZE);
        assertThat(testTargetAudience.getCreatetime()).isEqualTo(DEFAULT_CREATETIME);
        assertThat(testTargetAudience.getRefreshtime()).isEqualTo(DEFAULT_REFRESHTIME);
        assertThat(testTargetAudience.getCriteria()).isEqualTo(DEFAULT_CRITERIA);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = targetAudienceRepository.findAll().size();
        // set the field null
        targetAudience.setName(null);

        // Create the TargetAudience, which fails.

        restTargetAudienceMockMvc.perform(post("/api/targetAudiences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(targetAudience)))
                .andExpect(status().isBadRequest());

        List<TargetAudience> targetAudiences = targetAudienceRepository.findAll();
        assertThat(targetAudiences).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = targetAudienceRepository.findAll().size();
        // set the field null
        targetAudience.setDescription(null);

        // Create the TargetAudience, which fails.

        restTargetAudienceMockMvc.perform(post("/api/targetAudiences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(targetAudience)))
                .andExpect(status().isBadRequest());

        List<TargetAudience> targetAudiences = targetAudienceRepository.findAll();
        assertThat(targetAudiences).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSizeIsRequired() throws Exception {
        int databaseSizeBeforeTest = targetAudienceRepository.findAll().size();
        // set the field null
        targetAudience.setSize(null);

        // Create the TargetAudience, which fails.

        restTargetAudienceMockMvc.perform(post("/api/targetAudiences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(targetAudience)))
                .andExpect(status().isBadRequest());

        List<TargetAudience> targetAudiences = targetAudienceRepository.findAll();
        assertThat(targetAudiences).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCriteriaIsRequired() throws Exception {
        int databaseSizeBeforeTest = targetAudienceRepository.findAll().size();
        // set the field null
        targetAudience.setCriteria(null);

        // Create the TargetAudience, which fails.

        restTargetAudienceMockMvc.perform(post("/api/targetAudiences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(targetAudience)))
                .andExpect(status().isBadRequest());

        List<TargetAudience> targetAudiences = targetAudienceRepository.findAll();
        assertThat(targetAudiences).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTargetAudiences() throws Exception {
        // Initialize the database
        targetAudienceRepository.saveAndFlush(targetAudience);

        // Get all the targetAudiences
        restTargetAudienceMockMvc.perform(get("/api/targetAudiences"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(targetAudience.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE)))
                .andExpect(jsonPath("$.[*].createtime").value(hasItem(DEFAULT_CREATETIME.toString())))
                .andExpect(jsonPath("$.[*].refreshtime").value(hasItem(DEFAULT_REFRESHTIME.toString())))
                .andExpect(jsonPath("$.[*].criteria").value(hasItem(DEFAULT_CRITERIA.toString())));
    }

    @Test
    @Transactional
    public void getTargetAudience() throws Exception {
        // Initialize the database
        targetAudienceRepository.saveAndFlush(targetAudience);

        // Get the targetAudience
        restTargetAudienceMockMvc.perform(get("/api/targetAudiences/{id}", targetAudience.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(targetAudience.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE))
            .andExpect(jsonPath("$.createtime").value(DEFAULT_CREATETIME.toString()))
            .andExpect(jsonPath("$.refreshtime").value(DEFAULT_REFRESHTIME.toString()))
            .andExpect(jsonPath("$.criteria").value(DEFAULT_CRITERIA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTargetAudience() throws Exception {
        // Get the targetAudience
        restTargetAudienceMockMvc.perform(get("/api/targetAudiences/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTargetAudience() throws Exception {
        // Initialize the database
        targetAudienceRepository.saveAndFlush(targetAudience);

		int databaseSizeBeforeUpdate = targetAudienceRepository.findAll().size();

        // Update the targetAudience
        targetAudience.setName(UPDATED_NAME);
        targetAudience.setDescription(UPDATED_DESCRIPTION);
        targetAudience.setSize(UPDATED_SIZE);
        targetAudience.setCreatetime(UPDATED_CREATETIME);
        targetAudience.setRefreshtime(UPDATED_REFRESHTIME);
        targetAudience.setCriteria(UPDATED_CRITERIA);

        restTargetAudienceMockMvc.perform(put("/api/targetAudiences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(targetAudience)))
                .andExpect(status().isOk());

        // Validate the TargetAudience in the database
        List<TargetAudience> targetAudiences = targetAudienceRepository.findAll();
        assertThat(targetAudiences).hasSize(databaseSizeBeforeUpdate);
        TargetAudience testTargetAudience = targetAudiences.get(targetAudiences.size() - 1);
        assertThat(testTargetAudience.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTargetAudience.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTargetAudience.getSize()).isEqualTo(UPDATED_SIZE);
        assertThat(testTargetAudience.getCreatetime()).isEqualTo(UPDATED_CREATETIME);
        assertThat(testTargetAudience.getRefreshtime()).isEqualTo(UPDATED_REFRESHTIME);
        assertThat(testTargetAudience.getCriteria()).isEqualTo(UPDATED_CRITERIA);
    }

    @Test
    @Transactional
    public void deleteTargetAudience() throws Exception {
        // Initialize the database
        targetAudienceRepository.saveAndFlush(targetAudience);

		int databaseSizeBeforeDelete = targetAudienceRepository.findAll().size();

        // Get the targetAudience
        restTargetAudienceMockMvc.perform(delete("/api/targetAudiences/{id}", targetAudience.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TargetAudience> targetAudiences = targetAudienceRepository.findAll();
        assertThat(targetAudiences).hasSize(databaseSizeBeforeDelete - 1);
    }
}
