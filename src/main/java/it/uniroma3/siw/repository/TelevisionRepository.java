package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Television;

public interface TelevisionRepository extends CrudRepository<Television, Long>{

	boolean existsByModelAndBrand(String model, String brand);

}
