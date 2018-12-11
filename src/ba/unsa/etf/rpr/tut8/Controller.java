package ba.unsa.etf.rpr.tut8;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.File;
import java.util.ArrayList;

public class Controller {

    private final String HOME_DIRECTORY = "C:\\Users";

    // da nije public morali bi staviti @FXML?
    public Button traziBtn;
    public TextField queryTextField;
    public ListView lista;

    private SimpleStringProperty query;

    private ObservableList<String> items = FXCollections.observableArrayList(); // ili <File> ?; instanciranje se vrsi
        // pozivom staticne metode koja vraca objekat
    private ObjectProperty<String> file = new SimpleObjectProperty<>(); // ili <File> ?


    private String getQuery() {
        return query.get();
    }

    private SimpleStringProperty queryProperty() {
        return query;
    }

    private void setQuery(String query) {
        this.query.set(query);
    }




}
