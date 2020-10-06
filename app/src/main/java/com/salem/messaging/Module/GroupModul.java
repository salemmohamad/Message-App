package com.salem.messaging.Module;

public class GroupModul {

    private String month;
    private String day;
    private String title;

    public GroupModul() {
    }

    public GroupModul(String month, String day, String title) {
        this.month = month;
        this.day = day;
        this.title = title;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String members) {
        this.title = members;
    }
}
