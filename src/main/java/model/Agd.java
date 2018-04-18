package model;

/**
 * Created by Szymon on 07.04.2018.
 */
public class Agd {

    private String name;
    private String model;
    private String price;
    private String amount;
    private String desc;


    //Konstruktor
    public Agd(String name, String model, String price, String amount, String desc) {
        this.name = name;
        this.model = model;
        this.price = price;
        this.amount = amount;
        this.desc = desc;
    }


    //Gettery
    public String getName() {
        return name;
    }

    public String getModel() {
        return model;
    }

    public String getPrice() {
        return price;
    }

    public String getAmount() {
        return amount;
    }

    public String getDesc() {
        return desc;
    }


    //Settery
    public void setName(String name) {
        this.name = name;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


}
