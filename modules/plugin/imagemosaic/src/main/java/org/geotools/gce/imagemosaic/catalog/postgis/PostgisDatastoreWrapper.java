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

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.transform.Definition;
import org.geotools.gce.imagemosaic.catalog.oracle.DataStoreWrapper;
import org.geotools.gce.imagemosaic.catalog.oracle.FeatureTypeMapper;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Specific Postgis implementation for a {@link DataStoreWrapper} By default, Postgresql identifiers
 * can't be longer than 63 chars. See <a
 * href="http://www.postgresql.org/docs/9.3/static/sql-syntax-lexical.html#SQL-SYNTAX-IDENTIFIERS">SQL
 * Syntax identifiers</a>
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
public class PostgisDatastoreWrapper extends DataStoreWrapper {

    static final String SRID = "SRID";

    public PostgisDatastoreWrapper(DataStore datastore, String location, String subFolderName) {
        super(datastore, location, subFolderName);
    }

    public PostgisDatastoreWrapper(DataStore datastore, String location) {
        super(datastore, location);
    }

    /**
     * Return a specific {@link FeatureTypeMapper} by parsing mapping properties contained within
     * the specified {@link Properties} object
     */
    protected FeatureTypeMapper getFeatureTypeMapper(final Properties props) throws Exception {
        FeatureTypeMapper mapper = super.getFeatureTypeMapper(props);
        Object srID = props.get(SRID);
        if (srID != null) {
            ((PostgisFeatureTypeMapper) mapper).setSrID(Integer.valueOf((String) srID));
        }
        return mapper;
    }

    @Override
    protected FeatureTypeMapper getFeatureTypeMapper(SimpleFeatureType featureType)
            throws Exception {
        return new PostgisFeatureTypeMapper(featureType);
    }

    @Override
    protected SimpleFeatureSource transformFeatureStore(
            SimpleFeatureStore store, FeatureTypeMapper mapper) throws IOException {
        SimpleFeatureSource transformedSource = mapper.getSimpleFeatureSource();
        if (transformedSource != null) {
            return transformedSource;
        } else {
            transformedSource =
                    (SimpleFeatureSource)
                            new PostgisTransformFeatureStore(
                                    store, mapper.getName(), mapper.getDefinitions(), datastore);
            ((PostgisFeatureTypeMapper) mapper).setSimpleFeatureSource(transformedSource);
            return transformedSource;
        }
    }

    @Override
    protected void storeMapper(FeatureTypeMapper mapper) {
        final Properties properties = new Properties();
        final String typeName = mapper.getName().toString();
        properties.setProperty(NAME, typeName);
        properties.setProperty(MAPPEDNAME, mapper.getMappedName().toString());
        final List<Definition> definitions = mapper.getDefinitions();
        final StringBuilder builder = new StringBuilder();

        // Populating schema
        for (Definition definition : definitions) {
            builder.append(definition.getName())
                    .append(":")
                    .append(definition.getBinding().getName())
                    .append(",");
        }
        String schema = builder.toString();
        schema = schema.substring(0, schema.length() - 1);
        properties.setProperty(SCHEMA, schema);
        properties.setProperty(
                COORDINATE_REFERENCE_SYSTEM, mapper.getCoordinateReferenceSystem().toWKT());
        properties.setProperty(
                SRID, Integer.toString(((PostgisFeatureTypeMapper) mapper).getSrID()));
        // Storing properties
        storeProperties(properties, typeName);
    }
}
