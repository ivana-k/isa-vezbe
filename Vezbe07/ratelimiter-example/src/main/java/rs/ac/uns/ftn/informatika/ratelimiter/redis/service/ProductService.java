package rs.ac.uns.ftn.informatika.ratelimiter.redis.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import rs.ac.uns.ftn.informatika.ratelimiter.redis.domain.Product;

import java.util.List;

public interface ProductService {

	/*
	 * Anotacijom @Cacheable i nazivom "product"
	 * naznaceno je da se objekti tipa Product koji se dobave
	 * metodom findOne smestaju u kes kolekciju "product"
	 * (koju smo definisali u CacheConfiguation.java konfiguraciji)
	 */
	@Cacheable("product")
	Product findOne(long id);

	/*
	 * Anotacijom @CachePut i nazivom "product"
	 * naznaceno je da ce objekat biti updatovan u samom kesu.
	 * Na ovaj nacin kes sadrzi azurne podatke i nakon izmena.
	 * Glavna razlika izmedju @Cachable i @CachePut je sto ce se
	 * metoda anotirana @CachePut anotacijom UVEK izvrsiti.
	 */
	@CachePut(cacheNames = {"product"}, key = "#root.args[0]")
	Product updateOne(long id, Product product);

	/*
	 * Anotacijom @CacheEvict, nazivom kesa "product" i flagom allEntries
	 * naznaceno je da zelimo da izbacimo sve objekte iz kesa.
	 * Neki dodatni argumenti koji mogu biti postavljeni su:
	 * - beforeInvocation: u slucaju da je true, izbacivanje iz kesa desava se pre poziva anotirane metode
	 * - condition: logicki uslov za izbacivanje (u koliko je zadovoljen, objekat se izbacuje)
	 */
	@CacheEvict(cacheNames = {"product"}, allEntries = true)
	void removeFromCache();

	List<Product> findAll();
	void saveProduct(Product product);
	void delete(long id);

}
