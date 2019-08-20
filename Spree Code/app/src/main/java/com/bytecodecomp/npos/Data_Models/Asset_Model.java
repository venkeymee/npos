package com.bytecodecomp.npos.Data_Models;

/**
 * Created by limon on 21/02/2018.
 */

public class Asset_Model {

    public String asset_id;
    public String asset_name;
    public String asset_value;
    public String asset_type;
    public String asset_qnty;
    public String asset_purchase_Date;
    public String asset_update_date;
    public String asset_add_date;

    public Asset_Model() {
    }

    public Asset_Model(String asset_id, String asset_name, String asset_value, String asset_type, String asset_purchase_Date,String asset_qnty, String asset_update_date, String asset_add_date) {
        this.asset_id = asset_id;
        this.asset_name = asset_name;
        this.asset_value = asset_value;
        this.asset_type = asset_type;

        this.asset_qnty = asset_qnty;
        this.asset_purchase_Date = asset_purchase_Date;
        this.asset_update_date = asset_update_date;
        this.asset_add_date = asset_add_date;

    }


    public String getAsset_id() {return this.asset_id;}
    public void setAsset_id(String asset_id) {this.asset_id = asset_id;}

    public String getAsset_name() {return this.asset_name;}
    public void setAsset_name(String asset_name) {this.asset_name = asset_name;}

    public String getAsset_value() {return this.asset_value;}
    public void setAsset_value(String asset_value) {this.asset_value = asset_value;}

    public String getAsset_qnty() {return this.asset_qnty;}
    public void setAsset_qnty(String asset_qnty) {this.asset_qnty = asset_qnty;}

    public String getAsset_purchase_Date() {return this.asset_purchase_Date;}
    public void setAsset_purchase_Date(String asset_purchase_Date) {this.asset_purchase_Date = asset_purchase_Date;}

    public String getAsset_type() {return this.asset_type;}
    public void setAsset_type(String asset_type) {this.asset_type = asset_type;}

    public String getAsset_update_date() {return this.asset_update_date;}
    public void setAsset_update_date(String asset_update_date) {this.asset_update_date = asset_update_date;}

    public String getAsset_add_date() {return this.asset_add_date;}
    public void setAsset_add_date(String asset_add_date) {this.asset_add_date = asset_add_date;}

}
