package com.mad.riders;

public class DailyOfferItem {
    private String name;
    private String desc;
    private float price;
    private int quantity;
    private String photoPath;

    public DailyOfferItem(){}

    public DailyOfferItem(String name, String desc, float price, int quantity, String photoPath){
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.quantity = quantity;
        this.photoPath = photoPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String nome) {
        this.name = nome;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPhotoPath(){
        return photoPath;
    }

    public void setPhotoPath(String photoPath){
        this.photoPath = photoPath;
    }

}
