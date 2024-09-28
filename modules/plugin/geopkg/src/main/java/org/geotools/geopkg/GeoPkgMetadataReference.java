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

import java.util.Date;
import java.util.Objects;

public class GeoPkgMetadataReference {

    /** Scope of the metadata reference */
    public enum Scope {
        GeoPackage("geopackage"),
        Table("table"),
        Column("column"),
        Row("row"),
        RowCol("row/col");

        String sqlValue;

        Scope(String sqlValue) {
            this.sqlValue = sqlValue;
        }

        public String getSqlValue() {
            return sqlValue;
        }
    }

    Long id;
    Scope scope;
    String table;
    String column;
    Long rowId;
    Date timestamp;
    GeoPkgMetadata metadata;
    GeoPkgMetadata metadataParent;

    public GeoPkgMetadataReference(
            Long id,
            Scope scope,
            String table,
            String column,
            Long rowId,
            Date timestamp,
            GeoPkgMetadata metadata,
            GeoPkgMetadata metadataParent) {
        this.id = id;
        this.scope = scope;
        this.table = table;
        this.column = column;
        this.rowId = rowId;
        this.timestamp = timestamp;
        this.metadata = metadata;
        this.metadataParent = metadataParent;
    }

    public GeoPkgMetadataReference(
            Scope scope,
            String table,
            String column,
            Long rowId,
            Date timestamp,
            GeoPkgMetadata metadata,
            GeoPkgMetadata metadataParent) {
        this.scope = scope;
        this.table = table;
        this.column = column;
        this.rowId = rowId;
        this.timestamp = timestamp;
        this.metadata = metadata;
        this.metadataParent = metadataParent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Long getRowId() {
        return rowId;
    }

    public void setRowId(Long rowId) {
        this.rowId = rowId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public GeoPkgMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(GeoPkgMetadata metadata) {
        this.metadata = metadata;
    }

    public GeoPkgMetadata getMetadataParent() {
        return metadataParent;
    }

    public void setMetadataParent(GeoPkgMetadata metadataParent) {
        this.metadataParent = metadataParent;
    }

    @Override
    public String toString() {
        return "GeoPkgMetadataReference{"
                + "id="
                + id
                + ", scope="
                + scope
                + ", table='"
                + table
                + '\''
                + ", column='"
                + column
                + '\''
                + ", rowId="
                + rowId
                + ", timestamp="
                + timestamp
                + ", metadata="
                + metadata
                + ", metadataParent="
                + metadataParent
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeoPkgMetadataReference that = (GeoPkgMetadataReference) o;
        return Objects.equals(id, that.id)
                && scope == that.scope
                && Objects.equals(table, that.table)
                && Objects.equals(column, that.column)
                && Objects.equals(rowId, that.rowId)
                && Objects.equals(timestamp, that.timestamp)
                && Objects.equals(metadata, that.metadata)
                && Objects.equals(metadataParent, that.metadataParent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, scope, table, column, rowId, timestamp, metadata, metadataParent);
    }
}
