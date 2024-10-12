package ar.edu.um.temira.repository;

import ar.edu.um.temira.domain.VehicleBrand;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VehicleBrand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehicleBrandRepository extends JpaRepository<VehicleBrand, Long> {}
