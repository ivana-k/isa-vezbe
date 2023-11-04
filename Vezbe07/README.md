# Vežbe 6

## cp-example

Aplikacije često moraju da koriste resurse koji su skupi za kreiranje i održavanje (npr. konekcije za pristup bazi, niti, itd).
[Resource pool](https://martinfowler.com/bliki/ResourcePool.html) pruža dobar način za upravljanje takvim resursima.

[Object Pool](https://sourcemaking.com/design_patterns/object_pool) dizajn šablon opisuje način za upravljanje resursima koji su skupi. Klijent koji želi da pristupa aplikaciji koja ima implementiran resource pool za resurse koji su skupi, treba da zatražni resurs iz pool-a umesto da zatražni kreiranje novog.
U opštem slučaju, veličina pool-a može da raste, tj. u pool će se dodavati novi objekti ukoliko je u trenutku zahteva prazan, a obično do nekog predefinisanog broja objekata koje pool može da kreira i njima upravlja.

Tipičan primer resursa koji je skup za kreiranje i korišćenje je konekcija ka bazi.
Komunikacija između aplikacije i baze podataka je sledeća:

- Sloj aplikacije koji treba da komunicira sa bazom zahteva od DataSource objekta da otvori konekciju ka bazi
- DataSoruce mora da iskoristi drajver odgovarajuće baze da otvori konekciju ka istoj
- Konekcija se kreira i TCP socket se otvori
- Aplikacija u zavisnosti od zahteva klijenta upisuje ili čita podatke iz baze
- Konekcija ka bazi više nije potrebna te se zatvara
- TCP socket se zatvara

Connection pooling rešenja nam mogu pomoći da sagledamo razlike između slučajeva kada imamo implementiran i iskonfigurisan connection pool i kada isti nemamo.
Kad god se konekcija ka bazi zatraži, umesto da se svaki put kreira nova, dobaviće se slobodna konekcija iz connection pool-a. Connection pool će kreirati novu konekciju samo ako nema više slobodnih i pool nije dostigao svoju maksimalnu veličinu. Kada se obavi komunikacija sa bazom, konekcija se oslobađa i vraća u pool za ponovno korišćenje.
Developeri [HikariCP](https://github.com/brettwooldridge/HikariCP) kao jednog od trenutno najboljih rešenja za Java aplikacije istraživali su šta sve utiče na konfiguraciju connection pool-a i interesantan video možete videti na [linku](https://github.com/brettwooldridge/HikariCP/wiki/About-Pool-Sizing).

HikariCP se koristi kao podrazumevana implementacija connection poolinga u Spring Boot 2.x aplikacijama, te posebno uključivanje zavisnosti nije potrebno.
Dovoljno je samo u application.properties ili application.yml fajlu navesti konfiguracione parametre za pool.

Dodatni resursi:

- [[ISA] Resource pooling](https://www.youtube.com/watch?v=2OOkR4jgomU)
- [The anatomy of Connection Pooling](https://vladmihalcea.com/the-anatomy-of-connection-pooling/)
- [JDBC Connection Pooling](https://www.progress.com/tutorials/jdbc/jdbc-jdbc-connection-pooling)
- [JDBC Connection Pooling Best Practices](https://javaranch.com/journal/200601/JDBCConnectionPooling.html)
- [Tuning postgres connection pools](https://developer.bring.com/blog/tuning-postgres-connection-pools/)
- [Tomcat Thread Pool](https://tomcat.apache.org/tomcat-9.0-doc/config/executor.html)
- [How Exactly Does Tomcat's Thread Pool Work](https://stackoverflow.com/questions/22854498/how-exactly-does-tomcats-threadpool-work)
- [Configuring a Hikari Connection Pool with Spring Boot](https://www.baeldung.com/spring-boot-hikari)
- [Understanding HikariCP’s Connection Pooling behaviour](https://medium.com/@rajchandak1993/understanding-hikaricps-connection-pooling-behaviour-467c5a1a1506)

## cache-example

U primeru je predstavljena ideja o keširanju kao konceptu i o postojanju dva nivoa keša koja Hibernate podržava - L1 i L2.

U primeru je korišćena _in-memory_ baza [H2](http://www.h2database.com/html/main.html) koja je zgodna za brži i lakši razvoj i ne zahteva posebnu instalaciju (_workbench_-u se može pristupiti iz _browser_-a). Još neki proizvođači _in-memory_ baza su [HSQLDB](http://hsqldb.org/) i [Apache Derby](https://db.apache.org/derby/). H2 baza se integriše sa Maven aplikacijom dodavanjem sledeće zavisnosti:

```
<!-- Dependency za in-memory bazu H2 -->
<dependency>
	<groupId>com.h2database</groupId>
	<artifactId>h2</artifactId>
</dependency>
```

### Level 1 keširanje - L1

Ovaj nivo keširanja je podržan od strane Hibernate-a, nije potrebna nikakva dodatna konfiguracija i ne može se isključiti. L1 keširanje se tiče Hibernate sesije. Kada se objekat učita u sesiju, prilikom svakog sledećeg upita za taj isti objekat, Hibernate neće slati upit ka bazi, već će objekat dobavljati iz keša. L1 keš omogućava da, unutar sesije, zahtev za objektom iz baze uvek vraća istu instancu objekta i tako sprečava konflikte u podacima i sprečava Hibernate da učita isti objekat više puta. Bitno je napomenuti da objekat koji se čuva u kešu na ovom nivu nije vidljiv drugim sesijama i "živi" koliko i Hibernate sesija. Kada se sesija uništi, uništava se i keš i svi objekti koji se u njemu nalaze.

Pošto Hibernate čuva sve objekte u L1 kešu, treba pažljivo i efikasno izvršavati _query_-je, da bi se izbegli potencijalni problemi sa memorijom. Na primer, ne treba čitati objekte iz baze ukoliko oni nisu potrebni, ne bi trebalo učitavati objekte u _for_ petlji, voditi računa o _fetch type_-u koji se koristi itd. Prilikom izrade projekta, postavite opciju `spring.jpa.show-sql = true` u `application.properties` fajlu i obratite pažnju kako izgledaju upiti koje Hibernate šalje bazi i koliko ih ima. Hibernate šalje mnogo upita i vrlo brzo keš postaje memorijski veoma zahtev. Problem sa veličinom L1 keša ne bi trebalo da imate (verovatno i nećete) prilikom izrade projektnog zadataka, ali to predstavlja realan problem o kojem treba voditi računa.

### Level 2 keširanje - L2

Ovaj nivo keširanja je podržan od Hibernate-a, ali je neophodan eksterni provajder. U primeru je korišćen [EhCache](http://www.ehcache.org/documentation/), ali postoje i drugi poput [Infinispan](https://infinispan.org/), [Redis](https://redis.io/) ili [JBoss Cache](https://jbosscache.jboss.org/).

Postoje različite strategije keširanja:

1. **Read Only:** strategija koju treba koristiti za objekte koji će se uvek čitati, ali se nikada neće ažurirati. Ova strategija je dobra kada se radi sa statičkim podacima, kao na primer konfiguracija aplikacije. Ovo je najjednostavnija strategija sa najboljim performansama, jer nema dodatnog posla da bi se proverilo da li je objekt ažuriran u bazi podataka ili nije
2. **Read Write:** strategija koju treba koristiti za objekte koji se mogu i ažurirati. Ukoliko se baza podataka ažurira i van aplikacije kroz neke druge, Hibernate neće biti svestan tih izmena, a podaci koji se čuvaju u kešu mogu biti zastareli. Zato treba voditi računa da kada se koristi ova strategija keširanja, baza podataka isključivo ažurira kroz Hibernate API
3. **Nonrestricted Read Write:** strategija koja se koristi ukoliko se podaci ažuriraju retko, skoro nikad. Ne garantuje konzistentnost između keša i baze podataka, i zbog toga je prihvatljiva u sistemima gde zastareli podaci ne predstavljaju kritične probleme
4. **Transactional:** strategija koja se koristi kod visoko konkurentnih sistema gde je ključno sprečiti da se u bazi podataka nalaze zastareli podaci.

#### EHCache

EhCache podržava sve navedene strategije keširanja, i zbog toga predstavlja jedan od najboljih i najpopularnijih provajdera za L2 keširanje u Spring aplikacijama.

Da bi se EhCache uključio u Maven projekat, neophodno je uključiti sledeće zavisnosti:

```
<dependency>
	<groupId>org.ehcache</groupId>
	<artifactId>ehcache</artifactId>
</dependency>
<!-- Potrebno za logovanje dogadjaja -->
 <dependency>
	<groupId>javax.cache</groupId>
	<artifactId>cache-api</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```

Konfiguracija EhCache-a zahteva definisanje XML fajla `ehcache.xml` koji se nalazi u `resources` folderu, kao i uključivanje podrške za keširanje dodavanjem anotacije `@EnableCaching` u neku od konfiguracionih klasa (u primeru, klasa _CacheExampleApplication_). Takođe, u `application.properties` potrebno je dodati liniju za učitavanje konfiguracije u aplikaciju:

```
spring.cache.jcache.config=classpath:ehcache.xml
```

Ostatak konfiguracije podrazumeva dodavanje anotacija nad klasama modela ili metodama koje vraćaju objekte koji se trebaju keširati, npr. `@Cacheable` ili `@CacheEvict` u klasi _ProductService_.

EhCache pruža mogućnost definisanja vremena koliko dugo objekat živi u kešu putem `<expiry>` elementa:

- ttl - TIME TO LIVE - ukupno vreme koje će objekti provesti u kešu bez obzira da li im se pristupa ili ne i
- tti - TIME TO IDLE - ukupno vreme koje će objekti provesti u kešu ako im se ne pristupa

Implementirana je i klasa `CacheLogger` u paketu _rs.ac.uns.ftn.informatika.cache.logger_ koja osluškuje svaku promenu u kešu i zadužena je za logovanje svih događaja. Događaji koji postoje su sledeći:

1. **CREATED** - dodavanje objekta u keš
2. **EXPIRED** - detekcija da je objektu isteklo vreme u kešu (ttl, tti)
3. **EVICTED** - izbacivanje objekta iz keša (dešava se ili eksplicitnim pozivanjem _evict_-a ili po principu **LRU** (_Least Recently Used_, kada se dodaje novi objekt u popunjen keš izbacuje se iz keša onaj koji se najdavnije koristio)
4. **REMOVED** - uklanjanje objekta iz keša.

EhCache pruža mogućnost čuvanja keširanih objekata na Java heap-u, u RAM memoriji kao i na disku, što se podešava u `<resoruces>` elementu.

U primeru su konfigurisana dva keša:

1. **default**: predstavlja podrazumevani keš u kojem se keširaju svi objekti za koje nije naznačeno drugačije. Definiše se u `<cache-template name="default">` elementu.
2. **product**: keš kolekcija definisana se u `<cache alias="product" uses-template="default">` elementu. Atribut **alias** označava naziv keš kolekcije, dok **uses-template** atribut referencira šablon koji se _override_-uje. Vrednosti koje nisu navedene za keš kolekciju imaju vrednosti koje su definisane u referenciranom šablonu. Element `<key-type>` označava tip podatka koji će se koristiti za ključ, dok `<value-type>` označava tip podatka koji se nalaze u kešu. Prema konfiguraciji, u ovoj kolekciji se keširaju _Product_ objekti, a čuvaju se samo 2 objekta na Java _heap_-u. Kako će _resorces_ element da pregazi isti element iz _default_ keša, ne postoji mogućnost keširanja _Product_-a ni u RAM-u ni na disku.

U klasi `ProductService` iz paketa _rs.ac.uns.ftn.informatika.cache.service_ je definisana dodatna konfiguracija za EhCache:

1. Smeštanje _Product_ objekata u _product_ keš kolekciju

   ```
   @Cacheable("product")
   Product findOne(long id);
   ```

2. Eksplicitno brisanje svih objekata iz _product_ keš kolekcije

   ```
   @CacheEvict(cacheNames = { "product" }, allEntries = true)
   void removeFromCache();
   ```

### Demonstracija primera

Preko Postman-a slati zahteve i u konzoli Spring aplikacije čitati ispise.

1. HTTP GET http://localhost:8080/products/1

   Ispis na konzoli:

   ```
   2020-04-20 16:31:07.627  INFO 54359 --- [nio-8080-exec-1] r.a.u.f.i.c.service.ProductServiceImpl   : Product with id: 1 successfully cached!
   Hibernate: select product0_.id as id1_0_0_, product0_.name as name2_0_0_, product0_.origin as origin3_0_0_, product0_.price as price4_0_0_ from product product0_ where product0_.id=?
   2020-04-20 16:31:07.778  INFO 54359 --- [e [_default_]-0] r.a.u.f.i.cache.logger.CacheLogger       : Key: 1 | EventType: CREATED | Old value: null | New value: rs.ac.uns.ftn.informatika.cache.domain.Product@2d9a6dea
   ```

   Desio se događaj **CREATED** i objekat je uspešno dodat u keš pod ključem 1. Vidimo da je objekat pročitan iz baze (deo loga koji počinje sa _Hibernate:_)

2. Pošto je u `ehcache.xml` fajlu definisano da je _ttl_ vreme 15 sekundi, sačekati više od 15 sekundi pre nego što se pošalje nov zahtev za preuzimanje ponovo istog objekta

3. HTTP GET http://localhost:8080/products/1

   Ispis na konzoli:

   ```
   2020-04-20 16:34:50.116  INFO 54359 --- [nio-8080-exec-5] r.a.u.f.i.c.service.ProductServiceImpl   : Product with id: 1 successfully cached!
   Hibernate: select product0_.id as id1_0_0_, product0_.name as name2_0_0_, product0_.origin as origin3_0_0_, product0_.price as price4_0_0_ from product product0_ where product0_.id=?
   2020-04-20 16:34:50.116  INFO 54359 --- [e [_default_]-1] r.a.u.f.i.cache.logger.CacheLogger       : Key: 1 | EventType: EXPIRED | Old value: rs.ac.uns.ftn.informatika.cache.domain.Product@2d9a6dea | New value: null
   2020-04-20 16:34:50.118  INFO 54359 --- [e [_default_]-1] r.a.u.f.i.cache.logger.CacheLogger       : Key: 1 | EventType: CREATED | Old value: null | New value: rs.ac.uns.ftn.informatika.cache.domain.Product@59fa46ae
   ```

   Prvo se desio događaj **EXPIRED** za objekat koji je u keš dodat u koraku 1. Zatim se ponovo desio događaj **CREATED** i objekat je uspešno dodat u keš pod ključem 1. Vidimo da je objekat pročitan iz baze (deo loga koji počinje sa _Hibernate:_)

4. Za manje od 15 sekundi poslati novi zahtev (da ne istekne definisano _ttl_)
5. HTTP GET http://localhost:8080/products/2

   Ispis na konzoli:

   ```
   2020-04-20 16:38:07.904  INFO 54359 --- [nio-8080-exec-9] r.a.u.f.i.c.service.ProductServiceImpl   : Product with id: 2 successfully cached!
   Hibernate: select product0_.id as id1_0_0_, product0_.name as name2_0_0_, product0_.origin as origin3_0_0_, product0_.price as price4_0_0_ from product product0_ where product0_.id=?
   2020-04-20 16:38:07.907  INFO 54359 --- [e [_default_]-2] r.a.u.f.i.cache.logger.CacheLogger       : Key: 2 | EventType: CREATED | Old value: null | New value: rs.ac.uns.ftn.informatika.cache.domain.Product@91e5df7
   ```

   Desio se događaj **CREATED** i objekat je uspešno dodat u keš pod ključem 2. Vidimo da je objekat pročitan iz baze (deo loga koji počinje sa _Hibernate:_)

6. Za manje od 15 sekundi poslati novi zahtev (da ne istekne definisano _ttl_). Dodaje se treći objekat u keš, a podešeno je da se u kešu čuvaju do dva objekta

7. HTTP GET http://localhost:8080/products/3

   Ispis na konzoli:

   ```
   2020-04-20 16:41:56.692  INFO 54359 --- [nio-8080-exec-4] r.a.u.f.i.c.service.ProductServiceImpl   : Product with id: 3 successfully cached!
   Hibernate: select product0_.id as id1_0_0_, product0_.name as name2_0_0_, product0_.origin as origin3_0_0_, product0_.price as price4_0_0_ from product product0_ where product0_.id=?
   2020-04-20 16:41:56.694  INFO 54359 --- [e [_default_]-3] r.a.u.f.i.cache.logger.CacheLogger       : Key: 3 | EventType: CREATED | Old value: null | New value: rs.ac.uns.ftn.informatika.cache.domain.Product@39a3009d
   2020-04-20 16:41:56.696  INFO 54359 --- [e [_default_]-3] r.a.u.f.i.cache.logger.CacheLogger       : Key: 1 | EventType: EVICTED | Old value: rs.ac.uns.ftn.informatika.cache.domain.Product@7f9c0f4e | New value: null

   ```

   Desio se događaj **CREATED** i objekat je uspešno dodat u keš pod ključem 3. Događaj **EVICTED** se dešava za objekat koji se u kešu čuva pod ključem 1, što znači da se po LRU principu, ovaj objekat izbacuje iz keša, a novokreirani objekat pod ključem 3 ubacuje u keš. Vidimo da je objekat pročitan iz baze (deo loga koji počinje sa _Hibernate:_)

8. Za manje od 15 sekundi poslati novi zahtev (da ne istekne definisano _ttl_)

9. HTTP GET http://localhost:8080/products/3

   Na konzoli nema nikakvog ispisa. Bitno je primetiti da ne postoji deo loga koji počinje sa _Hibernate:_, što znači da objekat koji je vraćen klijentu nije pročitan iz baze već iz keša.

### Dodatni materijali za razumevanje keširanja:

1. [Hibernate Second-Level Cache](https://www.baeldung.com/hibernate-second-level-cache)
2. [Difference between First and Second Level Cache in Hibernate](https://javarevisited.blogspot.com/2017/03/difference-between-first-and-second-level-cache-in-Hibernate.html)
3. [Spring cache annotations: some tips & tricks](https://www.foreach.be/blog/spring-cache-annotations-some-tips-tricks)

## rate-limiter-example

U primeru je predstavljen RateLimiting mehanizam; ograničavanje broja zahteva u određenom vremenskom intervalu.

Korišćena je in-memory baza, slično primeru cache-example.

Biblioteka upotrebljena u okviru primera je [**Resilience4j**](https://resilience4j.readme.io/).
Neophodno je uključiti je kao zavisnost u okviru **pom.xml**, kao i zavisnost za **AOP** na koju se oslanja.

```
<dependency>
	<groupId>io.github.resilience4j</groupId>
	<artifactId>resilience4j-spring-boot2</artifactId>
	<version>1.5.0</version>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

Konfigurisanje moguće je kroz application.properties datoteku. Moguće je kreiranje više konfiguracija za više različitih slučajeva, s tim da ime mora biti jedinstveno.

U isečku, definisane su dve instance, **standard** i **premium**. Za svaku definisane su sledeće konfiguracije:

- **limitForPeriod**: maksimalan broj poziva za definisani interval
- **limitRefreshPeriod**: definisani vremenski interval
- **timeoutDuration**: vreme čekanja na obradu zahteva - korisno u slučaju dugih _limitRefreshPeriod_ intervala, kao na primer:
  1.  aplikacija je konfigurisana tako da je **limitForPeriod 10 zahteva** i **limitRefreshPeriod 1h**, dok je **timeoutDuration 2 sekunde**
  2.  klijent je poslao maksimalan broj poziva za definisani interval; svaki sledeći neće biti obrađen dok vremenski interval ne istekne
  3.  prošlo je **59 minuta i 59 sekundi** i klijent šalje zahtev; zahtev bi trebao da bude odbijen pošto ograničen interval nije prošao, ali pošto smo definisali da je vreme čekanja na obradu zahteva 2 sekunde, dok korisnik čeka proći će ograničeni vremenski interval i zahtev će biti obrađen; u ovakvoj situaciji smo uštedeli slanje još jednog zahteva od strane korisnika u slučaju da je vremenski interval blizu isteka

```
resilience4j.ratelimiter.instances.standard.limitForPeriod=1
resilience4j.ratelimiter.instances.standard.limitRefreshPeriod=10s
resilience4j.ratelimiter.instances.standard.timeoutDuration=2

resilience4j.ratelimiter.instances.premium.limitForPeriod=3
resilience4j.ratelimiter.instances.premium.limitRefreshPeriod=1s
resilience4j.ratelimiter.instances.premium.timeoutDuration=0
```

Moguće je deklarativno navesti za koje metode će biti iskorišćen RateLimiting princip. U okviru ProductServiceImpl klase definisana je upotreba **standard** RateLimiter instance:

```
@RateLimiter(name = "standard", fallbackMethod = "standardFallback")
public List<Product> findAll() {
	return productRepository.findAll();
}

// Metoda koja ce se pozvati u slucaju RequestNotPermitted exception-a
public List<Product> standardFallback(RequestNotPermitted rnp) {
	LOG.warn("Prevazidjen broj poziva u ogranicenom vremenskom intervalu");
	throw rnp;
}
```

Upotrebom **@RateLimiter** anotacije navedeno je da prilikom poziva metode _findAll_ treba da se prvo proveri da li je prekoračen maksimalan broj zahteva u zadatom vremenskom intervalu.
Broj zahteva i vremenski interval biblioteka zaključuje na osnovu navedenog parametra **name** (u ovom slučaju koristi se standard instanca definisana u okviru application.properties datoteke).
U slučaju prekoračenja, biblioteka baca **RequestNotPermitted** izuzetak. Moguće je, kao parametar anotacije, navesti i ime metode koja će obraditi nastali izuzetak.

Dokumentaciju prati i lista primera koji su javno dostupni na [GitHub repozitorijumu](https://github.com/resilience4j/resilience4j-spring-boot2-demo).

### Primena

- Sa porastom broja korisnika aplikacije, raste i broj zahteva koje server treba da opsluži. U takvim slučajevima želimo da izbegnemo situacije u kojima server može biti preopterećen. RateLimiting je jedan od načina ograničavanja opterećenja servera.

- Bezbednost aplikacija je bitan aspekt svakog razvoja. Jedan od čestih napada sa kojima se srećemo je i [DoS napad](https://www.paloaltonetworks.com/cyberpedia/what-is-a-denial-of-service-attack-dos). Jedan od načina zaštite od ovakvih napada jeste ograničavanje broja poziva upućenih na naš API.

- Mnogi servisi dostupni preko interneta koji pružaju mogućnost poziva svog API-ja nisu u potpunosti besplatni i podržavaju različite pakete. Način na koji se pomenuti princip može implementirati jeste ograničavanje broja poziva određene grupe korisnika. Moguće je definisati više grupa i za svaku od njih poseban broj poziva za određeni interval.

## redis-cache-example

U okviru primera prikazan je pristup keširanju upotrebom key-value NoSQL baze [Redis](https://redis.io/). Ova baza podataka spada u **in-memory** baze podataka (sve vrednosti se čuvaju u okviru RAM memorije računara). Dodatno, podržava perzistenciju na disk (slično EhCache).

Bazu je moguće instaliati lokalno na Linux i MacOS operativnim sistemima [(uputstvo za instalaciju)](https://redis.io/docs/getting-started/), dok direktna podrška za Windows mašine ne postoji. Instalacija i pokretanje baze je moguće upotrebom [WSL2 podsistema](https://learn.microsoft.com/en-us/windows/wsl/install) ili upotrebom [Docker kontejnera](https://hub.docker.com/_/redis). Na sledećem linku nalazi se [uputstvo za instalaciju Redis-a](https://redis.io/docs/getting-started/installation/install-redis-on-windows/).

NoSQL baze su sveprisutne u svetu razvoja softvera od 2010-ih godina. Glavna odlika ovih baza je nepostojanje klasične tabelarne strukture podataka kao i SQL-a kao upitnog jezika (odakle i potiče sam naziv). Potreba za NoSQL bazama posledica je raznolikosti i količine podataka koji nastaju u okviru modernih aplikacija, pogotovo socijalnih platformi. Zbog raznolikosti podataka ne možemo lako (ili uopšte) definisati šemu baze podataka i oslanjamo se na skladištenje polustruktuiranih i nestruktuiranih podataka.

U okviru primera demonstrirane su slične funkcionalnosti prikazane u okviru [cache-example](#cache-example).

Kako bi se uključila podrška za rad sa kešom i Redis bazom, u okviru **pom.xml** datoteke dodate su sledeće zavisnosti:

```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-cache</artifactId>
	<version>2.4.3</version>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-redis</artifactId>
	<version>2.4.3</version>
</dependency>
```

U okviru **CacheConfiguration** klase nalazi se konfiguracija keširanja uz pomoć Redis-a. Dat je primer izmene **default-ne** konfiguracije, kao i fine-tuning keša. U isečku su izdvojena sledeća podešavanja:

- **TTL**: ukupno vreme koje objekat može provesti u kešu pre nego što se automatski briše,
- **rukovanje null vrednostima**: eksplicitno je navedeno da se null vrednosti ne keširaju,
- **prefix**: svaki objekat se čuva u bazi pod ključem čija je predefinisana vrednost "ime_keša:redni_broj_objekta"; dodatno je moguće definisati prefiks koji će ključ sadržati,
- **serijalizacija**: moguće je definisati koji serijalizator će biti korišćen za zapisivanje objekata u bazi.

```
.entryTtl(Duration.ofMinutes(15))
.disableCachingNullValues()
.prefixCacheNameWith("isa-example:")
.serializeValuesWith(RedisSerializationContext.SerializationPair
		.fromSerializer(new GenericJackson2JsonRedisSerializer()));
```

U okviru **ProductService** interfejsa dodate su anotacije iznad **findOne**, **updateOne** i **removeFromCache** metoda.

```
@Cacheable("product")
Product findOne(long id);

@CachePut(cacheNames = {"product"}, key = "#root.args[0]")
Product updateOne(long id, Product product);

@CacheEvict(cacheNames = {"product"}, allEntries = true)
void removeFromCache();
```

Anotacije **@Cachable** i **@CacheEvict** opisane su u okviru prethodno [primera](#cache-example). Anotacija **@CachePut** razlikuje se od @Cachable u tome što će se metoda koju anotira uvek izvršiti. Nakon izvršavanja metode, povratna vrednost (Product objekat) biće **ažurirana u okviru keša**. Poseban značaj imaju podešavanja:

- **cacheNames**: definiše u okviru kog keša se vrši ažuriranje,
- **key**: definiše pod kojim ključem je objekat koji će biti ažuriran.

Vrednost koju key uzima je, u ovom slučaju, vrednost prvog argumenta metode. U okviru [dokumentacije](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/cache/annotation/CachePut.html#key--) dati su način preuzimanja drugih vrednosti koje mogu biti značajne.

Korisna strana @CachePut anotacije je što keš sadrži najnovije objekte koji su u skladu sa stanjem u samoj bazi. Na ovaj način postiže se konzistentnost podataka u kešu i u bazi. Ali, svakako treba obazrivo keširati vrednosti. Performanse će opadati u slučaju da često menjamo entitet i često osvežavamo vrednost keša u odnosu na slučaj kada direktno dobavljamo vrednost iz keša.

### Mehanizam izbacivanja vrednosti iz keša

Moguće je memorijski ograničiti količinu podataka koja se čuva u Redis kešu. Za razliku od EhCache implementacije, nije moguće ograničiti broj objekata u kešu. Redis podržava tri glavna načina za izbacivanje podataka iz keša:

1.  **noeviction** - bez izbacivanja; kada se memorija popuni, nove vrednosti se ne zapisuju u keš;
2.  **allkeys-lru** - Least Recently Used; upotrebom [probabilističkih metoda](https://www.cut-the-knot.org/Probability/ProbabilisticMethod.shtml) izbacuje se objekat kojem je najdavnije pristupano
3.  **allkeys-lfu** - Least Frequently Used; izbacuje se objekat kojem je najmanje pristupano

Vrednost maksimalne dostupne memorije, kao i strategije izbacivanja podataka konfiguriše se direktno nad Redis bazom kroz **redis.conf** datoteku.

```
maxmemory 100mb
maxmemory-policy allkeys-lru
```

### Testiranje

U okviru test direktorijuma nalaze se test slučajevi koji pokrivaju dobavljanje vrednosti objekta iz keša, kao i slučaj ažuriranja objekta koji se već nalazi u kešu.

Kako se primer oslanja Redis bazu, instanca baze mora postojati kako bu aplikacija funkcionisala. Baza se može lokalno instalirati. U tom slučaju, pored instalacije, neophodno je voditi računa o stanju podataka što može biti nezgodan posao u slučaju velikog broja konkurentnih testova. Rešenje kojim se prevazilaze ovi problemi jeste upotreba [Testcontainers biblioteke](https://www.testcontainers.org/) koja omogućava kreiranje lightweight instanci baza podataka, redova čekanja i sličnih zavisnosti neophodnih za izvršavanje testova. Biblioteka se oslanjajući se na [Docker](https://www.docker.com/) kontejnere. (Kako bi testovi mogli da se pokrenu, neophodno je instalirati i pokrenuti Docker.)

Neophodno je uključiti sledeću zavisnost u okviru **pom.xml** datoteke:

```
<dependency>
	<groupId>org.testcontainers</groupId>
	<artifactId>testcontainers</artifactId>
	<version>1.17.3</version>
	<scope>test</scope>
</dependency>
```

Dodatno, u okviru testova, upotrebom gorenavedene biblioteke, potrebno je kreirati sve potrebne zavisnosti na sledeći način:

```
static {
	GenericContainer<?> redis =
			new GenericContainer<>(DockerImageName.parse("redis:5.0.3-alpine")).withExposedPorts(6379);
	redis.start();
	System.setProperty("spring.redis.host", redis.getHost());
	System.setProperty("spring.redis.port", redis.getMappedPort(6379).toString());
}
```

Potrebno je navesti Docker sliku koja će se koristiti za kreiranje instance Redis baze. Nakon toga, biblioteka kreira instancu baze i dodeljuje joj nasumičan slobodan port kog dodatno postavlja u podešavanjima aplikacije kako bi se ostvarila konekcija. Nakon završetka svih testova, instanca baze se automatski uništava.

## Pokretanje instance Redis baze putem Docker alata

Konfiguracija Redisa kroz redis.conf datoteku je opciona!

```
# komanda za dobavljanje slike Redis baze
docker pull redis

# komanda za pokretanje Redis baze uz dodatnu konfiguraciju
docker run -p 6379:6379 -v putanja_do_redis_konfiguracije:/usr/local/etc/redis --name redis-cache redis redis-server /usr/local/etc/redis/redis.conf
```

## Pokretanje Spring aplikacije (Eclipse)

- importovati projekat u workspace: Import -> Maven -> Existing Maven Project
- instalirati sve dependency-je iz pom.xml
- desni klik na projekat -> Run as -> Java Application / Spring Boot app (ako je instaliran STS plugin sa Eclipse marketplace)
