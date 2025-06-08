/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.jdbc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.geotools.util.logging.Logging;

/**
 * Builder class for creating prepared statements.
 *
 * @author Justin Deoliveira, OpenGeo
 */
public class SqlUtil {

    static final Logger LOGGER = Logging.getLogger(SqlUtil.class);

    public static class PreparedStatementBuilder {

        PreparedStatement ps;
        int pos = 1;

        StringBuilder log = new StringBuilder();

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
            } else {
                ps.setNull(pos++, Types.INTEGER);
            }

            return this;
        }

        public PreparedStatementBuilder set(Double d) throws SQLException {
            log(d);
            if (d != null) {
                ps.setDouble(pos++, d);
            } else {
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
            // ps.setBinaryStream(pos++, is);
            return this;
        }

        public PreparedStatementBuilder log(Level l) {
            LOGGER.log(l, log.toString());
            return this;
        }

        public PreparedStatement statement() {
            return ps;
        }

        void log(Object v) {
            log.append("\n").append(" ").append(pos).append(" = ").append(v);
        }
    }

    public static void runScript(InputStream stream, Connection cx) throws SQLException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
                Statement st = cx.createStatement()) {
            StringBuilder buf = new StringBuilder();
            String sql = reader.readLine();
            while (sql != null) {
                sql = sql.trim();
                if (!sql.isEmpty() && !sql.startsWith("--")) {
                    buf.append(sql).append(" ");

                    if (sql.endsWith(";")) {
                        String stmt = buf.toString();
                        boolean skipError = stmt.startsWith("?");
                        if (skipError) {
                            stmt = stmt.replaceAll("^\\? *", "");
                        }

                        LOGGER.fine(stmt);
                        st.addBatch(stmt);

                        buf.setLength(0);
                    }
                }
                sql = reader.readLine();
            }
            st.executeBatch();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    private static final Pattern PROPERTY_PATTERN = Pattern.compile("\\$\\{(.+?)\\}");

    public static void runScript(InputStream stream, Connection cx, Map<String, String> properties)
            throws SQLException {

        int insideBlock = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
                Statement st = cx.createStatement()) {
            StringBuilder buf = new StringBuilder();
            String sql = reader.readLine();
            while (sql != null) {
                sql = sql.trim();
                if (!sql.isEmpty() && !sql.startsWith("--")) {
                    buf.append(sql).append(" ");

                    if (sql.startsWith("BEGIN")) {
                        insideBlock++;
                    } else if (insideBlock > 0 && sql.startsWith("END")) {
                        insideBlock--;
                    }

                    if (sql.endsWith(";") && insideBlock == 0) {
                        Matcher matcher = PROPERTY_PATTERN.matcher(buf);
                        while (matcher.find()) {
                            String propertyName = matcher.group(1);
                            String propertyValue = properties.get(propertyName);
                            if (propertyValue == null) {
                                throw new RuntimeException("Missing property " + propertyName + " for sql script");
                            } else {
                                buf.replace(matcher.start(), matcher.end(), propertyValue);
                                matcher.reset();
                            }
                        }

                        String stmt = buf.toString();

                        LOGGER.fine(stmt);
                        st.addBatch(stmt);

                        buf.setLength(0);
                    }
                }
                sql = reader.readLine();
            }
            st.executeBatch();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    public static PreparedStatementBuilder prepare(Connection conn, String sql) throws SQLException {
        return new PreparedStatementBuilder(conn, sql);
    }

    public static PreparedStatementBuilder prepare(PreparedStatement st) throws SQLException {
        return new PreparedStatementBuilder(st);
    }
}
