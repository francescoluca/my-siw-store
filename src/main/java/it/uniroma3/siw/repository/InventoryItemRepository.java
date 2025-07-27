package it.uniroma3.siw.repository;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.InventoryItem;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

	@Query("SELECT i FROM InventoryItem i JOIN i.television t "
			+ "WHERE (LOWER(t.model) LIKE LOWER(CONCAT('%', :keyword, '%')) "
			+ "OR LOWER(t.brand) LIKE LOWER(CONCAT('%', :keyword, '%')))"
			+ "AND (:minPrice IS NULL OR i.price >= :minPrice)" + "AND (:maxPrice IS NULL OR i.price <= :maxPrice)"
			+ "AND (:minInches IS NULL OR t.screenInches >= :minInches)"
			+ "AND (:maxInches IS NULL OR t.screenInches <= :maxInches)" + "ORDER BY "
			+ "CASE WHEN :sortField = 'screenInches' THEN t.screenInches END ASC, "
			+ "CASE WHEN :sortField = 'releaseDate' THEN t.releaseDate END ASC, "
			+ "CASE WHEN :sortField = 'price' THEN i.price END ASC")
	Page<InventoryItem> searchWithSort(@Param("keyword") String keyword, @Param("sortField") String sortField,
			@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice,
			@Param("minInches") Integer minInches, @Param("maxInches") Integer maxInches, Pageable pageable);

}
