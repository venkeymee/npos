package com.bytecodecomp.npos.Data_Models;

public class Device_Model {

    String device_name, device_id, device_serial, device_role;

    public Device_Model() {
    }

    public Device_Model(String device_name, String device_id, String device_serial, String device_role) {
        this.device_name = device_name;
        this.device_id = device_id;
        this.device_serial = device_serial;
        this.device_role = device_role;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDevice_serial() {
        return device_serial;
    }

    public void setDevice_serial(String device_serial) {
        this.device_serial = device_serial;
    }

    public String getDevice_role() {
        return device_role;
    }

    public void setDevice_role(String device_role) {
        this.device_role = device_role;
    }
}
