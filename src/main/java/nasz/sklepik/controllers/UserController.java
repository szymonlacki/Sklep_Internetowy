package nasz.sklepik.controllers;

import DAO.DTO.Purchase;
import DAO.DTO.User;
import communication.Protocol;
import communication.REQUEST_ID;
import communication.Request;
import communication.Response;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Szymon on 20.04.2018.
 */
public class UserController {


    @FXML
    private TableView userTable;
    @FXML
    private Button editButton;
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
    @FXML
    private Label passwordTop;
    @FXML
    private Label loginTop;
    @FXML
    private Label addressTop;
    @FXML
    private Label cityTop;
    @FXML
    private Label surnameTop;
    @FXML
    private Label nameTop;
    @FXML
    private Button saveEdditedDataButton;
    @FXML
    private Label transactions;
    @FXML
    private TableColumn<Purchase, String> userId;
    @FXML
    private TableColumn<Purchase, String> productId;
    @FXML
    private TableColumn<Purchase, String> date;
    @FXML
    private TableColumn<Purchase, String> paid;
    @FXML
    private TableColumn<Purchase, String> rate;


    private User logged;


    public void initialize() {


        name.setVisible(false);
        surname.setVisible(false);
        city.setVisible(false);
        address.setVisible(false);
        login.setVisible(false);
        password.setVisible(false);
        loginTop.setVisible(false);
        addressTop.setVisible(false);
        cityTop.setVisible(false);
        surnameTop.setVisible(false);
        nameTop.setVisible(false);
        passwordTop.setVisible(false);
        saveEdditedDataButton.setVisible(false);
        transactions.setText("Moje transakcje");
    }


    public void edit() {


        if (editButton.getText().equals("Edycja danych")) {


            if (logged == null) {

                Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Nie jesteś zalogowany!");
                alert2.showAndWait();
            } else if (logged != null) {
                editButton.setText("Moje transakcje");
                name.setVisible(true);
                surname.setVisible(true);
                city.setVisible(true);
                address.setVisible(true);
                login.setVisible(true);
                password.setVisible(true);
                loginTop.setVisible(true);
                addressTop.setVisible(true);
                cityTop.setVisible(true);
                surnameTop.setVisible(true);
                nameTop.setVisible(true);
                passwordTop.setVisible(true);
                userTable.setVisible(false);
                saveEdditedDataButton.setVisible(true);
                transactions.setText("Edycja danych");
            }

        } else if (editButton.getText().equals("Moje transakcje")) {

            name.setVisible(false);
            surname.setVisible(false);
            city.setVisible(false);
            address.setVisible(false);
            login.setVisible(false);
            password.setVisible(false);
            loginTop.setVisible(false);
            addressTop.setVisible(false);
            cityTop.setVisible(false);
            surnameTop.setVisible(false);
            nameTop.setVisible(false);
            passwordTop.setVisible(false);
            userTable.setVisible(true);
            transactions.setText("Moje transakcje");
            editButton.setText("Edycja danych");
            saveEdditedDataButton.setVisible(false);

        }


    }

    public void save() {


        Request r = new Request(REQUEST_ID.TEST_CONNECTION, "Handshake TESTClientTest");
        Request r2 = new Request(REQUEST_ID.UPDATE, "USER");
        logged.setId(logged.getId());
        logged.setLogin(login.getText());
        logged.setName(name.getText());
        logged.setSurname(surname.getText());
        logged.setCity(city.getText());
        logged.setAddress(address.getText());
        logged.setPassword(password.getText());
        logged.setAccount(logged.getAccount());
        System.out.println(logged);
        r2.setUser(logged);

        /*Nie działa
        try {
            Socket socket = new Socket(Protocol.serverIP, Protocol.port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(r);
            out.writeObject(r2);


            Response response = (Response) in.readObject(); // otrzymujemy  info success lub failed


            if (response.getId() == RESPONSE_ID.UPDATE_SUCCESS)
                System.out.println("Edycja przebiegła pomyślnie");
            else if (response.getId() == RESPONSE_ID.UPDATE_FAILED) {
                System.out.println("Nie udało się dokonać edycji: " + response.getMessage());
            }

            socket.close();
        } catch (Exception e) {
            System.out.println("Brak polaczenia z serverem!");
        }
*/

    }


    //wyświetlanie nie działą, bo nie działa mi to kupowanie w CartController
    public void initialize(URL url, ResourceBundle rb) {


        Request r = new Request(REQUEST_ID.TEST_CONNECTION, "Handshake TESTClientTest");
        Request r2 = new Request(REQUEST_ID.INSERT, "PURCHASE");
        try {
            Socket socket = new Socket(Protocol.serverIP, Protocol.port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(r);
            out.writeObject(r2);
            Response response = (Response) in.readObject();

            //Czytam wszystkie produkty
            ObservableList<Purchase> allProducts = (ObservableList) FXCollections.observableArrayList(response.getList());
            userTable.itemsProperty().setValue(allProducts);

            //Wyświetlam
            userId.setCellValueFactory(
                    new PropertyValueFactory<Purchase, String>("userId")
            );
            productId.setCellValueFactory(
                    new PropertyValueFactory<Purchase, String>("productId")
            );
            date.setCellValueFactory(
                    new PropertyValueFactory<Purchase, String>("date")
            );
            paid.setCellValueFactory(
                    new PropertyValueFactory<Purchase, String>("paid")
            );
            rate.setCellValueFactory(
                    new PropertyValueFactory<Purchase, String>("rate")
            );

            socket.close();
        } catch (Exception e) {
            System.out.println("Brak polaczenia z serverem!");
            // e.printStackTrace();
        }


    }

    public void setLogged(User logged) {
        this.logged = logged;
    }

}
