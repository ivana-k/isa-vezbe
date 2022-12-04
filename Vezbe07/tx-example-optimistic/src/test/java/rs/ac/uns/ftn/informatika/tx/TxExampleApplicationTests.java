package rs.ac.uns.ftn.informatika.tx;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;

import rs.ac.uns.ftn.informatika.tx.domain.Product;
import rs.ac.uns.ftn.informatika.tx.service.ProductService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TxExampleApplicationTests {
	
	
    @Autowired
	private ProductService productService;

	@Before
	public void setUp() throws Exception {
		productService.save(new Product("P1", "O1", 5L));
		productService.save(new Product("P2","O2", 4L));
		productService.save(new Product("P3","O3", 3L));
		productService.save(new Product("P4","O4", 1L));
		productService.save(new Product("P5","O4", 1L));
	}

	@Test(expected = ObjectOptimisticLockingFailureException.class)
	public void testOptimisticLockingScenario() throws Throwable {	

		ExecutorService executor = Executors.newFixedThreadPool(2);
		Future<?> future1 = executor.submit(new Runnable() {
			
			@Override
			public void run() {
		        System.out.println("Startovan Thread 1");
				Product productToUpdate = productService.findById(1L);// ocitan objekat sa id 1
				productToUpdate.setPrice(800L);// izmenjen ucitan objekat
				try { Thread.sleep(3000); } catch (InterruptedException e) {}// thread uspavan na 3 sekunde da bi drugi thread mogao da izvrsi istu operaciju
				productService.save(productToUpdate);// bacice ObjectOptimisticLockingFailureException
				
			}
		});
		executor.submit(new Runnable() {
			
			@Override
			public void run() {
		        System.out.println("Startovan Thread 2");
				Product productToUpdate = productService.findById(1L);// ocitan isti objekat sa id 1 kao i iz prvog threada
				productToUpdate.setPrice(900L);// izmenjen ucitan objekat
				/*
				 * prvi ce izvrsiti izmenu i izvrsi upit:
				 * Hibernate: 
				 *     update
				 *         product
				 *     set
				 *         name=?,
        		 *         origin=?,
                 *         price=?,
                 *         version=? 
                 *     where
                 *         id=? 
                 *         and version=?
                 *         
                 * Moze se primetiti da automatski dodaje na upit i proveru o verziji
				 */
				productService.save(productToUpdate);
			}
		});
		try {
		    future1.get(); // podize ExecutionException za bilo koji izuzetak iz prvog child threada
		} catch (ExecutionException e) {
		    System.out.println("Exception from thread " + e.getCause().getClass()); // u pitanju je bas ObjectOptimisticLockingFailureException
		    throw e.getCause();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		executor.shutdown();

	}

}
