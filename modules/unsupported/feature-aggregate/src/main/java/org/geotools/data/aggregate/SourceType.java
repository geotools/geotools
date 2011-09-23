package org.geotools.data.aggregate;

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
import java.io.Serializable;

import org.opengis.feature.type.Name;

/**
 * One of the source types making up a aggregated type
 */
public class SourceType implements Serializable {
    private static final long serialVersionUID = -3739314811871903310L;

    Name storeName;

    String typeName;

    /**
     * Creates a new source feature type
     * @param storeName The source store name
     * @param typeName The source type name
     */
    public SourceType(Name storeName, String typeName) {
        super();
        this.storeName = storeName;
        this.typeName = typeName;
    }

    public Name getStoreName() {
        return storeName;
    }

    public String getTypeName() {
        return typeName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((storeName == null) ? 0 : storeName.hashCode());
        result = prime * result + ((typeName == null) ? 0 : typeName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SourceType other = (SourceType) obj;
        if (storeName == null) {
            if (other.storeName != null)
                return false;
        } else if (!storeName.equals(other.storeName))
            return false;
        if (typeName == null) {
            if (other.typeName != null)
                return false;
        } else if (!typeName.equals(other.typeName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "SourceType [sourceName=" + storeName + ", typeName=" + typeName + "]";
    }

}
