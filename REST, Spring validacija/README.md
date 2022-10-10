# Vežbe 2 - REST OpenAPI i Spring validacija

## rest-example
Primer Spring web aplikacije. Pogledati u **pom.xml** zavisnosti koje moraju da se uključe. Ukoliko je potrebna dodatna konfiguracija projekta (van samih klasa koje predstavljaju konfiguraciju) piše se u **application.properties** fajlu koji se nalazi u src/main/resorces paketu.
Kontroleri su anotirani sa *@RestController*. Anotacija je izvedena od anotacije _@Controller_ i pridodata joj je anotacija _@ResponseBody_ koja kao podrazumevano ponašanje kao rezultat metode koja obrađuje zahtev vraća samo telo odgovora (response body, bez zaglavlja). Za vraćanje i drugih podataka (zaglavlje, status kod,...) osim glavnog objekta koji predstavlja telo odgovora, može se koristiti klasa _ResponseEntity_.

###### Materijali koje je neophodno proučiti da bi se primer mogao uspešno ispratiti:

* [Arhitekture klasičnih i savremenih web aplikacija](https://www.youtube.com/watch?v=XnEnUtSw8Rc)
* [REST.pdf](https://github.com/stojkovm/isara2021vezbe/blob/main/Vezbe03/REST.pdf) iz foldera _Vezbe03_

###### Struktura primera

Paketi su organizovani tako da se različite Spring komponente nalaze u svojim odgovarajućim paketima:

* kontroleri u __controller__ paketu: uloga kontrolera u aplikaciji jeste da samo prihvataju zahteve korisnika i pozivaju odgovarajuće metode servisa
* servisi u __service__ paketu: sva logika aplikacije se piše u servisnim metodama. Servisi pozivaju odgovarajuče metode repozitorijuma
* repozitorijum u __repository__ paketu: repozitorijumi su zaduženi za komunikaciju za bazom podataka. Kako još nismo učili komunikaciju sa bazom podataka, napravljene su privremene klase koje predstavljaju repozitorijume i čuvaju podatke u kolekcijama u memoriji
* model u __domain__ ili __model__ paketu: entiteti koje postoje u sistemu
* paket __dto__: _**D**ata **T**ransfer **O**bjects_ su objekti koji predstavljaju skraćene verzije objekata iz modela i služe da se razmenjuju između klijentske i serverske strane. DTO objekti sadrže samo one atribute koji su u tom trenutku neophodni da se razmene između klijenta i servera.

Sva mapiranja na konkretne metode u kontrolerima koje će obraditi zahteve rade se kroz anotacije:

* ___@RequestMapping___: sa specificiranjem atributa _method_ ili
* izvedenim anotacijama poput ___@GetMapping___, ___@PostMapping___, ___@PutMapping___, ___@DeleteMapping___...

Gore navedene anotacije dodatno mogu da sadrže i sledeće atribute:

* __value__:  predstavlja URL koji određuje putanju do metode
* __method__: označava tip HTTP metode (samo ukoliko se koristi _@RequestMapping_ anotacija)
* __consumes__: označava tip poruke koja se prosleđuje metodi (u kom formatu se podaci zapisuju u telu HTTP zahteva, default je JSON)
* __produces__: označava tip odgovora (u kom formatu se podaci zapisuju u telu HTTP odgovora, default je JSON).

###### Dodatni materijali:

* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Spring Restful Web Services Example](https://www.journaldev.com/2552/spring-rest-example-tutorial-spring-restful-web-services)

###### Pokretanje primera (Eclipse)

* importovati projekat u workspace: Import -> Maven -> Existing Maven Project
* instalirati sve dependency-je iz pom.xml
* desni klik na projekat -> Run as -> Java Application / Spring Boot app (ako je instaliran STS plugin sa Eclipse marketplace)


## OpenAPI dokumentacija

Primer je proširen potrebnim zavisnostima, konfiguracijom i anotacijama kako bi se podržao OpenAPI dizajn i omogućilo automatsko generisanje dokumentacije. OpenAPI specifikacija (OAS) definiše standardni interfejs za RESTful API-je, nezavisno od programskog jezika. Dokumentacija omogućava korisnicima API-ja (kako programerima tako i drugim servisima ili frontend aplikacijama) da razumeju način upotrebe izloženih ___endpoint-a___, bez uvida u izvorni kod.

Da bi SpringBoot aplikacija automatski generisala dokumentaciju, potrebno je:

 1. dodati zavisnost u pom.xml
	```
	<dependency>  
		 <groupId>org.springdoc</groupId>  
		 <artifactId>springdoc-openapi-ui</artifactId>  
		 <version>1.6.4</version>  
	</dependency>
	```

 2. Postaviti dodatne konfiguracione parametre i informacije o projektu (opciono, pogledati klasu _OpenApiConfig_)
 3. Anotirati kontroler i metode kontrolera

Rezultat je dostupan u grafičkom obliku na http://localhost:8080/swagger-ui/index.html#/ , a izgenerisani openapi.json dokument je dostupan na http://localhost:8080/v3/api-docs/. Podrazumevanu adresu je moguće promeniti u application.properties datoteci postavljajući  <span style="font-family:Courier;">springdoc.api-docs.path</span> parametar na željenu vrednost. Grafički prikaz takođe pruža simulaciju REST API klijenta pomoću kog je moguće testirati putanje (poput Postman-a).

###### Dodatni materijali:

* [OpenAPI](https://spec.openapis.org/oas/v3.1.0)

## validation-demo

Primer Spring aplikacije sa Custom validacijom.

Prilikom obrade zahteva, parametar metode u kontroleru koji predstavlja objekat koji se prosleđuje serveru (a koji je anotiran ograničenjima u modelu) anotiran je sa ___@Valid___.

Greške prilikom validiranja se mogu obraditi u komponenti __ValidationErrorsHandler__ koja je anotirana sa __@RestControllerAdvice__. Metoda ove klase se poziva automatski prilikom neuspešne validacije, a tip HTTP odgovora i status se definišu kroz __ResponseEntity__ i __@ResponseStatus__. Greške se nalaze u BindingResult objektu, koji se vezuje za odgovarajući izuzetak.

Pored predefinisanih anotacija za postavljanje ograničenja mogu se praviti nove anotacije. Primer jedne takve anotacije nalazi se u __validator__ paketu.

###### Dodatni materijali:

* [Bean validation Specification](https://beanvalidation.org/1.0/spec/)
* [Validation, Data Binding, and Type Conversion Documentation](https://docs.spring.io/spring/docs/4.1.x/spring-framework-reference/html/validation.html)

###### Pokretanje primera (Eclipse)

* importovati projekat u workspace: Import -> Maven -> Existing Maven Project
* instalirati sve dependency-je iz pom.xml
* desni klik na projekat -> Run as -> Java Application / Spring Boot app (ako je instaliran STS plugin sa Eclipse marketplace)
