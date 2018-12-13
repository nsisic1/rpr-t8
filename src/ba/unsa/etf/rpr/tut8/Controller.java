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

    public Button traziBtn;
    public TextField queryTextField;
    public ListView<String> lista;
    public Button prekiniBtn;

    private SimpleStringProperty query;
    private ListProperty<String> listProperty;

    private int searchNumber;

    public Controller() {
        query = new SimpleStringProperty("Ab");
        listProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
        searchNumber = 0;
    }

    @FXML
    public void initialize() {
        queryTextField.textProperty().bindBidirectional(query);
        lista.itemsProperty().bind(listProperty);
        prekiniBtn.setDisable(true);
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
            traziBtn.setDisable(true);
            prekiniBtn.setDisable(false);
            findMatchingFiles(getQuery());
        };
        Thread thread = new Thread(run);
        thread.start();
    }

    public void prekiniClick(ActionEvent actionEvent) {
        searchNumber++;
        traziBtn.setDisable(false);
        prekiniBtn.setDisable(true);
    }

    private void findMatchingFiles(String substr){
        System.out.println("Potraga zapoceta " + substr);
        File home_dr = new File(HOME_DIRECTORY);
        Platform.runLater(() -> listProperty.clear());
        searchNumber++;
        try {
            traverseFiles(substr, home_dr, searchNumber);
            searchNumber = 0;
            traziBtn.setDisable(false);
            prekiniBtn.setDisable(true);
            System.out.println("Pretraga zavrsena");
        } catch (Exception e) {

        }

        System.out.println("Pretraga prekinuta");
    }

    private void traverseFiles(String substr, File directory, int currentSearchNumber) throws Exception { // Pre-order traversal
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
                    Platform.runLater(() -> listProperty.add(f.getAbsolutePath()));
                }
            } else { // if f is a directory
                traverseFiles(substr, f, currentSearchNumber);
            }
        }
    }

}
