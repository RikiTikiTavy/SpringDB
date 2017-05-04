package my.vaadin.app.dao.objects;



@SuppressWarnings("serial")
public class Customer {

	public Customer() {
	
	}

	public Customer(Long id, String name, String position, String email) {
		this.id = id;
		this.name = name;
		this.position = position;
		this.email = email;
	}

	private Long id;

	public String getCustomerId() {
		return id + "";
	}

	private String name = "";

	private String position = "";

	private String email = "";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}