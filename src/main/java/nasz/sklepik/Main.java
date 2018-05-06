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
import javafx.stage.Window;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Szymon on 05.03.2018.
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public static Window mainApp;

    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/MainPane.fxml"));
        BorderPane borderPane = loader.load();
        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Shop");
        primaryStage.show();
        mainApp=primaryStage;
        connectToServer();
        // downloadPurchases();
        //downloadProducts();
        //registerUser();
        // doTransaction();
        //downloadPurchases();
        // ratePurchase();
        // accountTransfer();

        calculateRateForEachProduct();//trzeba wywolac jesli chcesz wyswietlac oceny produktow




    }

    private void calculateRateForEachProduct() {
        List<Product> productList = downloadProducts();
        List<Purchase> purchaseList = downloadPurchases();

        for (Product product : productList) {
            int i = 0, rateSum = 0;
            for (Purchase purchase : purchaseList) {
                if (product.getId().equals(purchase.getProductId())) {
                    if (purchase.getRate() != 0) {
                        rateSum += purchase.getRate();
                        i++;
                    }
                }

            }
            product.setBoughtCount(i);
            product.setAverageRate((double) rateSum / (double) i);
        }

      /*  productList.sort(Comparator.comparing(Product::getAverageRate).reversed());// sortuj po sredniej ocenie .reversed poniewaz od najwiekszej
        //niektore przedmioty nie maja oceny wiec sie zle wyswietlaja jako NaN. wyswietlaj ze brak ocen
        System.out.println("wyswietlanie wg najlepszej oceny:");
        for (Product p : productList
                ) {
            System.out.println(p.getId() + " " + p.getAverageRate());

        }

        productList.sort(Comparator.comparing(Product::getBoughtCount).reversed());//sortuj po ilosci zakupionych
        System.out.println("wyswietlanie wg najczesciej kupowanych:");
        for (Product p : productList
                ) {
            System.out.println(p.getId() + " " + p.getBoughtCount());
        }*/


    }

    private void accountTransfer() {
        User logged = new User((long) 1, "abc", "abc", "Krystian", "Cieplak", "Kielce", "Warszawska 6", 342.71);
        double refill = 200.0; // o ile chcemy zwiekszyc konto
        logged.setAccount(logged.getAccount() + refill);
        //wysylamy requesty
        Request r = new Request(REQUEST_ID.TEST_CONNECTION, "Handshake TESTClientTest");
        Request r2 = new Request(REQUEST_ID.UPDATE, "USER");
        r2.setUser(logged); // wrzucamy usera do requesta
        try {
            Socket socket = new Socket(Protocol.serverIP, Protocol.port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(r);
            out.writeObject(r2);
            Response response = (Response) in.readObject(); // otrzymujemy  info success lub failed
            if (response.getId() == RESPONSE_ID.UPDATE_SUCCESS)
                System.out.println("Dodano do konta pomyslnie");
            else if (response.getId() == RESPONSE_ID.UPDATE_FAILED) {
                System.out.println("Nie udalo sie dodac sumy do konta: " + response.getMessage());
            }
            socket.close();
        } catch (Exception e) {
            System.out.println("Brak polaczenia z serverem!");
        }


    }

    private void connectToServer() {
        Request r = new Request(REQUEST_ID.TEST_CONNECTION, "Handshake TESTClientTest");
        try {
            Socket socket = new Socket(Protocol.serverIP, Protocol.port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(r);
            out.writeObject(r);
            Response response = (Response) in.readObject();
            socket.close();
            System.out.println(response.getMessage());
        } catch (Exception e) {
            System.out.println("Brak polaczenia z serverem!");
            // e.printStackTrace();
        }
    }


    private List<Purchase> downloadPurchases() {
        Request r = new Request(REQUEST_ID.TEST_CONNECTION, "Handshake TESTClientTest");
        Request r2 = new Request(REQUEST_ID.SELECT, "PURCHASE");
        try {
            Socket socket = new Socket(Protocol.serverIP, Protocol.port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(r);
            out.writeObject(r2);
            Response response = (Response) in.readObject();
            System.out.println("Otrzymano:" + " \n" + response.getList().toString());
            socket.close();
            return (List<Purchase>) response.getList();
        } catch (Exception e) {
            System.out.println("Brak polaczenia z serverem!");
            // e.printStackTrace();
        }
        return null;
    }


    public List<Product> downloadProducts() {
        Request r = new Request(REQUEST_ID.TEST_CONNECTION, "Handshake TESTClientTest");
        Request r2 = new Request(REQUEST_ID.SELECT, "PRODUCT");
        try {
            Socket socket = new Socket(Protocol.serverIP, Protocol.port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(r);
            out.writeObject(r2);
            Response response = (Response) in.readObject();
            System.out.println("Otrzymano:" + " \n" + response.getList().toString());
            socket.close();
            return (List<Product>) response.getList();
        } catch (Exception e) {
            System.out.println("Brak polaczenia z serverem!");
            // e.printStackTrace();
        }
        return null;
    }

    private void registerUser() {
        Request r = new Request(REQUEST_ID.TEST_CONNECTION, "Handshake TESTClientTest");
        Request r2 = new Request(REQUEST_ID.REGISTER, "USER");
        //wysylamy pełne info o userze ( id losowe duze)
        r2.setUser(new User((long) 4, "login", "haslo", "name", "surname", "city", "adres", 39.30));
        try {
            Socket socket = new Socket(Protocol.serverIP, Protocol.port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(r);
            out.writeObject(r2);
            Response response = (Response) in.readObject(); // otrzymujemy  info success lub failed
            if (response.getId() == RESPONSE_ID.INSERT_SUCCESS)
                System.out.println("Zarejestrowano pomyslnie");
            else if (response.getId() == RESPONSE_ID.INSERT_FAILED) {
                System.out.println("Nie udalo sie zarejestrowac: " + response.getMessage());
            }
            socket.close();
        } catch (Exception e) {
            System.out.println("Brak polaczenia z serverem!");
        }
    }

    private void doTransaction() {
        Request r = new Request(REQUEST_ID.TEST_CONNECTION, "Handshake TESTClientTest");
        Request r2 = new Request(REQUEST_ID.INSERT, "PURCHASE");
        //wysylamy pełne info o userze zalogowanym
        User logged = new User((long) 4, "login", "haslo", "name", "surname", "city", "adres", 39.30);
        r2.setUser(logged);
        // ustawiamy liste transakcji które chcemy zrobic
        Purchase p1 = new Purchase((long) 6, (long) 4, (long) 1, 1, "4/7/2005", true, 0);
        Purchase p2 = new Purchase((long) 7, (long) 4, (long) 2, 1, "4/7/2005", true, 0);
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
            if (response.getId() == RESPONSE_ID.INSERT_SUCCESS)
                System.out.println("Kupiono pomyslnie!");
            else if (response.getId() == RESPONSE_ID.INSERT_FAILED) {
                System.out.println("Nie udalo sie kupic: " + response.getMessage());
            }
            socket.close();
        } catch (Exception e) {
            System.out.println("Brak polaczenia z serverem!");
        }
    }


    public void ratePurchase() {

        Purchase p = new Purchase((long) 1, (long) 1, (long) 1, 1, "2018/02/02", true, 3);//wyszystikie pola musza byc wypelnione
        //rate jest rowne 5 to jest to nowe rate, a wysylasz tylko te rate ktore mialy wczesniej 0 - nie byly ocenione, inne pola nie zmieniaj

        //czyli np bierzesz jakies Purchase i zmieniasz tylko setRate(ocenaUzytkownika) i pakujesz do requesta

        Request r = new Request(REQUEST_ID.TEST_CONNECTION, "Handshake TESTClientTest");
        Request r2 = new Request(REQUEST_ID.UPDATE, "PURCHASE");

        List<Purchase> lista = new ArrayList<>();
        lista.add(p); // pakujemy tylko jedna transakcje do listy

        r2.setPurchasesList(lista); // pakujemy liste do requesta

        //wysylanie
        try {
            Socket socket = new Socket(Protocol.serverIP, Protocol.port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(r);
            out.writeObject(r2);
            Response response = (Response) in.readObject(); // otrzymujemy  info success lub failed
            if (response.getId() == RESPONSE_ID.UPDATE_SUCCESS)
                System.out.println("Oceniono pomyslnie!");
            else if (response.getId() == RESPONSE_ID.UPDATE_FAILED) {
                System.out.println("Nie udalo sie ocenic: " + response.getMessage());
            }
            socket.close();
        } catch (Exception e) {
            System.out.println("Brak polaczenia z serverem!");
        }


    }


}



