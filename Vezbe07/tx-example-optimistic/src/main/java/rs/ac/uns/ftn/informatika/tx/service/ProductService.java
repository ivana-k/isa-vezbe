package rs.ac.uns.ftn.informatika.tx.service;

import java.util.List;

import rs.ac.uns.ftn.informatika.tx.domain.Product;

public interface ProductService {
	
	Product save(Product product);
	
	void delete(long id);
	
	Product findById(long id);
	
	List<Product> findAll();
	
	Product update(Product product) throws Exception;

}
