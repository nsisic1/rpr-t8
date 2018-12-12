package ba.unsa.etf.rpr.tut8;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
    public ListView<String> lista;

    private SimpleStringProperty query;
    private ListProperty<String> listProperty;


    /*private ObservableList<String> items = FXCollections.observableArrayList(); // ili <File> ?; instanciranje se vrsi
        // pozivom staticne metode koja vraca objekat
    private ObjectProperty<String> file = new SimpleObjectProperty<>(); // ili <File> ?*/

    public Controller() {
        query = new SimpleStringProperty("Ab");
        // listProperty = new SimpleListProperty<>();
        listProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    @FXML
    public void initialize() {
        queryTextField.textProperty().bindBidirectional(query);
        lista.itemsProperty().bind(listProperty);
    }


    private String getQuery() {
        return query.get();
    }

    private SimpleStringProperty queryProperty() {
        return query;
    }

    private void setQuery(String query) {
        this.query.set(query);
    }

    public void traziClick(ActionEvent actionEvent) {
        System.out.println(getQuery());
        findMatchingFiles(getQuery());
    }

    private ArrayList<String> findMatchingFiles(String substr) {
        System.out.println("Potraga zapoceta " + substr);
        ArrayList<String> results = new ArrayList<>();
        File home_dr = new File(HOME_DIRECTORY);
        listProperty.clear();
        /*System.out.println(lista.getItems());
        lista.getItems().clear();*/  // baca null, prije listPropertija nije to radilo
        traverseFiles(substr, results, home_dr);

        return results;
    }

    private void traverseFiles(String substr, ArrayList<String> results, File directory) { // Pre-order traversal
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }
        for (File f : files) {
            /*if (f.getName().contains(substr)) {
                //
            }*/
            if (f.isFile()) {
                if (f.getName().contains(substr)) {
                    System.out.println(f.getName());
                    listProperty.add(f.getName());
                    // lista.getItems().add(f.getName()); nakon sto smo napisali "listProperty = new SimpleListProperty<>(FXCollections.observableArrayList());" radi i ovo
                    // results.add(f.getName());
                }
            } else { // if f is a directory
                traverseFiles(substr, results, f);
            }
        }
    }

}
