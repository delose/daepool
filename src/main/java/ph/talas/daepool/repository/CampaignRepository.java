package ph.talas.daepool.repository;

import ph.talas.daepool.domain.Campaign;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Campaign entity.
 */
public interface CampaignRepository extends JpaRepository<Campaign,Long> {

    @Query("select campaign from Campaign campaign where campaign.user.login = ?#{principal.username}")
    List<Campaign> findByUserIsCurrentUser();

}
