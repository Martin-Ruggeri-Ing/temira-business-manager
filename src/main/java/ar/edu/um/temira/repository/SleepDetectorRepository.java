package ar.edu.um.temira.repository;

import ar.edu.um.temira.domain.SleepDetector;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SleepDetector entity.
 */
@Repository
public interface SleepDetectorRepository extends JpaRepository<SleepDetector, Long> {
    @Query("select sleepDetector from SleepDetector sleepDetector where sleepDetector.user.login = ?#{authentication.name}")
    List<SleepDetector> findByUserIsCurrentUser();

    default Optional<SleepDetector> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SleepDetector> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SleepDetector> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select sleepDetector from SleepDetector sleepDetector left join fetch sleepDetector.driver left join fetch sleepDetector.user",
        countQuery = "select count(sleepDetector) from SleepDetector sleepDetector"
    )
    Page<SleepDetector> findAllWithToOneRelationships(Pageable pageable);

    @Query("select sleepDetector from SleepDetector sleepDetector left join fetch sleepDetector.driver left join fetch sleepDetector.user")
    List<SleepDetector> findAllWithToOneRelationships();

    @Query(
        "select sleepDetector from SleepDetector sleepDetector left join fetch sleepDetector.driver left join fetch sleepDetector.user where sleepDetector.id =:id"
    )
    Optional<SleepDetector> findOneWithToOneRelationships(@Param("id") Long id);
}
