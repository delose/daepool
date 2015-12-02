package ph.talas.daepool.web.rest;

import ph.talas.daepool.Application;
import ph.talas.daepool.domain.Campaign;
import ph.talas.daepool.repository.CampaignRepository;
import ph.talas.daepool.repository.search.CampaignSearchRepository;

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

import ph.talas.daepool.domain.enumeration.Status;

/**
 * Test class for the CampaignResource REST controller.
 *
 * @see CampaignResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CampaignResourceIntTest {

    private static final String DEFAULT_NAME = "A";
    private static final String UPDATED_NAME = "B";
    private static final String DEFAULT_DESCRIPTION = "A";
    private static final String UPDATED_DESCRIPTION = "B";
    private static final String DEFAULT_TYPE = "AAAAA";
    private static final String UPDATED_TYPE = "BBBBB";

    private static final LocalDate DEFAULT_STARTTIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_STARTTIME = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ENDTIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ENDTIME = LocalDate.now(ZoneId.systemDefault());


private static final Status DEFAULT_STATUS = Status.active;
    private static final Status UPDATED_STATUS = Status.inactive;

    @Inject
    private CampaignRepository campaignRepository;

    @Inject
    private CampaignSearchRepository campaignSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCampaignMockMvc;

    private Campaign campaign;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CampaignResource campaignResource = new CampaignResource();
        ReflectionTestUtils.setField(campaignResource, "campaignRepository", campaignRepository);
        ReflectionTestUtils.setField(campaignResource, "campaignSearchRepository", campaignSearchRepository);
        this.restCampaignMockMvc = MockMvcBuilders.standaloneSetup(campaignResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        campaign = new Campaign();
        campaign.setName(DEFAULT_NAME);
        campaign.setDescription(DEFAULT_DESCRIPTION);
        campaign.setType(DEFAULT_TYPE);
        campaign.setStarttime(DEFAULT_STARTTIME);
        campaign.setEndtime(DEFAULT_ENDTIME);
        campaign.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createCampaign() throws Exception {
        int databaseSizeBeforeCreate = campaignRepository.findAll().size();

        // Create the Campaign

        restCampaignMockMvc.perform(post("/api/campaigns")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(campaign)))
                .andExpect(status().isCreated());

        // Validate the Campaign in the database
        List<Campaign> campaigns = campaignRepository.findAll();
        assertThat(campaigns).hasSize(databaseSizeBeforeCreate + 1);
        Campaign testCampaign = campaigns.get(campaigns.size() - 1);
        assertThat(testCampaign.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCampaign.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCampaign.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCampaign.getStarttime()).isEqualTo(DEFAULT_STARTTIME);
        assertThat(testCampaign.getEndtime()).isEqualTo(DEFAULT_ENDTIME);
        assertThat(testCampaign.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = campaignRepository.findAll().size();
        // set the field null
        campaign.setName(null);

        // Create the Campaign, which fails.

        restCampaignMockMvc.perform(post("/api/campaigns")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(campaign)))
                .andExpect(status().isBadRequest());

        List<Campaign> campaigns = campaignRepository.findAll();
        assertThat(campaigns).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = campaignRepository.findAll().size();
        // set the field null
        campaign.setDescription(null);

        // Create the Campaign, which fails.

        restCampaignMockMvc.perform(post("/api/campaigns")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(campaign)))
                .andExpect(status().isBadRequest());

        List<Campaign> campaigns = campaignRepository.findAll();
        assertThat(campaigns).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = campaignRepository.findAll().size();
        // set the field null
        campaign.setType(null);

        // Create the Campaign, which fails.

        restCampaignMockMvc.perform(post("/api/campaigns")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(campaign)))
                .andExpect(status().isBadRequest());

        List<Campaign> campaigns = campaignRepository.findAll();
        assertThat(campaigns).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStarttimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = campaignRepository.findAll().size();
        // set the field null
        campaign.setStarttime(null);

        // Create the Campaign, which fails.

        restCampaignMockMvc.perform(post("/api/campaigns")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(campaign)))
                .andExpect(status().isBadRequest());

        List<Campaign> campaigns = campaignRepository.findAll();
        assertThat(campaigns).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndtimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = campaignRepository.findAll().size();
        // set the field null
        campaign.setEndtime(null);

        // Create the Campaign, which fails.

        restCampaignMockMvc.perform(post("/api/campaigns")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(campaign)))
                .andExpect(status().isBadRequest());

        List<Campaign> campaigns = campaignRepository.findAll();
        assertThat(campaigns).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCampaigns() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaigns
        restCampaignMockMvc.perform(get("/api/campaigns"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(campaign.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].starttime").value(hasItem(DEFAULT_STARTTIME.toString())))
                .andExpect(jsonPath("$.[*].endtime").value(hasItem(DEFAULT_ENDTIME.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getCampaign() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get the campaign
        restCampaignMockMvc.perform(get("/api/campaigns/{id}", campaign.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(campaign.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.starttime").value(DEFAULT_STARTTIME.toString()))
            .andExpect(jsonPath("$.endtime").value(DEFAULT_ENDTIME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCampaign() throws Exception {
        // Get the campaign
        restCampaignMockMvc.perform(get("/api/campaigns/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCampaign() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

		int databaseSizeBeforeUpdate = campaignRepository.findAll().size();

        // Update the campaign
        campaign.setName(UPDATED_NAME);
        campaign.setDescription(UPDATED_DESCRIPTION);
        campaign.setType(UPDATED_TYPE);
        campaign.setStarttime(UPDATED_STARTTIME);
        campaign.setEndtime(UPDATED_ENDTIME);
        campaign.setStatus(UPDATED_STATUS);

        restCampaignMockMvc.perform(put("/api/campaigns")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(campaign)))
                .andExpect(status().isOk());

        // Validate the Campaign in the database
        List<Campaign> campaigns = campaignRepository.findAll();
        assertThat(campaigns).hasSize(databaseSizeBeforeUpdate);
        Campaign testCampaign = campaigns.get(campaigns.size() - 1);
        assertThat(testCampaign.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCampaign.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCampaign.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCampaign.getStarttime()).isEqualTo(UPDATED_STARTTIME);
        assertThat(testCampaign.getEndtime()).isEqualTo(UPDATED_ENDTIME);
        assertThat(testCampaign.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteCampaign() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

		int databaseSizeBeforeDelete = campaignRepository.findAll().size();

        // Get the campaign
        restCampaignMockMvc.perform(delete("/api/campaigns/{id}", campaign.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Campaign> campaigns = campaignRepository.findAll();
        assertThat(campaigns).hasSize(databaseSizeBeforeDelete - 1);
    }
}
