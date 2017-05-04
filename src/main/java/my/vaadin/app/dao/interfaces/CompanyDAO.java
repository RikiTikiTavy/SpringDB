package my.vaadin.app.dao.interfaces;

import java.util.List;

import my.vaadin.app.dao.objects.Company;



public interface CompanyDAO {

	
	List<Company>  getCompanyList();
	void delete(Long id);
	
	void insert(Company company);
}
