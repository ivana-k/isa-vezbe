# Vežbe 4 - Rad sa ORM i bazom podataka

## jpa-example

Za potrebe razumevanja primera potrebno je prethodno odgledati predavanja:

* [JDBC uvod](https://www.youtube.com/watch?v=xH9d3ZYUn6c)
* [ORM: Uvod](https://www.youtube.com/watch?v=D31s6wwtAjM)
* [ORM: životni ciklus perzistentnih objekata](https://www.youtube.com/watch?v=0cgPpAfHTVg)
* [ORM: asocijacije između perzistentnih klasa](https://www.youtube.com/watch?v=mlHJBgt5Os4)
* [ORM: Mapiranje nasleđivanja](https://www.youtube.com/watch?v=KMKmYUnzPqM)
* [ORM: izbor primarnih ključeva](https://www.youtube.com/watch?v=D7Ae7qENK98)

i pročitati [OR-mapiranje.pdf](https://github.com/stojkovm/isara2021vezbe/blob/main/Vezbe04/OR-mapiranje.pdf) iz foldera _Vezbe04_.

Takođe, u primeru se koristi [Postgres RDBMS](https://www.postgresql.org/) koji treba instalirati zajedno sa GUI okruženjem (pgAdmin Workbench) za lakši rad. Uputstva za instalaciju možete pronaći [ovde](https://www.youtube.com/watch?v=e1MwsT5FJRQ), [ovde](https://www.postgresql.org/docs/9.5/index.html) i [ovde](https://lmgtfy.com/?q=how+to+install+postgresql).

#### Konfiguracija projekta

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
```
Sve dodatne zavisnosti koje zatrebaju za kasniji razvoj moguće je naći na [MVN repository](https://mvnrepository.com/)

Konfiguracija funkcionisanja ORM i baze može se odraditi u `application.properties` ili `application.yml` fajlu koji se nalazi u `src/main/resources`:
```
#Spring DataSource drajver koji će se koristiti za komunikaciju aplikacije sa bazom
spring.datasource.driverClassName=org.postgresql.Driver

#Navodi se baza koja se koristi
spring.datasource.platform=postgres

#Navodi se URL do baze koja je pokrenuta u lokalu na podrazumevanom portu 5432 i na serveru je kreirana šema baze pod nazivom "jpa"
#https://www.pgadmin.org/docs/pgadmin4/4.14/schema_dialog.html (ako koristimo Hibernate za kreiranje tabela, SQL deo sa linka nije potreban)
spring.datasource.url=jdbc:postgresql://localhost:5432/jpa

#Navode se kredencijali za konekciju na server baze
spring.datasource.username=postgres
spring.datasource.password=root

#Umesto da sami pišemo SQL skriptu za kreiranje tabela u bazi, Hibernate kreira tabele na osnovu anotacija @Entity i kada aplikacija zavrsi sa radom dropuje ih (create-drop)
#https://www.baeldung.com/spring-boot-data-sql-and-schema-sql
#Ako želimo sami da kreiramo skriptu za kreiranje tabela potrebno je u src/main/resources folderu kreirati i popuniti fajl pod nazivom schema.sql koji će Spring Boot automatski pokrenuti pri pokretanju aplikacije
spring.jpa.hibernate.ddl-auto = create-drop

#Hibernate SQL upiti se ispisuju na IDE konzoli
spring.jpa.show-sql = true

#formatira ispis SQL upita koje Hibernate pravi ka bazi na IDE konzoli
spring.jpa.properties.hibernate.format_sql=true

#https://docs.spring.io/spring-boot/docs/2.1.0.M1/reference/html/howto-database-initialization.html#howto-initialize-a-database-using-spring-jdbc
spring.datasource.initialization-mode=always

#https://stackoverflow.com/questions/43905119/postgres-error-method-org-postgresql-jdbc-pgconnection-createclob-is-not-imple
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true

#Hibernate optimizacija SQL upita za Postgres bazu
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.PostgreSQL95Dialect
```

Ono što se očekuje od studenata je da za svoj projekat kreiraju i skriptu za popunu šeme baze podacima. Spring Boot može to da uradi automatski jer će pri skeniranju aplikacije tražiti u `src/main/resources` folderu `data.sql` i posle kreiranja šeme će skriptu pokrenuti (ili u slučaju da se koristi Postgres baza `data-postgres.sql`).

#### Struktura projekta

Model podataka koji se koriste u prilogu izgleda kao na slici: <br>
![ISA model jpa-example](https://i.imgur.com/SUpm4Z1.jpg  "jpa-example db model")

U primeru je prikazana tipična [višeslojna arhitektura](https://www.petrikainulainen.net/software-development/design/understanding-spring-web-application-architecture-the-classic-way/) serverske strane monolitne Spring Boot aplikacije koja ima sledeće komponente (objašnjene u primerima jsp-example i rest-example u folderu _Vezbe03_):

* [POJO entiteti](https://www.baeldung.com/java-pojo-class), za koje je izabrana **IDENTITY** strategija generisanja ključeva, koji se mapiraju na tabele u bazi u paketu __model__
* [Repozitorijumski interfejsi](https://thoughts-on-java.org/implementing-the-repository-pattern-with-jpa-and-hibernate/) koji predstavljaju ugovor koji Spring Data JPA zahteva da se napravi sa developerom da bi developer napisao uputstvo (upite) za komunikaciju sa bazom podataka u paketu __repository__
* [Servisne metode](https://martinfowler.com/eaaCatalog/serviceLayer.html) koje predstavljaju poslovnu logiku aplikacije (funkcionalnosti koje želimo klijentima da omogućimo da koriste) u paketu __service__
* [Kontrolere](https://www.baeldung.com/spring-controllers) koji prihvataju klijentske zahteve, prosleđuju ih servisima na obradu koji kontaktiraju repozitorijumske metode koje komuniciraju sa bazom, konvertuju torke u objekte, vraćaju rezultat servisnim metodama koje vraćaju odgovor kontrolerima koji konačno vraćaju odgovor klijentima u paketu __controller__
* (**opcione komponente**) Objekti za transfer između različitih komponenti sistema - [DTO](https://martinfowler.com/eaaCatalog/dataTransferObject.html) - koji su u primeru uvedeni kao Proof of Concept način na koji se može optimalnije rukovati većom količinom informacija različitih entiteta u paketu __dto__

Uprošćeni tok zahteva i odgovora dat je na slici:
![ISA flow jpa-example](https://i.imgur.com/0uF8Mw3.png "jpa-example request-response flow")

###### Dodatni materijali:

* [Data Transfer Object - DTO](https://martinfowler.com/eaaCatalog/dataTransferObject.html)
* [Local DTO](https://martinfowler.com/bliki/LocalDTO.html)
* [Entity To DTO Conversion for a Spring REST API](https://www.baeldung.com/entity-to-and-from-dto-for-a-java-spring-application)
* [@Controller vs @RestController](https://www.baeldung.com/spring-controller-vs-restcontroller)
* [What is Spring Data JPA? And why should you use it?](https://thoughts-on-java.org/what-is-spring-data-jpa-and-why-should-you-use-it/)
* [JPQL](https://thoughts-on-java.org/jpql/)
* [Guide to Spring Data JPA](https://stackabuse.com/guide-to-spring-data-jpa/)

## inheritance-example

Primer Spring aplikacije u kojoj su prikazane strategije mapiranja nasleđivanja na relacionu bazu.
Primer prati predavanja sa [linka](https://www.youtube.com/watch?v=KMKmYUnzPqM).

###### Dodatni materijali:

* [Hibernate Inheritance Mapping](https://www.baeldung.com/hibernate-inheritance)
* [Inheritance Strategies with JPA and Hibernate – The Complete Guide](https://thoughts-on-java.org/complete-guide-inheritance-strategies-jpa-hibernate/)
* [The best way to use entity inheritance with JPA and Hibernate](https://vladmihalcea.com/the-best-way-to-use-entity-inheritance-with-jpa-and-hibernate/)
* [How to inherit properties from a base class entity using @MappedSuperclass with JPA and Hibernate](https://vladmihalcea.com/how-to-inherit-properties-from-a-base-class-entity-using-mappedsuperclass-with-jpa-and-hibernate/)

###### Pokretanje primera (Eclipse)

* importovati projekat u workspace: Import -> Maven -> Existing Maven Project
* instalirati sve dependency-je iz pom.xml
* desni klik na projekat -> Run as -> Java Application / Spring Boot app (ako je instaliran STS plugin sa Eclipse marketplace)
