package rs.ac.uns.ftn.informatika.ratelimiter.redis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.informatika.ratelimiter.redis.domain.Product;

public interface ProductRepository extends JpaRepository<Product,Long> {

}
