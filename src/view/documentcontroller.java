package view;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Person;

public class documentcontroller {
	private ObservableList<String> documentname = FXCollections.observableArrayList();
	
	@FXML
	private TableView<String> friendsTable;
	@FXML 
	private TableColumn<String, String> nameColumn;
	@FXML
	private void initialize() {
		// Initialize the person table with the two columns.
        nameColumn.setCellValueFactory(
        		cellData -> cellData.getValue());
        

        // Listen for selection changes and show the person details when changed.
		ReadOnlyObjectProperty<String> a=friendsTable.getSelectionModel().selectedItemProperty();
				
    }
	
	public documentcontroller(){
	}
	@FXML
	
	
	
}
