package com.mad.customer;

import java.util.ArrayList;

public class OrderItem {
    private String name;
    private String addr;
    private String cell;
    private String time;
    private String img;
    private ArrayList<String> order;

    public OrderItem(){

    }

    public OrderItem(String name, String addr, String cell, String time, String img, ArrayList<String> order) {
        this.name = name;
        this.addr = addr;
        this.cell = cell;
        this.time = time;
        this.img = img;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getOrder() {
        return order;
    }

    public void setOrder(ArrayList<String> order) {
        this.order = order;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
