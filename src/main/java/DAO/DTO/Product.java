package DAO.DTO;

import DAO.InterfaceDTO;

import java.io.Serializable;
import java.util.Objects;

public class Product implements Serializable , InterfaceDTO{
    private static final long serialVersionUID=7526345237642736187L;

    private Long id;
    private String name;
    private String description;
    private String type;
    private Double price;
    private Integer amount;
    private Double vat;//standardowo 1.23 mnoznik

    public Double getVat() {
        return vat;
    }

    public void setVat(Double vat) {
        this.vat = vat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return id +","+ name +","+ description +','+type +','+price +","+amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(getId(), product.getId()) &&
                Objects.equals(getName(), product.getName()) &&
                Objects.equals(getDescription(), product.getDescription()) &&
                Objects.equals(getType(), product.getType()) &&
                Objects.equals(getPrice(), product.getPrice()) &&
                Objects.equals(getAmount(), product.getAmount());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getName(), getDescription(), getType(), getPrice(), getAmount());
    }
}
