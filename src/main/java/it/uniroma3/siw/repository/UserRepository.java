package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.InventoryItem;
import it.uniroma3.siw.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

	@Query("SELECT u.cartItems FROM User u WHERE u.id = :userId")
	List<InventoryItem> getCartItemsByUserId(@Param("userId") Long userId);

	@Query("SELECT SUM(i.price) FROM User u JOIN u.cartItems i WHERE u.id = :userId")
	Double getCartSubtotalByUserId(@Param("userId") Long userId);

	@Query("SELECT CASE WHEN :item MEMBER OF u.cartItems THEN true ELSE false END FROM User u WHERE u.id = :userId")
	boolean isItemInCart(@Param("userId") Long userId, @Param("item") InventoryItem item);

}