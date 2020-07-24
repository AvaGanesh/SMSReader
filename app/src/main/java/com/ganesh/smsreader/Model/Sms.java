package com.ganesh.smsreader.Model;

public class Sms {
    private String _id;
    private String _address;
    private String _msg;
    private Long _time;
    private String _folderName;

    public Sms(String _id, String _address, String _msg,Long _time, String _folderName) {
        this._id = _id;
        this._address = _address;
        this._msg = _msg;
        this._time = _time;
        this._folderName = _folderName;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_address() {
        return _address;
    }

    public void set_address(String _address) {
        this._address = _address;
    }

    public String get_msg() {
        return _msg;
    }

    public void set_msg(String _msg) {
        this._msg = _msg;
    }



    public Long get_time() {
        return _time;
    }

    public void set_time(Long _time) {
        this._time = _time;
    }

    public String get_folderName() {
        return _folderName;
    }

    public void set_folderName(String _folderName) {
        this._folderName = _folderName;
    }

    @Override
    public String toString() {
        return "Sms{" +
                "_id='" + _id + '\'' +
                ", _address='" + _address + '\'' +
                ", _msg='" + _msg + '\'' +
                ", _time='" + _time + '\'' +
                ", _folderName='" + _folderName + '\'' +
                '}';
    }
}
