package rs.ac.uns.ftn.informatika.tx.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.informatika.tx.domain.Product;
import rs.ac.uns.ftn.informatika.tx.repository.ProductRepository;

@Service
/*
 * Deklarativno oznacavanje da se nesto izvrsava u transakciji
 * radi se upotrebom anotacije @Transactional. @Transactional se moze
 * iskoristiti na nivou klase sto ce znaciti da ce svaka metoda unutar klase
 * biti obradjena u transakciji.
 * Od znacajnijih atributa koji se mogu postaviti izdavaju se:
 * - readOnly status - moze se iskoristiti da se naznaci da ce se metode koristiti
 * 						iskljucivo za citanje (ne modifikaciju podataka) i readOnly
 * 						transakcije mogu biti korisna optimizacija kada se koristi Hibernate
 * - timeout - oznacava koliko dugo ce se transakcija izvrsavati pre nego sto ce se odraditi rollback
 * - rollbackFor - oznacava za koje klase (Exception klase) ce se izvrsiti rollback
 * - noRollbackFor - oznacava za koje klase (Exception klase) se nece izvrsiti rollback
 * - propagation - obicno ce se sav kod izvrsiti unutar transakcije, medjutim pomocu ove opcije
 * 					moze se naznaciti kako ce se metoda izvrsiti u zavisnosti od postojanja/nepostojanja
 * 					transakcionog konteksta:
 * 					1. REQUIRED - metoda se prikljucuje tekucoj transakciji, otvara novu ako transakcija ne postoji
 * 					2. REQUIRES_NEW - metoda uvek pokrece novu transakciju, ako postoji tekuca transakcija ona se suspenduje
 * 					3. MANDATORY - metoda mora da se izvrsava u transakciji koja mora biti ranije pokrenuta; ako je nema jvlja se greska
 * 					4. SUPPORTS - metoda ce se prikljuciti tekucoj transakciji, ako ona postoji; ako ne postoji, izvrsava se bez transakcije
 * 					5. NOT_SUPPORTED - metoda se izvrsava bez transakcije, cak i ako postoji tekuca transakcija
 * 					6. NEVER - metoda se izvrsava bez tranksacije; ako postoji tekuca transakcija, javlja se greska
 * 					7. NESTED - metoda se izvrsava u ugnjezdenoj transakciji ako je trenutni thread povezan sa transakcijom u suprotnom startuje novu transakciju
 * - isolation - moze se definisati drugaciji nivo izolacije u odnosu na podrazumevani za odgovarajucu bazu:
 * 					1. READ_UNCOMMITTED
 * 					2. READ_COMMITED - eliminise problem "dirty read"
 * 					3. REPEATABLE_READ - eliminise problem "unrepeatable read"
 * 					4. SERIALIZABLE - eliminise problem "phantom read"
 */
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ProductRepository productRepository;
	/*
	 * Anotacija na nivou metode je specificnija od one na nivou klase, tako da ce se
	 * pri izvrsavanju metode save aktivirati specificnija podesavanja. U ovom slucaju
	 * readOnly parametar je postavljen na false jer metoda menja podatak (ne cita ga).
	 */
	@Transactional(readOnly = false)
	public Product save(Product product) {
		logger.info("> create");
		Product savedProduct = productRepository.save(product);
		logger.info("< create");
		return savedProduct;
	}

	/*
	 * Takodje se moze eksplicitno promeniti propagacija koja je podrazumevano REQUIRED.
	 * Granice transakcije su sama metoda delete.
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void delete(long id) {
		
		logger.info("> delete");
		productRepository.deleteById(id);
		logger.info("< delete");
	}

	public Product findById(long id) {
		
		logger.info("> findById id:{}", id);
		Product product = productRepository.findById(id).get();
		logger.info("< findById id:{}", id);
		return product;
	}
	
	public List<Product> findAll() {
		
		logger.info("> findAll");
		List<Product> products = productRepository.findAll();
		logger.info("< findAll");
		return products;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Product update(Product product) throws Exception {
		
        logger.info("> update id:{}", product.getId());
        Product productToUpdate = productRepository.getOne(product.getId());
        productToUpdate.setName(product.getName());
        productToUpdate.setOrigin(product.getOrigin());
        productToUpdate.setPrice(product.getPrice());
        productRepository.save(productToUpdate);
        logger.info(productToUpdate.toString());
        logger.info("< update id:{}", product.getId());
        return productToUpdate;
    }

}
