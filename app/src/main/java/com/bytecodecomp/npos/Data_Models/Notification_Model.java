package com.bytecodecomp.npos.Data_Models;

public class Notification_Model {

    String title, message, add_date, users;

    public Notification_Model() {
    }

    public Notification_Model(String title, String message, String add_date, String users) {
        this.title = title;
        this.message = message;
        this.add_date = add_date;
        this.users = users;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAdd_date() {
        return add_date;
    }

    public void setAdd_date(String add_date) {
        this.add_date = add_date;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }
}
