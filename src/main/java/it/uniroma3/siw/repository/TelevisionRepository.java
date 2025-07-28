package it.uniroma3.siw.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Television;

public interface TelevisionRepository extends JpaRepository<Television, Long> {

	boolean existsByCode(String code);

	@Query("SELECT t FROM Television t WHERE LOWER(t.model) LIKE LOWER(CONCAT('%', :keyword, '%')) "
			+ "OR LOWER(t.brand) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	Page<Television> searchTelevisionsByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
