package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Television;
import it.uniroma3.siw.model.Util.Brand;

public interface TelevisionRepository extends CrudRepository<Television, Long>{

	boolean existsByCode(String code);

}
