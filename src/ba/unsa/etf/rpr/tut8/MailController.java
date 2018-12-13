package ba.unsa.etf.rpr.tut8;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MailController {
    public TextField imeIPrezimeField;
    public TextField adresaField;
    public TextField gradField;
    public TextField postanskiBrojField;

    private SimpleStringProperty imeIPrezime;
    private SimpleStringProperty adresa;
    private SimpleStringProperty grad;
    private SimpleStringProperty postanskiBroj;

    public MailController() {
        imeIPrezime = new SimpleStringProperty();
        adresa = new SimpleStringProperty();
        grad = new SimpleStringProperty();
        postanskiBroj = new SimpleStringProperty();
    }

    @FXML
    public void initialize() {
        imeIPrezimeField.textProperty().bindBidirectional(imeIPrezime);
        adresaField.textProperty().bindBidirectional(adresa);
        gradField.textProperty().bindBidirectional(grad);
        postanskiBrojField.textProperty().bindBidirectional(postanskiBroj);
        postanskiBrojValidation();
    }

    public SimpleStringProperty imeIPrezimeProperty() {
        return imeIPrezime;
    }

    public String getImeIPrezime() {
        return imeIPrezime.get();
    }

    private void setImeIPrezime(String s) {
        imeIPrezime.setValue(s);
    }

    public SimpleStringProperty adresaProperty() {
        return adresa;
    }

    public String getAdresa() {
        return adresa.get();
    }

    private void setAdresa(String s) {
        adresa.setValue(s);
    }

    public SimpleStringProperty gradProperty() {
        return grad;
    }

    public String getGrad() {
        return grad.get();
    }

    private void setGrad(String s) {
        grad.setValue(s);
    }

    public SimpleStringProperty postanskiBrojProperty() {
        return postanskiBroj;
    }

    public String getPostanskiBroj() {
        return postanskiBroj.get();
    }

    private void setPostanskiBroj(String s) {
        postanskiBroj.setValue(s);
    }

    public void postanskiBrojValidation() {
        postanskiBrojField.getStyleClass().add("poljeNijeIspravno");
        System.out.println(22222222);
        postanskiBrojField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (!newValue) {
                    Runnable run = () -> {
                        try {
                            updateField(getPostanskiBroj());
                        } catch (Exception e) {

                        }
                    };
                    Thread thread = new Thread(run);
                    thread.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void updateField(String newValue) throws Exception {
        if (validanPostanskiBroj(getPostanskiBroj())) {
            System.out.println("True");
            postanskiBrojField.getStyleClass().removeAll("poljeNijeIspravno");
            postanskiBrojField.getStyleClass().add("poljeIspravno");
        } else {
            System.out.println("False");
            postanskiBrojField.getStyleClass().removeAll("poljeIspravno");
            postanskiBrojField.getStyleClass().add("poljeNijeIspravno");
        }

    }


    public boolean validanPostanskiBroj(String s) throws Exception {
        System.out.println("Aaaaaa");
        URL url = new URL("http://c9.etf.unsa.ba/proba/postanskiBroj.php?postanskiBroj=" + getPostanskiBroj());
        BufferedReader ulaz = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
        String json = "", line = null;
        while ((line = ulaz.readLine()) != null) {
            json = json + line;
        }
        return json.equals("OK");
    }
}