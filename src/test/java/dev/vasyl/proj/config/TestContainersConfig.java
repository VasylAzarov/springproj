package dev.vasyl.proj.config;

import org.testcontainers.containers.MySQLContainer;

public class TestContainersConfig {

    private static final MySQLContainer<?> mysqlContainer;

    static {
        mysqlContainer = new MySQLContainer<>("mysql:8.0.33")
                .withDatabaseName("springproj_db")
                .withUsername("testuser")
                .withPassword("testpassword");

        mysqlContainer.start();

        System.setProperty("DB_URL", mysqlContainer.getJdbcUrl());
        System.setProperty("DB_USERNAME", mysqlContainer.getUsername());
        System.setProperty("DB_PASSWORD", mysqlContainer.getPassword());
    }

    public static MySQLContainer<?> getMySQLContainer() {
        return mysqlContainer;
    }
}
