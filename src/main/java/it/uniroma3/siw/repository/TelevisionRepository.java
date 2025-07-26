package it.uniroma3.siw.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Television;

public interface TelevisionRepository extends JpaRepository<Television, Long> {

	boolean existsByCode(String code);

	@Query(value = "SELECT * FROM television t "
			+ "WHERE LOWER(t.model) LIKE LOWER(CONCAT('%', :keyword, '%')) ", nativeQuery = true)
	Page<Television> searchTelevisionsByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
