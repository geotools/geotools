/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Set;
import org.geotools.filter.FidFilterImpl;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.identity.Identifier;

/**
 * Id filter that knows what feature type it comes from.
 *
 * <p>Used by the sql encoder to determine how to properly encode the join query.
 */
public class JoinId extends FidFilterImpl {

    SimpleFeatureType featureType;
    String alias;

    protected JoinId(SimpleFeatureType featureType, String alias, Set<? extends Identifier> fids) {
        super(fids);
        this.featureType = featureType;
        this.alias = alias;
    }

    public SimpleFeatureType getFeatureType() {
        return featureType;
    }

    public String getAlias() {
        return alias;
    }
}
