/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.api.style.FeatureTypeConstraint;
import org.geotools.api.style.LayerFeatureConstraints;

public class LayerFeatureConstraintsImpl implements LayerFeatureConstraints {

    private FeatureTypeConstraint[] constraints;

    @Override
    public FeatureTypeConstraint[] getFeatureTypeConstraints() {
        return constraints;
    }

    @Override
    public void setFeatureTypeConstraints(FeatureTypeConstraint... constraints) {
        this.constraints = constraints;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof LayerFeatureConstraintsImpl) {
            LayerFeatureConstraintsImpl other = (LayerFeatureConstraintsImpl) obj;
            return Arrays.equals(constraints, other.constraints);
        }

        return false;
    }

    @Override
    public int hashCode() {
        final int PRIME = 1000003;
        int result = 0;

        if (constraints != null) {
            result = PRIME * result + Arrays.hashCode(constraints);
        }

        return result;
    }
}
