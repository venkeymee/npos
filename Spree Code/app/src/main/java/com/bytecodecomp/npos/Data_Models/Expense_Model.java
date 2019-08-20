package com.bytecodecomp.npos.Data_Models;

/**
 * Created by limon on 21/02/2018.
 */

public class Expense_Model {

    public String expense_id;
    public String expense_name;
    public String expense_value;
    public String expense_type;
    public String expense_Date;
    public String expense_for;
    public String expense_update_date;
    public String expense_add_date;

    public Expense_Model() {
    }

    public Expense_Model(String expense_id, String expense_name, String expense_value, String expense_type, String expense_Date,String expense_for, String expense_update_date, String expense_add_date) {
        this.expense_id = expense_id;
        this.expense_name = expense_name;
        this.expense_value = expense_value;
        this.expense_type = expense_type;

        this.expense_for= expense_for;
        this.expense_Date = expense_Date;
        this.expense_update_date = expense_update_date;
        this.expense_add_date = expense_add_date;

    }


    public String getExpense_id() {return this.expense_id;}
    public void setExpense_id(String expense_id) {this.expense_id = expense_id;}

    public String getExpense_name() {return this.expense_name;}
    public void setExpense_name(String expense_name) {this.expense_name = expense_name;}

    public String getExpense_value() {return this.expense_value;}
    public void setExpense_value(String expense_value) {this.expense_value = expense_value;}

    public String getExpense_purchase_Date() {return this.expense_Date;}
    public void setExpense_purchase_Date(String expense_Date) {this.expense_Date = expense_Date;}

    public String getExpenset_for() {return this.expense_for;}
    public void setExpenset_for(String expense_for) {this.expense_for = expense_for;}

    public String getExpense_type() {return this.expense_type;}
    public void setExpense_type(String expense_type) {this.expense_type = expense_type;}

    public String getExpense_update_date() {return this.expense_update_date;}
    public void setExpense_update_date(String expense_update_date) {this.expense_update_date = expense_update_date;}

    public String getExpense_add_date() {return this.expense_add_date;}
    public void setExpense_add_date(String expense_add_date) {this.expense_add_date = expense_add_date;}

}
