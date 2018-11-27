package com.springapp.push.jdbc;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by lenny on 2017/10/15.
 */
public interface MessageDao {

    /**
     * This is the method to be used to initialize
     * database resources ie. connection.
     */
    public void setdatasource(DataSource ds);

    public int addMessage(MessageBean messageBean);

    public List<MessageBean> queryAllMessage();

}
