package com.springapp.push.jdbc;/**
 * Created by lenny on 2017/10/15.
 */

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * MesaageMapper
 *
 * @author zhanglei
 * @date 2017/10/15
 */
public class MessageMapper implements RowMapper<MessageBean> {
    public MessageBean mapRow(ResultSet rs, int rownum) throws SQLException {
        MessageBean message = new MessageBean();
        message.setMessage(rs.getString("message"));
        message.setTime(rs.getString("time"));
        return message;
    }
}
