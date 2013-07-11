package org.geotools.geopkg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.logging.Level;

/**
 * Builder class for creating prepared statements.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class PreparedStatementBuilder {

    PreparedStatement ps;
    int pos = 1;

    StringBuilder log = new StringBuilder();

    public static PreparedStatementBuilder prepare(Connection conn, String sql) throws SQLException {
        return new PreparedStatementBuilder(conn, sql); 
    }

    public static PreparedStatementBuilder prepare(PreparedStatement st) throws SQLException {
        return new PreparedStatementBuilder(st); 
    }
    
    public PreparedStatementBuilder(PreparedStatement ps) throws SQLException {
        this.ps = ps;
    }

    public PreparedStatementBuilder(Connection conn, String sql) throws SQLException {
        this(conn.prepareStatement(sql));

        log.append(sql);
    }

    public PreparedStatementBuilder set(Long l) throws SQLException {
        log(l);
        ps.setLong(pos++, l);
        return this;
    }

    public PreparedStatementBuilder set(Integer i) throws SQLException {
        log(i);
        if (i != null) {
            ps.setInt(pos++, i);
        }
        else {
            ps.setNull(pos++, Types.INTEGER);
        }
        
        return this;
    }

    public PreparedStatementBuilder set(Double d) throws SQLException {
        log(d);
        if (d != null) {
            ps.setDouble(pos++, d);
        }
        else {
            ps.setNull(pos++, Types.DOUBLE);
        }
        
        return this;
    }

    public PreparedStatementBuilder set(String s) throws SQLException {
        log(s);
        ps.setString(pos++, s);
        return this;
    }

    public PreparedStatementBuilder set(Boolean b) throws SQLException {
        log(b);
        ps.setBoolean(pos++, b);
        return this;
    }

    public PreparedStatementBuilder set(Date d) throws SQLException {
        log(d);
        ps.setDate(pos++, d != null ? new java.sql.Date(d.getTime()) : null);
        return this;
    }

    public PreparedStatementBuilder set(byte[] b) throws SQLException {
        log(b);
        ps.setBytes(pos++, b);
        //ps.setBinaryStream(pos++, is);
        return this;
    }

    public PreparedStatementBuilder log(Level l) {
        GeoPackage.LOGGER.log(l, log.toString());
        return this;
    }

    public PreparedStatement statement() {
        return ps;
    }

    void log(Object v) {
        log.append("\n").append(" ").append(pos).append(" = ").append(v);
    }
}
