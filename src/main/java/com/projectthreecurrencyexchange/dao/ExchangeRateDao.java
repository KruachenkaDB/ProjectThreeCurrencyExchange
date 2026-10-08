package com.projectthreecurrencyexchange.dao;

import com.projectthreecurrencyexchange.model.Currency;
import com.projectthreecurrencyexchange.model.ExchangeRate;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateDao {
    public List<ExchangeRate> findAllRate() {
        String url = "jdbc:sqlite:C:\\Users\\krua_\\Desktop\\ProjectThreeCurrencyExchange\\identifier.sqlite";

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = DriverManager.getConnection(url)) {
            //может сменить названия алеасов для таблиц чтобы не было путаницы?
            String query = "SELECT t1.id as base_id, " +
                    "t2.id as exchange_id, " +
                    "t1.code as base_code, " +
                    "t1.name as base_name, " +
                    "t1.sign as base_sign, " +
                    "t3.id as target_id, " +
                    "t3.code as target_code, " +
                    "t3.name as target_name, " +
                    "t3.sign as target_sign, " +
                    "rate " +
                    "FROM currencies as t1 " +
                    "JOIN exchangeRates as t2 " +
                    "ON t1.id = t2.baseCurrencyId " +
                    "JOIN currencies as t3 " +
                    "ON t3.id = t2.targetCurrencyId";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<ExchangeRate> exchangeRateList = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("exchange_id");

                int base_id = resultSet.getInt("base_id");
                String base_code = resultSet.getString("base_code");
                String base_name = resultSet.getString("base_name");
                String base_sign = resultSet.getString("base_sign");

                int target_id = resultSet.getInt("target_id");
                String target_code = resultSet.getString("target_code");
                String target_name = resultSet.getString("target_name");
                String target_sign = resultSet.getString("target_sign");

                BigDecimal rate = resultSet.getBigDecimal("rate");

                Currency baseCurrency = new Currency(base_id, base_code, base_name, base_sign);
                Currency targetCurrency = new Currency(target_id, target_code, target_name, target_sign);
                exchangeRateList.add(new ExchangeRate(id, baseCurrency, targetCurrency, rate));
            }
            return exchangeRateList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ExchangeRate findByCodes(String baseCode, String targetCode) {
        String url = "jdbc:sqlite:C:\\Users\\krua_\\Desktop\\ProjectThreeCurrencyExchange\\identifier.sqlite";

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (Connection connection = DriverManager.getConnection(url)) {
            String query = "SELECT t1.id as base_id, " +
                    "t2.id as exchange_id, " +
                    "t1.code as base_code, " +
                    "t1.name as base_name, " +
                    "t1.sign as base_sign, " +
                    "t3.id as target_id, " +
                    "t3.code as target_code, " +
                    "t3.name as target_name, " +
                    "t3.sign as target_sign, " +
                    "rate " +
                    "FROM currencies as t1 " +
                    "JOIN exchangeRates as t2 " +
                    "ON t1.id = t2.baseCurrencyId " +
                    "JOIN currencies as t3 " +
                    "ON t3.id = t2.targetCurrencyId WHERE t1.code = ? AND t3.code = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, baseCode);
            preparedStatement.setString(2, targetCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("exchange_id");

                int base_id = resultSet.getInt("base_id");
                String base_code = resultSet.getString("base_code");
                String base_name = resultSet.getString("base_name");
                String base_sign = resultSet.getString("base_sign");

                int target_id = resultSet.getInt("target_id");
                String target_code = resultSet.getString("target_code");
                String target_name = resultSet.getString("target_name");
                String target_sign = resultSet.getString("target_sign");

                BigDecimal rate = resultSet.getBigDecimal("rate");

                Currency baseCurrency = new Currency(base_id, base_code, base_name, base_sign);
                Currency targetCurrency = new Currency(target_id, target_code, target_name, target_sign);
                return new ExchangeRate(id, baseCurrency, targetCurrency, rate);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //проверить можно ли так
        return null;
    }
}
