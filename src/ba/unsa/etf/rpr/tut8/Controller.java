package ba.unsa.etf.rpr.tut8;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class Controller {

    private final String HOME_DIRECTORY = "C:\\Users";

    public Button traziBtn;
    public TextField queryTextField;
    public ListView<String> lista;
    public Button prekiniBtn;
    public ProgressIndicator progIndicator;

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
        progIndicator.setVisible(false);
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
            progIndicator.setVisible(true);
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
        progIndicator.setVisible(false);
    }

    private void findMatchingFiles(String substr){
        System.out.println("Potraga zapoceta " + substr);
        File home_dr = new File(HOME_DIRECTORY);
        Platform.runLater(() -> listProperty.clear());
        searchNumber++;
        try {
            // Vrsenje pretrage
            traverseFiles(substr, home_dr, searchNumber);
            // Kada je zavrsila pretraga
            searchNumber = 0;
            traziBtn.setDisable(false);
            prekiniBtn.setDisable(true);
            progIndicator.setVisible(false);
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

    public void listViewClick(MouseEvent mouseEvent) {
        Parent root = null;
        try {
            Stage myStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mailProzor.fxml"));
            loader.load();
            MailController mailController;
            mailController = loader.getController();
            myStage.setTitle("Slanje datoteke");
            myStage.setScene(new Scene(loader.getRoot(), USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            myStage.setResizable(false);
            myStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
