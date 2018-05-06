package DAO;

import DAO.DTO.Product;
import DAO.DTO.Purchase;
import DAO.DTO.User;
import communication.RESPONSE_ID;
import communication.Request;
import communication.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PurchaseDAO implements InterfaceDAO {
    @Override
    public List<?> SelectAll(Request request) {
        CsvMenager csvMenager = new CsvMenager();
        return csvMenager.ReadAllPurchase();
    }


    @Override
    public InterfaceDTO SelectByID(Request request) {
        CsvMenager csvMenager = new CsvMenager();
        Purchase searchedPurchase = (Purchase) request.getPurchasesList();
        List<Purchase> list = csvMenager.ReadAllPurchase();
        for (Purchase p:list) {
            if(p.getId().equals( searchedPurchase.getId()) )
                return  p;
        }
        return null;
    }

    @Override
    public Response Insert(Request request) {
        CsvMenager csvMenager = new CsvMenager();
        List<Purchase> list = csvMenager.ReadAllPurchase();
        List<Purchase> newPurchases = request.getPurchasesList();
        if(newPurchases.isEmpty()){
            return new Response(RESPONSE_ID.INSERT_FAILED,"Nie odebrano transakcji do wstawienia");
        }
        Double transactionSum = 0.0;
        for (Purchase newP: newPurchases) {
            if(!(newP.getUserId().equals(request.getUser().getId())))//sprawdzanie czy user ktory wysyla dobrze ustawil swoje id
            {
                newP.setUserId(request.getUser().getId());
            }
            for (Purchase p:list) {
                if(p.getId().equals(newP.getId()))
                {
                    newP.setId(FindNextID());//znajdz nastepne losowe id
                }
            }
            csvMenager.savePurchase(newP);
        }
        UserDAO userDAO = new UserDAO();
        ProductDAO productDAO = new ProductDAO();
        productDAO.updateAmount(request);
        return new Response(RESPONSE_ID.INSERT_SUCCESS,userDAO.Update(request).toString());
    }

    @Override
    public long FindNextID() {
        Random r = new Random();
        long random = r.nextLong();
        if(random <0) random*=-1;
        return random;
    }

    @Override
    public Response Delete(Request request) {
        CsvMenager csvMenager = new CsvMenager();
        return new Response(csvMenager.deletePurchase(request.getPurchasesList().get(0)));
    }

    @Override
    public Response Update(Request request) {
        CsvMenager csvMenager = new CsvMenager();
        return new Response(csvMenager.updatePurchase(request.getPurchasesList().get(0)));
    }
}
