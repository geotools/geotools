/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling;

import java.util.Arrays;
import org.geotools.api.filter.Filter;
import org.geotools.api.style.Extent;
import org.geotools.api.style.FeatureTypeConstraint;
import org.geotools.api.style.StyleVisitor;
import org.geotools.util.Utilities;

public class FeatureTypeConstraintImpl implements FeatureTypeConstraint, Cloneable {
    /** the feature type name */
    String featureTypeName;

    /** the filter */
    Filter filter;

    /** the extents */
    Extent[] extents;

    @Override
    public String getFeatureTypeName() {
        return featureTypeName;
    }

    @Override
    public void setFeatureTypeName(String name) {
        this.featureTypeName = name;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    @Override
    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    @Override
    public Extent[] getExtents() {
        return extents;
    }

    @Override
    public void setExtents(Extent... extents) {
        this.extents = extents;
    }

    @Override
    public void accept(StyleVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        final int PRIME = 1000003;
        int result = 0;

        if (featureTypeName != null) {
            result = PRIME * result + featureTypeName.hashCode();
        }

        if (filter != null) {
            result = PRIME * result + filter.hashCode();
        }

        if (extents != null) {
            result = PRIME * result + Arrays.hashCode(extents);
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof FeatureTypeConstraintImpl) {
            FeatureTypeConstraintImpl other = (FeatureTypeConstraintImpl) obj;
            return Utilities.equals(featureTypeName, other.featureTypeName)
                    && Utilities.equals(filter, other.filter)
                    && Arrays.equals(extents, other.extents);
        }

        return false;
    }
}
