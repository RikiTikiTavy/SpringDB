package my.vaadin.app.dao.interfaces;

import java.util.List;

import my.vaadin.app.dao.objects.Customer;

public interface CustomerDAO {
	void insert(Customer customer);

	void delete(Long id);

	List<Customer> getCustomerList();

	void changeCustomer(Long id, String name, String position, String email);
}
