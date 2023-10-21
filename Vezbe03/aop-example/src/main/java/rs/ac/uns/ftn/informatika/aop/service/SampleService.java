package rs.ac.uns.ftn.informatika.aop.service;

import org.springframework.stereotype.Component;

@Component
public class SampleService {
	
	/*
	 * Primer metode koja vraca neku vrednost
	 */
	public boolean someMethodReturning() {
		System.out.println("Metoda someMethodReturning se izvrsava!");
		return false;
	}
	
	public void someMethodAround() {

		System.out.println("Metoda someMethodAround se izvrsava!");
	}	
	
	
	/*
	 * Primer metode koja ima neki parametar
	 */
	public void someMethodBefore(String test) {

		System.out.println("Metoda someMethodBefore se izvrsava!");
	}
}