package com.projectthreecurrencyexchange.dao;

import com.projectthreecurrencyexchange.exception.DatabaseOperationException;
import com.projectthreecurrencyexchange.exception.EntityExistsException;
import com.projectthreecurrencyexchange.exception.NotFoundException;
import com.projectthreecurrencyexchange.model.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDao {
    private static final String URL = "jdbc:sqlite:C:\\Users\\krua_\\Desktop\\ProjectThreeCurrencyExchange\\identifier.sqlite";

    public CurrencyDao() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new DatabaseOperationException(
                    "Java не может найти класс драйвера", e
            );
        }
    }

    public Currency findByCode(String code) {
        final String query = "SELECT id, code, name, sign " +
                "FROM currencies WHERE code = ?";

        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, code);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String codeFromDb = resultSet.getString("code");
                    String name = resultSet.getString("name");
                    String sign = resultSet.getString("sign");

                    return new Currency(id, codeFromDb, name, sign);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Проблема подключения к БД или выполнением SQL-запроса", e);
        }

        throw new NotFoundException("Валюта не найдена");
    }

    public List<Currency> findAll() {
        final String query = "SELECT id, code, name, sign " +
                "FROM currencies";

        List<Currency> currencyList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String codeFromDb = resultSet.getString("code");
                String name = resultSet.getString("name");
                String sign = resultSet.getString("sign");

                currencyList.add(new Currency(id, codeFromDb, name, sign));
            }
            return currencyList;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Проблема подключения к БД или выполнением SQL-запроса", e);
        }
    }

    public Currency add(Currency currency) {
        final String query = "INSERT INTO currencies (code, name, sign) " +
                "VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getName());
            preparedStatement.setString(3, currency.getSign());
            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt(1);

                    return new Currency(id, currency.getCode(), currency.getName(), currency.getSign());
                }
            } catch (SQLException e) {
                throw new DatabaseOperationException("Не удалось получить ID созданной валюты", e);
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Проблема подключения к БД или выполнением SQL-запроса", e);
        }

        throw new DatabaseOperationException("Проблема подключения к БД или выполнением SQL-запроса");
    }

    public boolean existsByCode(String code) {
        final String query = "SELECT 1 " +
                "FROM currencies WHERE code = ?";

        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, code);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Проблема подключения к БД или выполнением SQL-запроса", e);
        }
        return false;
    }
}