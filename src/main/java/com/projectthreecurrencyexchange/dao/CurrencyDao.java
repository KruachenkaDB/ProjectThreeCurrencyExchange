package com.projectthreecurrencyexchange.dao;

import com.projectthreecurrencyexchange.model.Currency;

import java.math.BigDecimal;
import java.sql.*;

public class CurrencyDao {

    public Currency findByCode(String code) {
        String url = "jdbc:sqlite:C:\\Users\\krua_\\Desktop\\ProjectThreeCurrencyExchange\\identifier.sqlite";

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (Connection connection = DriverManager.getConnection(url)){
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, code, name, sign FROM currencies WHERE code = ?");
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String codeFromDb = resultSet.getString("code");
                String name = resultSet.getString("name");
                String sign = resultSet.getString("sign");

                return new Currency(id, codeFromDb, name, sign);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
