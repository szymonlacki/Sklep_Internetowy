package nasz.sklepik.controllers;

import DAO.DTO.Product;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Szymon on 06.03.2018.
 */
public class MenuButtonsController {

    private MainController mainController;

    private ObservableList<Product> refreshProducts;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }


    public void showAgd() {

        // Załadowane nowego contentu i pobranie jego kontrolera
        AgdController ctrl = (AgdController) mainController.setCenter("/fxml/Agd.fxml");

        // Ustawienie w nowym kontrolerze pole menuButtons
        ctrl.setCtrl(this);
    }

    public void showBin() throws IOException {

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/Bin.fxml"));
        Pane root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        stage.setResizable(false);

        BinController controller = loader.getController();
        controller.setProducts(refreshProducts);

        //Odświeżam, zeby utworzyłe liste na podstawie dostarczonych produktów
        controller.refresh();
    }

    public void showWear() {

        // Załadowane nowego contentu i pobranie jego kontrolera
        WearController ctrl = (WearController) mainController.setCenter("/fxml/Wear.fxml");

        // Ustawienie w nowym kontrolerze pole menuButtons
        ctrl.setCtrl(this);
    }

    public void showMobiles() {

        // Załadowane nowego contentu i pobranie jego kontrolera
        MobilesController ctrl = (MobilesController) mainController.setCenter("/fxml/Mobiles.fxml");

        // Ustawienie w nowym kontrolerze pole menuButtons
        ctrl.setCtrl(this);
    }

    public void showNewest() {

        mainController.setCenter("/fxml/Newest.fxml");
    }

    public void showTop() {

        mainController.setCenter("/fxml/Top.fxml");
    }


    public void setAgdProducts(ObservableList<Product> agdProducts) {
        this.refreshProducts = agdProducts;
    }


}
