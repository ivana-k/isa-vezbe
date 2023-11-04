package rs.ac.uns.ftn.informatika.ratelimiter.redis;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import rs.ac.uns.ftn.informatika.ratelimiter.redis.service.ProductServiceImpl;
import rs.ac.uns.ftn.informatika.ratelimiter.redis.domain.Product;
import rs.ac.uns.ftn.informatika.ratelimiter.redis.service.ProductService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RateLimiterExampleTests {

	private final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);
	private final int productCount = 5;

	@Autowired
	private ProductService productService;

	@BeforeAll
	public void setUp() throws Exception {
		productService.saveProduct(new Product("P1", "O1", 5L));
		productService.saveProduct(new Product("P2","O2", 4L));
		productService.saveProduct(new Product("P3","O3", 3L));
		productService.saveProduct(new Product("P4","O4", 1L));
		productService.saveProduct(new Product("P5","O4", 1L));
	}

	@Test
	public void testRateLimiterExceptionScenario() throws InterruptedException {
		LOG.info("Prvi poziv metode anotirane sa @RateLimiter");
		List<Product> products = productService.findAll();
		assertEquals(productCount, products.size());

		LOG.info("Poziv metode anotirane sa @RateLimiter U OKVIRU ogranicenog vremena od 5 sekundi");
		assertThrows(RequestNotPermitted.class, () -> productService.findAll());

		Thread.sleep(5000); // da bi ogranicen interval od 5 sekundi prosao

		LOG.info("Poziv metode anotirane sa @RateLimiter NAKON ogranicenog vremena od 5 sekundi");
		products = productService.findAll();
		assertEquals(productCount, products.size());
	}

}
