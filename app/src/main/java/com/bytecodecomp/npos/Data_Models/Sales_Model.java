package com.bytecodecomp.npos.Data_Models;

public class Sales_Model {


    public String payment_date;
    public String payment_id;
    public String payment_money_id;
    public String payment_method;
    public String items;
    public String user_id;
    public String user_name;
    public String payment_amount;
    public String payment_user_staff;
    public String customer_name;
    public String discount_amount;


    public Sales_Model() {
    }

    public Sales_Model(String payment_date, String payment_id, String payment_money_id, String payment_method, String items, String user_id, String user_name, String payment_amount, String payment_user_staff, String customer_name, String discount_amount) {

        this.payment_date = payment_date;
        this.payment_id = payment_id;
        this.payment_money_id = payment_money_id;
        this.payment_method = payment_method;
        this.items = items;
        this.user_id = user_id;
        this.user_name = user_name;
        this.payment_amount = payment_amount;
        this.payment_user_staff = payment_user_staff;
        this.customer_name = customer_name;
        this.discount_amount = discount_amount;

    }


    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getPayment_money_id() {
        return payment_money_id;
    }

    public void setPayment_money_id(String payment_money_id) {
        this.payment_money_id = payment_money_id;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPayment_amount() {
        return payment_amount;
    }

    public void setPayment_amount(String payment_amount) {
        this.payment_amount = payment_amount;
    }

    public String getPayment_user_staff() {
        return payment_user_staff;
    }

    public void setPayment_user_staff(String payment_user_staff) {
        this.payment_user_staff = payment_user_staff;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(String discount_amount) {
        this.discount_amount = discount_amount;
    }

}
