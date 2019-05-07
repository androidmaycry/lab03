package com.mad.lib;

public final class Restaurateur{
    public String mail, name, addr, descr, phone, photoUri;

    public Restaurateur() {
        this.mail = "";
        this.name = "";
        this.addr = "";
        this.descr = "";
        this.phone = "";
        this.photoUri = "";
    }

    public Restaurateur(String mail, String name, String addr, String descr, String phone, String photoUri) {
        this.mail = mail;
        this.name = name;
        this.addr = addr;
        this.descr = descr;
        this.phone = phone;
        this.photoUri = photoUri;
    }

    public String getMail() {
        return mail;
    }

    public String getName() {
        return name;
    }

    public String getAddr() {
        return addr;
    }

    public String getDescr() {
        return descr;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhotoUri() {
        return photoUri;
    }
}