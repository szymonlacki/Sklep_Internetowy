package nasz.sklepik.controllers;

/**
 * Created by Szymon on 27.04.2018.
 */

import DAO.DTO.Product;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class DetailsController {

    @FXML
    private TextArea Detailsname;
    @FXML
    private TextArea Detailsdesc;
    @FXML
    private TextArea Detailsprice;
    @FXML
    private TextArea Detailsmodel;


    private Product details;

    @FXML
    private ImageView image;


    public void initialize() {


    }

    public void refresh() {

        Detailsname.setText(details.getName());
        Detailsdesc.setText(details.getDescription());
        Detailsprice.setText(String.valueOf(details.getPrice()));
        Detailsmodel.setText(details.getType());
        Detailsdesc.setWrapText(true);
        Detailsdesc.setEditable(false);
        Detailsname.setWrapText(true);
        Detailsname.setEditable(false);
        Detailsprice.setWrapText(true);
        Detailsprice.setEditable(false);
        Detailsmodel.setWrapText(true);
        Detailsmodel.setEditable(false);
        File file = new File("src/main/resources/icons/" + details.getId() + ".jpg");
        Image image1 = new Image(file.toURI().toString());
        image.setImage(image1);
        image.setFitHeight(182);
        image.setY(-3);

    }

    public void setDetails(Product details) {
        this.details = details;
    }


}
