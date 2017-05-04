package my.vaadin.app;

import java.util.Date;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import my.vaadin.app.dao.objects.Customer;
import my.vaadin.app.impls.CustomerDAOImpl;

public class AddCustomerWindow extends Window {
	
	private TextField firstNameField = new TextField("Name");
	private TextField positionField = new TextField("Position");
	private TextField emailField = new TextField("Email");
	private VerticalLayout addLayout = new VerticalLayout();
	private HorizontalLayout saveCancel = new HorizontalLayout();
	private Button save = new Button("Сохранить");
	private Button cancel = new Button("Отменить");
	private CustomerDAOImpl customerDAOImpl;

	
	public AddCustomerWindow(){
		super("Добавление пользователя");
		
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(KeyCode.ENTER);
		save.addClickListener(e -> this.save());
		
		saveCancel.addComponents(save, cancel);
		addLayout.addComponents(firstNameField, positionField, emailField, saveCancel);
		
		setContent(addLayout);
	}
	
private void save() {
		
		customerDAOImpl.insert(new Customer(new Date().getTime(), 
				firstNameField.getValue(), positionField.getValue(), emailField.getValue()));
		close();
		
	}
}
