/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.catalog.oracle;

import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.filter.text.cql2.CQLException;

/**
 * An Oracle specific {@link FeatureTypeMapper} instance
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
public class OracleFeatureTypeMapper extends AbstractFeatureTypeMapper {

    private static final int MAX_LENGTH = 30;

    /** Create a new {@link OracleFeatureTypeMapper} on top of the original featureType provided */
    public OracleFeatureTypeMapper(SimpleFeatureType featureType) throws CQLException {
        super(featureType, MAX_LENGTH);
        remapFeatureType();
    }
}
