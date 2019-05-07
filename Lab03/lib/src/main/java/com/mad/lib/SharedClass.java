package com.mad.lib;

public final class SharedClass {
    /**
     * Key for onSaveInstanceState() and onRestoreInstanceState()
     */
    public static final String Name = "keyName";
    public static final String Password= "keyPassword";
    public static final String Description = "keyDescription";
    public static final String Address = "keyAddress";
    public static final String Mail = "keyMail";
    public static final String Price = "keyEuroPrice";
    public static final String Photo ="keyPhoto";
    public static final String Phone ="keyPhone";
    public static final String Quantity = "keyQuantity";
    public static final String CameraOpen = "keyCameraDialog";
    public static final String PriceOpen = "keyPriceDialog";
    public static final String QuantOpen = "keyQuantityDialog";

    /**
     * Useful values key to retrieve data from activity (Intent)
     */
    public static final String EDIT_EXISTING_DISH = "DISH_NAME";

    /**
     * Permission values
     */
    public static final int PERMISSION_GALLERY_REQUEST = 1;

    /**
     * Firebase paths
     */
    public static String ROOT_UID = "";
    public static final String DISHES_PATH = "dishes/";
    public static final String RESERVATION_PATH = "reservation/";
    public static final String ACCEPTED_ORDER_PATH = "order/";
    public static final String RESTAURATEUR_INFO = "restaurants/";
}