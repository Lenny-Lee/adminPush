package com.springapp.push.jdbc;/**
 * Created by lenny on 2017/10/15.
 */

import java.io.Serializable;

/**
 * MessageBean
 *
 * @author zhanglei
 * @date 2017/10/15
 */
public class MessageBean implements Serializable {

    private String message;
    private String time;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
