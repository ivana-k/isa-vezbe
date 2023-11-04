package rs.ac.uns.ftn.informatika.tx.repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import rs.ac.uns.ftn.informatika.tx.domain.Product;

public interface ProductRepository extends JpaRepository<Product,Long> {
	
	//Zakljucavamo product koji se vraca za citanje i pisanje
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select p from Product p where p.id = :id")
	//Postgres po defaultu poziva for update bez no wait, tako da treba dodati vrednost 0 za timeout
	//kako bismo dobili PessimisticLockingFailureException ako pri pozivu ove metode torka nije dostupna
	
	/*
	 * To prevent the operation from waiting for other transactions to commit, use the NOWAIT option.
	 * With NOWAIT, the statement reports an error, rather than waiting, if a selected row cannot be locked immediately.
	 * Note that NOWAIT applies only to the row-level lock(s) — the required ROW SHARE table-level lock is still taken in the ordinary way.
	 * You can use LOCK with the NOWAIT option first, if you need to acquire the table-level lock without waiting.
	 * https://www.postgresql.org/docs/9.1/sql-select.html
	 */
	@QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="0")})
	public Product findOneById(@Param("id")Long id);
	

}
