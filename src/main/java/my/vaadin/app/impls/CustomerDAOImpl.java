package my.vaadin.app.impls;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import my.vaadin.app.dao.interfaces.CustomerDAO;
import my.vaadin.app.dao.objects.Customer;

public class CustomerDAOImpl implements CustomerDAO {
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	
	
	public CustomerDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
	
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
		
	}
	
	@Override
	public void insert(Customer customer) {
		String sql = "INSERT INTO Customers (id, name, position, email) VALUES (:id, :name, :position, :email)";
		Map namedParameters = new HashMap();
		namedParameters.put("id", customer.getId());
		namedParameters.put("name", customer.getName());
		namedParameters.put("position", customer.getPosition());
		namedParameters.put("email", customer.getEmail());
		namedParameterJdbcTemplate.update(sql, namedParameters);
		
	}

	@Override
	public void delete(Long id) {
		
		String sql = "DELETE FROM Customers WHERE id=:id";
		Map namedParameters = new HashMap();
		namedParameters.put("id", id);
		namedParameterJdbcTemplate.update(sql, namedParameters);
	}
	


	@Override
	public List<Customer> getCustomerList() {
		
		String sql = "SELECT name, position, email, id FROM Customers";

		return namedParameterJdbcTemplate.query(sql, new CustomerRowMapper());
	}
	
	
	private static final class CustomerRowMapper implements RowMapper<Customer> {

		@Override
		public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
			Customer customer = new Customer();
			 customer.setName(rs.getString("name"));
		 customer.setPosition(rs.getString("position"));
			 customer.setEmail(rs.getString("email"));
			 customer.setId(rs.getLong("id"));
			return customer;
		}

	}


	public void refreshTable(String customers ){
		String REFRESH = "REFRESH TABLE" + customers;
		namedParameterJdbcTemplate.update(REFRESH, new MapSqlParameterSource());
	}
	
	@Override
	public void changeCustomer(Long id, String name, String position, String email) {
		String sql = "UPDATE Customers SET name = :name, position = :position, email = :email  WHERE id = :id";
		Map namedParameters = new HashMap();
		namedParameters.put("id", id);
		namedParameters.put("name", name);
		namedParameters.put("position", position);
		namedParameters.put("email", email);
		namedParameterJdbcTemplate.update(sql, namedParameters);
		
	}


}
