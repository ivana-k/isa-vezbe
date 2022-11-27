package rs.ac.uns.ftn.informatika.cache.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import rs.ac.uns.ftn.informatika.cache.domain.Product;

public interface ProductNamedQueryRepositoryExample extends Repository<Product, Long> {
	
	//Primer NamedQuery koji se nalazi iznad klase Product
	List<Product> findByPrice(long price);
}
