package nasz.sklepik.controllers;

import DAO.DTO.Product;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * Created by Szymon on 07.04.2018.
 */
public class AgdController implements Initializable {


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


    private MenuButtonsController ctrl;


    private ObservableList<Product> addedProducts = FXCollections.observableArrayList();

    Product product = new Product();

    Product details = new Product();

    DetailsController detailsController;

    //private User logged;


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

    private ObservableList<Product> filtrProducts(ObservableList<Product> allProducts) {
        ObservableList<Product> checked = FXCollections.observableArrayList();

        Iterator<Product> iterator = allProducts.iterator();

        while (iterator.hasNext()) {
            Product actual = iterator.next();
            if (actual.getType().equals("AGD"))
                checked.add(actual);
        }


        // image.set("/sciezka/1.jpg")

        return checked;
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {


        Request r = new Request(REQUEST_ID.TEST_CONNECTION, "Handshake TESTClientTest");
        Request r2 = new Request(REQUEST_ID.SELECT, "PRODUCT");
        try {
            Socket socket = new Socket(Protocol.serverIP, Protocol.port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(r);
            out.writeObject(r2);
            Response response = (Response) in.readObject();

            //Czytam wszystkie produkty
            ObservableList<Product> allProducts = (ObservableList) FXCollections.observableArrayList(response.getList());

            //Wyświetlam
            ObservableList<Product> filteredProduct = filtrProducts(allProducts);
            table.itemsProperty().setValue(filteredProduct);
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

            socket.close();
        } catch (Exception e) {
            System.out.println("Brak polaczenia z serverem!");
            // e.printStackTrace();
        }


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


