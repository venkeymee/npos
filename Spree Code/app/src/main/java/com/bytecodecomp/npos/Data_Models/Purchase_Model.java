package com.bytecodecomp.npos.Data_Models;

public class Purchase_Model {

    String purchase_id;
    String purchase_qnty;
    String purchase_date;
    String purchase_item;
    String purchase_buying_price;
    String purchase_selling_price;
    String purchase_status;
    String purchase_supplier;

    public Purchase_Model() {
    }

    public Purchase_Model(String purchase_id, String purchase_qnty, String purchase_date, String purchase_item, String purchase_buying_price, String purchase_selling_price, String purchase_status, String purchase_supplier) {
        this.purchase_id = purchase_id;
        this.purchase_qnty = purchase_qnty;
        this.purchase_date = purchase_date;
        this.purchase_item = purchase_item;
        this.purchase_buying_price = purchase_buying_price;
        this.purchase_selling_price = purchase_selling_price;
        this.purchase_status = purchase_status;
        this.purchase_supplier = purchase_supplier;
    }


    public String getPurchase_id() {
        return purchase_id;
    }

    public void setPurchase_id(String purchase_id) {
        this.purchase_id = purchase_id;
    }

    public String getPurchase_qnty() {
        return purchase_qnty;
    }

    public void setPurchase_qnty(String purchase_name) {
        this.purchase_qnty = purchase_name;
    }

    public String getPurchase_date() {
        return purchase_date;
    }

    public void setPurchase_date(String purchase_date) {
        this.purchase_date = purchase_date;
    }

    public String getPurchase_item() {
        return purchase_item;
    }

    public void setPurchase_item(String purchase_item) {
        this.purchase_item = purchase_item;
    }

    public String getPurchase_buying_price() {
        return purchase_buying_price;
    }

    public void setPurchase_buying_price(String purchase_buying_price) {
        this.purchase_buying_price = purchase_buying_price;
    }

    public String getPurchase_selling_price() {
        return purchase_selling_price;
    }

    public void setPurchase_selling_price(String purchase_selling_price) {
        this.purchase_selling_price = purchase_selling_price;
    }

    public String getPurchase_status() {
        return purchase_status;
    }

    public void setPurchase_status(String purchase_status) {
        this.purchase_status = purchase_status;
    }


    public String getPurchase_supplier() {
        return purchase_supplier;
    }

    public void setPurchase_supplier(String purchase_supplier) {
        this.purchase_supplier = purchase_supplier;
    }

}
