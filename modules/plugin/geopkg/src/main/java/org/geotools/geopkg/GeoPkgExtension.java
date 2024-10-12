/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg;

import static java.lang.String.format;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Base class for GeoPackage extensions, containing the share-able functionality */
public class GeoPkgExtension {

    /** Extension scope */
    public enum Scope {
        ReadWrite("read-write"),
        WriteOnly("write-only");

        String sqlValue;

        Scope(String sqlValue) {
            this.sqlValue = sqlValue;
        }

        public String getSqlValue() {
            return sqlValue;
        }
    };

    public static class Association {
        String tableName;
        String columnName;

        public Association(String tableName) {
            this.tableName = tableName;
        }

        public Association(String tableName, String columnName) {
            this.tableName = tableName;
            this.columnName = columnName;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Association that = (Association) o;
            return Objects.equals(tableName, that.tableName) && Objects.equals(columnName, that.columnName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(tableName, columnName);
        }
    }

    protected final String name;
    protected final String definition;
    protected final Scope scope;
    protected final GeoPackage geoPackage;

    /**
     * Creates a new geopackage extension. By itself, it only allows to access the associated
     * tables.
     *
     * @param name The extension name, as registered with OGC
     * @param definition The definition, normally a URL pointing to a spec
     * @param scope The scope, read/write or write/only
     * @param geoPackage The GeoPackage providing access to the contents of the file
     */
    protected GeoPkgExtension(String name, String definition, Scope scope, GeoPackage geoPackage) {
        this.name = name;
        this.definition = definition;
        this.scope = scope;
        this.geoPackage = geoPackage;
    }

    /** The extension name, as registered with OGC */
    public String getName() {
        return name;
    }

    /** The definition, normally a URL pointing to a spec */
    public String getDefinition() {
        return definition;
    }

    /**
     * The scope of the extension
     *
     * @return
     */
    public Scope getScope() {
        return scope;
    }

    /**
     * Returns true if the extension has at least one registration in the geopkg_extensions table.
     * If not registered, the GeoPackage does not really have this extension, but the class could be
     * used to create one (this behavior is extension specific and delegated to sub-classes).
     */
    public boolean isRegistered() throws IOException {
        try {
            try (Connection cx = getConnection()) {
                return isRegistered(cx);
            }
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    protected boolean isRegistered(Connection cx) throws SQLException {
        String sql = format("SELECT * " + " FROM %s" + " WHERE extension_name = ? LIMIT 1", GeoPackage.EXTENSIONS);

        try (PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Returns the list of {@link Association} between this extension and table/columns in the
     * database
     */
    public List<Association> getAssociations() throws IOException {
        try {
            try (Connection cx = getConnection()) {
                String sql = format(
                        "SELECT table_name, column_name " + " FROM %s" + " WHERE extension_name = ?",
                        GeoPackage.EXTENSIONS);

                try (PreparedStatement ps = cx.prepareStatement(sql)) {
                    ps.setString(1, name);

                    List<Association> result = new ArrayList<>();
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            String tableName = rs.getString(1);
                            String columnName = rs.getString(2);
                            result.add(new Association(tableName, columnName));
                        }
                    }
                    return result;
                }
            }
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /** Grabs a connection from the GeoPackage data source */
    protected Connection getConnection() throws SQLException {
        return geoPackage.getDataSource().getConnection();
    }

    /**
     * Helper that returns the key generated by an insert, run with the provided prepared statement
     */
    protected long getGeneratedKey(PreparedStatement ps) throws SQLException {
        try (ResultSet keys = ps.getGeneratedKeys()) {
            if (keys.next()) return keys.getLong(1);
            else throw new SQLException("Coult not find a generated key!");
        }
    }
}
