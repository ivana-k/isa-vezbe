# Vežbe 7 - Rad sa transakcijama

Za potrebe razumevanja primera potrebno je prethodno odgledati predavanja:

- [Transakcije: uvod](https://www.youtube.com/watch?v=9qjldEdsXVw)
- [Transakcije: pesimističko i optimističko zaključavanje](https://www.youtube.com/watch?v=NpJcBmR6G8I)

i pročitati [Transakcije.pdf](https://github.com/ivana-k/isa-vezbe/tree/main/Vezbe07/Transakcije.pdf) iz foldera Vezbe07.
Podrazumeva se da ste upoznati sa gradivom predstavljenim u primerima sa [Vežbi 4](https://github.com/ivana-k/isa-vezbe/tree/main/Vezbe04).

#### Konfiguracija projekta

Primeri **tx-example-optimistic** i **tx-example-pessimistic** demonstriraju rad sa transakcijama u Spring Boot aplikacijama gde je uključena podrška za rad sa [Spring Data JPA](https://spring.io/projects/spring-data-jpa).
Takođe, u primeru se koristi [Postgres RDBMS](https://www.postgresql.org/) koji treba instalirati zajedno sa GUI okruženjem (pgAdmin Workbench) za lakši rad. Uputstva za instalaciju možete pronaći [ovde](https://www.youtube.com/watch?v=e1MwsT5FJRQ), [ovde](https://www.postgresql.org/docs/9.5/index.html) i [ovde](https://lmgtfy.com/?q=how+to+install+postgresql).

Spring Boot aplikacije kao podrazumevani ORM koriste ujedno i najpopularniji [Hibernate](https://hibernate.org/orm/)
Podrška za korišćenje [Spring Data JPA](https://spring.io/projects/spring-data-jpa#overview) projekta se može uključiti dodavanjem odgovarajuće zavisnosti u `pom.xml` štikliranjem iste u [starter aplikaciji](https://start.spring.io/) ili ručno:

```
    <dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-data-jpa</artifactId>
	</dependency>
```

Za komunikaciju sa bazom potrebno je uključiti zavisnost za drajver baze (u primeru se koristi Postgres):

```
	<dependency>
        	<groupId>org.postgresql</groupId>
        	<artifactId>postgresql</artifactId>
        </dependency>
    </dependency>
```

#### Spring transakcije

Da bi se lakše razumelo kako Spring framework putem anotacija upravlja transakcijama potrebno je prisetiti se kako bi se ista stvar uradila čisto, bez dodatnih biblioteka (uprošćeno prikazano).

```
import java.sql.Connection;

Connection connection = DriverManager.getConnection("<url>", "<korisnicko_ime>", "<lozinka>"); // (1)

try (connection) {
    connection.setAutoCommit(false); // (2)

    // izvršavanje SQL upita

    connection.commit(); // (3)

} catch (SQLException e) {
    connection.rollback(); // (4)
}
```

1. Potrebno je dobaviti konekciju za započinjanje transakcije.
2. Kako se po default-u svaki SQL upit u Javi tretira kao posebna transakcija, da bismo to izbegli i sami upravljali transakcijama potrebno je isključiti auto commit.
3. Komitujemo izmene
4. Ili odradimo rollback ukoliko je došlo do neke greške.

Spring rad sa transakcijama rešava na dva načina:

1. [Programski](https://docs.spring.io/spring/docs/3.0.0.M4/reference/html/ch10s06.html) - pisanjem koda, ređe korišćeno
2. [Deklarativno](https://docs.spring.io/spring/docs/3.0.0.M4/reference/html/ch10s05.html) - upotrebom anotacija uz prateću konfiguraciju (XML ili Java kod), češće korišćeno

Kako je deklarativni pristup danas jednostavniji za korišćenje primeri su implementirani na ovaj način.
Tipičan primer deklarativno napisane transakcije u Spring je sledeći:

```
@Service
public class ProductService {
    @Transactional
    public Product save(Product product) {
		//neki SQL upit koji insertuje proizvod u bazu
        //i vraća torku koja sada ima pridodat izgenerisani ključ
		return savedProduct;
	}
}
```

Da bi se neka metoda proglasila transakcionom, potrebno je:

1. Da je uključena podrška za rad sa transakcijama, tj. da je konfiguraciona klasa anotirana sa `@EnableTransactionManagement`. Ukoliko se koristi Spring Boot ovaj korak je automatski odrađen kroz autokonfiguraciju (primer \_**\_tx-example-optimistic\_\_**, klasa `TxExampleApplication`)
2. Da je metoda deo klase koja je proglašena Spring komponentom (`@Component`, `@Controller`, `@Service,` `@Repository`, itd)
3. Da je definisan [transakcioni menadžer](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/transaction/PlatformTransactionManager.html) koji će upravljati transakcijama. Ukoliko se koristi Spring Boot transakcioni menadžer je takođe uključen autokonfiguracijom.
4. Da je sama [**public** metoda](https://docs.spring.io/spring/docs/current/spring-framework-reference/data-access.html#transaction) anotirana sa `@Transactional`.

Kada je sve podešeno (pod pretpostavkom da se podaci o konekciji sa bazom poput URL, korisničkog imena i lozinke nalaze u `dataSource` objektu umesto da su hardkodovani), metoda koja je anotirana sa `@Transactional` se (plastično predstavljeno) prevodi u:

```
@Service
public class ProductService {
    public Product save(Product product) {
        Connection connection = dataSource.getConnection();
        try (connection) {
            connection.setAutoCommit(false);

            //neki SQL upit koji insertuje proizvod u bazu
            //i vraća torku koja sada ima prododat izgenerisani ključ

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
    }
}
```

Šta se u stvari ovde dešava?
Kako je Spring [IoC kontejner](https://martinfowler.com/articles/injection.html) a `ProductService` je anotiran sa `@Service`, time je u stvari `ProductService` klasa registrovana kao komponenta Spring kontejnera koji upravlja njome kroz ceo njen životni ciklus. Kada se `ProductService` injektuje u drugu komponentu pomoću `@Autowired`, Spring ne samo da instancira tu komponentu već kreira i [**transakcioni proxy**](https://docs.spring.io/spring/docs/3.0.0.M3/reference/html/ch08s06.html) te komponente koji započinje transakciju pomoću transakcionog menadžera, zatim delegira posao `save` metodi `ProductService` klase koja obavi sve što je predviđeno i zatim, kada mu se vrati kontrola, pomoću transakcionog menadžera završava transakciju (commit, rollback).
![AOP Proxy](https://docs.spring.io/spring/docs/3.0.0.M3/reference/html/images/tx.png "AOP Proxy")

(Slika preuzeta sa [Declarative transaction management](https://docs.spring.io/spring/docs/3.0.0.M3/reference/html/ch11s05.html))

#### Nivoi propagacije

Nivo propagacije u Spring transakcijama može se definisati pomoću `propagation` atributa anotacije `@Transactional`, npr:

```
@Transactional(propagation = Propagation.REQUIRES_NEW)
```

Podržane opcije su:

- REQUIRED - metoda se priključuje tekućoj transakciji, otvara novu ako transakcija ne postoji
- REQUIRES_NEW - metoda uvek pokreće novu transakciju, ako postoji tekuća transakcija ona se suspenduje
- MANDATORY - metoda mora da se izvršava u transakciji koja mora biti ranije pokrenuta; ako je nema javlja se greška
- SUPPORTS - metoda će se prikljuciti tekućoj transakciji, ako ona postoji; ako ne postoji, izvršava se bez transakcije
- NOT_SUPPORTED - metoda se izvršava bez transakcije, čak i ako postoji tekuća transakcija
- NEVER - metoda se izvršava bez tranksacije; ako postoji tekuća transakcija, javlja se greška
- NESTED - metoda se izvršava u ugnježdenoj transakciji ako je trenutni thread povezan sa transakcijom u suprotnom startuje novu transakciju

#### Nivoi izolacije

Nivo izolacije u Spring transakcijama može se definisati pomoću `isolation` atributa anotacije `@Transactional`, npr:

```
@Transactional(isolation = Propagation.REPEATABLE_READ)
```

Ovo podešavanje se može prevesti u:

```
connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
```
Eksplicitno podešavanje nivoa izolacije iz Spring aplikacije nije preporučljivo, bolji pristup je definisanje nivoa izolacije na nivou baze podataka.
Više o nivoima izolacije možete naći na [linku](https://www.postgresql.org/docs/9.5/transaction-iso.html).

#### Optimističko zaključavanje

Preporučeni materijali za razumevanje primera **tx-example-optimistic**:

- [Hibernate optimistic locking](https://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html#locking-optimistic)
- [Optimistic locking version property with JPA and Hibernate](https://vladmihalcea.com/optimistic-locking-version-property-jpa-hibernate/)
- [Optimistic Locking in JPA](https://www.baeldung.com/jpa-optimistic-locking)

U klasi `Product` dodat je atribut `Long version` koji je anotiran sa `@Version` što omogućava Hibernate-u da pri svakoj izmeni torke inkrementira njegovu verziju u slučaju da je ona bila ista kao i na početku konverzacije. U slučaju da se verzije ne poklapaju dobija se `ObjectOptimisticLockingFailureException` izuzetak.
Pri svakoj operaciji koja ažurira torku Hibernate pravi sledeći upit:

```
update
    product
set
    name=?,
    origin=?,
    price=?,
    version=?
where
    id=?
    and version=?
```

U klasi `TxExampleApplicationTests` je simulirana jedna konfliktna situacija koja je rezultirala `ObjectOptimisticLockingFailureException` izuzetkom.

#### Pesimističko zaključavanje

Preporučeni materijali za razumevanje primera **tx-example-pessimistic**:

- [Hibernate pessimistic locking](https://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html#locking-pessimistic)
- [How do LockModeType.PESSIMISTIC_READ and LockModeType.PESSIMISTIC_WRITE work in JPA and Hibernate](https://vladmihalcea.com/hibernate-locking-patterns-how-do-pessimistic_read-and-pessimistic_write-work/)
- [Pessimistic Locking in JPA](https://www.baeldung.com/jpa-pessimistic-locking)

U interfejsu `ProductRepository` kreirana je metoda `findOneById` koja pravi upit u bazu koji za cilj ima da vrati jednu torku tipa `Product`. Pesimističko zaključavanje pomoću anotacija može se odraditi isključivo u repozitorijumima navođenjem anotacije `@Lock` uz odabir tipa zaključavanja, npr:

```
@Lock(LockModeType.PESSIMISTIC_WRITE)
```

Na originalni upit će se u ovom slučaju dodati `FOR UPDATE`:

```
select
    product0_.id as id1_0_,
    product0_.name as name2_0_,
    product0_.origin as origin3_0_,
    product0_.price as price4_0_
from
    product product0_
    product0_.id=? for update of product0_
```

Kada se radi sa pesimističkim zaključavanjem moguće je dobiti različite izuzetke:

- `PessimisticLockingFailureException` – dobija se kada nije moguće dobiti lock na torke (jer ih neko drugi drži)
- `LockTimeoutException` – dobija se kada nije moguće dobiti lock na torke u konfigurisanom vremenu
- `PersistanceException` – dobija se kada se desi problem pri perzistenciji torki

U primeru je izabran pristup da prva transakcija koja dođe do torke, taj red zaključa dok traje transakcija, a svaka sledeća transakcija da isti resurs ne čeka i za rezultat dobijemo `PessimisticLockingFailureException`.
Postgres baza nam to omogućava tako što na postojeći upit dodajemo `NOWAIT` pomoću:

```
@QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="0")})
```

Kompletan upit sada izgleda ovako:

```
select
    product0_.id as id1_0_,
    product0_.name as name2_0_,
    product0_.origin as origin3_0_,
    product0_.price as price4_0_
from
    product product0_
    product0_.id=? for update
        of product0_ nowait
```

U klasi `TxExampleApplicationTests` je simulirana jedna konfliktna situacija koja je rezultirala `PessimisticLockingFailureException` izuzetkom.

U logu se može videti ispis:

```
[pool-1-thread-2] o.h.engine.jdbc.spi.SqlExceptionHelper : SQL Error: 0, SQLState: 55P03
[pool-1-thread-2] o.h.engine.jdbc.spi.SqlExceptionHelper : ERROR: could not obtain lock on row in relation "product"
```

Prema Postgres [dokumentaciji](https://www.postgresql.org/docs/9.3/errcodes-appendix.html), kod 55P03 oznacava lock_not_available.

###### Dodatni materijali:

- [Using Transactions](https://docs.oracle.com/javase/tutorial/jdbc/basics/transactions.html)
- [Transaction Isolation](https://www.postgresql.org/docs/9.5/transaction-iso.html)
- [Data Access](https://docs.spring.io/spring/docs/current/spring-framework-reference/data-access.html)
- [A beginner’s guide to database locking and the lost update phenomena](https://vladmihalcea.com/a-beginners-guide-to-database-locking-and-the-lost-update-phenomena/)
- [LockMode and LockModeType](https://docs.jboss.org/hibernate/orm/5.0/userguide/html_single/Hibernate_User_Guide.html#locking-LockMode)
- [How to Avoid Double Booking and Race Conditions in Online Web Applications](https://www.youtube.com/watch?v=_95dCYv2Xv4)

## Pokretanje Spring aplikacije (Eclipse)

- importovati projekat u workspace: Import -> Maven -> Existing Maven Project
- instalirati sve dependency-je iz pom.xml
- desni klik na jedinu test klasu u projektu u src/test/java -> Run as -> JUnit Test
