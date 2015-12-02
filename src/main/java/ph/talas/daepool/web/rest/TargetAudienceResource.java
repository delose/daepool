package ph.talas.daepool.web.rest;

import com.codahale.metrics.annotation.Timed;
import ph.talas.daepool.domain.TargetAudience;
import ph.talas.daepool.repository.TargetAudienceRepository;
import ph.talas.daepool.repository.search.TargetAudienceSearchRepository;
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
 * REST controller for managing TargetAudience.
 */
@RestController
@RequestMapping("/api")
public class TargetAudienceResource {

    private final Logger log = LoggerFactory.getLogger(TargetAudienceResource.class);

    @Inject
    private TargetAudienceRepository targetAudienceRepository;

    @Inject
    private TargetAudienceSearchRepository targetAudienceSearchRepository;

    /**
     * POST  /targetAudiences -> Create a new targetAudience.
     */
    @RequestMapping(value = "/targetAudiences",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TargetAudience> createTargetAudience(@Valid @RequestBody TargetAudience targetAudience) throws URISyntaxException {
        log.debug("REST request to save TargetAudience : {}", targetAudience);
        if (targetAudience.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new targetAudience cannot already have an ID").body(null);
        }
        TargetAudience result = targetAudienceRepository.save(targetAudience);
        targetAudienceSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/targetAudiences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("targetAudience", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /targetAudiences -> Updates an existing targetAudience.
     */
    @RequestMapping(value = "/targetAudiences",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TargetAudience> updateTargetAudience(@Valid @RequestBody TargetAudience targetAudience) throws URISyntaxException {
        log.debug("REST request to update TargetAudience : {}", targetAudience);
        if (targetAudience.getId() == null) {
            return createTargetAudience(targetAudience);
        }
        TargetAudience result = targetAudienceRepository.save(targetAudience);
        targetAudienceSearchRepository.save(targetAudience);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("targetAudience", targetAudience.getId().toString()))
            .body(result);
    }

    /**
     * GET  /targetAudiences -> get all the targetAudiences.
     */
    @RequestMapping(value = "/targetAudiences",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<TargetAudience>> getAllTargetAudiences(Pageable pageable)
        throws URISyntaxException {
        Page<TargetAudience> page = targetAudienceRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/targetAudiences");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /targetAudiences/:id -> get the "id" targetAudience.
     */
    @RequestMapping(value = "/targetAudiences/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TargetAudience> getTargetAudience(@PathVariable Long id) {
        log.debug("REST request to get TargetAudience : {}", id);
        return Optional.ofNullable(targetAudienceRepository.findOne(id))
            .map(targetAudience -> new ResponseEntity<>(
                targetAudience,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /targetAudiences/:id -> delete the "id" targetAudience.
     */
    @RequestMapping(value = "/targetAudiences/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTargetAudience(@PathVariable Long id) {
        log.debug("REST request to delete TargetAudience : {}", id);
        targetAudienceRepository.delete(id);
        targetAudienceSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("targetAudience", id.toString())).build();
    }

    /**
     * SEARCH  /_search/targetAudiences/:query -> search for the targetAudience corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/targetAudiences/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TargetAudience> searchTargetAudiences(@PathVariable String query) {
        return StreamSupport
            .stream(targetAudienceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
