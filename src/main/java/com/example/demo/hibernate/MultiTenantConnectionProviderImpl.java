package com.example.demo.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MultiTenantConnectionProviderImpl implements MultiTenantConnectionProvider {
    private final DataSource dataSource;

    public MultiTenantConnectionProviderImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(Object tenantIdentifier) throws SQLException {
        final Connection connection = getAnyConnection();
        try {
            System.out.println("setando o getConnection");
            System.out.println(tenantIdentifier);
            String tenant = (String) tenantIdentifier;
            connection.createStatement().execute("set search_path to " + tenant);
            ResultSet resultSet = connection.createStatement().executeQuery("show search_path;");
            while (resultSet.next()) {
                System.out.println("Current schema: " + resultSet.getString(1));
            }
        } catch (SQLException e) {
            throw new HibernateException("Não foi possível alterar para o schema " + tenantIdentifier);
        }
        return connection;
    }

    @Override
    public void releaseConnection(Object tenantIdentifier, Connection connection) throws SQLException {
        try (connection) {
            System.out.println("setando o releaseConnection");
            System.out.println(tenantIdentifier);
            String tenant = (String) tenantIdentifier;
            connection.createStatement().execute("set search_path to " + tenant);
            ResultSet resultSet = connection.createStatement().executeQuery("show search_path;");
            while (resultSet.next()) {
                System.out.println("Current schema: " + resultSet.getString(1));
            }

        } catch (SQLException e) {
            throw new HibernateException("Não foi possível se conectar ao schema " + tenantIdentifier);
        }
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return true;
    }

    @Override
    public boolean isUnwrappableAs(Class aClass) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return null;
    }
}
