package rs.ac.uns.ftn.informatika.ratelimiter.redis;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;
import rs.ac.uns.ftn.informatika.ratelimiter.redis.domain.Product;
import rs.ac.uns.ftn.informatika.ratelimiter.redis.service.ProductService;
import rs.ac.uns.ftn.informatika.ratelimiter.redis.service.ProductServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RedisExampleTests {

    /*
     * Konfiguracija Testcontainers biblioteke
     * Kreiran je Docker kontejner na osnovu Redis slike
     * i preuzet je port na kom je pokrenut kontejner kako bi se ostvarila konekcija iz aplikacije
     * (kontejner nije pokrenut na portu 6379 vec mu je port dodeljen nasumicno,
     * tako da moramo da konfigurisemo konekciju)
     */
    static {
        GenericContainer<?> redis =
                new GenericContainer<>(DockerImageName.parse("redis:5.0.3-alpine"))
                .withExposedPorts(6379);
        redis.start();
        System.setProperty("spring.redis.host", redis.getHost());
        System.setProperty("spring.redis.port", redis.getMappedPort(6379).toString());
    }

    private final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final long productId = 1L;
    private final String updateName = "P-Updated";
    private final String updateOrigin = "Updated";
    private final long updatePrice = 10L;
    private Product updateProduct;

    @Autowired
    private ProductService productService;

    @BeforeAll
    public void setUp() throws Exception {
        productService.saveProduct(new Product("P1", "O1", 5L));
        productService.saveProduct(new Product("P2","O2", 4L));
        productService.saveProduct(new Product("P3","O3", 3L));

        updateProduct = new Product(updateName, updateOrigin, updatePrice);
    }

    @Test
    void testRedisCachingScenario() {
        LOG.info("Prvo dobavljanje po id-u sa vrednoscu 1L");
        Product productRepo = productService.findOne(productId);

        LOG.info("Drugo dobavljanje po id-u sa vrednoscu 1L");
        Product productCache = productService.findOne(productId);

        // provera da je entitet vracen iz baze isto kao entite vracen iz kesa
        assertEquals(productRepo.getId(), productCache.getId());
        assertEquals(productRepo.getName(), productCache.getName());
        assertEquals(productRepo.getOrigin(), productCache.getOrigin());
        assertEquals(productRepo.getPrice(), productCache.getPrice());
    }

    @Test
    void testRedisUpdatingScenario() {
        LOG.info("Prvo dobavljanje po id-u sa vrednoscu 1L");
        Product productRepo = productService.findOne(productId);

        LOG.info("Update Product po id-u sa vrednoscu 1L");
        Product updatedProduct = productService.updateOne(productId, updateProduct);

        LOG.info("Drugo dobavljanje po id-u sa vrednoscu 1L NAKON update");
        Product productCache = productService.findOne(productId);

        // provera da je entitet vracen nakon update operaciji isto kao entite vracen iz kesa
        assertEquals(updatedProduct.getId(), productCache.getId());
        assertEquals(updatedProduct.getName(), productCache.getName());
        assertEquals(updatedProduct.getOrigin(), productCache.getOrigin());
        assertEquals(updatedProduct.getPrice(), productCache.getPrice());
    }
}
