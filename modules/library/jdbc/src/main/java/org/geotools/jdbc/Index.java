/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.geotools.util.factory.Hints;

/**
 * Represents a database index
 *
 * @author Andrea Aime - GeoSolutions
 */
public class Index {
    String typeName;

    String indexName;

    boolean unique;

    List<String> attributes;

    Hints hints;

    public Index(String typeName, String indexName, boolean unique, String... attributes) {
        super();
        this.typeName = typeName;
        this.indexName = indexName;
        this.attributes = new ArrayList<String>(Arrays.asList(attributes));
        this.unique = unique;
    }

    public Index(
            String typeName, String indexName, boolean unique, Hints hints, String... attributes) {
        super();
        this.typeName = typeName;
        this.indexName = indexName;
        this.hints = hints;
        this.attributes = new ArrayList<String>(Arrays.asList(attributes));
        this.unique = unique;
    }

    /** True if the index is a unique one, false otherwise */
    public boolean isUnique() {
        return unique;
    }

    /** An un-modifiable list of the attributes in the index */
    public List<String> getAttributes() {
        return Collections.unmodifiableList(attributes);
    }

    /** The feature type owning this index */
    public String getTypeName() {
        return typeName;
    }

    /** The index name */
    public String getIndexName() {
        return indexName;
    }

    /** Retrieves the hints for this index. */
    public Hints getHints() {
        return hints;
    }

    @Override
    public String toString() {
        return "Index [typeName="
                + typeName
                + ", indexName="
                + indexName
                + ", unique="
                + unique
                + ", attributes="
                + attributes
                + ", hints="
                + hints
                + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
        result = prime * result + ((hints == null) ? 0 : hints.hashCode());
        result = prime * result + ((indexName == null) ? 0 : indexName.hashCode());
        result = prime * result + ((typeName == null) ? 0 : typeName.hashCode());
        result = prime * result + (unique ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Index other = (Index) obj;
        if (attributes == null) {
            if (other.attributes != null) return false;
        } else if (!attributes.equals(other.attributes)) return false;
        if (hints == null) {
            if (other.hints != null) return false;
        } else if (!hints.equals(other.hints)) return false;
        if (indexName == null) {
            if (other.indexName != null) return false;
        } else if (!indexName.equals(other.indexName)) return false;
        if (typeName == null) {
            if (other.typeName != null) return false;
        } else if (!typeName.equals(other.typeName)) return false;
        if (unique != other.unique) return false;
        return true;
    }
}
