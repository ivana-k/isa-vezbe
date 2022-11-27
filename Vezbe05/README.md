# Vežbe 5 - Spring Security

Dat je primer Spring aplikacije koja koristi _Spring Security_ radni okvir za autentifikaciju i autorizaciju. Klijentska aplikacija je Angular aplikacija.

Za razumevanje primera, potrebno je pročitati ovaj dokument, kao i [Spring Security.pdf](https://github.com/stojkovm/isara2021vezbe/blob/main/Vezbe06/Spring%20Security.pdf) iz foldera _Vezbe 5_.

## spring-security-example

Spring Security je radni okvir koji pruža podršku za autentifikaciju i autorizaciju korisnika, kao i dodatne mehanizme zaštite od određenih napada na web aplikacije. Spring Security se u suštini zasniva na upotrebi Servlet Filtera, koji presreću zahteve koji stižu od korisnika i odlučuju da li zahtev treba da se odbije ili prosledi dalje u aplikaciju, do kontrolera. Može da se integriše i sa običnom Spring aplikacijom, kao i sa Spring Boot i Spring MVC radnim okvirom.

## Osnovni koncepti

Pre nego što krenete sa integracijom Spring Security-ja sa aplikacijom, neophodno je razumeti tri osnovna koncepta:

1. Autentifikacija
2. Autorizacija
3. Filteri

### Autentifikacija

Autentifikacija je proces koji se sastoji od utvrđivanja da li je korisnik poznat sistemu (identifikacija) i potvrđivanja da je korisnik stvarno onaj koji tvrdi da jeste. Postoje različiti mehanizmi za autentifikaciju, ali se najčešće koristi korisničko ime i lozinka. Korisnik unosi svoje korisničko ime i lozinku koji se u paru šalju na serversku stranu. Server zatim provera da li je korisničko ime poznato sistemu, i ako jeste, proverava se da li se lozinka poklapa sa onim što je zapisano u bazi.

### Autorizacija

U jednostavnijim aplikacijama autentifikacija može biti dovoljna: čim korisnik potvrdi svoj identitet, može pristupiti svakom delu aplikacije.

Ali u većini aplikacija postoje koncepti permisija (dozvola) ili uloga. Razmotrimo primer web shop aplikacije. Postoje kupci koji imaju pristup javnom delu web shop-a, gde mogu da pretražuju i kupuju proizvode. Sa druge strane, postoje i administratori koji imaju pristup zasebnom administrativnom delu aplikacije, gde mogu da unose nove proizvode i ažuriraju cene.

Oba tipa korisnika se moraju autentifikovati, ali sama činjenica da je korisnik poznat sistemu ne govori ništa o tome šta im je dozvoljeno da rade u sistemu. Stoga se moraju proveriti i uloge ili permisije autentifikovanog korisnika, tj. treba proći kroz proces autorizacije.

### Filteri

U prethodnom primeru objašnjen je koncept _DispatcherServlet_-a koji redirektuje svaki _HTTP Request_ (na primer, koji šalje _Browser_) do odgovarajućih kontrolera. To znači da svaku Spring aplukaciju možemo posmatrati kao samo jedan Servlet. Stvar je u tome što _DispatcherServlet_ nema implementirane bezbednosne mehanizme, a verovatno nećete želeti da konfigurišete samo osnovnu autentifikaciju (HTTP Basic Auth) u kontrolerima. Optimalno, autentifikaciju i autorizaciju treba odraditi pre nego što zahtev pogodi neki endpoint.

Srećom, u Java web svetu postoji način da se upravo ovo uradi: mogu se definisati filteri ispred servleta, što znači da možemo razmisliti o pisanju _SecurityFiltera_ i konfigurisati ga u _Tomcat_-u i na taj način filtrirati sve dolazne _HTTP Request_-ove pre nego što pogode aplikaciju.

![Security Filter](https://i.imgur.com/ub0WYGt.jpg "Security Filter")

U suštini, tok komunikacije je sledeći:

1. Prva stvar koja treba da se uradi jeste da se izvuku korisničko ime i lozinka iz zahteva. Kredencijali mogu biti poslati putem osnovnog _Auth HTTP_ zaglavlja, tela HTTP zahteva, kolačića i sl.
2. Zatim se potvrđuje da je kombinacija korisničkog imena i lozinke ispravna (na primer, čita se iz baze podataka). Ukoliko to ne može da se potvrdi, zahtev se odbija, a klijentu koji je inicirao komunikaciju se vraća _HTTP Response_ sa postavljenim status kodom **401 _Unauthorized_**, u suprotnom se izvršava korak 3.
3. Nakon uspešne provere identiteta, vrši se proveri da li korisnik ima prava za pristup traženom _endpoint_-u, proverom uloge ili dozvola. Ukoliko korisnik nema prava pristupa, zahtev se odbija, a klijentu koji je inicirao komunikaciju se vraća _HTTP Response_ sa postavljenim status kodom **403 _Forbidden_**.

Ako zahtev uspešno zadovolji sve ove provere, tada se zahtev može propustiti dalje do traženog kontrolera!

### Filter Chain

U aplikaciji, kod jednog filtera brzo postaje ogroman i težak za održavanje, jer se zahtev može na različite načine procesirati. U tom slučaju, najbolji pristup bi bio da se jedan filter razbije na više manjih, koji će se kasnije uvezati da rade zajedno - eng. _Filter Chain_. Upotrebom ovog principa, zahtev prolazi kroz čitav lanac filtera od prvog do poslednjeg u tačno definisanom redosledu. Svaki filter vrši proveru za koju je zadužen. Ukoliko zahtev ne zadovoljava neki uslov filtera, zahtev se odbija i vraća se korisniku sa statusom o grešci, dok se u suprotnom prosleđuje sledećem filteru, sve dok se ne prosledi do _Dispatcher Servlet_-a.

Na primer, HTTP zahtev koji se šalju serveru aplikacije:

1. Prvo, prolazi kroz _LoginMethodFilter_
2. Zatim, prolazi kroz _AuthenticationFilter_
3. Zatim, prolazi kroz _AuthorizationFilter_
4. Konačno stiže do _DispatcherServlet_-a.

Ovaj koncept se naziva  _Filter Chain_. Poslednja metoda u **svakom** filteru mora da delegira zahtev sledećem filteru u nizu:

```
chain.doFilter(request, response);
```

Pomoću ovako definisanog lanca filtera može sa implementirati svaki mehanizam autentifikacije ili autorizacije koji odgovara potrebama aplikacije, bez potrebe izmene prethodno implementiranih metoda!

### Default Security Filter Chain

Kada se _Spring Security_ ispravno integriše sa ostatkom aplikacije, on dodaje svojih 15 predefinisanih filtera u odgovarajućem redosledu. Kada _HTTP Request_ stigne do _Spring_ aplikacije, on će proći kroz svih ovih 15 filtera pre nego što konačno pogodi kontrolere. Redosled filtera je takođe važan, kao što je ranije napomenuto.

Kada se aplikacija pokrene, na konzoli se prikazuje sledeća log poruka, u kojoj su izlistani svi filteri iz lanca:

```
2020-04-12 17:41:58.082  INFO 26179 --- [           main] o.s.s.web.DefaultSecurityFilterChain     : Creating filter chain: any request, [org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter@682bd3c4, org.springframework.security.web.context.SecurityContextPersistenceFilter@6ceb7b5e, org.springframework.security.web.header.HeaderWriterFilter@1bc425e7, org.springframework.security.web.csrf.CsrfFilter@48c4245d, org.springframework.security.web.authentication.logout.LogoutFilter@3a4ba480, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter@415156bf, org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter@dffa30b, org.springframework.security.web.authentication.ui.DefaultLogoutPageGeneratingFilter@f2e4acf, org.springframework.security.web.authentication.www.BasicAuthenticationFilter@353efdbf, org.springframework.security.web.savedrequest.RequestCacheAwareFilter@f14e5bf, org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter@392a04e7, org.springframework.security.web.authentication.AnonymousAuthenticationFilter@24097e9b, org.springframework.security.web.session.SessionManagementFilter@322803db, org.springframework.security.web.access.ExceptionTranslationFilter@614aeccc, org.springframework.security.web.access.intercept.FilterSecurityInterceptor@5b22b970]
```

![Default Security Filter Chain](https://i.imgur.com/lDm1NKG.png "Default Security Filter Chain")

Objasnićemo samo par ključnih filtera. Detaljnije o filterima možete pročitati na [linku](https://docs.spring.io/spring-security/site/docs/4.2.1.RELEASE/reference/htmlsingle/#web-app-security) ili pogledati [Spring Security source code](https://github.com/spring-projects/spring-security) za detalje implementacije.

* __BasicAuthenticationFilter__:  iz zahteva čita _Basic Auth HTTP Header_ ako postoji. Iz tog zaglavlja se čitaju korisničko ime i lozinka i pokušava se autentifikacija korisnika. Kako postaviti _Basic_ autentifikaciju, možete pogledati na [linku](https://www.baeldung.com/spring-security-basic-authentication).

* __UsernamePasswordAuthenticationFilter__: iz _request_ parametara ili tela HTTP zahteva se čitaju korisničko ime i lozinka i pokušava se autentifikacija korisnika.

* __DefaultLoginPageGeneratingFilter__: generiše login stranicu. Mora se eksplicitno isključiti da se stranica ne bi generisala. Ukoliko ste dobili izgenerisanu login stranicu nakon uključivanja _Spring Security_-ja u projekat, to je zbog ovog filtera.

* __DefaultLogoutPageGeneratingFilter__: generiše logout stranicu. More se eksplicitno isključiti da se stranica ne bi generisala.

* __FilterSecurityInterceptor__: u ovom filteru se vrši proces autorizacije.

Ovi filteri su u suštini _Spring Security_ i oni rade većinu posla. Na nama je da konfigurišemo način na koji rade svoj posao, npr. koje URL adrese treba zaštititi, koje ignorisati, koje tabele baza podataka koristiti za autentifikaciju, itd.

U nastavku je opisano kako se konfiguriše _Spring Security_.

## Konfiguracija

### WebSecurityConfigurerAdapter

Konfiguraciju _Spring Security_-ja ćemo izvršiti kroz klasu koja je:

1. Anotirana **_@EnableWebSecurity_** anotacijom i
2. Nasleđuje **_WebSecurityConfigurerAdapter_** koja u osnovi nudi DSL i metode za konfiguraciju. Pomoću ovih metoda možemo odrediti koje URL-ove u aplikaciji želimo zaštititi, kao i definisati ostale mehanizme za autentifikaciju.

Primer jednog _WebSecurityConfigurerAdapter_-a:

```
@Configuration
@EnableWebSecurity // (1)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter { // (1)

  @Override
  protected void configure(HttpSecurity http) throws Exception {  // (2)
      http
        .authorizeRequests()
          .antMatchers("/", "/home").permitAll() // (3)
          .anyRequest().authenticated() // (4)
          .and()
       .formLogin() // (5)
         .loginPage("/login") // (5)
         .permitAll()
         .and()
      .logout() // (6)
        .permitAll()
        .and()
      .httpBasic(); // (7)
  }
}
```

1. dodavanje potrebnih anotacija i nasleđivanje klase
2. _override_-ovanjem ove metode, dobijamo DSL kojim konfigurišemo naš filter
3. svi zahtevi koji stižu na _/_ i _/home_ URL su dozvoljeni svima, što znači da korisnik ne mora biti autentifikovan da bi im pristupio. Koriste se [antMatcher](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/util/AntPathMatcher.html) izrazi, koji takođe omogućavaju i upotrebu _wildcard_ karaktera (*, \*\*, ?)
4. za pristup svakom drugom URL-u u aplikaciji, korisnik prethodno mora biti uspešno autentifikovan
5. omogućavanje autentifikacije korisnika preko forme. Može se koristiti stranica koja je napravljena, a ne _default_-na koju generiše _Spring Security_, što se postavlja kroz _loginPage("/login")_. Svako mora da pristupi _login_ stranici i zato se navodi _permitAll()_
6. ista priča kao i za _login_
7. dozvoljava se i _HTTP Basic_ autentifikacija (postavljanje _HTTP Basic Auth Header_-a za autentifikaciju.)

Da sumiramo, u metodi _configure(HttpSecurity http)_ definišemo koji _endpoint_-i su zaštićeni, a koji su javni:

* sa **_authenticated()_** označavamo URL-ove koji su zaštićeni (samo im autentifikovani korisnici mogu pristupiti),
* sa **_permitAll()_** označavamo URL-ove kojima svi mogu pristupati (što će sigurno biti metode za logovanje i registraciju).

U našem primeru, pogledati implementaciju klase **_WebSecurityConfig_** koja se nalazi u _rs.ac.uns.ftn.informatika.spring.security.config_ paketu.

## Implementacija autentifikacije

U bazi podataka se čuvaju informacije o korisnicima. Postoji tabela u kojoj se čuvaju korisničko ime, lozinka (ali ne u _plain_ tekstu, već [heširana](https://auth0.com/blog/hashing-passwords-one-way-road-to-security/)) i proizvoljne dodatne informacije o korisnicima sistema. Moramo da konfigurišemo _Spring Security_-ju tako da zna u kojoj tabeli će da traži informacije o korisnicima prilikom procesa autentifikacije. Da bismo ovo postigli, moramo da definišemo dva _bean_-a:

1. UserDetailsService i
2. PasswordEncoder.

### UserDetailsService

Interfejs koji ima samo jednu metodu _loadUserByUsername_ koja vraća _UserDetails_ objekat. Metoda kao parametar prima samo jednu vrednost - korisničko ime. U ovoj metodi se proverava da li korisnik postoji u sistemu i učitava se iz baze podataka. U primeru, pogledati implementaciju klase **_CustomUserDetailsService_** iz paketa _rs.ac.uns.ftn.informatika.spring.security.service.impl_.

Ovaj _bean_ se uvodi u _SpringSecurity_ konfiguraciju u klasi _WebSecurityConfig_, pomoću sledećeg koda:

```
@Autowired
private CustomUserDetailsService jwtUserDetailsService;
```

i kasnije se koristi na sledeći način

```
@Autowired
public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
 auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
}
```

```
.addFilterBefore(new TokenAuthenticationFilter(tokenUtils, jwtUserDetailsService), BasicAuthenticationFilter.class);
```

**_TokenAuthenticationFilter_** je filter kojji je definisan u paketu _rs.ac.uns.ftn.informatika.spring.security.security.auth_. Kod koji je iznad napisan dodaje ovaj filter u lanac pre _BasicAuthenticationFilter_-a. Filter će, kao i svaki drugi iz _Default Security Filter Chain_-a presresti svaki HTTP zahtev koji stiže od klijenta ka serverskoj strani. Ima jednu metodu _doFilterInternal_ koja zapravo predstavlja kod koji se izvršava kada dođe na red u lancu. U primeru je definisano da se koristi autentifikacija bazirana na tokenima, i koristi se [JWT](https://jwt.io/) token (objašnjeno u prezentaciji). Kreirana je **_TokenUtils_** klasa u paketu _rs.ac.uns.ftn.informatika.spring.security.security_ u kojoj su implementirane metode za rad sa JWT tokenom. Ukoliko je token prosleđen, pripada korisniku koji postoji u sistemu i validan je, podaci o korisniku koji je vlasnik tokena se smeštaju u **_SecurityContextHolder_**, kako bi ti podaci dalje bili dostupni ostalim filterima (npr. za autorizaciju), ali i bilo gde u korisničkom kodu (npr. u metodama servisa ili kontrolera). Prilikom implementacije filtera, ne sme se zaboraviti da poslednja naredba _doFilterInternal_ metode bude prosleđivanje HTTP zahteva sledećem filteru u lancu:

```
chain.doFilter(request, response);
```

### PasswordEncoder

Pomoću ovog _bean_-a se definiše algoritam koji će se koristiti za heširanje lozinki. Prilikom autentifikacije, _Spring Security_ automatski hešira vrednosti koje dolaze sa strane klijenta i poredi ih sa onim što piše u bazi podataka (što je pročitano pomoću _UserDetailsService bean_-a), zato treba voditi računa da se prilikom upisivanja korisnika u bazu podataka lozinke heširaju!

Ovaj _bean_ se uvodi u _SpringSecurity_ konfiguraciju u klasi _WebSecurityConfig_, pomoću sledećeg koda:

```
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

## Autorizacija

Prethodno smo naveli primer web shop aplikacije koja razlikuje dva tipa korisnika: kupce i administratore. Neka je npr. URL aplikacije _www.webshop.com_ a deo aplikacije za administraciju _www.webshop.com/admin_. Običan kupac ne sme da pristupi dalu aplikacije za administraciju. U suštini, želimo drugačija prava pristupa za različite korisnike na osnovu njihovih uloga i dozvola. Mehanizmi autentifikacije koji su do sada objašnjeni nam neće biti dovoljni da bismo ovo implementirati bez izmena već postojećih metoda.

### GrantedAuthority

_Spring Security_ poseduje sledeće pojmove (objedinjene pod pojmom __GrantedAuthority__):

* _authority_ - predstavlja prava koja korisnik ima (fine grained permissions) i
* _role_ - predstavlja ulogu koju korisnik ima, sa prefiksom *ROLE_* koji _Spring Security_ sam dodaje (coarse grained permissions).

Predstavljene su interfejsom _GrantedAuthority_.

U primeru, pogledati klasu **_Role_** iz paketa _rs.ac.uns.ftn.informatika.spring.security.model_, koja je ujedno anotirana i _@Entity_ anotacijom. Klasa _User_ iz modela ima listu _roles_, što znači da se one čuvaju u bazi. _Role_ koje postoje u sistemu su u bazu dodati kroz skriptu za inicijalizaciju baze podataka _data-postgres.sql_.

```
INSERT INTO ROLE (name) VALUES ('ROLE_USER');
INSERT INTO ROLE (name) VALUES ('ROLE_ADMIN');
```

_GrantedAuthority_ se koristi na nivou URL-ova da se dodatno zaštite. Koriste se za one URL-ove koji su vezani za tip korisnika, odnosno tamo gde autentifikacija nije dovoljna.

_GrantedAuthority_-je definisane iznad možemo koristiti na sledeće načine:

1. metoda **hasAuthority()** upotrebom _Spring Security_ DSL-a:

```
http
    .authorizeRequests()
        .antMatchers("/admin").hasAuthority("ROLE_ADMIN") (1)
        .antMatchers("/shop").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER") (2)
```

(1) za pristup ruti _admin_ korisnik mora da bude autentifikovan i da ima _authority_ "ROLE_ADMIN".
(2) za pristup ruti_shop_korisnik mora da bude autentifikovan i da ima_authority_ili "ROLE_ADMIN" ili "ROLE_USER".

2. metoda **hasRole()** _Spring Security_ DSL-a:

```
http
    .authorizeRequests()
        .antMatchers("/admin").hasRole("ADMIN") (1)
        .antMatchers("/shop").hasAnyRole("ADMIN", "USER") (2)
```

(1) za pristup ruti _admin_ korisnik mora da bude autentifikovan i da ima _authority_ "ROLE_ADMIN". Obratiti pažnju da se poziva_hasRole_metoda koja dodaje prefiks *ROLE_*i zato se uloge navode bez ovog prefiksa!
(2) za pristup ruti _shop_ korisnik mora da bude autentifikovan i da ima _authority_ ili "ROLE_ADMIN" ili "ROLE_USER". Obratiti pažnju da se poziva _hasAnyRole_ metoda koja dodaje prefiks*ROLE_* i zato se uloge navode bez ovog prefiksa!

3. upotrebom **_@PreAuthorize_** anotacije

Da ne bismo morali u _WebSecurityConfig_ klasi da navodimo sve rute koje želimo dodatno da osiguramo, može se koristiti anotacija **_@PreAuthorize_** kojom se direktno anotiraju metode kontrolera. Sve napisano pod tačkama 1. i 2. ove sekcije važi za ovu anotaciju, samo se odgovarajuće metode pišu kao parametar anotacije.

Na primer, anotaciju možemo napisati na sledeći način:

```
@PreAuthorize("hasRole('USER')")
```

U primeru, pogledati klasu **_UserController_** iz paketa _rs.ac.uns.ftn.informatika.spring.security.controller_.

Da bi ova anotacija radila, mora se navesti _@EnableGlobalMethodSecurity(prePostEnabled = true)_ u _WebSecurityConfig_ klasi!

## Pokretanje Spring aplikacije (Eclipse)

* importovati projekat u workspace: Import -> Maven -> Existing Maven Project
* instalirati sve dependency-je iz pom.xml
* desni klik na projekat -> Run as -> Java Application / Spring Boot app (ako je instaliran STS plugin sa Eclipse marketplace)

## Pokretanje Angular aplikacije

* [Prerequisites](https://angular.io/guide/setup-local#prerequisites)
* preko komandne linije pozicionirati se u folder _client_ i kucati naredbu: _npm install_, zatim _ng serve_

**Napomena:** za razumevanje _Spring Security_ primera nije neophodno da pokrećete klijentsku aplikaciju. Svi potrebni koncepti se nalaze na serverskoj strani. Sve zahteve na Spring aplikaciju možete da šaljete preko [_Postman_-a](https://www.postman.com/) na sledeći način:

1. Poslati zahtev za logovanje. Kredencijali korisnika su sledeći - korisničko ime: user, lozinka: 123.

![Login](https://i.imgur.com/m2xlfEi.png "Login")

Kada je autentifikacija uspešna, login metoda vraća JWT token u _accessToken_ atributu. Kopirati dobijeni token (kopirati ceo token, bez navodnika).

![Access Token Response](https://i.imgur.com/wjEMbd1.png "Access Token Response")

2. Kada šaljete zahteve na zaštećene URL-ove, obavezno poslati i token koji ste prethodno dobili. Token smeštate u zaglavlje zahteva pod ključem _Authorization_, jer serverska strana očekuje token u tom zaglavlju. Kao vrednost polja navodi se prefiks _Bearer_ razmak pa JWT token kao na slici.

![Postavljanje tokena u zahtev](https://i.imgur.com/6mJ7Axd.png "Postavljanje tokena u zahtev")

Ako je sve u redu, odgovor koji ste dobili od servera je sledeći:

![Server Response](https://i.imgur.com/LUPkEaf.png "Server Response")
