package rs.ac.uns.ftn.informatika.aop.aspect;

import java.util.Date;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/*
 * Aspekt se registruje kao komponenta anotacijom @Component kako bi
 * usla u nadleznost Spring kontejnera koji ce voditi racuna o ovom beanu.
 */
@Component
@Aspect
public class TimeLoggingAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TimeLoggingAspect.class);
	
	/*
	 * Najznacajniji termini koji se koriste prilikom implementacije aspekata u Springu su:
	 * 	1. Advice - predstavlja akciju koja se sprovodi od strane aspekta nad odgovarajucim join point-om
	 * 				(opcije su Before, AfterReturning, AfterThrowing, After, Around)
	 * 	2. Join point - tacka koja predstavlja originalnu metodu koja se izvrsava
	 * 	3. Pointcut - predikat koji odgovara join point-u, specificira izraz koji se dodaje u Advice i specificira
	 * 				koja se metoda izvrsava kada se aktivira aspekt metoda (izraz se moze pisati i pod zasebnom anotacijom @Pointcut)
	 * Vise informacija na http://docs.spring.io/spring/docs/current/spring-framework-reference/html/aop.html
	 */
	@AfterReturning(pointcut = "execution(* rs.ac.uns.ftn.informatika.aop.service.SampleService.someMethodReturning(..))",
			returning = "result")
	public void sampleAdviceReturning(JoinPoint joinPoint, Object result) throws Throwable {
		LOGGER.info("@After: Posle poziva metode - " + joinPoint.getSignature() + " - " + new Date());
		System.out.println("Objekat vracen iz metode: " + result);
		
	}
	
	/*
	 * Za Around advice prvi parametar mora biti tipa ProceedingJoinPoint.
	 * Pozivom metode proceed() izvrsava se originalna metoda za koju se poziva aspekt.
	 * proceed metoda kao povratnu vrednost ima vrednosti koju vraca originalna metoda,
	 * dok se metodi proceed moze proslediti Object[] koji
	 * odgovara parametrima koji se prosledjuju originalnoj metodi.
	 */
	@Around("execution(* rs.ac.uns.ftn.informatika.aop.service.SampleService.someMethodAround(..))")
	public void sampleAdviceAround(ProceedingJoinPoint joinPoint) throws Throwable {
		LOGGER.info("@Around: Pre poziva metode - " + joinPoint.getSignature().getName() + " - " + new Date());
		joinPoint.proceed();
		LOGGER.info("@Around: Posle poziva metode - " + joinPoint.getSignature().getName() + " - " + new Date());
	}
	
	@Before("execution(* rs.ac.uns.ftn.informatika.aop.service.SampleService.someMethodBefore(..)) && args(text,..)")
	public void sampleAdviceBefore(JoinPoint joinPoint, String text) throws Throwable {
		LOGGER.info("@Before: Pre poziva metode - " + joinPoint.getTarget().getClass().getName() + " - " + new Date());
		System.out.println("Objekat koji se prosledjuje metodi: " + text);
	}
}