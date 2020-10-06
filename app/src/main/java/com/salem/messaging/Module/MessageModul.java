package com.salem.messaging.Module;

import java.util.Date;

public class MessageModul {

    private String msg;
    private String time;


    public MessageModul() {
    }

    public MessageModul(String msg, String time) {
        this.msg=msg;
        this.time=time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
