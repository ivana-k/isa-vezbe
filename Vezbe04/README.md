# Vežbe 4

## aop-example

Primer Spring aplikacije u kojoj je definisan jedan aspekt (__TimeLoggingAspect__).

Podrška za aspekte je automatski uključena u Spring Boot aplikacije putem _@SpringBootApplication_ anotacije.  U slučaju da se nije koristio Spring Boot, podrška za aspekte bi se mogla uključiti dodavanjem anotacije _@EnableAspectJAutoProxy_ na konfiguracionu klasu ili _<aop:aspectj-autoproxy/>_ u slučaju XML konfiguracije.

###### Materijali koje je neophodno proučiti da bi se primer mogao uspešno ispratiti:

* [Aspect-oriented programming](https://www.youtube.com/watch?v=3KKUP7-o3ps)
* [AOP.pdf](https://github.com/stojkovm/isara2021vezbe/blob/main/Vezbe05/AOP.pdf) iz foldera _Vezbe03_

###### Definisanje aspekta

Da bi se definisao aspekt, potrebno je Java klasu anotirati ___@Aspect___ anotacijom. Zatim svaku metodu ove klase anotirati anotacijom koja će opisati u kom trenutku će se aspekt izvršiti, a kao atribut ove anotacije potrebno je navesti __pointcut__ izraz kojim se definiše konkretno mesto u aplikaciji na kojem će aspekt biti primenjen. Anotacije kojim se anotiraju metode su sledeće:

* ___@Before___: pre poziva metode na koju se aspekt odnosi
* ___@After___: nakon metode (bez obzira na ishod metode)
* ___@AfterReturning___: nakon uspešnog završetka metode
* ___@AfterThrowing___ : nakon što metoda izazove izuzetak
* ___@Around___: omotač oko metode, tako što se deo koda izvršava pre, a deo posle metode.

__Pointcut__ izrazom se definiše __šablon__, što znači da se aspekt primenjuje na __svaku__ metodu koja se uklapa u definisani šablon!

###### Dodatni materijali:

* [Primer još jedne Spring aplikacije sa aspektima](https://www.journaldev.com/2583/spring-aop-example-tutorial-aspect-advice-pointcut-joinpoint-annotations)
* [AOP i Spring Dokumentacija](https://docs.spring.io/spring/docs/2.0.x/reference/aop.html)
* [Specifikacija AsspectJ jezika](https://www.eclipse.org/aspectj/doc/released/progguide/language.html)

###### Pokretanje primera (Eclipse)

* importovati projekat u workspace: Import -> Maven -> Existing Maven Project
* instalirati sve dependency-je iz pom.xml
* desni klik na projekat -> Run as -> Java Application / Spring Boot app (ako je instaliran STS plugin sa Eclipse marketplace)

## async-example

Asinhrono procesiranje je u Spring radnom okviru pojednostavljeno korišćenjem specijalnih anotacija.
Potrebno je uključiti podršku za asinhrono izvršavanje metoda pomoću anotacije `@EnableAsync` i anotirati metodu koja treba asinhrono da se izvršava pomoću anotacije `@Async`.
Kada se metoda anotira `@Async` anotacijom, Spring će izdvojiti izvršavanje te metode u odvojenu nit iz TaskExecutor thread pool-a, a pozivalac metode neće morati da čeka na njeno izvršavanje.

U **async-example** primeru prikazano je jednostavan kod za slanje e-maila sinhrono i asinhrono. Dodat je `Thread.sleep()` kako bi se istakao efekat dugotrajne operacije koja ima smisla da se izvršava asinhrono i kakav efekat takvo izvršavanje ima za korisnika. Za potrebe slanja e-maila koristi se objekat klase `JavaMailSender`. Konekcioni parametri za programsko slanje e-maila zadati su kroz `application.properties`.

Napomena: Za programsko slanje e-maila u primeru je korišćen Gmail nalog. Kako bi primer radio, potrebno je na nalogu koji ste postavili u `application.properties` omogućiti dvofaktorsku autentifikaciju. Pored toga, potrebno je izgenerisati lozinku za aplikaciju, koja će se postaviti kao vrednost `spring.mail.password` parametra. Na [linku](https://support.google.com/accounts/answer/185833?hl=en) se nalazi uputstvo.

Dodatni materijali za razumevanje asinhronog izvršavanja metoda u Springu:

1. [Task Execution and Scheduling](https://docs.spring.io/spring/docs/current/spring-framework-reference/integration.html#scheduling)
2. [Effective Advice on Spring Async: Part 1](https://dzone.com/articles/effective-advice-on-spring-async-part-1)
3. [Effective Advice on Spring Async (ExceptionHandler): Part 2](https://dzone.com/articles/effective-advice-on-spring-async-exceptionhandler-1)
4. [Effective Advice on Spring @Async: Final Part](https://dzone.com/articles/effective-advice-on-spring-async-final-part-1)
5. [How To Do @Async in Spring](https://www.baeldung.com/spring-async)

###### Pokretanje primera (Eclipse)

* importovati projekat u workspace: Import -> Maven -> Existing Maven Project
* instalirati sve dependency-je iz pom.xml
* desni klik na projekat -> Run as -> Java Application / Spring Boot app (ako je instaliran STS plugin sa Eclipse marketplace)


## schedule-example

Primer Spring aplikacije sa zakazanim taskovima koji se izvršavaju u fiksnim vremenskim intervalima. Kada se primer pokrene, u konzoli analizirati vremenske trenutke kada su se metode izvršile.

Da bi Spring mogao automatski da izvršava zakazane taskove, potrebno je konfiguracinu klasu anotirati anotacijom ___@EnableScheduling___ (u našem primeru, to je main klasa ScheduleExampleApplication.java).

Metode koje predstavljaju taskove koji se izvršavaju potrebno je anotirati sa ___@Scheduled___ i podesiti atribute na željene vrednosti. Atributi su sledeći:

* [__cron__](https://en.wikipedia.org/wiki/Cron): Logika se izvrsava u vremenskim trenucima opisanim cron sintaksom. Cron je izraz koji opisuje neki vremenski trenutak (npr. svaki dan 20:00:00, nedelja 23:59:59...). Ovaj atribut se koristi ukoliko je potrebno definisati task koji će se izvršavati u nekim vremenskim trenucima. Sintaksa cron izraza je prikazana na slici ispod.

![spring cron expression](https://i.imgur.com/T0G0LJM.png)

* __initialDelay__: Atribut čija vrednost označava koje vreme treba da prođe od trenutka kada se aplikacija startuje do trenutka kada se metoda prvi put može izvršiti. Vreme se navodi u milisekundama.
* __fixedRate__: Atribut čija vrednost označava koliko vremena treba da prođe između izvrašavanja (gleda se trenutak kada je metoda _započela_ izvršavanje). Vreme se navodi u milisekundama. Npr. ako je vrednost atributa 5000 ms znači da će se metoda izvršavati na svakih 5 sekundi od trenutka poziva (npr. ako je metoda počela sa izvršavanjem u 20:00:00, završila u 20:03:00, sledeći poziv iste metode će biti u 20:05:00).
* __fixedDelay__: Atribut čija vrednost označava koliko vremena treba da prođe između izvrašavanja (gleda se trenutak kada je metoda _završila_ izvršavanje). Vreme se navodi u milisekundama. Npr. ako je vrednost atributa 5000 ms znači da će se metoda izvršavati na svakih 5 sekundi od trenutka završetka (npr. ako je metoda počela sa izvršavanjem u 20:00:00, završila u 20:03:00, sledeći poziv iste metode će biti u 20:08:00).

Sve taskove koji su zakazani Spring će izvršavati u posebnom thread-u. Koristi se samo jedan thread za sve zadatke, pa treba biti oprezan jer u jednom trenutku samo jedan zakazani task može da se izvršava. Ukoliko neki zakazani task treba da se izvrši, a Spring već izvrašava neki raniji zakazani, task će otići na čekanje i čim se thread oslobi, preuzima se na izvršavanje.

Primer ispisa na konzoli:
```
2020-03-30 14:14:24.226  INFO 28365 --- [           main] r.a.u.f.i.s.ScheduleExampleApplication   : Started ScheduleExampleApplication in 2.171 seconds (JVM running for 2.561)
2020-03-30 14:14:30.000  INFO 28365 --- [   scheduling-1] r.a.u.f.i.s.c.GreetingContoller          : > cronJob
2020-03-30 14:14:35.002  INFO 28365 --- [   scheduling-1] r.a.u.f.i.s.c.GreetingContoller          : Procesiranje je trajalo 5 sekundi.
2020-03-30 14:14:35.005  INFO 28365 --- [   scheduling-1] r.a.u.f.i.s.c.GreetingContoller          : < cronJob
```

U uglastim zagradama je oznaka thread-a koji izvršava metodu. Vidimo da postoje 2 thread-a:

1. __main__: main thread koji startuje aplikaciju i
2. __scheduling-1__: thread koji izvršava zakazane taskove.

###### Dodatni materijali:

* Više o cron sintaksi na [linku](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/scheduling/support/CronSequenceGenerator.html)
* [The @Scheduled Annotation in Spring](https://www.baeldung.com/spring-scheduled-tasks)
* [Spring Scheduling Tasks Guidelines](https://spring.io/guides/gs/scheduling-tasks/)
* [Task Execution and Scheduling Spring documentation](https://docs.spring.io/spring/docs/current/spring-framework-reference/integration.html#scheduling)

###### Pokretanje primera (Eclipse)

* importovati projekat u workspace: Import -> Maven -> Existing Maven Project
* instalirati sve dependency-je iz pom.xml
* desni klik na projekat -> Run as -> Java Application / Spring Boot app (ako je instaliran STS plugin sa Eclipse marketplace)
