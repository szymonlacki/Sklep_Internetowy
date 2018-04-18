package nasz.sklepik.controllers;

import DAO.CsvMenager;
import DAO.DTO.Product;
import DAO.DTO.User;
import communication.Protocol;
import communication.REQUEST_ID;
import communication.Request;
import communication.Response;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import nasz.sklepik.Main;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

    @FXML
    private Button kup;

    private MenuButtonsController ctrl;

    private ObservableList<Product> addedProducts = FXCollections.observableArrayList();

    Product product = new Product();

    //private User logged;


    public void addToBin() {


        product = table.getSelectionModel().getSelectedItem();

        if (product!=null) {
            Alert alert1 = new Alert(AlertType.CONFIRMATION, "Czy na pewno chcesz dodać " +
                    product.getName() + " do koszyka?  ", ButtonType.YES, ButtonType.NO);
            alert1.showAndWait();
            if (alert1.getResult() == ButtonType.YES) {
                Alert alert2 = new Alert(AlertType.INFORMATION, "Dodano do koszyka!");
                alert2.showAndWait();
                product = table.getSelectionModel().getSelectedItem();
                addedProducts.add(product);
                System.out.println("Liczba produktów w koszyku: " + addedProducts.size());
                System.out.println(addedProducts);
                ctrl.setAgdProducts(addedProducts);
            } else if (alert1.getResult() == ButtonType.NO)
                System.out.println("Rezygnacja");
        }    else{
            Alert alert3 = new Alert(AlertType.INFORMATION, "Nie wybrano zadnego pola", ButtonType.OK);
            alert3.showAndWait();

        }
    }

    private ObservableList<Product> filtrProducts(ObservableList<Product> allProducts)
    {
        ObservableList<Product> checked = FXCollections.observableArrayList();

        Iterator<Product> iterator = allProducts.iterator();

        while (iterator.hasNext()) {
            Product actual = iterator.next();
            if(actual.getType().equals("AGD"))
                checked.add(actual);
        }

        return checked;
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {



        Request r = new Request(REQUEST_ID.TEST_CONNECTION,"Handshake TESTClientTest");
        Request r2 = new Request(REQUEST_ID.SELECT,"PRODUCT");
        try {
            Socket socket = new Socket(Protocol.serverIP, Protocol.port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(r);
            out.writeObject(r2);
            Response response = (Response) in.readObject();

            //Czytam wszystkie produkty
            ObservableList<Product> allProducts = (ObservableList)FXCollections.observableArrayList(response.getList());

            //Wyświetlam
            ObservableList<Product> filteredProducy = filtrProducts(allProducts);
            table.itemsProperty().setValue(filteredProducy);
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
        }
        catch (Exception e)
        {
            System.out.println("Brak polaczenia z serverem!");
            // e.printStackTrace();
        }
       /* ObservableList<Product> allProducts = FXCollections.observableArrayList(man.ReadAllProducts());

        ObservableList<Product> agdOnly = filtrAgd(allProducts);

        table.itemsProperty().setValue(agdOnly);



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
        );*/


    }

    public void setCtrl(MenuButtonsController ctrl) {
        this.ctrl = ctrl;
    }



}


