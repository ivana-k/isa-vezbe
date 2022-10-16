package rs.ac.uns.ftn.informatika.springapp.domain;

public class Asset {

	private String name;
	private String description;

	public Asset() {

	}

	public Asset(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
