package nasz.sklepik;

import DAO.DTO.Product;
import DAO.DTO.Purchase;
import DAO.DTO.User;
import communication.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Szymon on 05.03.2018.
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/MainPane.fxml"));
        BorderPane borderPane = loader.load();
        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Shop");
        primaryStage.show();
        connectToServer();
        // downloadPurchases();
        //downloadProducts();
        //registerUser();
       // doTransaction();
        //downloadPurchases();
    }

    private void connectToServer()
    {
        Request r = new Request(REQUEST_ID.TEST_CONNECTION,"Handshake TESTClientTest");
        try {
            Socket socket = new Socket(Protocol.serverIP, Protocol.port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(r);
            out.writeObject(r);
            Response response = (Response) in.readObject();
            socket.close();
            System.out.println( response.getMessage() );
        }
        catch (Exception e)
        {
            System.out.println("Brak polaczenia z serverem!");
            // e.printStackTrace();
        }
    }


    private void downloadPurchases()
    {
        Request r = new Request(REQUEST_ID.TEST_CONNECTION,"Handshake TESTClientTest");
        Request r2 = new Request(REQUEST_ID.SELECT,"PURCHASE");
        try {
            Socket socket = new Socket(Protocol.serverIP, Protocol.port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(r);
            out.writeObject(r2);
            Response response = (Response) in.readObject();
            System.out.println("Otrzymano:" + " \n" + response.getList().toString());
            socket.close();
        }
        catch (Exception e)
        {
            System.out.println("Brak polaczenia z serverem!");
            // e.printStackTrace();
        }
    }


    private void downloadProducts()
    {
        Request r = new Request(REQUEST_ID.TEST_CONNECTION,"Handshake TESTClientTest");
        Request r2 = new Request(REQUEST_ID.SELECT,"PRODUCT");
        try {
            Socket socket = new Socket(Protocol.serverIP, Protocol.port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(r);
            out.writeObject(r2);
            Response response = (Response) in.readObject();
            System.out.println("Otrzymano:"+ " \n" + response.getList().toString());
            socket.close();
        }
        catch (Exception e)
        {
            System.out.println("Brak polaczenia z serverem!");
            // e.printStackTrace();
        }
    }
    private void registerUser()
    {
        Request r = new Request(REQUEST_ID.TEST_CONNECTION,"Handshake TESTClientTest");
        Request r2 = new Request(REQUEST_ID.REGISTER,"USER");
        //wysylamy pełne info o userze ( id losowe duze)
        r2.setUser(new User((long) 4,"login","haslo","name","surname","city","adres",39.30));
        try {
            Socket socket = new Socket(Protocol.serverIP, Protocol.port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(r);
            out.writeObject(r2);
            Response response = (Response) in.readObject(); // otrzymujemy  info success lub failed
            if(response.getId()== RESPONSE_ID.INSERT_SUCCESS)
                System.out.println("Zarejestrowano pomyslnie");
            else if(response.getId()==RESPONSE_ID.INSERT_FAILED)
            {
                System.out.println("Nie udalo sie zarejestrowac: "+ response.getMessage());
            }
            socket.close();
        }
        catch (Exception e)
        {
            System.out.println("Brak polaczenia z serverem!");
        }
    }

    private void doTransaction()
    {
        Request r = new Request(REQUEST_ID.TEST_CONNECTION,"Handshake TESTClientTest");
        Request r2 = new Request(REQUEST_ID.INSERT,"PURCHASE");
        //wysylamy pełne info o userze zalogowanym
        User logged = new User((long) 4,"login","haslo","name","surname","city","adres",39.30);
        r2.setUser(logged);
        // ustawiamy liste transakcji które chcemy zrobic
        Purchase p1 = new Purchase((long)6,(long)4,(long)1,1,"4/7/2005",true,0);
        Purchase p2 = new Purchase((long)7,(long)4,(long)2,1,"4/7/2005",true,0);
        List<Purchase> toBuyList = new ArrayList<>();
        toBuyList.add(p1);
        toBuyList.add(p2);
        //wrzucamy liste do requesta
        r2.setPurchasesList(toBuyList);

        try {
            Socket socket = new Socket(Protocol.serverIP, Protocol.port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(r);
            out.writeObject(r2);
            Response response = (Response) in.readObject(); // otrzymujemy  info success lub failed
            if(response.getId()== RESPONSE_ID.INSERT_SUCCESS)
                System.out.println("Kupiono pomyslnie!");
            else if(response.getId()==RESPONSE_ID.INSERT_FAILED)
            {
                System.out.println("Nie udalo sie kupic: "+ response.getMessage());
            }
            socket.close();
        }
        catch (Exception e)
        {
            System.out.println("Brak polaczenia z serverem!");
        }
    }


}
