package my.vaadin.app;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.annotation.WebServlet;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import my.vaadin.app.dao.objects.Customer;
import my.vaadin.app.impls.CustomerDAOImpl;

@Theme("mytheme")
public class MyUI extends UI {

	private Grid<Customer> grid = new Grid<>(Customer.class);
	private TextField filterText = new TextField();
	private Button updateCustomerBtn = new Button("Редактировать");

	private Window addWindow = new Window();
	private Window deleteWindow = new Window();
	
	private Window updateWindow = new Window("Редактирование пользователя");
	private Button addCustomerBtn = new Button("Добавить пользователя");
	private Button save = new Button("Сохранить");
	private Button cancel = new Button("Отменить");
	private Button update = new Button("Редактировать");
	private TextField firstNameField = new TextField("Name");
	private TextField positionField = new TextField("Position");
	private TextField emailField = new TextField("Email");
	private TextField firstNameField2 = new TextField("Name");
	private TextField positionField2 = new TextField("Position");
	private TextField emailField2 = new TextField("Email");
	private Button delete = new Button("Удалить пользователя");

	private SingleConnectionDataSource customerConnection;
	private CustomerDAOImpl customerDAOImpl;
	private TabSheet tabsheet = new TabSheet();

	private final VerticalLayout companyLayout = new CompanyLayout();
	
	private Button yes = new Button("ДА");
	private Button no = new Button("НЕТ");
	
	private Long id;
	
	
	public MyUI() {

		customerConnection = new SingleConnectionDataSource();
		customerConnection.setDriverClassName("io.crate.client.jdbc.CrateDriver");
		customerConnection.setUrl("jdbc:crate://localhost:4300");
	
		NamedParameterJdbcTemplate customerNpjt = new NamedParameterJdbcTemplate(customerConnection);
		customerDAOImpl = new CustomerDAOImpl(customerNpjt);

		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(KeyCode.ENTER);
		cancel.setStyleName(ValoTheme.BUTTON_PRIMARY);
		cancel.setClickShortcut(KeyCode.ENTER);
		cancel.addClickListener(e -> this.cancel());
		delete.addClickListener(e -> this.delete());
		save.addClickListener(e -> this.save());
		update.addClickListener(e -> this.update());
		yes.addClickListener(e -> this.yes());
		no.addClickListener(e -> this.no());
	}

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		final VerticalLayout layout = new VerticalLayout();

		filterText.setPlaceholder("Search");
		filterText.addValueChangeListener(e -> updateList());
		filterText.setValueChangeMode(ValueChangeMode.LAZY);

		Button clearFilterTextBtn = new Button(FontAwesome.TIMES);
		clearFilterTextBtn.setDescription("Clear the current filter");
		clearFilterTextBtn.addClickListener(e -> filterText.clear());

		CssLayout search = new CssLayout();
		search.addComponents(filterText, clearFilterTextBtn);
		search.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

		HorizontalLayout toolbar = new HorizontalLayout();

		toolbar.addComponents(addCustomerBtn, updateCustomerBtn, delete, search);
		toolbar.setSizeFull();
		toolbar.setExpandRatio(delete, 1.0f);

		VerticalLayout addLayout = new VerticalLayout();
		HorizontalLayout saveCancel = new HorizontalLayout();
		saveCancel.addComponents(save, cancel);
		addLayout.addComponents(firstNameField, positionField, emailField, saveCancel);

		VerticalLayout updateLayout = new VerticalLayout();
		HorizontalLayout updateCancel = new HorizontalLayout();
		updateCancel.addComponents(update);
		updateLayout.addComponents(firstNameField2, positionField2, emailField2, updateCancel);


		grid.setColumns("id", "name", "position", "email");


		HorizontalLayout main = new HorizontalLayout(grid);

		main.setSizeFull();
		grid.setSizeFull();
		VerticalLayout labelLayout = new VerticalLayout();
		Label label = new Label("Вы действительно хотите удалить этого пользователя?");
		labelLayout.addComponent(label);
		VerticalLayout deleteLayout = new VerticalLayout();
		HorizontalLayout yesNo = new HorizontalLayout();
		deleteLayout.addComponents(labelLayout, yesNo);
		yesNo.addComponents(yes, no);
	
		
		updateWindow.setContent(updateLayout);
		addWindow.setContent(addLayout);
		
		deleteWindow.setContent(deleteLayout);
//		addWindow.addCloseListener(e->{
//			
//			customerDAOImpl.refreshTable("customers");
//			updateList();
//			System.out.println("close");
//		});

		addCustomerBtn.addClickListener(e -> {
			grid.asSingleSelect().clear();
			addWindow(addWindow);
			addWindow.setModal(true);
			
		});
		
		

		updateCustomerBtn.addClickListener(e -> {
			grid.asSingleSelect().clear();
			updateList();
			
			addWindow(updateWindow);
			updateWindow.setModal(true);
		});

		layout.addComponents(toolbar, main);

		updateList();
		
		
		
		tabsheet.addTab(layout).setCaption("Customers");
		tabsheet.addTab(companyLayout).setCaption("Companies");
		setContent(tabsheet);
		
		updateCustomerBtn.setEnabled(false);
		delete.setEnabled(false);

		
		grid.addSelectionListener(event -> {
			Set<Customer>   selected = event.getAllSelectedItems();
		    	for (Customer c : selected) {
		    		id = c.getId();
			firstNameField2.setValue(c.getName());
			positionField2.setValue(c.getPosition());
			emailField2.setValue(c.getEmail());
		}
		});
		
		
		grid.asSingleSelect().addValueChangeListener(event -> {
			updateCustomerBtn.setEnabled(true);
			delete.setEnabled(true);
		});
	}

	private void save() {
		
		customerDAOImpl.insert(new Customer(new Date().getTime(), 
				firstNameField.getValue(), positionField.getValue(), emailField.getValue()));
		updateList();
		addWindow.close();
	}

	private void update() {
		
	
	while(firstNameField2.getValue()== ""){
		Notification.show("Заполните поле",
                Notification.Type.HUMANIZED_MESSAGE);
	}
		customerDAOImpl.changeCustomer(id, firstNameField2.getValue(), positionField2.getValue(), emailField2.getValue());

		updateWindow.close();
		updateList();
	}

	public void updateList() {
		List<Customer> customers = customerDAOImpl.getCustomerList();
		System.out.println("update");
		grid.setItems(customers);
	}

	private void cancel() {
		addWindow.close();
	}

	private void delete() {
		
		addWindow(deleteWindow);
	
		
		deleteWindow.setModal(true);
		
//		Set<Customer> set = grid.getSelectedItems();
//		for (Customer c : set) {
//			customerDAOImpl.delete(c.getId());
//		}
//		updateList();
	}
	
	private void yes() {
		
		Set<Customer> set = grid.getSelectedItems();
		for (Customer c : set) {
			customerDAOImpl.delete(c.getId());
		}
		updateList();
		deleteWindow.close();
	}
	
	private void no(){
		deleteWindow.close();
	}
	

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}