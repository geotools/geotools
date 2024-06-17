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

import java.util.Objects;

/** A metadata entry in a GeoPackage */
public class GeoPkgMetadata {

    /** Scope of the metadata */
    public enum Scope {
        Undefined("undefined"),
        FieldSession("fieldSession"),
        CollectionSession("collectionSession"),
        Series("series"),
        Dataset("dataset"),
        FeatureType("featureType"),
        Feature("feature"),
        AttributeType("attributeType"),
        Attribute("attribute"),
        Tile("tile"),
        Model("model"),
        Catalog("catalog"),
        Schema("schema"),
        Taxonomy("taxonomy"),
        Software("software"),
        Service("service"),
        CollectionHardware("collectionHardware"),
        NonGeographicDataset("nonGeographicDataset"),
        DimensionGroup("dimensionGroup"),
        Style("style");

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
    String standardURI;
    String mimeType;
    String metadata;

    public GeoPkgMetadata(long id, Scope scope, String URI, String mimeType, String metadata) {
        this.id = id;
        this.scope = scope;
        this.standardURI = URI;
        this.mimeType = mimeType;
        this.metadata = metadata;
    }

    public GeoPkgMetadata(Scope scope, String URI, String mimeType, String metadata) {
        this.scope = scope;
        this.standardURI = URI;
        this.mimeType = mimeType;
        this.metadata = metadata;
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

    public String getStandardURI() {
        return standardURI;
    }

    public void setStandardURI(String standardURI) {
        this.standardURI = standardURI;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "GeoPkgMetadata{"
                + "id="
                + id
                + ", scope="
                + scope
                + ", standardURI='"
                + standardURI
                + '\''
                + ", mimeType='"
                + mimeType
                + '\''
                + ", metadata='"
                + metadata
                + '\''
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeoPkgMetadata that = (GeoPkgMetadata) o;
        return Objects.equals(id, that.id)
                && scope == that.scope
                && Objects.equals(standardURI, that.standardURI)
                && Objects.equals(mimeType, that.mimeType)
                && Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, scope, standardURI, mimeType, metadata);
    }
}
