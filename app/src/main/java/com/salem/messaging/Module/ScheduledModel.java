package com.salem.messaging.Module;

public class ScheduledModel {

    String date,time,msg,group;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public ScheduledModel() {
    }

    public ScheduledModel(String date, String time, String msg, String group) {
        this.date = date;
        this.time = time;
        this.msg = msg;
        this.group = group;
    }
}
