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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.geotools.util.NumberRange;
import org.geotools.util.logging.Logging;

/**
 * The geopackage schema extension, allowing to declare more information about columns and
 * eventually express constraints on them
 */
public class GeoPkgSchemaExtension extends GeoPkgExtension {

    public static class Factory implements GeoPkgExtensionFactory {

        @Override
        public GeoPkgExtension getExtension(String name, GeoPackage geoPackage) {
            if (NAME.equals(name)) {
                return new GeoPkgSchemaExtension(geoPackage);
            }

            return null;
        }

        @Override
        public GeoPkgExtension getExtension(Class extensionClass, GeoPackage geoPackage) {
            if (GeoPkgSchemaExtension.class.equals(extensionClass)) {
                return new GeoPkgSchemaExtension(geoPackage);
            }

            return null;
        }
    }

    static final Logger LOGGER = Logging.getLogger(GeoPkgSchemaExtension.class);

    static final String NAME = "gpkg_schema";
    static final String DEFINITION =
            "http://www.geopackage.org/spec121/index.html#extension_schema";

    public GeoPkgSchemaExtension(GeoPackage geoPackage) {
        super(NAME, DEFINITION, Scope.ReadWrite, geoPackage);
    }

    public List<DataColumn> getDataColumns(String tableName) throws IOException {
        try (Connection cx = geoPackage.connPool.getConnection()) {
            return getDataColumns(tableName, cx);
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    List<DataColumn> getDataColumns(String tableName, Connection cx)
            throws IOException, SQLException {
        List<DataColumn> result = new ArrayList<>();
        if (!isRegistered(cx)) {
            return Collections.emptyList();
        }
        String sql =
                format(
                        "SELECT *"
                                + " FROM %s"
                                + " WHERE table_name = ? order by table_name, column_name",
                        GeoPackage.DATA_COLUMNS);

        try (PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setString(1, tableName);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DataColumn dc = new DataColumn();
                    String columnName = rs.getString("column_name");

                    dc.setColumnName(columnName);
                    dc.setName(rs.getString("column_name"));
                    dc.setTitle(rs.getString("title"));
                    dc.setDescription(rs.getString("description"));
                    dc.setMimeType(rs.getString("mime_type"));

                    String constraintName = rs.getString("constraint_name");
                    if (constraintName != null) {
                        dc.setConstraint(getConstraint(constraintName, cx));
                    }

                    result.add(dc);
                }
            }
        }

        return result;
    }

    /**
     * Returns the constraint by name, or sets to null if missing
     *
     * @param contraintName
     * @return
     * @throws IOException
     */
    public DataColumnConstraint getConstraint(String contraintName) throws IOException {
        try (Connection cx = geoPackage.connPool.getConnection()) {
            return getConstraint(contraintName, cx);
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    DataColumnConstraint getConstraint(String contraintName, Connection cx) throws SQLException {
        if (!isRegistered(cx)) {
            return null;
        }
        String sql =
                format(
                        "SELECT *" + " FROM %s dcc " + " WHERE dcc.constraint_name = ?",
                        GeoPackage.DATA_COLUMN_CONSTRAINTS);

        DataColumnConstraint constraint = null;
        try (PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setString(1, contraintName);

            Map<String, String> enumValues = null;
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String constraintName = rs.getString("constraint_name");
                    String constraintType = rs.getString("constraint_type");

                    if (constraint == null) {
                        if ("enum".equals(constraintType)) {
                            // enum values to be populated later
                            enumValues = new LinkedHashMap<>();
                            constraint = new DataColumnConstraint.Enum(constraintName, enumValues);
                        } else if ("glob".equals(constraintType)) {
                            constraint =
                                    new DataColumnConstraint.Glob(
                                            constraintName, rs.getString("value"));
                        } else if ("range".equals(constraintType)) {
                            Double min = rs.getDouble("min");
                            boolean minInclusve = rs.getBoolean("min_is_inclusive");
                            Double max = rs.getDouble("max");
                            boolean maxInclusve = rs.getBoolean("max_is_inclusive");
                            NumberRange<Double> range =
                                    new NumberRange<>(
                                            Double.class, min, minInclusve, max, maxInclusve);
                            constraint = new DataColumnConstraint.Range<>(constraintName, range);
                        } else {
                            LOGGER.warning(
                                    "Cannot process a constraint of type "
                                            + constraintType
                                            + ", ignoring");
                        }
                    }

                    if ("enum".equals(constraintType)) {
                        // just collecting values for the enum
                        enumValues.put(rs.getString("value"), rs.getString("description"));
                    }
                }
            }
        }
        return constraint;
    }

    public void addDataColumn(String tableName, DataColumn dataColumn) throws IOException {
        try (Connection cx = geoPackage.connPool.getConnection()) {
            addDataColumn(tableName, dataColumn, cx);
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    void addDataColumn(String tableName, DataColumn dataColumn, Connection cx)
            throws IOException, SQLException {
        DataColumnConstraint constraint = dataColumn.getConstraint();
        String constraintName = null;
        if (constraint != null) {
            constraintName = constraint.getName();
            if (getConstraint(constraint.getName(), cx) == null) {
                addConstraint(cx, constraint);
            }
        }

        String dataColumnSql =
                format("INSERT INTO %s VALUES(?, ?, ?, ?, ?, ?, ?)", GeoPackage.DATA_COLUMNS);
        try (PreparedStatement ps = cx.prepareStatement(dataColumnSql)) {
            ps.setString(1, tableName);
            ps.setString(2, dataColumn.getColumnName());
            ps.setString(3, dataColumn.getName());
            ps.setString(4, dataColumn.getTitle());
            ps.setString(5, dataColumn.getDescription());
            ps.setString(6, dataColumn.getMimeType());
            ps.setString(7, constraintName);
            ps.executeUpdate();
        }

        // declare the extension
        String extensionSql = format("INSERT INTO %s VALUES(?, ?, ?, ?, ?)", GeoPackage.EXTENSIONS);
        try (PreparedStatement ps = cx.prepareStatement(extensionSql)) {
            ps.setString(1, tableName);
            ps.setString(2, dataColumn.getColumnName());
            ps.setString(3, "gpkg_schema");
            ps.setString(4, "http://www.geopackage.org/spec121/index.html#extension_schema");
            ps.setString(5, "read-write");
            ps.executeUpdate();
        }
    }

    public void addConstraint(DataColumnConstraint constraint) throws SQLException {
        try (Connection cx = geoPackage.connPool.getConnection()) {
            addConstraint(cx, constraint);
        }
    }

    public void addConstraint(Connection cx, DataColumnConstraint constraint) throws SQLException {
        String constraintName = constraint.getName();
        String sql =
                format(
                        "INSERT INTO %s VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                        GeoPackage.DATA_COLUMN_CONSTRAINTS);
        if (constraint instanceof DataColumnConstraint.Enum) {
            DataColumnConstraint.Enum cEnum = (DataColumnConstraint.Enum) constraint;
            for (Map.Entry<String, String> entry : cEnum.getValues().entrySet()) {
                try (PreparedStatement ps = cx.prepareStatement(sql)) {
                    ps.setString(1, constraintName);
                    ps.setString(2, "enum");
                    ps.setString(3, entry.getKey());
                    ps.setObject(4, null);
                    ps.setObject(5, null);
                    ps.setObject(6, null);
                    ps.setObject(7, null);
                    ps.setString(8, entry.getValue());
                    ps.executeUpdate();
                }
            }
        } else if (constraint instanceof DataColumnConstraint.Glob) {
            try (PreparedStatement ps = cx.prepareStatement(sql)) {
                ps.setString(1, constraintName);
                ps.setString(2, "glob");
                ps.setString(3, ((DataColumnConstraint.Glob) constraint).getGlob());
                ps.setObject(4, null);
                ps.setObject(5, null);
                ps.setObject(6, null);
                ps.setObject(7, null);
                ps.setString(8, null);
                ps.executeUpdate();
            }
        } else if (constraint instanceof DataColumnConstraint.Range) {
            DataColumnConstraint.Range rangeConstraint = (DataColumnConstraint.Range) constraint;
            NumberRange range = rangeConstraint.getRange();
            try (PreparedStatement ps = cx.prepareStatement(sql)) {
                ps.setString(1, constraintName);
                ps.setString(2, "range");
                ps.setString(3, null);
                ps.setDouble(4, range.getMinimum());
                ps.setBoolean(5, range.isMinIncluded());
                ps.setDouble(6, range.getMaximum());
                ps.setBoolean(7, range.isMaxIncluded());
                ps.setString(8, null);
                ps.executeUpdate();
            }
        }
    }
}
