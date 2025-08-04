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

import static org.geotools.geopkg.GeoPackage.METADATA;
import static org.geotools.geopkg.GeoPackage.METADATA_REFERENCE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;

/**
 * Supports the GeoPackage metadata extension, defined as a "registered extension" in the GeoPackage standard at
 * http://www.geopackage.org/spec121/#extension_metadata
 */
public class GeoPkgMetadataExtension extends GeoPkgExtension {

    static final Logger LOGGER = Logging.getLogger(GeoPkgMetadataExtension.class);

    public static class Factory implements GeoPkgExtensionFactory {

        @Override
        public GeoPkgExtension getExtension(String name, GeoPackage geoPackage) {
            if (NAME.equals(name)) {
                return new GeoPkgMetadataExtension(geoPackage);
            }

            return null;
        }

        @Override
        public GeoPkgExtension getExtension(Class extensionClass, GeoPackage geoPackage) {
            if (GeoPkgMetadataExtension.class.equals(extensionClass)) {
                return new GeoPkgMetadataExtension(geoPackage);
            }

            return null;
        }
    }

    static final String DEFINITION = "http://www.geopackage.org/spec121/#extension_metadata";
    public static final String NAME = "gpkg_metadata";

    protected GeoPkgMetadataExtension(GeoPackage geoPackage) {
        super(NAME, DEFINITION, Scope.ReadWrite, geoPackage);
    }

    /**
     * Lists all metadata entries present in the GeoPackage
     *
     * @throws SQLException
     */
    public List<GeoPkgMetadata> getMetadatas() throws SQLException {
        try (Connection cx = getConnection()) {

            if (!isRegistered(cx)) {
                return null;
            }
            String sql = "SELECT * FROM %s".formatted(METADATA);
            try (PreparedStatement ps = cx.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery()) {
                List<GeoPkgMetadata> metadata = new ArrayList<>();
                while (rs.next()) {
                    metadata.add(mapMetadata(rs));
                }
                return metadata;
            }
        }
    }

    /** Returns a {@link GeoPkgMetadata} with the given identifier */
    public GeoPkgMetadata getMetadata(Long id) throws SQLException {
        if (id == null) return null;
        try (Connection cx = getConnection()) {
            String sql = "SELECT * FROM %s where id = ?".formatted(METADATA);
            try (PreparedStatement ps = cx.prepareStatement(sql)) {
                ps.setLong(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return mapMetadata(rs);
                }
            }
        }
        return null;
    }

    /** Adds a metadata entry in the GeoPacakge */
    public void addMetadata(GeoPkgMetadata metadata) throws SQLException {
        String sql =
                "INSERT INTO %s(md_scope, md_standard_uri, mime_type, metadata) VALUES(?, ?, ?, ?)".formatted(METADATA);
        try (Connection cx = getConnection();
                PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setString(1, metadata.getScope().getSqlValue());
            ps.setString(2, metadata.getStandardURI());
            ps.setString(3, metadata.getMimeType());
            ps.setString(4, metadata.metadata);
            ps.executeUpdate();

            metadata.setId(getGeneratedKey(ps));
        }
    }

    /** Updates metadata entry in the GeoPackage */
    public void updateMetadata(GeoPkgMetadata metadata) throws SQLException {
        String sql = String.format(
                "UPDATE %s "
                        + "set md_scope = ?, "
                        + "md_standard_uri = ?, "
                        + "mime_type = ?, "
                        + "metadata = ? "
                        + "WHERE id = ?",
                METADATA);
        try (Connection cx = getConnection();
                PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setString(1, metadata.getScope().getSqlValue());
            ps.setString(2, metadata.getStandardURI());
            ps.setString(3, metadata.getMimeType());
            ps.setString(4, metadata.getMetadata());
            ps.setLong(5, metadata.getId());
            ps.executeUpdate();
        }
    }

    /** Deletes a metadata entry from the GeoPackage */
    public void removeMetadata(GeoPkgMetadata metadata) throws SQLException {
        String sql = "DELETE from %s WHERE id = ?".formatted(METADATA);
        try (Connection cx = getConnection();
                PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setLong(1, metadata.getId());
            ps.executeUpdate();
        }
    }

    private GeoPkgMetadata mapMetadata(ResultSet rs) throws SQLException {
        return new GeoPkgMetadata(
                rs.getLong("id"),
                getMetadataScope(rs),
                rs.getString("md_standard_uri"),
                rs.getString("mime_type"),
                rs.getString("metadata"));
    }

    private GeoPkgMetadata.Scope getMetadataScope(ResultSet rs) throws SQLException {
        String scope = rs.getString("md_scope");
        if (scope != null) {
            Optional<GeoPkgMetadata.Scope> enumValue = Arrays.stream(GeoPkgMetadata.Scope.values())
                    .filter(s -> scope.equals(s.getSqlValue()))
                    .findFirst();
            if (enumValue.isPresent()) return enumValue.get();
        }

        LOGGER.log(
                Level.FINE,
                "md_scope is supposed to be non null and in a finite set of values defined by the spec, but got '"
                        + scope
                        + "' instead. Using the default value, 'dataset'.");
        return GeoPkgMetadata.Scope.Dataset;
    }

    public List<GeoPkgMetadataReference> getReferences(GeoPkgMetadata metadata) throws SQLException {
        try (Connection cx = getConnection()) {
            if (!isRegistered(cx)) {
                return null;
            }

            // TODO: is it worth doing a join to get parents in a single query?
            String sql = "SELECT rowid, * FROM %s where md_file_id = ?".formatted(METADATA_REFERENCE);
            try (PreparedStatement ps = cx.prepareStatement(sql)) {
                ps.setLong(1, metadata.getId());
                List<GeoPkgMetadataReference> references = new ArrayList<>();
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        references.add(mapReference(rs, metadata));
                    }
                }
                return references;
            }
        }
    }

    private GeoPkgMetadataReference mapReference(ResultSet rs, GeoPkgMetadata metadata) throws SQLException {
        Long rowId = rs.getLong("row_id_value");
        if (rs.wasNull()) rowId = null;
        return new GeoPkgMetadataReference(
                rs.getLong("rowid"), // Hack to get a PK, the definition of the table left this open
                getReferenceScope(rs.getString("reference_scope")),
                rs.getString("table_name"),
                rs.getString("column_name"),
                rowId,
                toDate(rs.getString("timestamp")),
                metadata,
                getMetadata(rs.getLong("md_parent_id")));
    }

    private Date toDate(String timestamp) throws SQLException {
        try {
            return GeoPackage.getDateFormat().parse(timestamp);
        } catch (ParseException e) {
            throw new SQLException(e);
        }
    }

    private GeoPkgMetadataReference.Scope getReferenceScope(String scope) throws SQLException {
        if (scope != null) {
            Optional<GeoPkgMetadataReference.Scope> enumValue = Arrays.stream(GeoPkgMetadataReference.Scope.values())
                    .filter(s -> scope.equals(s.getSqlValue()))
                    .findFirst();
            if (enumValue.isPresent()) return enumValue.get();
        }

        LOGGER.log(
                Level.FINE,
                "reference_scope is supposed to be non null and in a finite set of values defined by the spec, but got '"
                        + scope
                        + "' instead. Using the default value, 'dataset'.");
        return GeoPkgMetadataReference.Scope.GeoPackage;
    }

    /**
     * Adds a metadata reference in the package
     *
     * @param reference
     * @throws SQLException
     */
    public void addReference(GeoPkgMetadataReference reference) throws SQLException {
        String sql = String.format(
                "INSERT INTO %s(reference_scope, table_name, column_name, row_id_value, "
                        + "timestamp, md_file_id, md_parent_id) "
                        + "VALUES(?, ?, ?, ?, ?, ?, ?)",
                METADATA_REFERENCE);
        try (Connection cx = getConnection();
                PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setString(1, reference.getScope().getSqlValue());
            ps.setString(2, reference.getTable());
            ps.setString(3, reference.getColumn());
            ps.setObject(4, reference.getRowId());
            ps.setString(5, GeoPackage.getDateFormat().format(reference.getTimestamp()));
            ps.setLong(6, reference.getMetadata().getId());
            GeoPkgMetadata metadataParent = reference.getMetadataParent();
            if (metadataParent != null) {
                ps.setLong(7, metadataParent.getId());
            } else {
                ps.setNull(7, Types.BIGINT);
            }
            ps.executeUpdate();

            reference.setId(getGeneratedKey(ps));
        }
    }

    /**
     * Updates a metadata reference in the pacakge
     *
     * @param reference
     * @throws SQLException
     */
    public void updateReference(GeoPkgMetadataReference reference) throws SQLException {
        String sql = String.format(
                "UPDATE %s set reference_scope = ?, "
                        + "table_name = ?, "
                        + "column_name = ?, "
                        + "row_id_value = ?, "
                        + "timestamp = ?, "
                        + "md_file_id = ?,"
                        + "md_parent_id = ? "
                        + "where rowid = ?",
                METADATA_REFERENCE);
        try (Connection cx = getConnection();
                PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setString(1, reference.getScope().getSqlValue());
            ps.setString(2, reference.getTable());
            ps.setString(3, reference.getColumn());
            ps.setObject(4, reference.getRowId());
            ps.setString(5, GeoPackage.getDateFormat().format(reference.getTimestamp()));
            ps.setLong(6, reference.getMetadata().getId());
            GeoPkgMetadata metadataParent = reference.getMetadataParent();
            if (metadataParent != null) {
                ps.setLong(7, metadataParent.getId());
            } else {
                ps.setNull(7, Types.BIGINT);
            }
            ps.setLong(8, reference.getId());
            ps.executeUpdate();
        }
    }

    /** Removes the given metadata reference from the package */
    public void removeReference(GeoPkgMetadataReference reference) throws SQLException {
        String sql = "DELETE FROM %s WHERE rowid = ?".formatted(METADATA_REFERENCE);

        try (Connection cx = getConnection();
                PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setLong(1, reference.getId());
            ps.executeUpdate();
        }
    }
}
