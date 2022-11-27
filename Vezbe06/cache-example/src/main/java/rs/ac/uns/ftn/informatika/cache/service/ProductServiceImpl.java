package rs.ac.uns.ftn.informatika.cache.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import rs.ac.uns.ftn.informatika.cache.domain.Product;
import rs.ac.uns.ftn.informatika.cache.repository.ProductNamedQueryRepositoryExample;
import rs.ac.uns.ftn.informatika.cache.repository.ProductQueryRepositoryExample;
import rs.ac.uns.ftn.informatika.cache.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {
	
	private final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);
	
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ProductQueryRepositoryExample productQueryRepository;
	@Autowired
	private ProductNamedQueryRepositoryExample productNamedQueryRepository;	
	
	//@Autowired
	//private ObjectMapper objectMapper;

	public List<Product> findAll() {
		return productRepository.findAll();
	}

	public List<Product> findByName(String name) {
		return productQueryRepository.findByName(name);
	}

	public List<Product> findByNameMatch(String name) {
		return productQueryRepository.findByNameMatch(name);
	}

	public List<Product> findByNamedParam(String name, String origin, long price) {
		return productQueryRepository.findByNamedParam(name, origin, price);
	}

	public List<Product> findByPriceRange(long price1, long price2) {
		return productQueryRepository.findByPriceRange(price1, price2);
	}

	public List<Product> findByPrice(long price) {
		return productNamedQueryRepository.findByPrice(price);
	}

	public void saveProduct(Product product) {
		productRepository.save(product);
	}

	public Product findOne(long id) {
		LOG.info("Product with id: " + id + " successfully cached!");
		return productRepository.findById(id).get();
	}

	public void delete(long id) {
		productRepository.deleteById(id);
	}
	
	public void removeFromCache() {
		LOG.info("Products removed from cache!");
		
	}
}
