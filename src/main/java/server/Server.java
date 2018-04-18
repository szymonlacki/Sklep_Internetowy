package server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import server.net.PortListener;

import java.io.IOException;
import java.net.ServerSocket;

public class Server extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private Controller controller = null;
    private PortListener portListener = null ;
    /**
     * Metoda odpowiedzialna za poprawne uruchomienie w�tku serwera
     *
     */
    private void startPortListener() {
        if (getPortListener() == null) {
            setPortListener(new PortListener(controller));
            getPortListener().start();
        }
    }

    /**
     * Metoda odpowiedzialna za zako�czenie w�tku akceptuj�cego po��czenia, a
     * po�rednio wszystkich w�tk�w serwera
     */
    private void stopPortListener() {
        if (getPortListener() != null) {
            try {ServerSocket ss =
                    getPortListener().getServerSocket();
                if(ss != null)
                    ss.close();
                getPortListener().interrupt();
                getPortListener().join();
                if(!getPortListener().isAlive())
                    controller.write("Port Listener Stopped");
                    System.out.println("PL Stopped");


            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
            setPortListener(null);
        }
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
       /* FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/ServerView.fxml"));
        GridPane borderPane = loader.load();
        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Shop");
        primaryStage.show();*/

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ServerView.fxml"));
        primaryStage.setTitle("Server");
        primaryStage.setScene(new Scene(loader.load(), 800, 600));
        controller = loader.getController();
        if(controller != null ){
            System.out.println("Podlaczono controller");
            controller.write("Podlaczono controller");
            controller.setMainApp(this);
        }
        else {
            System.out.println("Brak controllera");

            controller.write("Brak controllera");
        }

        primaryStage.setOnCloseRequest((WindowEvent we) -> stopPortListener());
        startPortListener();


        primaryStage.show();


    }
    /*
     * @return the portListener
     */
    private PortListener getPortListener() {

        return this.portListener;

    }

    /*
     * @param portListener the portListener to set
     */
    private void setPortListener(PortListener portListener) {
        this.portListener = portListener;
    }

}
