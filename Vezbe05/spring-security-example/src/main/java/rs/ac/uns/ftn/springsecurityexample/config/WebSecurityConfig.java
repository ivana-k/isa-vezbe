package rs.ac.uns.ftn.springsecurityexample.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import rs.ac.uns.ftn.springsecurityexample.security.auth.RestAuthenticationEntryPoint;
import rs.ac.uns.ftn.springsecurityexample.security.auth.TokenAuthenticationFilter;
import rs.ac.uns.ftn.springsecurityexample.service.impl.CustomUserDetailsService;
import rs.ac.uns.ftn.springsecurityexample.util.TokenUtils;

@Configuration
// Injektovanje bean-a za bezbednost 
@EnableWebSecurity

// Ukljucivanje podrske za anotacije "@Pre*" i "@Post*" koje ce aktivirati autorizacione provere za svaki pristup metodi
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

	// Servis koji se koristi za citanje podataka o korisnicima aplikacije
	@Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }
	
	// Implementacija PasswordEncoder-a koriscenjem BCrypt hashing funkcije.
	// BCrypt po defalt-u radi 10 rundi hesiranja prosledjene vrednosti.
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
  	
  
 	@Bean
 	public DaoAuthenticationProvider authenticationProvider() {
 	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
 	   // 1. koji servis da koristi da izvuce podatke o korisniku koji zeli da se autentifikuje
 	    // prilikom autentifikacije, AuthenticationManager ce sam pozivati loadUserByUsername() metodu ovog servisa
 	    authProvider.setUserDetailsService(userDetailsService());
 	    // 2. kroz koji enkoder da provuce lozinku koju je dobio od klijenta u zahtevu 
	    // da bi adekvatan hash koji dobije kao rezultat hash algoritma uporedio sa onim koji se nalazi u bazi (posto se u bazi ne cuva plain lozinka)
 	    authProvider.setPasswordEncoder(passwordEncoder());
 	 
 	    return authProvider;
 	}
 	
 	
 	
 	 // Handler za vracanje 401 kada klijent sa neodogovarajucim korisnickim imenom i lozinkom pokusa da pristupi resursu
 	@Autowired
 	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
 
 	
    // Registrujemo authentication manager koji ce da uradi autentifikaciju korisnika za nas
 	@Bean
 	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
 	    return authConfig.getAuthenticationManager();
 	}
 	
	// Injektujemo implementaciju iz TokenUtils klase kako bismo mogli da koristimo njene metode za rad sa JWT u TokenAuthenticationFilteru
	@Autowired
	private TokenUtils tokenUtils;
	
	// Definisemo prava pristupa za zahteve ka odredjenim URL-ovima/rutama
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	// svim korisnicima dopusti da pristupe sledecim putanjama:
    	// komunikacija izmedju klijenta i servera je stateless posto je u pitanju REST aplikacija
        // ovo znaci da server ne pamti nikakvo stanje, tokeni se ne cuvaju na serveru 
		// ovo nije slucaj kao sa sesijama koje se cuvaju na serverskoj strani - STATEFULL aplikacija
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        // sve neautentifikovane zahteve obradi uniformno i posalji 401 gresku	
        http.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint);
    	http.authorizeRequests().antMatchers("/auth/**").permitAll()		// /auth/**
			.antMatchers("/h2-console/**").permitAll()	// /h2-console/** ako se koristi H2 baza)
			.antMatchers("/api/foo").permitAll()		// /api/foo
			// ukoliko ne zelimo da koristimo @PreAuthorize anotacije nad metodama kontrolera, moze se iskoristiti hasRole() metoda da se ogranici
			// koji tip korisnika moze da pristupi odgovarajucoj ruti. Npr. ukoliko zelimo da definisemo da ruti 'admin' moze da pristupi
			// samo korisnik koji ima rolu 'ADMIN', navodimo na sledeci nacin: 
			// .antMatchers("/admin").hasRole("ADMIN") ili .antMatchers("/admin").hasAuthority("ROLE_ADMIN")
			
			// za svaki drugi zahtev korisnik mora biti autentifikovan
			.anyRequest().authenticated().and()
			// za development svrhe ukljuci konfiguraciju za CORS iz WebConfig klase
			.cors().and()

			// umetni custom filter TokenAuthenticationFilter kako bi se vrsila provera JWT tokena umesto cistih korisnickog imena i lozinke (koje radi BasicAuthenticationFilter)
			.addFilterBefore(new TokenAuthenticationFilter(tokenUtils,  userDetailsService()), BasicAuthenticationFilter.class);
		
		// zbog jednostavnosti primera ne koristimo Anti-CSRF token (https://cheatsheetseries.owasp.org/cheatsheets/Cross-Site_Request_Forgery_Prevention_Cheat_Sheet.html)
		http.csrf().disable();
                
        
        // ulancavanje autentifikacije
        http.authenticationProvider(authenticationProvider());
       
        return http.build();
    }
 
    // metoda u kojoj se definisu putanje za igorisanje autentifikacije
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
    	// Autentifikacija ce biti ignorisana ispod navedenih putanja (kako bismo ubrzali pristup resursima)
    	// Zahtevi koji se mecuju za web.ignoring().antMatchers() nemaju pristup SecurityContext-u
    	// Dozvoljena POST metoda na ruti /auth/login, za svaki drugi tip HTTP metode greska je 401 Unauthorized
    	return (web) -> web.ignoring().antMatchers(HttpMethod.POST, "/auth/login")
    			
    			
    			// Ovim smo dozvolili pristup statickim resursima aplikacije
    			.antMatchers(HttpMethod.GET, "/", "/webjars/**", "/*.html", "favicon.ico",
    			"/**/*.html", "/**/*.css", "/**/*.js");	 

    }

}
