package DAO;

import DAO.DTO.Product;
import communication.Request;
import communication.Response;


import java.util.List;

public class ProductDAO implements InterfaceDAO {
    @Override
    public List<?> SelectAll(Request request) {
        CsvMenager csvMenager = new CsvMenager();
        return csvMenager.ReadAllProducts();
    }
    @Override
    public InterfaceDTO SelectByID(Request request) {
        return null;
    }

    @Override
    public Response Insert(Request request) {
        return null;
    }

    @Override
    public long FindNextID() {
        return 0;
    }

    @Override
    public Response Delete(Request request) {
        return null;
    }

    @Override
    public Response Update(Request request) {
        return null;
    }

    public  Response updateAmount(Request request)
    {
        CsvMenager csvMenager = new CsvMenager();
        return new Response(csvMenager.updateProductAmount(request.getPurchasesList()));
    }
}
