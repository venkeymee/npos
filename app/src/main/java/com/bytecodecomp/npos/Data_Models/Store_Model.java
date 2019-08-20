package com.bytecodecomp.npos.Data_Models;

public class Store_Model {

    public static String store_name = "", store_address, store_location, store_print, store_contacts, package_id, package_expiry, package_name, store_status;

    public Store_Model() {
    }

    public Store_Model(String store_name, String store_address, String store_location, String store_print, String store_contacts, String package_id, String package_expiry, String package_name, String store_status) {
        this.store_name = store_name;
        this.store_address = store_address;
        this.store_location = store_location;
        this.store_print = store_print;
        this.store_contacts = store_contacts;
        this.package_id = package_id;
        this.package_expiry = package_expiry;
        this.package_name = package_name;
        this.store_status = store_status;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_address() {
        return store_address;
    }

    public void setStore_address(String store_address) {
        this.store_address = store_address;
    }

    public String getStore_location() {
        return store_location;
    }

    public void setStore_location(String store_location) {
        this.store_location = store_location;
    }

    public String getStore_print() {
        return store_print;
    }

    public void setStore_print(String store_print) {
        this.store_print = store_print;
    }

    public String getStore_contacts() {
        return store_contacts;
    }

    public void setStore_contacts(String store_contacts) {
        this.store_contacts = store_contacts;
    }

    public  String getPackage_id() {
        return package_id;
    }

    public  void setPackage_id(String package_id) {
        this.package_id = package_id;
    }

    public  String getPackage_expiry() {
        return package_expiry;
    }

    public  void setPackage_expiry(String package_expiry) {
        this.package_expiry = package_expiry;
    }

    public  String getPackage_name() {
        return package_name;
    }

    public  void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public  String getStore_status() {
        return store_status;
    }

    public  void setStore_status(String store_status) {
        this.store_status = store_status;
    }
}
