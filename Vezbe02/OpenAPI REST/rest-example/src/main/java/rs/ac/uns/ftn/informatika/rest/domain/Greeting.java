package rs.ac.uns.ftn.informatika.rest.domain;

import rs.ac.uns.ftn.informatika.rest.dto.GreetingDTO;

public class Greeting {
	
    private Long id;
    private String text;
    private String author;

    public Greeting() {

    }

    public Greeting(GreetingDTO greeting) {
        this.text = greeting.getText();
        this.author = greeting.getAuthor();
        this.id = greeting.getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    public void copyValues(GreetingDTO greeting) {
    		this.text = greeting.getText();
            this.author = greeting.getAuthor();
    }



}
