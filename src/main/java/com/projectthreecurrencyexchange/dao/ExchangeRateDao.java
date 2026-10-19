package com.projectthreecurrencyexchange.dao;

import com.projectthreecurrencyexchange.config.DatabaseConfig;
import com.projectthreecurrencyexchange.exception.DatabaseOperationException;
import com.projectthreecurrencyexchange.exception.NotFoundException;
import com.projectthreecurrencyexchange.model.Currency;
import com.projectthreecurrencyexchange.model.ExchangeRate;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateDao extends AbstractDao{
    public List<ExchangeRate> findAll() {
        final String query = "SELECT t1.id as baseId, " +
                "t2.id as exchangeId, " +
                "t1.code as baseCode, " +
                "t1.name as baseName, " +
                "t1.sign as baseSign, " +
                "t3.id as targetId, " +
                "t3.code as targetCode, " +
                "t3.name as targetName, " +
                "t3.sign as targetSign, " +
                "rate " +
                "FROM currencies as t1 " +
                "JOIN exchangeRates as t2 " +
                "ON t1.id = t2.baseCurrencyId " +
                "JOIN currencies as t3 " +
                "ON t3.id = t2.targetCurrencyId";

        List<ExchangeRate> exchangeRateList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DatabaseConfig.URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("exchangeId");

                int base_id = resultSet.getInt("baseId");
                String base_code = resultSet.getString("baseCode");
                String base_name = resultSet.getString("baseName");
                String base_sign = resultSet.getString("baseSign");

                int target_id = resultSet.getInt("targetId");
                String target_code = resultSet.getString("targetCode");
                String target_name = resultSet.getString("targetName");
                String target_sign = resultSet.getString("targetSign");

                BigDecimal rate = resultSet.getBigDecimal("rate");

                Currency baseCurrency = new Currency(base_id, base_code, base_name, base_sign);
                Currency targetCurrency = new Currency(target_id, target_code, target_name, target_sign);
                exchangeRateList.add(new ExchangeRate(id, baseCurrency, targetCurrency, rate));
            }
            return exchangeRateList;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database connection or SQL query execution error", e);
        }
    }

    public ExchangeRate findByCodes(String baseCode, String targetCode) {
        final String query = "SELECT t1.id as baseId, " +
                "t2.id as exchangeId, " +
                "t1.code as baseCode, " +
                "t1.name as baseName, " +
                "t1.sign as baseSign, " +
                "t3.id as targetId, " +
                "t3.code as targetCode, " +
                "t3.name as targetName, " +
                "t3.sign as targetSign, " +
                "rate " +
                "FROM currencies as t1 " +
                "JOIN exchangeRates as t2 " +
                "ON t1.id = t2.baseCurrencyId " +
                "JOIN currencies as t3 " +
                "ON t3.id = t2.targetCurrencyId " +
                "WHERE t1.code = ? AND t3.code = ?";

        try (Connection connection = DriverManager.getConnection(DatabaseConfig.URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, baseCode);
            preparedStatement.setString(2, targetCode);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("exchangeId");

                    int base_id = resultSet.getInt("baseId");
                    String base_code = resultSet.getString("baseCode");
                    String base_name = resultSet.getString("baseName");
                    String base_sign = resultSet.getString("baseSign");

                    int target_id = resultSet.getInt("targetId");
                    String target_code = resultSet.getString("targetCode");
                    String target_name = resultSet.getString("targetName");
                    String target_sign = resultSet.getString("targetSign");

                    BigDecimal rate = resultSet.getBigDecimal("rate");

                    Currency baseCurrency = new Currency(base_id, base_code, base_name, base_sign);
                    Currency targetCurrency = new Currency(target_id, target_code, target_name, target_sign);
                    return new ExchangeRate(id, baseCurrency, targetCurrency, rate);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database connection or SQL query execution error", e);
        }
        throw new NotFoundException("Exchange rate not found");
    }

    public ExchangeRate add(ExchangeRate exchangeRate) {
        final String query = "INSERT INTO exchangeRates (baseCurrencyId, targetCurrencyId, rate) " +
                "VALUES (?, ?, ?) ";

        try (Connection connection = DriverManager.getConnection(DatabaseConfig.URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){

            preparedStatement.setInt(1, exchangeRate.getBaseCurrency().getId());
            preparedStatement.setInt(2, exchangeRate.getTargetCurrency().getId());
            preparedStatement.setBigDecimal(3, exchangeRate.getRate());
            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt(1);

                    return new ExchangeRate(id, exchangeRate.getBaseCurrency(), exchangeRate.getTargetCurrency(), exchangeRate.getRate());
                } else {
                    throw new DatabaseOperationException("Failed to get the ID of the created currency");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database connection or SQL query execution error", e);
        }
    }

    public ExchangeRate update(ExchangeRate exchangeRate) {
        final String query = "UPDATE exchangeRates " +
                "SET rate = ? WHERE baseCurrencyId = ? " +
                "AND targetCurrencyId = ?";

        try (Connection connection = DriverManager.getConnection(DatabaseConfig.URL);
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setBigDecimal(1, exchangeRate.getRate());
            preparedStatement.setInt(2, exchangeRate.getBaseCurrency().getId());
            preparedStatement.setInt(3, exchangeRate.getTargetCurrency().getId());

            int result = preparedStatement.executeUpdate();
            if (result == 1) {
                return new ExchangeRate(exchangeRate.getId(), exchangeRate.getBaseCurrency(), exchangeRate.getTargetCurrency(), exchangeRate.getRate());
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Database connection or SQL query execution error", e);
        }

        throw new NotFoundException("Currency pair is missing from the database");
    }

    public boolean existsByCurrencyPair(int baseCurrencyId, int targetCurrencyId) {
        final String query = "SELECT 1 " +
                "FROM exchangeRates " +
                "WHERE baseCurrencyId = ? " +
                "AND targetCurrencyId = ?";

        try (Connection connection = DriverManager.getConnection(DatabaseConfig.URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, baseCurrencyId);
            preparedStatement.setInt(2, targetCurrencyId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database connection or SQL query execution error", e);
        }
        return false;
    }
}
