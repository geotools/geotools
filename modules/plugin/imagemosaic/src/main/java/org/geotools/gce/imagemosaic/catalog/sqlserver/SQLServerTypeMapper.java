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
package org.geotools.gce.imagemosaic.catalog.sqlserver;

import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.gce.imagemosaic.catalog.oracle.AbstractFeatureTypeMapper;
import org.geotools.gce.imagemosaic.catalog.oracle.DataStoreWrapper;

/**
 * Specific SQLServer implementation for a {@link DataStoreWrapper} By default, SQLServer identifiers can't be longer
 * than 128 chars. See <a
 * href="https://docs.microsoft.com/en-us/sql/relational-databases/databases/database-identifiers?view=sql-server-ver15">SQL
 * Syntax identifiers</a>
 */
public class SQLServerTypeMapper extends AbstractFeatureTypeMapper {

    private static final int MAX_LENGTH = 128;

    /** Create a new {@link SQLServerTypeMapper} on top of the original featureType provided */
    public SQLServerTypeMapper(SimpleFeatureType featureType) throws CQLException {
        super(featureType, MAX_LENGTH);
        // When the geometry index will be created on the table, it will have an identifier
        // as featureType name + "_" + geometryName + "_index" so that the max
        // characters limit will be passed again. Thus the remap of the name takes
        // the length of geometry index name in consideration
        String indexPart = "_" + featureType.getGeometryDescriptor().getLocalName() + "_index";
        mappedName = originalName.getLocalPart();
        mappedName = remap(mappedName, MAX_LENGTH - indexPart.toCharArray().length);
        remapFeatureType();
    }
}
