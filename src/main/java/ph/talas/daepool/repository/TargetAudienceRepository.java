package ph.talas.daepool.repository;

import ph.talas.daepool.domain.TargetAudience;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TargetAudience entity.
 */
public interface TargetAudienceRepository extends JpaRepository<TargetAudience,Long> {

    @Query("select targetAudience from TargetAudience targetAudience where targetAudience.user.login = ?#{principal.username}")
    List<TargetAudience> findByUserIsCurrentUser();

}
