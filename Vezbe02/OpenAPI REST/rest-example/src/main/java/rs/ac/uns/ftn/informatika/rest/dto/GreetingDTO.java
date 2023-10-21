package rs.ac.uns.ftn.informatika.rest.dto;

public class GreetingDTO {
    private Long id;
    private String text;
    private String author;

    public GreetingDTO() {

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

}
