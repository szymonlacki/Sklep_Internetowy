package nasz.sklepik.controllers;

import DAO.DTO.Product;
import DAO.DTO.Purchase;
import DAO.DTO.User;
import communication.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import nasz.sklepik.PdfCreator;
import javafx.util.Callback;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javafx.application.Application;

import javafx.scene.Group;

import javafx.scene.Scene;

import javafx.stage.Stage;

import javafx.beans.property.BooleanProperty;

import javafx.beans.property.SimpleBooleanProperty;

import javafx.beans.property.StringProperty;

import javafx.beans.property.SimpleStringProperty;

import javafx.beans.value.ChangeListener;

import javafx.beans.value.ObservableValue;

import javafx.collections.FXCollections;

import javafx.collections.ObservableList;

import javafx.event.EventHandler;

import javafx.geometry.Pos;

import javafx.scene.control.CheckBox;

import javafx.scene.control.TableCell;

import javafx.scene.control.TableColumn;

import javafx.scene.control.TableColumn.CellEditEvent;

import javafx.scene.control.TableView;

import javafx.scene.control.TextField;

import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.input.KeyCode;

import javafx.scene.input.KeyEvent;

import javafx.util.Callback;
/**
 * Created by Szymon on 20.04.2018.
 */
public class UserController {


    @FXML
    private TableView<Purchase> userTable;
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
    private PasswordField password;
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
    private TableColumn<Purchase, CheckBox> selectCol;
    @FXML
    private TableColumn<Purchase, String> date;
    @FXML
    private TableColumn<Purchase, String> paid;
    @FXML
    private TableColumn<Purchase, String> rate;
    @FXML
    private Button rechargeAccount;
    @FXML
    private TextField rateText;
    @FXML
    private javafx.scene.text.Text accountBallance;
    @FXML
    private TextField refillText;
    @FXML
    private javafx.scene.text.Text accountBallanceText;
    @FXML
    private Button editedButton;
    @FXML
    private Button rateButton;

    @FXML
    private Button invoiceButton;
    private List<Purchase> MyTransactions;
    private User logged;


    @FXML
    public void invoiceButtonClicked() {

        ObservableList<Purchase> selectedItems = userTable.getSelectionModel().getSelectedItems();
        if (userTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Nie wybrano przedmiotu do faktury!");
            alert2.showAndWait();
            //Jeśli zaznaczony to wchodze w dalsze pętle  i zapisuje zaznaczony wiersz tabeli w toRate
        } else if (userTable.getSelectionModel().getSelectedItem() != null) {

            List<Purchase> purchaseListPdf =  new ArrayList<Purchase>();
            for (Purchase row : selectedItems) {
                purchaseListPdf.add(row);
            }
            PdfCreator pdfCreator = new PdfCreator(purchaseListPdf, logged);
            pdfCreator.createInvoice();
        }
    }


    Purchase toRate = new Purchase();


    MenuButtonsController ctrl = new MenuButtonsController();

    public void initialize() {


        // userTable.getColumns().add(select);
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
        refillText.setVisible(false);
        accountBallance.setVisible(false);
        rechargeAccount.setVisible(false);
        accountBallanceText.setVisible(false);
        editedButton.setVisible(false);
        transactions.setText("Moje transakcje");
        invoiceButton.setVisible(true);


        if (logged != null) System.out.printf("Zalogowano jako id" + logged.getId());

        Request r = new Request(REQUEST_ID.TEST_CONNECTION, "Handshake TESTClientTest");
        Request r2 = new Request(REQUEST_ID.SELECT, "PURCHASE");
        try {
            Socket socket = new Socket(Protocol.serverIP, Protocol.port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(r);
            out.writeObject(r2);
            Response response = (Response) in.readObject();
            //Czytam wszystkie transakcje
            List<Purchase> transactionList = (List<Purchase>) response.getList();
            List<Purchase> myTransactions = new ArrayList<>(); // lista transakcji usera
            MyTransactions = myTransactions;
            if (logged != null) {
                invoiceButton.setDisable(false);
                accountBallance.setText(String.valueOf(logged.getAccount()));
                for (Purchase t : transactionList
                        ) {
                    if (t.getUserId().equals(logged.getId())) {
                        //System.out.println("dopasowano id transakcji!");
                        myTransactions.add(t);
                    }

                }

                myTransactions.sort(Comparator.comparing(Purchase::getDate).reversed());

                List<Product> allProducts = downloadProducts();
                for (Purchase p:myTransactions
                        ) {
                    for (Product product: allProducts
                            ) {
                        if(p.getProductId().equals(product.getId()))
                        {p.setProductName(product.getName());
                            break;
                        }

                    }

                }

                ObservableList<Purchase> allPurchases = (ObservableList) FXCollections.observableArrayList(myTransactions);


                userTable.itemsProperty().setValue(allPurchases);
                //Wyświetlam
                userId.setCellValueFactory(
                        new PropertyValueFactory<Purchase, String>("productName")
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


                //Ustawiam, żeby zaznaczało się więcej niż 1 wiersz
                userTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            } else {
                System.out.println("Nie zalogowano!");
                invoiceButton.setDisable(true);
            }
            socket.close();
        } catch (Exception e) {
            System.out.println("Brak polaczenia z serverem!");
            // e.printStackTrace();
        }


    }

    public void edit() {


        if (editButton.getText().equals("Dane")) {
            invoiceButton.setVisible(true);
            name.setDisable(true);
            surname.setDisable(true);
            city.setDisable(true);
            address.setDisable(true);
            login.setDisable(true);
            password.setDisable(true);
            if (logged == null) {

                Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Nie jesteś zalogowany!");
                alert2.showAndWait();
            } else {
                editButton.setText("Moje transakcje");
                invoiceButton.setVisible(false);
                name.setVisible(true);
                surname.setVisible(true);
                city.setVisible(true);
                address.setVisible(true);
                login.setVisible(true);
                login.setDisable(true);
                password.setVisible(true);
                loginTop.setVisible(true);
                addressTop.setVisible(true);
                cityTop.setVisible(true);
                surnameTop.setVisible(true);
                nameTop.setVisible(true);
                passwordTop.setVisible(true);
                refillText.setVisible(true);
                accountBallance.setVisible(true);
                rechargeAccount.setVisible(true);
                accountBallanceText.setVisible(true);
                userTable.setVisible(false);
                saveEdditedDataButton.setVisible(true);
                editedButton.setVisible(true);
                transactions.setText("Dane");
                populateWithLoggedData();

            }

        } else if (editButton.getText().equals("Moje transakcje")) {
            invoiceButton.setVisible(true);
            editedButton.setVisible(false);
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
            editButton.setText("Dane");
            saveEdditedDataButton.setVisible(false);
            refillText.setVisible(false);
            accountBallance.setVisible(false);
            rechargeAccount.setVisible(false);
            accountBallanceText.setVisible(false);


        }

    }

    public void enableEdit() {

        if (editedButton.getText().equals("Edycja")) {

            editedButton.setVisible(false);
            name.setDisable(false);
            surname.setDisable(false);
            city.setDisable(false);
            address.setDisable(false);
            login.setDisable(false);
            password.setDisable(false);
            login.setDisable(true);
        }
    }

    private void populateWithLoggedData() {
        if (logged != null) {
            name.setText(logged.getName());
            surname.setText(logged.getSurname());
            city.setText(logged.getCity());
            address.setText(logged.getAddress());
            login.setText(logged.getLogin());
        }
    }

    public void save() {


        //Sprawdzam, czy pola są puste
        if (password.getText().isEmpty() || name.getText().isEmpty() ||
                surname.getText().isEmpty() || city.getText().isEmpty() ||
                address.getText().isEmpty()) {
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Puste pola!");
            alert2.showAndWait();
        } else if (!password.getText().isEmpty() || !name.getText().isEmpty() ||
                !surname.getText().isEmpty() || !city.getText().isEmpty() ||
                !address.getText().isEmpty()) {
            Request r = new Request(REQUEST_ID.TEST_CONNECTION, "Handshake TESTClientTest");
            Request r2 = new Request(REQUEST_ID.UPDATE, "USER");

            User updateUser = logged;

            //najpierw wysylasz pozniej zmieniasz jesli sie powiedzie!
            updateUser.setId(logged.getId());
            updateUser.setLogin(login.getText());
            updateUser.setName(name.getText());
            updateUser.setSurname(surname.getText());
            updateUser.setCity(city.getText());
            updateUser.setAddress(address.getText());
            updateUser.setPassword(password.getText());
            updateUser.setAccount(logged.getAccount());
            //System.out.println(updateUser);
            r2.setUser(updateUser);

            try {
                Socket socket = new Socket(Protocol.serverIP, Protocol.port);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                out.writeObject(r);
                out.writeObject(r2);
                Response response = (Response) in.readObject(); // otrzymujemy  info success lub failed

                if (response.getId() == RESPONSE_ID.UPDATE_SUCCESS) {
                    System.out.println("Edycja przebiegła pomyślnie");
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Edycja przebiegła pomyślnie!");
                    alert2.showAndWait();
                } else if (response.getId() == RESPONSE_ID.UPDATE_FAILED) {
                    System.out.println("Nie udało się dokonać edycji: " + response.getMessage());
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Błędne hasło!");
                    alert2.showAndWait();
                }

                socket.close();
            } catch (Exception e) {
                System.out.println("Brak polaczenia z serverem!");
            }

        }
    }


    public void rate() {


        //Sprawdzam, czy w ogóle został zaznaczony wiersz
        if (userTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Nie wybrano przedmiotu do oceny!");
            alert2.showAndWait();
            //Jeśli zaznaczony to wchodze w dalsze pętle  i zapisuje zaznaczony wiersz tabeli w toRate
        } else if (userTable.getSelectionModel().getSelectedItem() != null) {
            toRate = userTable.getSelectionModel().getSelectedItem();
            //Sprawdzam, czy jest juz ocenione, czyli, czy wartość jest w przedziale 1-5
            if (Integer.valueOf(toRate.getRate()) > 0 && Integer.valueOf(toRate.getRate()) < 6) {
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Już oceniono");
                alert2.showAndWait();
                //Jeśli równe 0, to znaczy, że  jeszcze nie ocenione
            } else if (Integer.valueOf(toRate.getRate()) == 0) {
                if (rateText.getText().equals("")) {
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Puste pole");
                    alert2.showAndWait();
                } else {
                    //Sprawdzenie przedziału oceny
                    if (Integer.valueOf(rateText.getText()) < 0 || Integer.valueOf(rateText.getText()) > 5) {
                        Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Zły przedział, podaj cyfrę od 1 do 5");
                        alert2.showAndWait();
                    } else if (Integer.valueOf(rateText.getText()) > 0 || Integer.valueOf(rateText.getText()) < 6) {
                        Request r = new Request(REQUEST_ID.TEST_CONNECTION, "Handshake TESTClientTest");
                        Request r2 = new Request(REQUEST_ID.UPDATE, "PURCHASE");

                        Purchase ratedItem = toRate;
                        ratedItem.setId(toRate.getId());
                        ratedItem.setUserId(toRate.getUserId());
                        ratedItem.setProductId(toRate.getProductId());
                        ratedItem.setAmount(toRate.getAmount());
                        ratedItem.setDate(toRate.getDate());
                        ratedItem.setPaid(toRate.getPaid());
                        ratedItem.setRate(Integer.valueOf(rateText.getText()));
                        List<Purchase> lista = new ArrayList<>();
                        lista.add(ratedItem); // pakujemy tylko jedna transakcje do listy

                        //r2.setPurchasesList(lista); // pakujemy liste do requesta
                        System.out.println(lista);

                        r2.setPurchasesList(lista); // pakujemy liste do requesta

                        try {
                            Socket socket = new Socket(Protocol.serverIP, Protocol.port);
                            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                            out.writeObject(r);
                            out.writeObject(r2);
                            Response response = (Response) in.readObject(); // otrzymujemy  info success lub failed
                            if (response.getId() == RESPONSE_ID.UPDATE_SUCCESS) {
                                Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Oceniono pomyślnie!");
                                alert2.showAndWait();
                                userTable.refresh();
                            } else if (response.getId() == RESPONSE_ID.UPDATE_FAILED) {
                                System.out.println("Nie udalo sie ocenic: " + response.getMessage());
                            }
                            socket.close();
                        } catch (Exception e) {
                            System.out.println("Brak polaczenia z serverem!");
                        }
                    }
                }
            }
        }

    }

    public void accountTransfer() {

        try {
            if (refillText.getText().equals("")) {
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Podano puste pole!");
                alert2.showAndWait();
            } else {
                if (Integer.valueOf(refillText.getText()) <= 0) {

                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Podano ujemną kwotę!");
                    alert2.showAndWait();
                } else {
                    double refill = Double.parseDouble(refillText.getText());
                    System.out.println(refill);// o ile chcemy zwiekszyc konto
                    double newAccountBalance = logged.getAccount() + refill;
                    logged.setAccount(newAccountBalance);
                    //wysylamy requesty
                    Request r = new Request(REQUEST_ID.TEST_CONNECTION, "Handshake TESTClientTest");
                    Request r2 = new Request(REQUEST_ID.UPDATE, "USER");
                    r2.setUser(logged); // wrzucamy usera do requesta
                    Socket socket = new Socket(Protocol.serverIP, Protocol.port);
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    out.writeObject(r);
                    out.writeObject(r2);
                    Response response = (Response) in.readObject(); // otrzymujemy  info success lub failed

                    if (response.getId() == RESPONSE_ID.UPDATE_SUCCESS) {
                        Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Doładowano pomyślnie!");
                        alert2.showAndWait();
                        this.accountBallance.setText(String.valueOf(newAccountBalance));
                    } else if (response.getId() == RESPONSE_ID.UPDATE_FAILED) {
                        System.out.println("Nie udalo sie dodac sumy do konta: " + response.getMessage());
                    }
                    socket.close();

                }
            }



        } catch (Exception e) {
            System.out.println("Brak polaczenia z serverem!");
        }


    }

    public void setLogged(User logged) {
        if (logged != null) System.out.printf("Ustawono logged jako id" + logged.getId());
        this.logged = logged;
    }

    MenuButtonsController menuButtonsController;

    public void setMenuButtonsController(MenuButtonsController menuButtonsController) {
        this.menuButtonsController = menuButtonsController;
    }


    public List<Product> downloadProducts() {
        Request r = new Request(REQUEST_ID.TEST_CONNECTION, "Handshake TESTClientTest");
        Request r2 = new Request(REQUEST_ID.SELECT, "PRODUCT");
        try {
            Socket socket = new Socket(Protocol.serverIP, Protocol.port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(r);
            out.writeObject(r2);
            Response response = (Response) in.readObject();
            System.out.println("Otrzymano:" + " \n" + response.getList().toString());
            socket.close();
            return (List<Product>) response.getList();
        } catch (Exception e) {
            System.out.println("Brak polaczenia z serverem!");
            // e.printStackTrace();
        }
        return null;
    }

}
