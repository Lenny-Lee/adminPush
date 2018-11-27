package com.springapp.push.jdbc;/**
 * Created by lenny on 2017/10/15.
 */

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

/**
 * MessageImpl
 *
 * @author zhanglei
 * @date 2017/10/15
 */
@Service
public class MessageImpl implements MessageDao{
    private DataSource datasource;
    private JdbcTemplate jdbcTemplateObject;

    @Override
    public void setdatasource(DataSource ds) {
        this.datasource = ds;
        this.jdbcTemplateObject = new JdbcTemplate(datasource);
    }

    @Override
    public int addMessage(MessageBean messageBean) {
        String sql = "INSERT INTO 365_ios_warning(message,time)VALUES(?,?)";
        return jdbcTemplateObject.update(sql, messageBean.getMessage(),messageBean.getTime());
    }

    @Override
    public List<MessageBean> queryAllMessage() {
        List<MessageBean> messageBeans = null;
        String sql = "SELECT * FROM 365_ios_warning order by time desc limit 50";
        messageBeans = jdbcTemplateObject.query(sql, new MessageMapper());
        return messageBeans;
    }
}
