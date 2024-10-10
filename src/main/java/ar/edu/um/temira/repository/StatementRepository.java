package ar.edu.um.temira.repository;

import ar.edu.um.temira.domain.Statement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Statement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StatementRepository extends JpaRepository<Statement, Long> {}
