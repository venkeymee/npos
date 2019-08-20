package com.bytecodecomp.npos.Data_Models;

/**
 * Created by limon on 21/02/2018.
 */

public class Expense_Type_Model {

    // private variables
    public String expenseType_id;
    public String expenseType_name;
    public String expenseType_update_date;
    public String expenseType_add_date;

    public Expense_Type_Model() {
    }

    // constructor
    public Expense_Type_Model(String expenseType_id, String expenseType_name, String expenseType_update_date, String expenseType_add_date) {
        this.expenseType_id = expenseType_id;
        this.expenseType_name = expenseType_name;
        this.expenseType_update_date = expenseType_update_date;
        this.expenseType_add_date = expenseType_add_date;

    }


    public String getExpenseType_id() {return this.expenseType_id;}
    public void setExpenseType_id(String expenseType_id) {this.expenseType_id = expenseType_id;}

    public String getExpenseType_name() {return this.expenseType_name;}
    public void setExpenseType_name(String expenseType_name) {this.expenseType_name = expenseType_name;}

    public String getExpenseType_update_date() {return this.expenseType_update_date;}
    public void setExpenseType_update_date(String expenseType_update_date) {this.expenseType_update_date = expenseType_update_date;}

    public String getExpenseType_add_date() {return this.expenseType_add_date;}
    public void setExpenseType_add_date(String expenseType_add_date) {this.expenseType_add_date = expenseType_add_date;}

}
