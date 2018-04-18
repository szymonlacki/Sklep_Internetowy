package communication;



import DAO.DTO.User;

import java.io.Serializable;
import java.util.List;

public class Response implements Serializable {
    private static final long serialVersionUID = 7526472295622776147L;

    private String message;
    private RESPONSE_ID id;
    private List<?> list;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Response()
    {

    }

    public RESPONSE_ID getId() {
        return id;
    }

    public void setId(RESPONSE_ID id) {
        this.id = id;
    }

    public Response(String message)
    {
        this.message = message;
        this.list = null;
    }

    public Response(RESPONSE_ID id)
    {
        this.id = id;
    }
    public Response(RESPONSE_ID id,String message)
    {
        this.id = id;
        this.message = message;
    }

    public Response(String message, List<?> list)
    {
        this.message = message;
        this.list = list;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<?> getList() {
        return list;
    }
    public void setList(List<?> list) {
        this.list = list;
    }


}
