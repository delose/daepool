package ph.talas.daepool.web.rest;

import com.codahale.metrics.annotation.Timed;
import ph.talas.daepool.domain.Campaign;
import ph.talas.daepool.repository.CampaignRepository;
import ph.talas.daepool.repository.search.CampaignSearchRepository;
import ph.talas.daepool.web.rest.util.HeaderUtil;
import ph.talas.daepool.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Campaign.
 */
@RestController
@RequestMapping("/api")
public class CampaignResource {

    private final Logger log = LoggerFactory.getLogger(CampaignResource.class);

    @Inject
    private CampaignRepository campaignRepository;

    @Inject
    private CampaignSearchRepository campaignSearchRepository;

    /**
     * POST  /campaigns -> Create a new campaign.
     */
    @RequestMapping(value = "/campaigns",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Campaign> createCampaign(@Valid @RequestBody Campaign campaign) throws URISyntaxException {
        log.debug("REST request to save Campaign : {}", campaign);
        if (campaign.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new campaign cannot already have an ID").body(null);
        }
        Campaign result = campaignRepository.save(campaign);
        campaignSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/campaigns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("campaign", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /campaigns -> Updates an existing campaign.
     */
    @RequestMapping(value = "/campaigns",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Campaign> updateCampaign(@Valid @RequestBody Campaign campaign) throws URISyntaxException {
        log.debug("REST request to update Campaign : {}", campaign);
        if (campaign.getId() == null) {
            return createCampaign(campaign);
        }
        Campaign result = campaignRepository.save(campaign);
        campaignSearchRepository.save(campaign);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("campaign", campaign.getId().toString()))
            .body(result);
    }

    /**
     * GET  /campaigns -> get all the campaigns.
     */
    @RequestMapping(value = "/campaigns",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Campaign>> getAllCampaigns(Pageable pageable)
        throws URISyntaxException {
        Page<Campaign> page = campaignRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/campaigns");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /campaigns/:id -> get the "id" campaign.
     */
    @RequestMapping(value = "/campaigns/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Campaign> getCampaign(@PathVariable Long id) {
        log.debug("REST request to get Campaign : {}", id);
        return Optional.ofNullable(campaignRepository.findOne(id))
            .map(campaign -> new ResponseEntity<>(
                campaign,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /campaigns/:id -> delete the "id" campaign.
     */
    @RequestMapping(value = "/campaigns/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id) {
        log.debug("REST request to delete Campaign : {}", id);
        campaignRepository.delete(id);
        campaignSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("campaign", id.toString())).build();
    }

    /**
     * SEARCH  /_search/campaigns/:query -> search for the campaign corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/campaigns/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Campaign> searchCampaigns(@PathVariable String query) {
        return StreamSupport
            .stream(campaignSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
