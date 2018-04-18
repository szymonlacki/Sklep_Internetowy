package nasz.sklepik.controllers;

import DAO.DTO.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Agd;
import DAO.CsvMenager;
import DAO.DTO.Product;
import DAO.DTO.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Agd;

import javafx.scene.control.Dialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ChoiceDialog;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Szymon on 15.04.2018.
 */
public class BinController implements Initializable {

	private ObservableList<Product> products;

	
	@FXML
	private TableView<Product> BinTable;
	@FXML
	private TableColumn<Product, String> BinName;
	@FXML
	private TableColumn<Product, String> BinPrice;

	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}
	
	// ladowanie listy wczesniej wczytamymi produktami
	public void refresh() {
		BinTable.itemsProperty().setValue(products);

		BinName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));

		BinPrice.setCellValueFactory(new PropertyValueFactory<Product, String>("price"));
	}

	public void setProducts(ObservableList<Product> products) {
		this.products = products;
	}
	
	

}