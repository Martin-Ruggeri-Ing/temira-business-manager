package ar.edu.um.temira.repository;

import ar.edu.um.temira.domain.Driver;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Driver entity.
 */
@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    @Query("select driver from Driver driver where driver.user.login = ?#{authentication.name}")
    List<Driver> findByUserIsCurrentUser();

    default Optional<Driver> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Driver> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Driver> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select driver from Driver driver left join fetch driver.user", countQuery = "select count(driver) from Driver driver")
    Page<Driver> findAllWithToOneRelationships(Pageable pageable);

    @Query("select driver from Driver driver left join fetch driver.user")
    List<Driver> findAllWithToOneRelationships();

    @Query("select driver from Driver driver left join fetch driver.user where driver.id =:id")
    Optional<Driver> findOneWithToOneRelationships(@Param("id") Long id);
}
