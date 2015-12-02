package ph.talas.daepool.repository.search;

import ph.talas.daepool.domain.Campaign;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Campaign entity.
 */
public interface CampaignSearchRepository extends ElasticsearchRepository<Campaign, Long> {
}
