/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.catalog.postgis;

import java.util.Map;
import java.util.Set;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.data.transform.Definition;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.gce.imagemosaic.catalog.oracle.AbstractFeatureTypeMapper;
import org.geotools.gce.imagemosaic.catalog.oracle.FeatureTypeMapper;
import org.geotools.jdbc.JDBCDataStore;

/**
 * A Postgis specific {@link FeatureTypeMapper} instance
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
public class PostgisFeatureTypeMapper extends AbstractFeatureTypeMapper {

    private static final int MAX_LENGTH = 63;

    private int srID = 0;

    /** Create a new {@link PostgisFeatureTypeMapper} on top of the original featureType provided */
    public PostgisFeatureTypeMapper(SimpleFeatureType featureType) throws CQLException {
        super(featureType, MAX_LENGTH);
        remapFeatureType();
    }

    @Override
    protected void remapGeometryAttribute(
            SimpleFeatureTypeBuilder tb, Definition definition, AttributeDescriptor descriptor, AttributeType type) {
        Map<Object, Object> userData = descriptor.getUserData();
        if (userData != null && !userData.isEmpty()) {
            Set<Object> keys = userData.keySet();
            for (Object key : keys) {
                Object value = userData.get(key);
                tb.userData(key, value);
                if (key instanceof String id) {
                    if (id.equalsIgnoreCase(JDBCDataStore.JDBC_NATIVE_SRID) && value != null) {
                        srID = (Integer) value;
                    }
                }
            }
        }
        super.remapGeometryAttribute(tb, definition, descriptor, type);
    }

    int getSrID() {
        return srID;
    }

    void setSrID(int srID) {
        this.srID = srID;
    }
}
