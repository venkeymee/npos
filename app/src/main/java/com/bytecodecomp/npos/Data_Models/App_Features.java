package com.bytecodecomp.npos.Data_Models;

public class App_Features {

    //Module Inventory
    public  boolean store_inventory = false;
    public  int store_inventory_limit;


    //Module Purchase
    public  boolean store_purchase = false;
    public  int store_purchase_daily_limit;


    //Module Sales
    public  boolean store_sales = false;
    public  int store_sales_daily_limit;


    //Module Customers
    public  boolean store_customer = false;
    public  int store_customer_limit;


    //Module Suppliers
    public  boolean store_supplier = false;
    public  int store_supplier_limit;


    //Module Staff
    public  boolean store_staff = false;
    public  int store_staff_limit;


    //Module Assets
    public  boolean store_assets = false;
    public  int store_assets_limit;


    //Module Exp Type
    public  boolean store_exp_type = false;
    public  int store_exp_type_limit;


    //Module Exp
    public  boolean store_exp = false;
    public  int store_exp_limit;


    public App_Features() {
    }


    public App_Features(boolean store_inventory, int store_inventory_limit, boolean store_purchase, int store_purchase_daily_limit, boolean store_sales, int store_sales_daily_limit, boolean store_customer, int store_customer_limit, boolean store_supplier, int store_supplier_limit, boolean store_staff, int store_staff_limit, boolean store_assets, int store_assets_limit, boolean store_exp_type, int store_exp_type_limit, boolean store_exp, int store_exp_limit) {
        this.store_inventory = store_inventory;
        this.store_inventory_limit = store_inventory_limit;
        this.store_purchase = store_purchase;
        this.store_purchase_daily_limit = store_purchase_daily_limit;
        this.store_sales = store_sales;
        this.store_sales_daily_limit = store_sales_daily_limit;
        this.store_customer = store_customer;
        this.store_customer_limit = store_customer_limit;
        this.store_supplier = store_supplier;
        this.store_supplier_limit = store_supplier_limit;
        this.store_staff = store_staff;
        this.store_staff_limit = store_staff_limit;
        this.store_assets = store_assets;
        this.store_assets_limit = store_assets_limit;
        this.store_exp_type = store_exp_type;
        this.store_exp_type_limit = store_exp_type_limit;
        this.store_exp = store_exp;
        this.store_exp_limit = store_exp_limit;
    }


    public boolean isStore_inventory() {
        return store_inventory;
    }

    public void setStore_inventory(boolean store_inventory) {
        this.store_inventory = store_inventory;
    }

    public int getStore_inventory_limit() {
        return store_inventory_limit;
    }

    public void setStore_inventory_limit(int store_inventory_limit) {
        this.store_inventory_limit = store_inventory_limit;
    }

    public boolean isStore_purchase() {
        return store_purchase;
    }

    public void setStore_purchase(boolean store_purchase) {
        this.store_purchase = store_purchase;
    }

    public int getStore_purchase_daily_limit() {
        return store_purchase_daily_limit;
    }

    public void setStore_purchase_daily_limit(int store_purchase_daily_limit) {
        this.store_purchase_daily_limit = store_purchase_daily_limit;
    }

    public boolean isStore_sales() {
        return store_sales;
    }

    public void setStore_sales(boolean store_sales) {
        this.store_sales = store_sales;
    }

    public int getStore_sales_daily_limit() {
        return store_sales_daily_limit;
    }

    public void setStore_sales_daily_limit(int store_sales_daily_limit) {
        this.store_sales_daily_limit = store_sales_daily_limit;
    }

    public boolean isStore_customer() {
        return store_customer;
    }

    public void setStore_customer(boolean store_customer) {
        this.store_customer = store_customer;
    }

    public int getStore_customer_limit() {
        return store_customer_limit;
    }

    public void setStore_customer_limit(int store_customer_limit) {
        this.store_customer_limit = store_customer_limit;
    }

    public boolean isStore_supplier() {
        return store_supplier;
    }

    public void setStore_supplier(boolean store_supplier) {
        this.store_supplier = store_supplier;
    }

    public int getStore_supplier_limit() {
        return store_supplier_limit;
    }

    public void setStore_supplier_limit(int store_supplier_limit) {
        this.store_supplier_limit = store_supplier_limit;
    }

    public boolean isStore_staff() {
        return store_staff;
    }

    public void setStore_staff(boolean store_staff) {
        this.store_staff = store_staff;
    }

    public int getStore_staff_limit() {
        return store_staff_limit;
    }

    public void setStore_staff_limit(int store_staff_limit) {
        this.store_staff_limit = store_staff_limit;
    }

    public boolean isStore_assets() {
        return store_assets;
    }

    public void setStore_assets(boolean store_assets) {
        this.store_assets = store_assets;
    }

    public int getStore_assets_limit() {
        return store_assets_limit;
    }

    public void setStore_assets_limit(int store_assets_limit) {
        this.store_assets_limit = store_assets_limit;
    }

    public boolean isStore_exp_type() {
        return store_exp_type;
    }

    public void setStore_exp_type(boolean store_exp_type) {
        this.store_exp_type = store_exp_type;
    }

    public int getStore_exp_type_limit() {
        return store_exp_type_limit;
    }

    public void setStore_exp_type_limit(int store_exp_type_limit) {
        this.store_exp_type_limit = store_exp_type_limit;
    }

    public boolean isStore_exp() {
        return store_exp;
    }

    public void setStore_exp(boolean store_exp) {
        this.store_exp = store_exp;
    }

    public int getStore_exp_limit() {
        return store_exp_limit;
    }

    public void setStore_exp_limit(int store_exp_limit) {
        this.store_exp_limit = store_exp_limit;
    }
}
