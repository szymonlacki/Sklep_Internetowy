package server;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;


public class Controller  implements WritableGUI{


    @FXML
    TextArea logTextArea;


    private Server mainApp;

    public void write(String s)
    {
        logTextArea.appendText(s + "\n");
    }

    void setMainApp(Server server)
    {
        this.mainApp = server;
    }



}
