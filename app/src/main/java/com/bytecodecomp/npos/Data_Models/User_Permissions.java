package com.bytecodecomp.npos.Data_Models;

public class User_Permissions {
    
    //Authentication Perms
    public    boolean user_login = false;

    public    boolean user_change_password = false;

    public    boolean user_change_email = false;

    public    boolean user_change_profile = false;
    
    
    //Reports
    public    boolean user_access_reports = false;

    public    boolean user_dashboard = false;
    
    
    //Manage Inventory
    public    boolean user_access_inventory = false;

    public    boolean user_add_new_product = false;

    public    boolean user_import_product = false;

    public    boolean user_export_product = false;

    public    boolean user_edit_product = false;

    public    boolean user_print_label = false;

    public    boolean user_restock = false;
    
    
    //Manage Purchases
    public    boolean user_access_purchases = false;

    public    boolean user_create_purchases = false;
    
    
    //Manage Sales
    public    boolean user_access_sales = false;

    public    boolean user_view_sales = false;

    public    boolean user_make_sales = false;

    public    boolean user_access_cart = false;
    
    
    //Checkout
    public    boolean user_cash_checkout = false;

    public    boolean user_paypal_checkout = false;

    public    boolean user_credit_checkout = false;

    public    boolean user_bank_checkout = false;
    
    
    //Manage Customers
    public    boolean user_access_customers = false;

    public    boolean user_view_customers = false;

    public    boolean user_create_customers = false;

    public    boolean user_edit_customers = false;
    
    
    //Manage Staff
    public    boolean user_access_staff = false;

    public    boolean user_view_staff = false;

    public    boolean user_create_staff = false;

    public    boolean user_set_staff_password = false;
    
    
    //Manage Assets
    public    boolean user_access_assets = false;

    public    boolean user_view_assets = false;

    public    boolean user_create_assets = false;

    public    boolean user_edit_assets = false;
    
    
    //Manage Expense Type
    public    boolean user_access_exp_type = false;

    public    boolean user_view_exp_type = false;

    public    boolean user_add_exp_type = false;
    
    
    //Manage Expense
    public    boolean user_access_exp = false;

    public    boolean user_create_exp = false;


    public User_Permissions() {
    }

    public User_Permissions(boolean user_login, boolean user_change_password, boolean user_change_email, boolean user_change_profile, boolean user_access_reports, boolean user_dashboard, boolean user_access_inventory, boolean user_add_new_product, boolean user_import_product, boolean user_export_product, boolean user_edit_product, boolean user_print_label, boolean user_restock, boolean user_access_purchases, boolean user_create_purchases, boolean user_access_sales, boolean user_view_sales, boolean user_make_sales, boolean user_access_cart, boolean user_cash_checkout, boolean user_paypal_checkout, boolean user_credit_checkout, boolean user_bank_checkout, boolean user_access_customers, boolean user_view_customers, boolean user_create_customers, boolean user_edit_customers, boolean user_access_staff, boolean user_view_staff, boolean user_create_staff, boolean user_set_staff_password, boolean user_access_assets, boolean user_view_assets, boolean user_create_assets, boolean user_edit_assets, boolean user_access_exp_type, boolean user_view_exp_type, boolean user_add_exp_type, boolean user_access_exp, boolean user_create_exp) {
        this.user_login = user_login;
        this.user_change_password = user_change_password;
        this.user_change_email = user_change_email;
        this.user_change_profile = user_change_profile;
        this.user_access_reports = user_access_reports;
        this.user_dashboard = user_dashboard;
        this.user_access_inventory = user_access_inventory;
        this.user_add_new_product = user_add_new_product;
        this.user_import_product = user_import_product;
        this.user_export_product = user_export_product;
        this.user_edit_product = user_edit_product;
        this.user_print_label = user_print_label;
        this.user_restock = user_restock;
        this.user_access_purchases = user_access_purchases;
        this.user_create_purchases = user_create_purchases;
        this.user_access_sales = user_access_sales;
        this.user_view_sales = user_view_sales;
        this.user_make_sales = user_make_sales;
        this.user_access_cart = user_access_cart;
        this.user_cash_checkout = user_cash_checkout;
        this.user_paypal_checkout = user_paypal_checkout;
        this.user_credit_checkout = user_credit_checkout;
        this.user_bank_checkout = user_bank_checkout;
        this.user_access_customers = user_access_customers;
        this.user_view_customers = user_view_customers;
        this.user_create_customers = user_create_customers;
        this.user_edit_customers = user_edit_customers;
        this.user_access_staff = user_access_staff;
        this.user_view_staff = user_view_staff;
        this.user_create_staff = user_create_staff;
        this.user_set_staff_password = user_set_staff_password;
        this.user_access_assets = user_access_assets;
        this.user_view_assets = user_view_assets;
        this.user_create_assets = user_create_assets;
        this.user_edit_assets = user_edit_assets;
        this.user_access_exp_type = user_access_exp_type;
        this.user_view_exp_type = user_view_exp_type;
        this.user_add_exp_type = user_add_exp_type;
        this.user_access_exp = user_access_exp;
        this.user_create_exp = user_create_exp;
    }


    public boolean isUser_login() {
        return user_login;
    }

    public void setUser_login(boolean user_login) {
        this.user_login = user_login;
    }

    public boolean isUser_change_password() {
        return user_change_password;
    }

    public void setUser_change_password(boolean user_change_password) {
        this.user_change_password = user_change_password;
    }

    public boolean isUser_change_email() {
        return user_change_email;
    }

    public void setUser_change_email(boolean user_change_email) {
        this.user_change_email = user_change_email;
    }

    public boolean isUser_change_profile() {
        return user_change_profile;
    }

    public void setUser_change_profile(boolean user_change_profile) {
        this.user_change_profile = user_change_profile;
    }

    public boolean isUser_access_reports() {
        return user_access_reports;
    }

    public void setUser_access_reports(boolean user_access_reports) {
        this.user_access_reports = user_access_reports;
    }

    public boolean isUser_dashboard() {
        return user_dashboard;
    }

    public void setUser_dashboard(boolean user_dashboard) {
        this.user_dashboard = user_dashboard;
    }

    public boolean isUser_access_inventory() {
        return user_access_inventory;
    }

    public void setUser_access_inventory(boolean user_access_inventory) {
        this.user_access_inventory = user_access_inventory;
    }

    public boolean isUser_add_new_product() {
        return user_add_new_product;
    }

    public void setUser_add_new_product(boolean user_add_new_product) {
        this.user_add_new_product = user_add_new_product;
    }

    public boolean isUser_import_product() {
        return user_import_product;
    }

    public void setUser_import_product(boolean user_import_product) {
        this.user_import_product = user_import_product;
    }

    public boolean isUser_export_product() {
        return user_export_product;
    }

    public void setUser_export_product(boolean user_export_product) {
        this.user_export_product = user_export_product;
    }

    public boolean isUser_edit_product() {
        return user_edit_product;
    }

    public void setUser_edit_product(boolean user_edit_product) {
        this.user_edit_product = user_edit_product;
    }

    public boolean isUser_print_label() {
        return user_print_label;
    }

    public void setUser_print_label(boolean user_print_label) {
        this.user_print_label = user_print_label;
    }

    public boolean isUser_restock() {
        return user_restock;
    }

    public void setUser_restock(boolean user_restock) {
        this.user_restock = user_restock;
    }

    public boolean isUser_access_purchases() {
        return user_access_purchases;
    }

    public void setUser_access_purchases(boolean user_access_purchases) {
        this.user_access_purchases = user_access_purchases;
    }

    public boolean isUser_create_purchases() {
        return user_create_purchases;
    }

    public void setUser_create_purchases(boolean user_create_purchases) {
        this.user_create_purchases = user_create_purchases;
    }

    public boolean isUser_access_sales() {
        return user_access_sales;
    }

    public void setUser_access_sales(boolean user_access_sales) {
        this.user_access_sales = user_access_sales;
    }

    public boolean isUser_view_sales() {
        return user_view_sales;
    }

    public void setUser_view_sales(boolean user_view_sales) {
        this.user_view_sales = user_view_sales;
    }

    public boolean isUser_make_sales() {
        return user_make_sales;
    }

    public void setUser_make_sales(boolean user_make_sales) {
        this.user_make_sales = user_make_sales;
    }

    public boolean isUser_access_cart() {
        return user_access_cart;
    }

    public void setUser_access_cart(boolean user_access_cart) {
        this.user_access_cart = user_access_cart;
    }

    public boolean isUser_cash_checkout() {
        return user_cash_checkout;
    }

    public void setUser_cash_checkout(boolean user_cash_checkout) {
        this.user_cash_checkout = user_cash_checkout;
    }

    public boolean isUser_paypal_checkout() {
        return user_paypal_checkout;
    }

    public void setUser_paypal_checkout(boolean user_paypal_checkout) {
        this.user_paypal_checkout = user_paypal_checkout;
    }

    public boolean isUser_credit_checkout() {
        return user_credit_checkout;
    }

    public void setUser_credit_checkout(boolean user_credit_checkout) {
        this.user_credit_checkout = user_credit_checkout;
    }

    public boolean isUser_bank_checkout() {
        return user_bank_checkout;
    }

    public void setUser_bank_checkout(boolean user_bank_checkout) {
        this.user_bank_checkout = user_bank_checkout;
    }

    public boolean isUser_access_customers() {
        return user_access_customers;
    }

    public void setUser_access_customers(boolean user_access_customers) {
        this.user_access_customers = user_access_customers;
    }

    public boolean isUser_view_customers() {
        return user_view_customers;
    }

    public void setUser_view_customers(boolean user_view_customers) {
        this.user_view_customers = user_view_customers;
    }

    public boolean isUser_create_customers() {
        return user_create_customers;
    }

    public void setUser_create_customers(boolean user_create_customers) {
        this.user_create_customers = user_create_customers;
    }

    public boolean isUser_edit_customers() {
        return user_edit_customers;
    }

    public void setUser_edit_customers(boolean user_edit_customers) {
        this.user_edit_customers = user_edit_customers;
    }

    public boolean isUser_access_staff() {
        return user_access_staff;
    }

    public void setUser_access_staff(boolean user_access_staff) {
        this.user_access_staff = user_access_staff;
    }

    public boolean isUser_view_staff() {
        return user_view_staff;
    }

    public void setUser_view_staff(boolean user_view_staff) {
        this.user_view_staff = user_view_staff;
    }

    public boolean isUser_create_staff() {
        return user_create_staff;
    }

    public void setUser_create_staff(boolean user_create_staff) {
        this.user_create_staff = user_create_staff;
    }

    public boolean isUser_set_staff_password() {
        return user_set_staff_password;
    }

    public void setUser_set_staff_password(boolean user_set_staff_password) {
        this.user_set_staff_password = user_set_staff_password;
    }

    public boolean isUser_access_assets() {
        return user_access_assets;
    }

    public void setUser_access_assets(boolean user_access_assets) {
        this.user_access_assets = user_access_assets;
    }

    public boolean isUser_view_assets() {
        return user_view_assets;
    }

    public void setUser_view_assets(boolean user_view_assets) {
        this.user_view_assets = user_view_assets;
    }

    public boolean isUser_create_assets() {
        return user_create_assets;
    }

    public void setUser_create_assets(boolean user_create_assets) {
        this.user_create_assets = user_create_assets;
    }

    public boolean isUser_edit_assets() {
        return user_edit_assets;
    }

    public void setUser_edit_assets(boolean user_edit_assets) {
        this.user_edit_assets = user_edit_assets;
    }

    public boolean isUser_access_exp_type() {
        return user_access_exp_type;
    }

    public void setUser_access_exp_type(boolean user_access_exp_type) {
        this.user_access_exp_type = user_access_exp_type;
    }

    public boolean isUser_view_exp_type() {
        return user_view_exp_type;
    }

    public void setUser_view_exp_type(boolean user_view_exp_type) {
        this.user_view_exp_type = user_view_exp_type;
    }

    public boolean isUser_add_exp_type() {
        return user_add_exp_type;
    }

    public void setUser_add_exp_type(boolean user_add_exp_type) {
        this.user_add_exp_type = user_add_exp_type;
    }

    public boolean isUser_access_exp() {
        return user_access_exp;
    }

    public void setUser_access_exp(boolean user_access_exp) {
        this.user_access_exp = user_access_exp;
    }

    public boolean isUser_create_exp() {
        return user_create_exp;
    }

    public void setUser_create_exp(boolean user_create_exp) {
        this.user_create_exp = user_create_exp;
    }
}
