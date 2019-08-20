package com.bytecodecomp.npos.Data_Models;

/**
 * Created by limon on 21/02/2018.
 */

public class Supplier_Model {

    // private variables
    public String supplier_id;
    public String supplier_name;
    public String supplier_email;
    public String supplier_debit;
    public String supplier_business;
    public String supplier_phone_number;
    public String supplier_update_date;
    public String supplier_add_date;

    public Supplier_Model() {
    }

    // constructor
    public Supplier_Model(String supplier_id, String supplier_name, String supplier_email, String supplier_debit, String supplier_phone_number, String supplier_update_date, String supplier_add_date, String supplier_business) {
        this.supplier_id = supplier_id;
        this.supplier_name = supplier_name;
        this.supplier_email = supplier_email;
        this.supplier_debit = supplier_debit;
        this.supplier_business = supplier_business;
        this.supplier_phone_number = supplier_phone_number;
        this.supplier_update_date = supplier_update_date;
        this.supplier_add_date = supplier_add_date;

    }


    public String getSupplier_id() {return this.supplier_id;}
    public void setSupplier_id(String supplier_id) {this.supplier_id = supplier_id;}

    public String getSupplier_name() {return this.supplier_name;}
    public void setSupplier_name(String supplier_name) {this.supplier_name = supplier_name;}

    public String getSupplier_business() {return this.supplier_business;}
    public void setSupplier_business(String supplier_business) {this.supplier_business = supplier_business;}

    public String getSupplier_email() {return this.supplier_email;}
    public void setSupplier_email(String supplier_email) {this.supplier_email = supplier_email;}

    public String getSupplier_debit() {return this.supplier_debit;}
    public void setSupplier_debit(String supplier_debit) {this.supplier_debit = supplier_debit;}

    public String getSupplier_phone_number() {return this.supplier_phone_number;}
    public void setSupplier_phone_number(String supplier_phone_number) {this.supplier_phone_number = supplier_phone_number;}

    public String getSupplier_update_date() {return this.supplier_update_date;}
    public void setSupplier_update_date(String supplier_update_date) {this.supplier_update_date = supplier_update_date;}

    public String getSupplier_add_date() {return this.supplier_add_date;}
    public void setSupplier_add_date(String supplier_add_date) {this.supplier_add_date = supplier_add_date;}

}
