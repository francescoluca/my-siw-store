package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.InventoryItem;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	protected UserRepository userRepository;
	@Autowired
	private CredentialsService credentialsService;

	@Transactional
	public User getUser(Long id) {
		Optional<User> result = this.userRepository.findById(id);
		return result.orElse(null);
	}

	@Transactional
	public User saveUser(User user) {
		return this.userRepository.save(user);
	}

	@Transactional
	public List<User> getAllUsers() {
		List<User> result = new ArrayList<>();
		Iterable<User> iterable = this.userRepository.findAll();
		for (User user : iterable)
			result.add(user);
		return result;
	}

	public User getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		return this.getUser(credentials.getId());
	}

	public List<InventoryItem> getCartItemsByUser(User currentuser) {
		return this.userRepository.getCartItemsByUserId(currentuser.getId());
	}

	public Double getCartSubtotalByUser(User currentUser) {
		return this.userRepository.getCartSubtotalByUserId(currentUser.getId());
	}

	public boolean isItemAlreadyInCart(User currentUser, InventoryItem item) {
		return userRepository.isItemInCart(currentUser.getId(), item);
	}
}
