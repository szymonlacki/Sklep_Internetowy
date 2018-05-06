package DAO;

import DAO.DTO.User;
import communication.RESPONSE_ID;
import communication.Request;
import communication.Response;

import java.util.List;
import java.util.Random;

public class UserDAO implements  InterfaceDAO {
    @Override
    public List<?> SelectAll(Request request) {
        CsvMenager csvMenager = new CsvMenager();
        return csvMenager.ReadAllUsers();
    }


    @Override
    public InterfaceDTO SelectByID(Request request) {
        CsvMenager csvMenager = new CsvMenager();
        User searchedPurchase =  request.getUser();
        List<User> list = csvMenager.ReadAllUsers();
        for (User p:list) {
            if(p.getId().equals( searchedPurchase.getId()))
                return  p;
        }
        return null;
    }

    @Override
    public Response Insert(Request request) {
        CsvMenager csvMenager = new CsvMenager();
        List<User> users =(List<User>) SelectAll(request);
        //sprawdzanie czy nie ma takiego usera lub id ktore koliduje
        if((request!=null) && (request.getUser()!=null))
        {
            for (User u:users) {
                if(u.getLogin().equals(request.getUser().getLogin()))
                {
                    Response response = new Response(RESPONSE_ID.INSERT_FAILED);
                    response.setMessage("jest juz taki uzytkownik!");
                    return  response;
                }
                if(u.getId().equals(request.getUser().getId()))
                {
                    request.getUser().setId(FindNextID());//randomowo zwraca inne id
                }
            }
            csvMenager.saveUser(request.getUser());
            return new Response(RESPONSE_ID.INSERT_SUCCESS);
        }
        else  return new Response(RESPONSE_ID.INSERT_FAILED);


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
        return new Response(csvMenager.deleteUser( request.getUser()));
    }

    @Override
    public Response Update(Request request) {
        CsvMenager csvMenager = new CsvMenager();
        return new Response(csvMenager.updateUser(request.getUser()));
    }
}
