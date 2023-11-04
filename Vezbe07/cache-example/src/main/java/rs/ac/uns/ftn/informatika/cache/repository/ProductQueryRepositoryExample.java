package rs.ac.uns.ftn.informatika.cache.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import rs.ac.uns.ftn.informatika.cache.domain.Product;

public interface ProductQueryRepositoryExample extends Repository<Product, Long> {
	
	@Query(value = "select * from #{#entityName} b where b.name=?1", nativeQuery = true)
	List<Product> findByName(String name);

	@Query(value = "select name, origin, price from Product p where p.price>?1 and p.price<?2")
	List<Product> findByPriceRange(long price1, long price2);

	@Query(value = "select name, origin, price from Product p where p.name like %:name%")
	List<Product> findByNameMatch(@Param("name") String name);

	@Query(value = "select name, origin, price from Product p where p.name = :name AND p.origin=:origin AND p.price=:price")
	List<Product> findByNamedParam(@Param("name") String name, @Param("origin") String origin,
			@Param("price") long price);
}
