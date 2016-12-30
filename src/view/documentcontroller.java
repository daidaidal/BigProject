package view;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Document;
import model.Person;

public class documentcontroller {
	private ObservableList<Document> documentname = FXCollections.observableArrayList();
	
	@FXML
	private TableView<Document> documentTable;
	@FXML 
	private TableColumn<Document, String> nameColumn;
	@FXML
	private void initialize() {
		// Initialize the person table with the two columns.
        nameColumn.setCellValueFactory(
        		cellData -> cellData.getValue().nameProperty());
        for(int i=0;i<10;i++)
        {
        	Document d=new Document();
        	d.setName(String.valueOf(i));
        	documentname.add(d);
        }
        documentTable.setItems(documentname);
    }
	
	public documentcontroller(){
	}
	@FXML
	private void handledownload()
	{
		String name1=documentTable.getSelectionModel().selectedItemProperty().get().getName();
		System.out.println(name1);
	}
	
	
	
}
