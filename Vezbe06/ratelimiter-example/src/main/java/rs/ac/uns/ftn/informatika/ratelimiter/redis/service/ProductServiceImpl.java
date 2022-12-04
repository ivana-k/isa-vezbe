package rs.ac.uns.ftn.informatika.ratelimiter.redis.service;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.ratelimiter.redis.repository.ProductRepository;
import rs.ac.uns.ftn.informatika.ratelimiter.redis.domain.Product;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
	
	private final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);
	
	@Autowired
	private ProductRepository productRepository;

	public Product findOne(long id) {
		LOG.info("Product with id: " + id + " fetched from DB and successfully cached!");
		return productRepository.findById(id).get();
	}

	public Product updateOne(long id, Product product) {
		Product dbProduct = productRepository.findById(id).get();
		dbProduct.setName(product.getName());
		dbProduct.setOrigin(product.getOrigin());
		dbProduct.setPrice(product.getPrice());

		LOG.info("Product with id: " + id + " successfully updated!");
		return productRepository.save(dbProduct);
	}

	public void removeFromCache() {
		LOG.info("Products removed from cache!");
	}

	/*
	 * Deklarativno oznacavanje da metodu mozemo izvrsiti
	 * ogranicen broj puta u okviru vremenskog intervala.
	 * Od znacajnih atributa koji se mogu postaviti izdvajaju se:
	 * - name - ime rateLimiter instance definisano u konfiguraciji
	 * - fallbackMethod - ime metode koja se poziva u slucaju da dodje do izuzetka
	 */
	@RateLimiter(name = "standard", fallbackMethod = "standardFallback")
	public List<Product> findAll() {
		return productRepository.findAll();
	}

	// Metoda koja ce se pozvati u slucaju RequestNotPermitted exception-a
	public List<Product> standardFallback(RequestNotPermitted rnp) {
		LOG.warn("Prevazidjen broj poziva u ogranicenom vremenskom intervalu");
		// Samo prosledjujemo izuzetak -> global exception handler koji bi ga obradio :)
		throw rnp;
	}

	public void saveProduct(Product product) {
		productRepository.save(product);
	}

	public void delete(long id) {
		productRepository.deleteById(id);
	}

}
