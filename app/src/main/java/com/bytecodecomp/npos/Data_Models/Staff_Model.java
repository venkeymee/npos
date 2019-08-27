package com.bytecodecomp.npos.Data_Models;

/**
 * Created by limon on 21/02/2018.
 */

public class Staff_Model {

    // private variables
    public String staff_id;
    public String staff_name;
    public String staff_email;
    public String staff_phone;
    public String staff_update_date;
    public String staff_add_date;
    public String staff_device_id;
    public String staff_profile_photo;
    public String staff_docs;
    public String staff_password;
    public String staff_commision;

    public Staff_Model() {
    }


    public Staff_Model(String staff_id, String staff_name, String staff_email, String staff_phone, String staff_update_date, String staff_add_date, String staff_device_id, String staff_profile_photo, String staff_docs, String staff_password, String staff_commision) {
        this.staff_id = staff_id;
        this.staff_name = staff_name;
        this.staff_email = staff_email;
        this.staff_phone = staff_phone;
        this.staff_update_date = staff_update_date;
        this.staff_add_date = staff_add_date;
        this.staff_device_id = staff_device_id;
        this.staff_profile_photo = staff_profile_photo;
        this.staff_docs = staff_docs;
        this.staff_password = staff_password;
        this.staff_commision = staff_commision;
    }


    public String getStaff_id() {return this.staff_id;}
    public void setStaff_id(String staff_id) {this.staff_id = staff_id;}

    public String getStaff_email() {return this.staff_email;}
    public void setStaff_email(String staff_email) {this.staff_email = staff_email;}

    public String getStaff_name() {return this.staff_name;}
    public void setStaff_name(String staff_name) {this.staff_name = staff_name;}

    public String getStaff_phone() {return this.staff_phone;}
    public void setStaff_phone(String staff_phone) {this.staff_phone = staff_phone;}

    public String getStaff_update_date() {return this.staff_update_date;}
    public void setStaff_update_date(String staff_update_date) {this.staff_update_date = staff_update_date;}

    public String getStaff_add_date() {return this.staff_add_date;}
    public void setStaff_add_date(String staff_add_date) {this.staff_add_date = staff_add_date;}

    public String getStaff_device_id() {
        return staff_device_id;
    }

    public void setStaff_device_id(String staff_device_id) {
        this.staff_device_id = staff_device_id;
    }

    public String getStaff_profile_photo() {
        return staff_profile_photo;
    }

    public void setStaff_profile_photo(String staff_profile_photo) {
        this.staff_profile_photo = staff_profile_photo;
    }

    public String getStaff_docs() {
        return staff_docs;
    }

    public void setStaff_docs(String staff_docs) {
        this.staff_docs = staff_docs;
    }

    public String getStaff_password() {
        return staff_password;
    }

    public void setStaff_password(String staff_password) {
        this.staff_password = staff_password;
    }

    public String getStaff_commision() {
        return staff_commision;
    }

    public void setStaff_commision(String staff_commision) {
        this.staff_commision = staff_commision;
    }
}
