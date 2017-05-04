package my.vaadin.app.impls;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import my.vaadin.app.dao.interfaces.CompanyDAO;
import my.vaadin.app.dao.objects.Company;




public class CompanyDAOImpl implements CompanyDAO {
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public CompanyDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;

	}

	@Override
	public List<Company> getCompanyList() {
		String sql = "SELECT id, companyName FROM Companies";

		return namedParameterJdbcTemplate.query(sql, new CompanyRowMapper());
	}

	private static final class CompanyRowMapper implements RowMapper<Company> {

		@Override
		public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
			Company company = new Company();
			company.setCompanyName(rs.getString("companyname"));
			company.setId(rs.getLong("id"));
			return company;
		}

	}

	@Override
	public void delete(Long id) {

		String sql = "DELETE FROM Companies WHERE id=:id";
		Map namedParameters = new HashMap();
		namedParameters.put("id", id);
		namedParameterJdbcTemplate.update(sql, namedParameters);
	}

	@Override
	public void insert(Company company) {
		String sql = "INSERT INTO Companies (id, companyname) VALUES (:id, :companyname)";
		Map namedParameters = new HashMap();
		namedParameters.put("id", company.getId());
		namedParameters.put("companyname", company.getCompanyName());
		namedParameterJdbcTemplate.update(sql, namedParameters);

	}
}
