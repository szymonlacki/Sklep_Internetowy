package DAO;


import DAO.DTO.Product;
import DAO.DTO.Purchase;
import DAO.DTO.User;
import communication.RESPONSE_ID;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class CsvMenager {
    /**
     * Lokalizacja pliku CSV
     */
    private String plikProductCSV = "baza/products.csv";
    private String plikUserCSV = "baza/users.csv";
    private String plikPurchaseCSV = "baza/purchases.csv";
    /**
     * Zmienna przechowuj�ca lini� odczytan� z pliku
     */
    private String odczytanaLinia = "";
    /**
     * Ustalony znak podzia�u w pliku CSV
     */
    private String znakPodzialu = ",";

    /**
     * Odczyt pliku CSV
     *
     * @return Lista odczytanych pojazd�w
     */
    public List<Product> ReadAllProducts() {
        String[] productData;
        List<Product> products = new ArrayList<>();
        System.out.println("Tu maja byc pliki: " + new File(plikProductCSV).getAbsolutePath());
        try (BufferedReader br = new BufferedReader(new FileReader(plikProductCSV))) {
            while ((odczytanaLinia = br.readLine()) != null) {
                // podzia� odczytanej linii z pliku z zastosowaniem znaku podzialu
                productData = odczytanaLinia.split(znakPodzialu);
                // utworzenie obiektu na podstawie odczytanych danych
                Product product = new Product();
                product.setId(Long.parseLong(productData[0]));
                product.setName(productData[1]);
                product.setDescription(productData[2]);
                product.setType(productData[3]);
                product.setPrice(Double.parseDouble(productData[4]));
                product.setAmount(Integer.parseInt(productData[5]));
                product.setVat(Double.parseDouble(productData[6]));
                products.add(product);
            }
            // zamkni�cie strumienia odczytuj�cego
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }

    public List<User> ReadAllUsers() {
        String[] userData;
        List<User> users = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(plikUserCSV))) {
            while ((odczytanaLinia = br.readLine()) != null) {
                // podzia� odczytanej linii z pliku z zastosowaniem znaku podzialu
                userData = odczytanaLinia.split(znakPodzialu);
                // utworzenie obiektu na podstawie odczytanych danych
                User user = new User();
                user.setId(Long.parseLong(userData[0]));
                user.setLogin(userData[1]);
                user.setPassword(userData[2]);
                user.setName(userData[3]);
                user.setSurname(userData[4]);
                user.setCity((userData[5]));
                user.setAddress(userData[6]);
                user.setAccount(Double.parseDouble(userData[7]));
                users.add(user);
            }
            // zamkni�cie strumienia odczytuj�cego
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }


    public List<Purchase> ReadAllPurchase() {
        String[] purchaseData;
        List<Purchase> purchaseList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(plikPurchaseCSV))) {
            while ((odczytanaLinia = br.readLine()) != null) {
                // podzia� odczytanej linii z pliku z zastosowaniem znaku podzialu
                purchaseData = odczytanaLinia.split(znakPodzialu);
                // utworzenie obiektu na podstawie odczytanych danych
                Purchase purchase = new Purchase();
                purchase.setId(Long.parseLong(purchaseData[0]));
                purchase.setUserId(Long.parseLong(purchaseData[1]));
                purchase.setProductId(Long.parseLong(purchaseData[2]));
                purchase.setAmount(Integer.parseInt(purchaseData[3]));
                purchase.setDate(purchaseData[4]);
                purchase.setPaid(Boolean.parseBoolean(purchaseData[5]));
                purchase.setRate(Integer.parseInt(purchaseData[6]));
                purchaseList.add(purchase);
            }
            // zamkni�cie strumienia odczytuj�cego
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return purchaseList;
    }

    /**
     * Zapis do pliku CSV
     *
     */
    public void savePurchase(Purchase newPurchase) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(plikPurchaseCSV,true))) {
                // zapis pojedynczego obiektu do pliku
                bw.write(newPurchase.toString());
                // przej�cie do nowej linii
                bw.newLine();
            // zamkniecie strumienia zapisujecego
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(User newUser) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(plikUserCSV,true))) {
            // zapis pojedynczego obiektu do pliku
            bw.write(newUser.toString());
            // przej�cie do nowej linii
            bw.newLine();
            // zamkni�cie strumienia zapisuj�cego
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RESPONSE_ID updateUser(User updatedUser){

        String[] userData;
        RESPONSE_ID responseId=RESPONSE_ID.UPDATE_FAILED;
        try (BufferedReader br = new BufferedReader(new FileReader(plikUserCSV))) {
            String plikUserCSVCreate= "src/baza/users2.csv";
            BufferedWriter bw = new BufferedWriter(new FileWriter(plikUserCSVCreate));
            while ((odczytanaLinia = br.readLine()) != null) {
                // podzia� odczytanej linii z pliku z zastosowaniem znaku podzialu
                userData = odczytanaLinia.split(znakPodzialu);
                // utworzenie obiektu na podstawie odczytanych danych
                User user = new User();
                user.setId(Long.getLong(userData[0]));
                user.setLogin(userData[1]);
                user.setPassword(userData[2]);
                user.setName(userData[3]);
                user.setSurname(userData[4]);
                user.setCity((userData[5]));
                user.setAddress(userData[6]);
                user.setAccount(Double.valueOf(userData[7]));
                if(user.getId().equals(updatedUser.getId()))
                {
                    user = updatedUser;
                    responseId = RESPONSE_ID.UPDATE_SUCCESS;
                }
                // zapis pojedynczego obiektu do pliku
                bw.write(user.toString());
                // przej�cie do nowej linii
                bw.newLine();
            }
            // zamkni�cie strumienia odczytuj�cego
            br.close();
            // zamkni�cie strumienia piszacego
            bw.close();
            Files.delete(Paths.get(plikUserCSV));
            Path source = Paths.get(plikUserCSVCreate);
            Files.move(source,source.resolveSibling("users.csv"));
            return responseId;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseId;
    }

    public RESPONSE_ID updatePurchase(Purchase updatedPurchase){

        String[] purchaseData;
        RESPONSE_ID responseId = RESPONSE_ID.UPDATE_FAILED;
        try (BufferedReader br = new BufferedReader(new FileReader(plikPurchaseCSV))) {
            String plikPurchaseCSVCreate= "src/baza/purchases2.csv";
            BufferedWriter bw = new BufferedWriter(new FileWriter(plikPurchaseCSVCreate));
            while ((odczytanaLinia = br.readLine()) != null) {
                // podzia� odczytanej linii z pliku z zastosowaniem znaku podzialu
                purchaseData = odczytanaLinia.split(znakPodzialu);
                // utworzenie obiektu na podstawie odczytanych danych
                Purchase purchase = new Purchase();
                purchase.setId(Long.getLong(purchaseData[0]));
                purchase.setUserId(Long.getLong(purchaseData[1]));
                purchase.setProductId(Long.getLong(purchaseData[2]));
                purchase.setAmount(Integer.getInteger(purchaseData[3]));
                purchase.setDate(purchaseData[4]);
                purchase.setPaid(Boolean.getBoolean(purchaseData[5]));
                purchase.setRate(Integer.getInteger(purchaseData[6]));
                if(purchase.getId().equals(updatedPurchase.getId()))
                {
                    purchase = updatedPurchase;
                    responseId = RESPONSE_ID.UPDATE_SUCCESS;
                }
                // zapis pojedynczego obiektu do pliku
                bw.write(purchase.toString());
                // przej�cie do nowej linii
                bw.newLine();
            }
            // zamkni�cie strumienia odczytuj�cego
            br.close();
            // zamkni�cie strumienia piszacego
            bw.close();
            Files.delete(Paths.get(plikPurchaseCSV));
            Path source = Paths.get(plikPurchaseCSVCreate);
            Files.move(source,source.resolveSibling("purchases.csv"));
            return responseId;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  responseId;
    }

    public RESPONSE_ID deleteUser(User deletedUser){

        String[] userData;
        RESPONSE_ID responseId= RESPONSE_ID.DELETE_FAILED;
        try (BufferedReader br = new BufferedReader(new FileReader(plikUserCSV))) {
            String plikUserCSVCreate= "src/baza/users2.csv";
            BufferedWriter bw = new BufferedWriter(new FileWriter(plikUserCSVCreate));
            while ((odczytanaLinia = br.readLine()) != null) {
                // podzia� odczytanej linii z pliku z zastosowaniem znaku podzialu
                userData = odczytanaLinia.split(znakPodzialu);
                // utworzenie obiektu na podstawie odczytanych danych
                User user = new User();
                user.setId(Long.getLong(userData[0]));
                user.setLogin(userData[1]);
                user.setPassword(userData[2]);
                user.setName(userData[3]);
                user.setSurname(userData[4]);
                user.setCity((userData[5]));
                user.setAddress(userData[6]);
                user.setAccount(Double.valueOf(userData[7]));
                if(user.getId().equals(deletedUser.getId()))
                {
                    responseId=RESPONSE_ID.DELETE_SUCCESS;
                    continue;
                }
                // zapis pojedynczego obiektu do pliku
                bw.write(user.toString());
                // przej�cie do nowej linii
                bw.newLine();
            }
            // zamkni�cie strumienia odczytuj�cego
            br.close();
            // zamkni�cie strumienia piszacego
            bw.close();
            Files.delete(Paths.get(plikUserCSV));
            Path source = Paths.get(plikUserCSVCreate);
            Files.move(source,source.resolveSibling("users.csv"));
            return responseId;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  responseId;
    }

    public RESPONSE_ID deletePurchase(Purchase deletedPurchase){

        String[] purchaseData;
        RESPONSE_ID response_id=RESPONSE_ID.DELETE_FAILED;
        try (BufferedReader br = new BufferedReader(new FileReader(plikPurchaseCSV))) {
            String plikPurchaseCSVCreate= "src/baza/purchases2.csv";
            BufferedWriter bw = new BufferedWriter(new FileWriter(plikPurchaseCSVCreate));
            while ((odczytanaLinia = br.readLine()) != null) {
                // podzia� odczytanej linii z pliku z zastosowaniem znaku podzialu
                purchaseData = odczytanaLinia.split(znakPodzialu);
                // utworzenie obiektu na podstawie odczytanych danych
                Purchase purchase = new Purchase();
                purchase.setId(Long.getLong(purchaseData[0]));
                purchase.setUserId(Long.getLong(purchaseData[1]));
                purchase.setProductId(Long.getLong(purchaseData[2]));
                purchase.setAmount(Integer.getInteger(purchaseData[3]));
                purchase.setDate(purchaseData[4]);
                purchase.setPaid(Boolean.getBoolean(purchaseData[5]));
                purchase.setRate(Integer.getInteger(purchaseData[6]));
                if(purchase.getId().equals(deletedPurchase.getId()))
                {
                    response_id= RESPONSE_ID.DELETE_SUCCESS;
                    continue;
                }
                // zapis pojedynczego obiektu do pliku
                bw.write(purchase.toString());
                // przej�cie do nowej linii
                bw.newLine();
            }
            // zamkni�cie strumienia odczytuj�cego
            br.close();
            // zamkni�cie strumienia piszacego
            bw.close();
            Files.delete(Paths.get(plikPurchaseCSV));
            Path source = Paths.get(plikPurchaseCSVCreate);
            Files.move(source,source.resolveSibling("purchases.csv"));
            return response_id;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response_id;
    }




}



