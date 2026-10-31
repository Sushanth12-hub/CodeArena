package com.codearena.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public final class Database {
    private static final String URL = "jdbc:h2:./data/codearena_demo;MODE=MySQL;DATABASE_TO_LOWER=TRUE";
    private Database() { }
    public static Connection connect() throws SQLException { return DriverManager.getConnection(URL, "sa", ""); }
    public static void initialise() {
        try (Connection connection = connect()) {
            runScript(connection, "/database/schema.sql");
            try (Statement statement = connection.createStatement(); ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM users")) {
                rs.next();
                if (rs.getInt(1) == 0) runScript(connection, "/database/sample_data.sql");
            }
            runScript(connection, "/database/extra_topics.sql");
        } catch (SQLException | IOException exception) { throw new IllegalStateException("Could not initialise database", exception); }
    }
    private static void runScript(Connection connection, String resource) throws IOException, SQLException {
        String sql = new String(Database.class.getResourceAsStream(resource).readAllBytes(), StandardCharsets.UTF_8);
        for (String statement : sql.split(";")) {
            if (!statement.trim().isEmpty()) try (Statement query = connection.createStatement()) { query.execute(statement); }
        }
    }
}
