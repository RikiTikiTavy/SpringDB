package my.vaadin.app;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import com.vaadin.ui.UI;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import my.vaadin.app.dao.objects.Company;
import my.vaadin.app.impls.CompanyDAOImpl;

public class CompanyLayout extends VerticalLayout {

	private SingleConnectionDataSource companyConnection = new SingleConnectionDataSource();
	private CompanyDAOImpl companyDAOImpl;
	private Grid<Company> grid = new Grid<>(Company.class);
	private HorizontalLayout toolbar;;
	private HorizontalLayout main;
	private Button addCompanyBtn = new Button("Добавить компанию");
	private Window addWindow = new Window("Добавление пользователя");
	private Button delete = new Button("Удалить");
	private TextField nameField = new TextField("Name");
	private Button save = new Button("Сохранить");

	
	public CompanyLayout() {

		
		companyConnection = new SingleConnectionDataSource();
		companyConnection.setDriverClassName("io.crate.client.jdbc.CrateDriver");
		companyConnection.setUrl("jdbc:crate://localhost:4300");
		
		NamedParameterJdbcTemplate companyNpjt = new NamedParameterJdbcTemplate(companyConnection);
		companyDAOImpl = new CompanyDAOImpl(companyNpjt);
		
		toolbar = new HorizontalLayout();
		//toolbar.addComponents(addCustomerBtn, updateCustomerBtn, delete, search);
		toolbar.addComponents(addCompanyBtn, delete);
		toolbar.setSizeFull();
		toolbar.setExpandRatio(delete, 1.0f);
		delete.addClickListener(e -> this.delete());
	
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(KeyCode.ENTER);
		save.addClickListener(e -> this.save());

		grid.setColumns("companyName");
	
		VerticalLayout addLayout = new VerticalLayout();
		HorizontalLayout saveCancel = new HorizontalLayout();
		saveCancel.addComponents(save);
		addLayout.addComponents(nameField, saveCancel);
		main = new HorizontalLayout(grid);
		grid.setSizeFull();
		addWindow.setContent(addLayout);
		
		addComponents(toolbar, main);
		
		updateList();
		delete.setEnabled(false);

		grid.asSingleSelect().addValueChangeListener(event -> {
			delete.setEnabled(true);
		});
		
		
		addCompanyBtn.addClickListener(e -> {
			grid.asSingleSelect().clear();
			updateList();
			UI.getCurrent().addWindow(addWindow);
			addWindow.setModal(true);
		});
	}
	

	public void updateList() {
		List<Company> companies = companyDAOImpl.getCompanyList();
		grid.setItems(companies);
	}
	
	private void delete() {
		Set<Company> set = grid.getSelectedItems();
		for (Company c : set) {
			companyDAOImpl.delete(c.getId());
		}
		updateList();
	}
	
private void save() {
		
		companyDAOImpl.insert(new Company(new Date().getTime(), 
				nameField.getValue()));
		updateList();
		addWindow.close();
	}

}
