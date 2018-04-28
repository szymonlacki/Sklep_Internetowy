package nasz.sklepik.controllers;

import DAO.DTO.User;
import communication.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Szymon on 20.04.2018.
 */
public class RegistrationController {

    @FXML
    private TextField name;
    @FXML
    private TextField surname;
    @FXML
    private TextField city;
    @FXML
    private TextField address;
    @FXML
    private TextField login;
    @FXML
    private TextField password;

    private Stage registrationPane;


    public void registry() throws IOException {

        if (login.getText().isEmpty() || password.getText().isEmpty() || name.getText().isEmpty() ||
                surname.getText().isEmpty() || city.getText().isEmpty() ||
                address.getText().isEmpty()) {
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Puste pola!");
            alert2.showAndWait();
        } else if (!login.getText().isEmpty() || !password.getText().isEmpty() || !name.getText().isEmpty() ||
                !surname.getText().isEmpty() || !city.getText().isEmpty() ||
                !address.getText().isEmpty()) {
            Request r = new Request(REQUEST_ID.TEST_CONNECTION, "Handshake TESTClientTest");
            Request r2 = new Request(REQUEST_ID.REGISTER, "USER");
            // wysylamy pełne info o userze ( id losowe duze) - dałem pieniądze na 0, musi sobie doładować konto
            r2.setUser(new User((long) 4, login.getText(), password.getText(), name.getText(), surname.getText(),
                    city.getText(), address.getText(), 0.00));
            try {
                Socket socket = new Socket(Protocol.serverIP, Protocol.port);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                out.writeObject(r);
                out.writeObject(r2);


                Response response = (Response) in.readObject(); // otrzymujemy info

                if (response.getId() == RESPONSE_ID.INSERT_SUCCESS) {
                    System.out.println("Zarejestrowano pomyslnie");
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Rejestracja przebiegła pomyślnie!");
                    alert2.showAndWait();
                    registrationPane.close();
                } else if (response.getId() == RESPONSE_ID.INSERT_FAILED) {
                    System.out.println("Nie udalo sie zarejestrowac: " + response.getMessage());
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION, "Podany użytkownik już istnieje!");
                    alert1.showAndWait();

                }

                socket.close();
            } catch (Exception e) {
                System.out.println("Brak polaczenia z serverem!");
            }
        }
    }

    public void setRegistrationPane(Stage registrationPane) {
        this.registrationPane = registrationPane;
    }


}
