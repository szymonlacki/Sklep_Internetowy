package nasz.sklepik.controllers;

import DAO.DTO.Product;
import DAO.DTO.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by Szymon on 06.03.2018.
 */
public class MenuButtonsController {

    private MainController mainController;

    private LoginController loginController;

    private ObservableList<Product> refreshProducts = FXCollections.observableArrayList();

    private User logged;


    public void refresh() {
        loginController.setCtrl(this);
    }


    public void showAgd() {

        // Załadowane nowego contentu i pobranie jego kontrolera
        AgdController ctrl = (AgdController) mainController.setCenter("/fxml/Agd.fxml");

        // Ustawienie w nowym kontrolerze pole menuButtons
        ctrl.setCtrl(this);
    }

    public void showCart() throws IOException {

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/Cart.fxml"));
        Pane root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        stage.setResizable(false);

        CartController controller = loader.getController();

        controller.setProducts(refreshProducts);
        controller.setLogged(logged);
        // Odświeżam, zeby utworzyłe liste na podstawie dostarczonych
        // produktów

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

    public void showProfile() {

        UserController ctrl = (UserController) mainController.setCenter("/fxml/UserPane.fxml");
        ctrl.setLogged(logged);

    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void addProducts(ObservableList<Product> products) {
        Iterator<Product> iterator = products.iterator();

        while (iterator.hasNext()) {
            this.refreshProducts.add(iterator.next());
        }
    }

    public void setLogged(User logged) {
        this.logged = logged;
    }


}
