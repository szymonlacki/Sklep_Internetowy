package nasz.sklepik.controllers;

import DAO.DTO.Product;
import DAO.DTO.Purchase;
import communication.Protocol;
import communication.REQUEST_ID;
import communication.Request;
import communication.Response;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Szymon on 07.04.2018.
 */
public class TopController implements Initializable {


    @FXML
    private TableView<Product> table;
    @FXML
    private TableColumn<Product, String> name;
    @FXML
    private TableColumn<Product, String> model;
    @FXML
    private TableColumn<Product, String> price;
    @FXML
    private TableColumn<Product, String> amount;
    @FXML
    private TableColumn<Product, String> desc;
    @FXML
    private TableColumn<Product,String> averageRate;
    @FXML
    private TableColumn<Product,String> boughtCount;

    @FXML
    private Button mostBoughtButton;
    @FXML
    private Button topRatedButton;



    private MenuButtonsController ctrl;


    private ObservableList<Product> addedProducts = FXCollections.observableArrayList();

    Product product = new Product();
    Product details = new Product();

    DetailsController detailsController;

    //private User logged;

    List<Product> productList = downloadProducts();
    List<Purchase> purchaseList = downloadPurchases();

    public void addToBin() {


        product = table.getSelectionModel().getSelectedItem();

        if (product != null) {
            Alert alert1 = new Alert(AlertType.CONFIRMATION, "Czy na pewno chcesz dodać " +
                    product.getName() + " do koszyka?  ", ButtonType.YES, ButtonType.NO);
            alert1.showAndWait();
            if (alert1.getResult() == ButtonType.YES) {
                Alert alert2 = new Alert(AlertType.INFORMATION, "Dodano do koszyka!");
                alert2.showAndWait();
                addedProducts.add(product);
                System.out.println("Dodano " + addedProducts);
                ctrl.addProducts(addedProducts);
            } else if (alert1.getResult() == ButtonType.NO)
                System.out.println("Rezygnacja");
        } else {
            Alert alert3 = new Alert(AlertType.INFORMATION, "Nie wybrano zadnego pola", ButtonType.OK);
            alert3.showAndWait();

        }
    }

    private List<Purchase> downloadPurchases() {
        Request r = new Request(REQUEST_ID.TEST_CONNECTION, "Handshake TESTClientTest");
        Request r2 = new Request(REQUEST_ID.SELECT, "PURCHASE");
        try {
            Socket socket = new Socket(Protocol.serverIP, Protocol.port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(r);
            out.writeObject(r2);
            Response response = (Response) in.readObject();
            System.out.println("Otrzymano:" + " \n" + response.getList().toString());
            socket.close();
            return (List<Purchase>) response.getList();
        } catch (Exception e) {
            System.out.println("Brak polaczenia z serverem!");
            // e.printStackTrace();
        }
        return null;
    }

    private List<Product> downloadProducts() {
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


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //inicjuje np najlepiej kupowanymi, a niżej znajdują się 2 osobne funkcje do wyświetlania
        for (Product product : productList) {
            int i = 0, rateSum = 0;
            for (Purchase purchase : purchaseList) {
                if (product.getId().equals(purchase.getProductId())) {
                    if (purchase.getRate() != 0) {

                        rateSum += purchase.getRate();
                        i++;
                    }
                }

            }
            product.setBoughtCount(i);
            product.setAverageRate((double) rateSum / (double) i);
        }

        productList.sort(Comparator.comparing(Product::getBoughtCount).reversed());//sortuj po ilosci zakupionych
        //konwertuje na Obvervable i zapisuje w nowej o nazwie productListConverted
        ObservableList<Product> productListConverted = FXCollections.observableArrayList(productList);
        productListConverted = getFirstly(productListConverted);
       // System.out.println("BICZ----------------------------------------------- " + productListConverted);
        table.itemsProperty().setValue((ObservableList<Product>) productListConverted);
        name.setCellValueFactory(
                new PropertyValueFactory<Product, String>("name")
        );
        model.setCellValueFactory(
                new PropertyValueFactory<Product, String>("type")
        );
        price.setCellValueFactory(
                new PropertyValueFactory<Product, String>("price")
        );
        amount.setCellValueFactory(
                new PropertyValueFactory<Product, String>("amount")
        );
        desc.setCellValueFactory(
                new PropertyValueFactory<Product, String>("description")
        );
        boughtCount.setCellValueFactory(
                new PropertyValueFactory<Product, String>("boughtCount")
        );
        averageRate.setCellValueFactory(
                new PropertyValueFactory<Product, String>("averageRate")
        );

    }

    public void showMostBought() {

        productList.sort(Comparator.comparing(Product::getBoughtCount).reversed());//sortuj po ilosci zakupionych
        //konwertuje na Obvervable i zapisuje w nowej o nazwie productListConverted
        ObservableList<Product> productListConverted = FXCollections.observableArrayList(productList);
        productListConverted = getFirstly(productListConverted);
        table.itemsProperty().setValue((ObservableList<Product>) productListConverted);
        name.setCellValueFactory(
                new PropertyValueFactory<Product, String>("name")
        );
        model.setCellValueFactory(
                new PropertyValueFactory<Product, String>("type")
        );
        price.setCellValueFactory(
                new PropertyValueFactory<Product, String>("price")
        );
        amount.setCellValueFactory(
                new PropertyValueFactory<Product, String>("amount")
        );
        desc.setCellValueFactory(
                new PropertyValueFactory<Product, String>("description")
        );
        boughtCount.setCellValueFactory(
                new PropertyValueFactory<Product, String>("boughtCount")
        );
        averageRate.setCellValueFactory(
                new PropertyValueFactory<Product, String>("averageRate")
        );


    }


    public void showTopRated() {

        productList.sort(Comparator.comparing(Product::getAverageRate).reversed());//sortuj po ilosci zakupionych
        ObservableList<Product> productListConverted = FXCollections.observableArrayList(productList);
        productListConverted = getFirstly(productListConverted);
        table.itemsProperty().setValue((ObservableList<Product>) productListConverted);
        name.setCellValueFactory(
                new PropertyValueFactory<Product, String>("name")
        );
        model.setCellValueFactory(
                new PropertyValueFactory<Product, String>("type")
        );
        price.setCellValueFactory(
                new PropertyValueFactory<Product, String>("price")
        );
        amount.setCellValueFactory(
                new PropertyValueFactory<Product, String>("amount")
        );
        desc.setCellValueFactory(
                new PropertyValueFactory<Product, String>("description")
        );
        boughtCount.setCellValueFactory(
                new PropertyValueFactory<Product, String>("boughtCount")
        );
        averageRate.setCellValueFactory(
                new PropertyValueFactory<Product, String>("averageRate")
        );

    }

    //filtruje i wyświetlam pierwsze 6 produktów
    private ObservableList<Product> getFirstly(ObservableList<Product> list) {
        ObservableList<Product> firstly = FXCollections.observableArrayList();

        Iterator<Product> iterator = list.iterator();
        int quantity = 6, counter = 0;
        while (iterator.hasNext()) {
            if (counter < quantity)
                firstly.add(iterator.next());
            else
                break;
            counter++;
        }

        return firstly;
    }

    public void showDetails() throws IOException {


        details = table.getSelectionModel().getSelectedItem();

        if (details != null) {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/Details.fxml"));
            Pane root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            stage.setResizable(false);
            detailsController = loader.getController();
            detailsController.setDetails(details);
            detailsController.refresh();
        } else if (details == null) {
            Alert alert3 = new Alert(AlertType.INFORMATION, "Nie wybrano zadnego pola", ButtonType.OK);
            alert3.showAndWait();

        }

    }


    public void setCtrl(MenuButtonsController ctrl) {
        this.ctrl = ctrl;
    }


}


