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

import java.util.*;
import org.geotools.data.transform.Definition;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.gce.imagemosaic.catalog.AbstractFeatureTypeMapper;
import org.geotools.gce.imagemosaic.catalog.DataStoreWrapper;
import org.geotools.jdbc.JDBCDataStore;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;

/**
 * Specific SQLServer implementation for a {@link DataStoreWrapper} By default, SQLServer
 * identifiers can't be longer than 128 chars. See <a
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

    /**
     * Remap the original featureType on top of the available definitions to create the Postgis
     * specific featureType
     */
    protected void remapFeatureType() {
        final SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName(mappedName);
        final List<AttributeDescriptor> descriptors = wrappedFeatureType.getAttributeDescriptors();

        // Loop over the attribute descriptors
        for (AttributeDescriptor descriptor : descriptors) {

            // Get main properties (name and type)
            Name name = descriptor.getName();
            Definition definition = definitionsMapping.get(name);
            AttributeType type = descriptor.getType();
            if (type instanceof GeometryType) {
                coordinateReferenceSystem = ((GeometryType) type).getCoordinateReferenceSystem();
                Map<Object, Object> userData = descriptor.getUserData();
                if (userData != null && !userData.isEmpty()) {
                    Set<Object> keys = userData.keySet();
                    for (Object key : keys) {
                        Object value = userData.get(key);
                        tb.userData(key, value);
                        if (key instanceof String) {
                            String id = (String) key;
                            if (id.equalsIgnoreCase(JDBCDataStore.JDBC_NATIVE_SRID)
                                    && value != null) {
                                srID = (Integer) value;
                            }
                        }
                    }
                }
                tb.add(
                        definition.getExpression().toString(),
                        definition.getBinding(),
                        coordinateReferenceSystem);
            } else {
                tb.add(definition.getExpression().toString(), definition.getBinding());
            }
        }
        mappedFeatureType = tb.buildFeatureType();
    }

    @Override
    public String remap(String name) {
        return remap(name, MAX_LENGTH);
    }

    public String remap(String name, int maxLength) {
        String mappedName = name;
        mappedName =
                mappedName.length() >= maxLength ? mappedName.substring(0, maxLength) : mappedName;
        return mappedName;
    }
}
