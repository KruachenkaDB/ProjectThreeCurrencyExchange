package com.projectthreecurrencyexchange.dao;

import com.projectthreecurrencyexchange.model.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDao {

    // в методах повторяющийся код, может вынести саму логику отдельно
    public Currency findByCode(String code) {
        String url = "jdbc:sqlite:C:\\Users\\krua_\\Desktop\\ProjectThreeCurrencyExchange\\identifier.sqlite";

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (Connection connection = DriverManager.getConnection(url)) {
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

    public List<Currency> findAll() {
        String url = "jdbc:sqlite:C:\\Users\\krua_\\Desktop\\ProjectThreeCurrencyExchange\\identifier.sqlite";

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (Connection connection = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, code, name, sign FROM currencies");
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Currency> currencyList = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String codeFromDb = resultSet.getString("code");
                String name = resultSet.getString("name");
                String sign = resultSet.getString("sign");

                currencyList.add(new Currency(id, codeFromDb, name, sign));
            }
            return currencyList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Currency add(Currency currency) {
        String url = "jdbc:sqlite:C:\\Users\\krua_\\Desktop\\ProjectThreeCurrencyExchange\\identifier.sqlite";

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = DriverManager.getConnection(url)) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO currencies (code, name, sign) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getName());
            preparedStatement.setString(3, currency.getSign());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                int id = resultSet.getInt(1);

                return new Currency(id, currency.getCode(), currency.getName(), currency.getSign());
            } else {
                //тут позже разобраться
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
