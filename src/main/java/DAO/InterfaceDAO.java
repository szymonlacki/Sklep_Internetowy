package DAO;

import communication.Request;
import communication.Response;


import java.util.List;

public interface InterfaceDAO {
    List<?> SelectAll(Request request);
    InterfaceDTO SelectByID(Request request);
    Response Insert(Request request);
    long FindNextID();
    Response Delete(Request request);
    Response Update(Request request);
}
