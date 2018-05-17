package DAO.DTO;

import DAO.InterfaceDTO;

import java.io.Serializable;
import java.util.Objects;

public class Purchase implements Serializable, InterfaceDTO, PDFWritable{
    private static final long serialVersionUID=7436472215642765747L;
    private Long id;
    private Long userId;
    private Long productId;



    private String productName;
    private Integer amount;// ilosc
    private String date;// data zakupu
    private Boolean paid;// czy zaplacone
    private Integer rate;// ocena w gwiazdkach 1-5 dodawana pozniej, 0 oznacza ze nieocenione jeszcze

    public Purchase(Long id, Long userId, Long productId, Integer amount, String date, Boolean paid, Integer rate) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.amount = amount;
        this.date = date;
        this.paid = paid;
        this.rate = rate;
    }


    public Purchase() {
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long user_id) {
        this.userId = user_id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long product_id) {
        this.productId = product_id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return  id +","+userId +","+productId +"," + amount +","+date+","+paid +","+rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Purchase)) return false;
        Purchase purchase = (Purchase) o;
        return Objects.equals(getId(), purchase.getId()) &&
                Objects.equals(getUserId(), purchase.getUserId()) &&
                Objects.equals(getProductId(), purchase.getProductId()) &&
                Objects.equals(getAmount(), purchase.getAmount()) &&
                Objects.equals(getDate(), purchase.getDate()) &&
                Objects.equals(getPaid(), purchase.getPaid()) &&
                Objects.equals(getRate(), purchase.getRate());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getUserId(), getProductId(), getAmount(), getDate(), getPaid(), getRate());
    }

    @Override
    public String toPdfString() {
        return " ilosc:" + amount +" data zakupu: "+date;
    }
}
