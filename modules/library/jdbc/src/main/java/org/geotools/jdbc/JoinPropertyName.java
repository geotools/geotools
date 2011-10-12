/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.filter.AttributeExpressionImpl;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Property name that knows what feature type it comes from.
 * <p>
 * Used by the sql encoder to determine how to property encode the join query. 
 * </p>
 * 
 * @author Justin Deoliveira, OpenGeo
 */
public class JoinPropertyName extends AttributeExpressionImpl {

    SimpleFeatureType featureType;
    String alias;
    
    public JoinPropertyName(SimpleFeatureType featureType, String alias, String name) {
        super(name);
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
