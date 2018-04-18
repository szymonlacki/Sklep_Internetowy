package DAO;

public class DAOFactory {
    public static InterfaceDAO getDAO(String sql)
    {

        if(sql.toUpperCase().contains("PRODUCT")){
            return new ProductDAO();
        }
        else if (sql.toUpperCase().contains("USER")){
            return new UserDAO();
        }
        else if (sql.toUpperCase().contains("PURCHASE")){
            return new PurchaseDAO();
        }
        return null;
    }
}
