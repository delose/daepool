package ph.talas.daepool.repository.search;

import ph.talas.daepool.domain.TargetAudience;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TargetAudience entity.
 */
public interface TargetAudienceSearchRepository extends ElasticsearchRepository<TargetAudience, Long> {
}
