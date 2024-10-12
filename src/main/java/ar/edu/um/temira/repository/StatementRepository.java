package ar.edu.um.temira.repository;

import ar.edu.um.temira.domain.Statement;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Statement entity.
 */
@Repository
public interface StatementRepository extends JpaRepository<Statement, Long> {
    @Query("select statement from Statement statement where statement.user.login = ?#{authentication.name}")
    List<Statement> findByUserIsCurrentUser();

    default Optional<Statement> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Statement> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Statement> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select statement from Statement statement left join fetch statement.sleepDetector left join fetch statement.vehicle left join fetch statement.driver left join fetch statement.user",
        countQuery = "select count(statement) from Statement statement"
    )
    Page<Statement> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select statement from Statement statement left join fetch statement.sleepDetector left join fetch statement.vehicle left join fetch statement.driver left join fetch statement.user"
    )
    List<Statement> findAllWithToOneRelationships();

    @Query(
        "select statement from Statement statement left join fetch statement.sleepDetector left join fetch statement.vehicle left join fetch statement.driver left join fetch statement.user where statement.id =:id"
    )
    Optional<Statement> findOneWithToOneRelationships(@Param("id") Long id);
}
