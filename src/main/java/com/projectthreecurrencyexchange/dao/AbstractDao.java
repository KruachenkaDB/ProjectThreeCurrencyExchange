package com.projectthreecurrencyexchange.dao;

import com.projectthreecurrencyexchange.exception.DatabaseOperationException;

public abstract class AbstractDao {
    protected AbstractDao() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new DatabaseOperationException(
                    "SQLite JDBC driver not found", e
            );
        }
    }
}
