package rs.ac.uns.ftn.informatika.async.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import rs.ac.uns.ftn.informatika.async.model.User;
import rs.ac.uns.ftn.informatika.async.service.EmailService;

@Controller
@RequestMapping("/users")
public class UserController {

	private Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private EmailService emailService;
	
	@GetMapping
	public String getNew(Model model) {
		model.addAttribute("user", new User());
		return "registration";
	}

	@PostMapping("/signup/async")
	public String signUpAsync(User user){

		//slanje emaila
		try {
			System.out.println("Thread id: " + Thread.currentThread().getId());
			emailService.sendNotificaitionAsync(user);
		}catch( Exception e ){
			logger.info("Greska prilikom slanja emaila: " + e.getMessage());
		}

		return "success";
	}
	
	@PostMapping("/signup/sync")
	public String signUpSync(User user){
		System.out.println("Thread id: " + Thread.currentThread().getId());
		//slanje emaila
		try {
			emailService.sendNotificaitionSync(user);
		}catch( Exception e ){
			logger.info("Greska prilikom slanja emaila: " + e.getMessage());
		}

		return "success";
	}

}
