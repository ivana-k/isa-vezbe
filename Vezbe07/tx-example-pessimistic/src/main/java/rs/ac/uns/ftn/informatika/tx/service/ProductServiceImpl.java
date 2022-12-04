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
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ProductRepository productRepository;

	@Transactional(readOnly = false)
	public Product save(Product product) {
		logger.info("> create");
		Product savedProduct = productRepository.save(product);
		logger.info("< create");
		return savedProduct;
	}

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

	/*
	 * Iako je po prirodi read-only metoda, findOneById iz ProductRepository je anotirana sa @Lock(LockModeType.PESSIMISTIC_WRITE)
	 * sto na osnovni SELECT upit dodaje klauzulu "FOR UPDATE" i tako je cini not read-only
	 * 
	 * Hibernate:
	 *     select
     *         product0_.id as id1_0_,
     *         product0_.name as name2_0_,
     *         product0_.origin as origin3_0_,
     *         product0_.price as price4_0_ 
     *     from
     *         product product0_ 
     *     where
     *         product0_.id=? for update
     *             of product0_ nowait
	 */
	@Transactional(readOnly = false)
	public Product findOneById(long id) {

		logger.info("> findOneById id:{}", id);
		Product product = productRepository.findOneById(id);
		logger.info("< findOneById id:{}", id);
		return product;
	}
	

	public List<Product> findAll() {

		logger.info("> findAll");
		List<Product> products = productRepository.findAll();
		logger.info("< findAll");
		return products;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Product update(Product product) {

		logger.info("> update id:{}", product.getId());
		Product productToUpdate = productRepository.findOneById(product.getId());
		productToUpdate.setName(product.getName());
		productToUpdate.setOrigin(product.getOrigin());
		productToUpdate.setPrice(product.getPrice());
		productRepository.save(productToUpdate);
		logger.info(productToUpdate.toString());
		logger.info("< update id:{}", product.getId());

		return productToUpdate;
	}


}
