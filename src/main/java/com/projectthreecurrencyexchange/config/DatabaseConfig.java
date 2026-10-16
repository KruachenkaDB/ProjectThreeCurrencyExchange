package com.projectthreecurrencyexchange.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class DatabaseConfig {

    private static final String DATABASE_PATH = System.getenv("DATABASE_PATH");

    public static final String URL = initializeDatabase();

    private static String initializeDatabase() {
        try {
            Path databasePath;

            if (DATABASE_PATH != null && !DATABASE_PATH.isBlank()) {
                databasePath = Path.of(DATABASE_PATH).toAbsolutePath();
            } else {
                String dataDirectory = System.getenv("DATA_DIR");

                Path directory = dataDirectory != null && !dataDirectory.isBlank()
                        ? Path.of(dataDirectory)
                        : Path.of(System.getProperty("user.home"), "currency-exchange");

                Files.createDirectories(directory);
                databasePath = directory.resolve("identifier.sqlite");
            }

            Files.createDirectories(databasePath.getParent());

            if (Files.notExists(databasePath)) {
                try (InputStream input = DatabaseConfig.class
                        .getClassLoader()
                        .getResourceAsStream("identifier.sqlite")) {

                    if (input == null) {
                        throw new IllegalStateException("Database file identifier.sqlite not found in resources");
                    }

                    Files.copy(input, databasePath,
                            StandardCopyOption.REPLACE_EXISTING);
                }
            }

            return "jdbc:sqlite:" + databasePath;

        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}