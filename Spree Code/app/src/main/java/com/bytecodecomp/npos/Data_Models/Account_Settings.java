package com.bytecodecomp.npos.Data_Models;

public class Account_Settings {

    public static String str_avatar, str_name, str_email, str_phone, str_role;

    public Account_Settings(String str_avatar, String str_name, String str_email, String str_phone, String str_role) {
        this.str_avatar = str_avatar;
        this.str_name = str_name;
        this.str_email = str_email;
        this.str_phone = str_phone;
        this.str_role = str_role;
    }

    public String getStr_avatar() {
        return str_avatar;
    }

    public void setStr_avatar(String str_avatar) {
        this.str_avatar = str_avatar;
    }

    public String getStr_name() {
        return str_name;
    }

    public void setStr_name(String str_name) {
        this.str_name = str_name;
    }

    public String getStr_email() {
        return str_email;
    }

    public void setStr_email(String str_email) {
        this.str_email = str_email;
    }

    public String getStr_phone() {
        return str_phone;
    }

    public void setStr_phone(String str_phone) {
        this.str_phone = str_phone;
    }

    public String getStr_role() {
        return str_role;
    }

    public void setStr_role(String str_role) {
        this.str_role = str_role;
    }
}
