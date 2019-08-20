package com.bytecodecomp.npos.Data_Models;

/**
 * Created by limon on 21/02/2018.
 */

public class Customer_Model {

    // private variables
    public String customer_id;
    public String customer_name;
    public String customer_email;
    public String customer_credit;
    public String customer_phone_number;
    public String customer_update_date;
    public String customer_add_date;

    public Customer_Model() {
    }

    // constructor
    public Customer_Model(String customer_id, String customer_name, String customer_email, String customer_credit, String customer_phone_number, String customer_update_date, String customer_add_date) {
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.customer_email = customer_email;
        this.customer_credit = customer_credit;

        this.customer_phone_number = customer_phone_number;
        this.customer_update_date = customer_update_date;
        this.customer_add_date = customer_add_date;

    }


    public String getCustomer_id() {return this.customer_id;}
    public void setCustomer_id(String customer_id) {this.customer_id = customer_id;}

    public String getCustomer_name() {return this.customer_name;}
    public void setCustomer_name(String customer_name) {this.customer_name = customer_name;}

    public String getCustomer_email() {return this.customer_email;}
    public void setCustomer_email(String customer_email) {this.customer_email = customer_email;}

    public String getCustomer_credit() {return this.customer_credit;}
    public void setCustomer_credit(String customer_credit) {this.customer_credit = customer_credit;}

    public String getCustomer_phone_number() {return this.customer_phone_number;}
    public void setCustomer_phone_number(String customer_phone_number) {this.customer_phone_number = customer_phone_number;}

    public String getCustomer_update_date() {return this.customer_update_date;}
    public void setCustomer_update_date(String customer_update_date) {this.customer_update_date = customer_update_date;}

    public String getCustomer_add_date() {return this.customer_add_date;}
    public void setCustomer_add_date(String customer_add_date) {this.customer_add_date = customer_add_date;}

}
