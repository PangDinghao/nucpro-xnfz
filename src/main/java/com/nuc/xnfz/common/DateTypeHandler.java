package com.nuc.xnfz.common;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//sql查询时将Date转化成String
public class DateTypeHandler extends BaseTypeHandler<Date> {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Date date, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, sdf.format(date));
    }

    @Override
    public Date getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        String dateString = resultSet.getString(columnName);
        try {
            if (dateString != null) {
                return sdf.parse(dateString);
            }
        } catch (ParseException e) {
            // Handle parsing exception
        }
        return null;
    }

    @Override
    public Date getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        String dateString = resultSet.getString(columnIndex);
        try {
            if (dateString != null) {
                return sdf.parse(dateString);
            }
        } catch (ParseException e) {
            // Handle parsing exception
        }
        return null;
    }

    @Override
    public Date getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        String dateString = callableStatement.getString(columnIndex);
        try {
            if (dateString != null) {
                return sdf.parse(dateString);
            }
        } catch (ParseException e) {
            // Handle parsing exception
        }
        return null;
    }
}
