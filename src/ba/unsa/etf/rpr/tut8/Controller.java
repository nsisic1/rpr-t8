package ba.unsa.etf.rpr.tut8;

import javafx.application.Platform;
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

    private int searchNumber;

    public Controller() {
        query = new SimpleStringProperty("Ab");
        // listProperty = new SimpleListProperty<>();
        listProperty = new SimpleListProperty<>(FXCollections.observableArrayList()); // https://stackoverflow.com/questions/24430191/is-there-a-changeable-listproperty-in-javafx
        searchNumber = 0;
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
        Runnable run = () -> {
            findMatchingFiles(getQuery());
        };
        Thread thread = new Thread(run);
        thread.start();
    }

    private ArrayList<String> findMatchingFiles(String substr){
        System.out.println("Potraga zapoceta " + substr);
        ArrayList<String> results = new ArrayList<>();
        File home_dr = new File(HOME_DIRECTORY);
        Platform.runLater(() -> listProperty.clear());
        /*System.out.println(lista.getItems());
        lista.getItems().clear();*/  // baca null, prije listPropertija nije to radilo
        searchNumber++;
        try {
            traverseFiles(substr, home_dr, searchNumber);
            searchNumber = 0;
            System.out.println("Pretraga zavrsena");
        } catch (Exception e) {

        }

        System.out.println("Pretraga prekinuta");
        return results;
    }

    private void traverseFiles(String substr, File directory, int currentSearchNumber) throws Exception { // Pre-order traversal
        // System.out.println(1);
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }
        for (File f : files) {
            if (currentSearchNumber != searchNumber) {
                throw new Exception();
            }
            if (f.isFile()) {
                if (f.getName().contains(substr)) {
                    System.out.println(f.getName());
                    Platform.runLater(() -> listProperty.add(f.getAbsolutePath())); // https://stackoverflow.com/a/21119757 i u predavanjima
                }
            } else { // if f is a directory
                traverseFiles(substr, f, currentSearchNumber);
            }
        }
    }

}
