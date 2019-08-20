package com.bytecodecomp.npos.Data_Models;

/**
 * Created by limon on 21/02/2018.
 */

public class Product_Cart_Details {

    // private variables
    public String product_id;
    public String product_name;
    public String product_value;
    public String product_units;

    public String product_add_date;
    public String product_update_date;
    public String product_gtin;
    public String product_inventory;
    public String product_vat;

    public Product_Cart_Details() {
    }

    // constructor
    public Product_Cart_Details(String product_id, String product_name, String product_value, String product_units, String product_add_date, String product_update_date, String product_gtin, String product_inventory, String product_vat) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_value = product_value;
        this.product_units = product_units;
        this.product_add_date = product_add_date;
        this.product_update_date = product_update_date;
        this.product_gtin = product_gtin;
        this.product_inventory = product_inventory;
        this.product_vat = product_vat;

    }

    public String getProduct_id() {return this.product_id;}
    public void setProduct_id(String product_id) {this.product_id = product_id;}

    public String getProduct_name() {return this.product_name;}
    public void setProduct_name(String product_name) {this.product_name = product_name;}

    public String getProduct_value() {return this.product_value;}
    public void setProduct_value(String product_value) {this.product_value = product_value;}

    public String getProduct_units() {return this.product_units;}
    public void setProduct_units(String product_units) {this.product_units = product_units;}

    public String getProduct_add_date() {return this.product_add_date;}
    public void setProduct_add_date(String product_add_date) {this.product_add_date = product_add_date;}

    public String getProduct_update_date() {return this.product_update_date;}
    public void setProduct_update_date(String product_update_date) {this.product_update_date = product_update_date;}

    public String getProduct_gtin() {return this.product_gtin;}
    public void setProduct_gtin(String product_gtin) {this.product_gtin = product_gtin;}

    public String getProduct_inventory() {return this.product_inventory;}
    public void setProduct_inventory(String product_inventory) {this.product_inventory = product_inventory;}

    public String getProduct_vat() {
        return product_vat;
    }

    public void setProduct_vat(String product_vat) {
        this.product_vat = product_vat;
    }
}
