package nasz.sklepik.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

/**
 * Created by Szymon on 05.03.2018.
 */
public class MainController {


    @FXML
    private BorderPane borderPane;
    @FXML
    private MenuButtonsController menuButtonsController;


    @FXML
    private void initialize() {
        menuButtonsController.setMainController(this);
    }

    public Object setCenter(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource(fxmlPath));
        Parent parent = null;
        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        borderPane.setCenter(parent);
        
        return loader.getController();
    }
}
