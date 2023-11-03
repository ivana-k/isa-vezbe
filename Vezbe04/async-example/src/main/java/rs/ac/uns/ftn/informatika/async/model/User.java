package rs.ac.uns.ftn.informatika.async.model;

public class User {
	
	private String firstname;
	
	private String lastname;
	
	private String emailAddress;
	
	public User() {
		
	}

	public User(String firstname, String lastname, String emailAddress) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.emailAddress = emailAddress;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@Override
	public String toString() {
		return "User [firstname=" + firstname + ", lastname=" + lastname + ", emailAddress=" + emailAddress + "]";
	}

}
