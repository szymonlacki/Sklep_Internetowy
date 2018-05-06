package DAO.DTO;

import DAO.InterfaceDTO;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable, InterfaceDTO,PDFWritable{

    private static final long serialVersionUID=7526472215642776147L;

    private Long id;
    private String login;
    private String password;
    private String name;
    private String surname;
    private String city;
    private String address;
    private Double account;//stan konta

    public User(){}

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }
    public User(Long id, String login, String password, String name, String surname, String city, String address, Double account) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.city = city;
        this.address = address;
        this.account = account;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Double getAccount() {
        return account;
    }

    public void setAccount(Double account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return id +"," + login +"," + password +"," +name+"," + surname +","+ city +"," + address +"," + account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId()) &&
                Objects.equals(getLogin(), user.getLogin()) &&
                Objects.equals(getPassword(), user.getPassword()) &&
                Objects.equals(getCity(), user.getCity()) &&
                Objects.equals(getAddress(), user.getAddress()) &&
                Objects.equals(getName(), user.getName()) &&
                Objects.equals(getSurname(), user.getSurname()) &&
                Objects.equals(getAccount(), user.getAccount());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getLogin(), getPassword(), getCity(), getAddress(), getName(), getSurname(), getAccount());
    }

    @Override
    public String toPdfString() {
        return " " +name+" " +surname +" "+ city +" " + address;
    }
}
