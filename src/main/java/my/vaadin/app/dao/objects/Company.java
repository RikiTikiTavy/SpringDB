package my.vaadin.app.dao.objects;

public class Company {

	public Company() {
		super();
	}

	public Company(Long id, String companyName) {
		
		this.id = id;
		this.companyName = companyName;
	}

	private Long id;

	private String companyName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompanyId() {
		return id + "";
	}

	

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
}
