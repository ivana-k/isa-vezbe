package rs.ac.uns.ftn.informatika.rest.domain;

public class GreetingTextDTO {
    private String text;

    public GreetingTextDTO(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
