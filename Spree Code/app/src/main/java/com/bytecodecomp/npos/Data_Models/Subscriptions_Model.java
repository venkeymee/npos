package com.bytecodecomp.npos.Data_Models;

public class Subscriptions_Model {

    public String subscription_id, subscription_date, package_name, package_id, subscription_price, subscription_business_name, subscription_business_id, subscription_expiry, subscription_transaction_id, subscription_payment;

    public Subscriptions_Model(String subscription_id, String subscription_date, String package_name, String package_id, String subscription_price, String subscription_business_name, String subscription_business_id, String subscription_expiry, String subscription_transaction_id, String subscription_payment) {
        this.subscription_id = subscription_id;
        this.subscription_date = subscription_date;
        this.package_name = package_name;
        this.package_id = package_id;
        this.subscription_price = subscription_price;
        this.subscription_business_name = subscription_business_name;
        this.subscription_business_id = subscription_business_id;
        this.subscription_expiry = subscription_expiry;
        this.subscription_transaction_id = subscription_transaction_id;
        this.subscription_payment = subscription_payment;
    }


    public Subscriptions_Model() {
    }

    public String getSubscription_id() {
        return subscription_id;
    }

    public void setSubscription_id(String subscription_id) {
        this.subscription_id = subscription_id;
    }

    public String getSubscription_date() {
        return subscription_date;
    }

    public void setSubscription_date(String subscription_date) {
        this.subscription_date = subscription_date;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getPackage_id() {
        return package_id;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
    }

    public String getSubscription_price() {
        return subscription_price;
    }

    public void setSubscription_price(String subscription_price) {
        this.subscription_price = subscription_price;
    }

    public String getSubscription_business_name() {
        return subscription_business_name;
    }

    public void setSubscription_business_name(String subscription_business_name) {
        this.subscription_business_name = subscription_business_name;
    }

    public String getSubscription_business_id() {
        return subscription_business_id;
    }

    public void setSubscription_business_id(String subscription_business_id) {
        this.subscription_business_id = subscription_business_id;
    }

    public String getSubscription_expiry() {
        return subscription_expiry;
    }

    public void setSubscription_expiry(String subscription_expiry) {
        this.subscription_expiry = subscription_expiry;
    }

    public String getSubscription_transaction_id() {
        return subscription_transaction_id;
    }

    public void setSubscription_transaction_id(String subscription_transaction_id) {
        this.subscription_transaction_id = subscription_transaction_id;
    }

    public String getSubscription_payment() {
        return subscription_payment;
    }

    public void setSubscription_payment(String subscription_payment) {
        this.subscription_payment = subscription_payment;
    }
}
