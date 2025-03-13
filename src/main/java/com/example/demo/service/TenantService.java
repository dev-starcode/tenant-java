package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@Service
public class TenantService {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private static final String SQL_FILE_PATH = "resources/db/migration/create_table.sql";


    public void createTenant(String tenantName) {
        // Criação do schema para o novo tenant
        try (var connection = dataSource.getConnection()) {
            connection.createStatement().execute("CREATE SCHEMA IF NOT EXISTS " + tenantName);
            System.out.println("Schema criado para o tenant: " + tenantName);
            String processedSql = "create table "+tenantName +".products (\n" +
                    "    product_id uuid not null primary key,\n" +
                    "    name varchar(255)\n" +
                    ");\n" +
                    "\n";
            connection.createStatement().execute(processedSql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar schema para o tenant " + tenantName, e);
        }
    }

    private static void executeSql(String sql) throws Exception {
        // Configuração do banco de dados
        String url = "jdbc:postgresql://localhost:5432/multitenant";
        String username = "postgres";
        String password = "root";

        // Conectar ao banco de dados
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {

            // Executar o SQL processado
            statement.execute(sql);
        }
    }
}
