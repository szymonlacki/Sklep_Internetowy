package nasz.sklepik.controllers;

import DAO.DTO.Product;
import DAO.DTO.Purchase;
import DAO.DTO.User;
import communication.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Szymon on 15.04.2018.
 */
public class CartController implements Initializable {

    private ObservableList<Product> productsInCart;

    private User logged;

    Product toDelete = new Product();



    private MenuButtonsController menuButtonsController;

    public MenuButtonsController getMenuButtonsController() {
        return menuButtonsController;
    }

    public void setMenuButtonsController(MenuButtonsController menuButtonsController) {
        this.menuButtonsController = menuButtonsController;
    }

    //Lista do transakcji
    List<Purchase> transactionList = new ArrayList<>();

    @FXML
    private TableView<Product> BinTable;
    @FXML
    private TableColumn<Product, String> BinName;
    @FXML
    private TableColumn<Product, String> BinPrice;
    @FXML
    private Button buy;
    @FXML
    private TableView<Product> Bintable;
    @FXML
    private javafx.scene.text.Text money;
    @FXML
    private Label account;
    @FXML
    private javafx.scene.text.Text toPay;
    @FXML
    private Label textsum;

    private double sum = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    // ladowanie listy wczesniej wczytamymi produktami
    public void refresh() {
        //Zliczam sume do zapłaty za produkty w koszyku i sprawdzam czy stać klienta(gdy nie stać, stan konta świeci sie na czerwono np+ komunikat
        //Zliczam liczbe produktów w koszyku ,tylko dla informacji w konsoli
        System.out.print("LICZBA PRODUKTÓW W KOSZYKU: " + productsInCart.size());
        sum = 0;
        if (logged != null) {

            //sumLabel.setVisible(false);
            Iterator<Product> iterator1 = productsInCart.iterator();
            Integer z = 0;
            while (iterator1.hasNext()) {
                Product actual = iterator1.next();
                sum = sum + actual.getPrice();
                z++;
            }
            System.out.println("Do zaplaty " + sum);
            if (sum > logged.getAccount()) {
                money.setFill(Color.RED);
            }
            if (sum <= logged.getAccount()) {
                money.setFill(Color.GREEN);
            }
            DecimalFormat df = new DecimalFormat("#.00");
            money.setText(df.format(logged.getAccount()));
            toPay.setText(String.valueOf(sum));
        } else {
            buy.setDisable(true);
            account.setVisible(false);
            //textsum.setVisible(false); Nie chce działać
            Alert alert2 = new Alert(AlertType.INFORMATION, "Nie jesteś zalogowany!");
            alert2.showAndWait();
        }

        BinTable.itemsProperty().setValue(productsInCart);

        BinName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));

        BinPrice.setCellValueFactory(new PropertyValueFactory<Product, String>("price"));

    }

    @FXML
    public void buyClicked() {


        //aktualna data
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        Request r = new Request(REQUEST_ID.TEST_CONNECTION, "Handshake TESTClientTest");
        Request r2 = new Request(REQUEST_ID.INSERT, "PURCHASE");
        r2.setUser(logged);
        Iterator<Product> iterator = productsInCart.iterator();
        List<Purchase> toBuyList = new ArrayList<>();
        if (productsInCart.isEmpty()) {
            Alert alert2 = new Alert(AlertType.INFORMATION, "Nie ma nic do kupienia!");
            alert2.showAndWait();
        } else {
            if (sum > logged.getAccount()) {
                Alert alert2 = new Alert(AlertType.INFORMATION, "Za niski stan konta!");
                alert2.showAndWait();
            } else {
                for (Product p : productsInCart
                        ) {
                    Purchase bought = new Purchase((long) 7, logged.getId(), p.getId(), 1, dateFormat.format(cal.getTime()).toString(), true, 0);
                    toBuyList.add(bought);
                }
                //wrzucamy liste do requesta
                r2.setPurchasesList(toBuyList);
                r2.getUser().setAccount(logged.getAccount() - sum);//zmiana stanu konta po wykonaniu


                try {
                    Socket socket = new Socket(Protocol.serverIP, Protocol.port);
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    out.writeObject(r);
                    out.writeObject(r2);
                    Response response = (Response) in.readObject(); // otrzymujemy  info success lub failed
                    if (response.getId() == RESPONSE_ID.INSERT_SUCCESS) {
                        System.out.println("Kupiono pomyślnie");
                        Alert alert2 = new Alert(AlertType.INFORMATION, "Kupiono pomyślnie!");
                        productsInCart.clear();
                        menuButtonsController.setRefreshProducts(productsInCart);
                        refresh();
                        alert2.showAndWait();
                    } else if (response.getId() == RESPONSE_ID.INSERT_FAILED) {
                        System.out.println("Nie udalo sie kupić: " + response.getMessage());
                        Alert alert2 = new Alert(AlertType.INFORMATION, "Nie udalo sie kupić: " + response.getMessage());
                        alert2.showAndWait();
                    }
                    socket.close();
                } catch (Exception e) {
                    System.out.println("Brak polaczenia z serverem!");
                }
            }
        }

    }

    @FXML
    public void deleteProduct() {

        toDelete = BinTable.getSelectionModel().getSelectedItem();
        if (toDelete != null) {
            //Usuwam element z TableView
            BinTable.getItems().remove(toDelete);


            for (Product p:productsInCart
                 ) {
                if(p.getId().equals(toDelete.getId())) {productsInCart.remove(p); break;}
            }
            menuButtonsController.setRefreshProducts(productsInCart);
            Alert alert2 = new Alert(AlertType.INFORMATION, "Pomyślnie usunięto " + toDelete.getName());
            alert2.showAndWait();
            refresh();
        } else {
            Alert alert2 = new Alert(AlertType.INFORMATION, "Nie wybrano pola!");
            alert2.showAndWait();
            refresh();
        }
    }

    public void setProducts(ObservableList<Product> products) {
        this.productsInCart = products;
    }

    public void setLogged(User logged) {
        this.logged = logged;
    }


}