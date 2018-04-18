package communication;

import DAO.DTO.Purchase;
import DAO.DTO.User;
import DAO.InterfaceDTO;

import java.io.Serializable;
import java.util.List;

public class Request implements Serializable{

    private static final long serialVersionUID = 7526132132122776147L;

    private String sql;
    private REQUEST_ID id;
    private List<Purchase> purchasesList;
    private InterfaceDTO objectDTO;
    private User user;

    public List<Purchase> getPurchasesList() {
        return purchasesList;
    }

    public void setPurchasesList(List<Purchase> object) {
        purchasesList = object;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public Request( REQUEST_ID id ,String sql, User user)
    {
        this.id = id;
        this.sql = sql;
        this.user = user;
    }

    public Request( REQUEST_ID id ,String sql)
    {
        this.id = id;
        this.sql = sql;
    }

    public Request ()
    {
        id = REQUEST_ID.TEST_CONNECTION;
        sql="TEST CONNECTION";
    }


    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public REQUEST_ID getId() {
        return id;
    }

    public void setId(REQUEST_ID id) {
        this.id = id;
    }
}
