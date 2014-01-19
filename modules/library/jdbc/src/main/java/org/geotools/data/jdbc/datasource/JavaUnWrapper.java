package org.geotools.data.jdbc.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Wrapper;

public class JavaUnWrapper implements UnWrapper {

    @Override
    public boolean canUnwrap(Connection conn) {
        return conn instanceof Wrapper;
    }

    @Override
    public boolean canUnwrap(Statement st) {
        return st instanceof Wrapper;
    }

    @Override
    public Connection unwrap(Connection conn) {
        try {
            return ((Wrapper) conn).unwrap(Connection.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Statement unwrap(Statement statement) {
        try {
            return ((Wrapper) statement).unwrap(Statement.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
