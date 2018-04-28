package nasz.sklepik.controllers;

import DAO.DTO.User;
import communication.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Szymon on 18.03.2018.
 */
public class LoginController {

    @FXML
    private Button przycisk, registrationButton;
    @FXML
    private TextField login;
    @FXML
    private PasswordField passsword;
    @FXML
    private Label loginAs;
    private User logged;
    private MenuButtonsController ctrl;


    public void initialize() {
        loginAs.setVisible(false);
    }

    public void login() throws IOException {
        String loginData, passwordData;
        loginData = login.getText();
        passwordData = passsword.getText();

        User user = new User(loginData, passwordData);

        if (przycisk.getText().equals("Zaloguj sie")) {

            Request r = new Request(REQUEST_ID.TEST_CONNECTION, "Handshake TESTClientTest");
            Request r2 = new Request(REQUEST_ID.LOG_IN, "USER");
            r2.setUser(new User(login.getText(), passsword.getText()));
            try {
                Socket socket = new Socket(Protocol.serverIP, Protocol.port);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                out.writeObject(r);
                out.writeObject(r2);
                Response response = (Response) in.readObject();

                if (response.getId() == RESPONSE_ID.LOGIN_SUCCESS) {
                    logged = response.getUser();

                    System.out.println("Zalogowano jako:" + " \n" + logged.toString());
                    ctrl.setLogged(logged);
                    loginAs.setText("Zalogowano jako " + user.getLogin());
                    przycisk.setText("Wyloguj sie");
                    login.setVisible(false);
                    passsword.setVisible(false);
                    registrationButton.setVisible(false);
                    loginAs.setVisible(true);
                    loginAs.setTranslateY(-80);
                    loginAs.setTranslateX(-10);
                    przycisk.setTranslateY(-55);


                }
                if (response.getId() == RESPONSE_ID.LOGIN_FAILED) {
                    login.setVisible(true);
                    passsword.setVisible(true);
                    registrationButton.setVisible(true);

                    loginAs.setVisible(true);
                    loginAs.setText("Podano złe dane!");
                    loginAs.setTranslateY(-5);
                    loginAs.setTranslateX(8);


                }
                if (login.getText().isEmpty() || passsword.getText().isEmpty()) {

                    login.setVisible(true);
                    passsword.setVisible(true);
                    registrationButton.setVisible(true);
                    loginAs.setVisible(true);
                    loginAs.setText("Puste pola!");
                    loginAs.setTranslateY(-5);
                    loginAs.setTranslateX(30);

                }

                socket.close();
            } catch (Exception e) {


                //System.out.println("Brak polaczenia z serverem!");

                e.printStackTrace();
            }


        } else if (przycisk.getText().equals("Wyloguj sie")) {

            przycisk.setText("Zaloguj sie");
            login.setVisible(true);
            passsword.setVisible(true);
            registrationButton.setVisible(true);
            loginAs.setVisible(false);
            przycisk.setTranslateY(0);
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Wylogowano pomyślnie!");
            alert2.showAndWait();

            //Ustawiam na nulla, żeby po wylogowaniu znowu kupowanie było niedostępne
            logged = null;
            ctrl.setLogged(logged);


        }
    }

    public void showRegPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/RegistrationPane.fxml"));
        Pane root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        stage.setResizable(false);

        RegistrationController controller = loader.getController();
        controller.setRegistrationPane(stage);

    }

    public void setCtrl(MenuButtonsController ctrl) {
        this.ctrl = ctrl;
    }


}
