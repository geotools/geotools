/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.geoparquet;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.feature.NameImpl;
import org.geotools.jdbc.JDBCDataStore;

/**
 * A GeoParquet DataStore implementation that decorates a JDBCDataStore.
 *
 * <p>This implementation uses lazy initialization for performance, delaying the creation of database views until they
 * are needed. This improves response time when working with large datasets, especially over remote connections.
 *
 * <p>The class overrides key methods from DataStore to intercept calls that require schema information, ensuring that
 * the necessary database views are created before the underlying JDBC store handles the request.
 */
public class GeoparquetDataStore extends ForwardingDataStore<JDBCDataStore> implements DataStore {

    /**
     * Creates a new GeoParquet datastore that delegates to the provided JDBC datastore.
     *
     * @param delegate The JDBC datastore to delegate operations to
     */
    public GeoparquetDataStore(JDBCDataStore delegate) {
        super(delegate);
    }

    /**
     * Returns the names of all feature types available in this datastore.
     *
     * <p>This implementation retrieves type names directly from the GeoParquet dialect, which maintains a mapping of
     * available GeoParquet datasets.
     *
     * @return An array of type names
     * @throws IOException If there is a problem retrieving the type names
     */
    @Override
    public String[] getTypeNames() throws IOException {
        return getSQLDialect().getTypeNames().toArray(String[]::new);
    }

    /**
     * Returns a list of qualified names for all feature types in this datastore.
     *
     * <p>This implementation creates proper Name objects for each type name, using the datastore's namespace if
     * available.
     *
     * @return A list of qualified names
     * @throws IOException If there is a problem retrieving the names
     */
    @Override
    public List<Name> getNames() throws IOException {
        String namespaceURI = delegate.getNamespaceURI();
        return Arrays.stream(getTypeNames())
                .map(localName -> new NameImpl(namespaceURI, localName))
                .collect(Collectors.toList());
    }

    /**
     * Returns the feature type schema for the given type name.
     *
     * <p>This implementation ensures the database view exists for the requested schema before delegating to the JDBC
     * datastore.
     *
     * @param typeName The name of the feature type
     * @return The feature type schema
     * @throws IOException If there is a problem retrieving the schema
     */
    @Override
    public SimpleFeatureType getSchema(String typeName) throws IOException {
        return getSchema(new NameImpl(delegate.getNamespaceURI(), typeName));
    }

    /**
     * Returns a feature source for the given type name.
     *
     * <p>This implementation ensures the database view exists for the requested feature source before delegating to the
     * JDBC datastore.
     *
     * @param typeName The name of the feature type
     * @return A feature source for reading features
     * @throws IOException If there is a problem creating the feature source
     */
    @Override
    public SimpleFeatureSource getFeatureSource(String typeName) throws IOException {
        return getFeatureSource(new NameImpl(delegate.getNamespaceURI(), typeName));
    }

    /**
     * Returns the feature type schema for the given name.
     *
     * <p>This implementation ensures the database view exists for the requested schema before delegating to the JDBC
     * datastore. It sets the CURRENT_TYPENAME thread-local variable to provide context for geometry type handling
     * during the operation.
     *
     * @param name The qualified name of the feature type
     * @return The feature type schema with correct geometry types
     * @throws IOException If there is a problem retrieving the schema
     */
    @Override
    public SimpleFeatureType getSchema(Name name) throws IOException {
        GeoParquetDialect.CURRENT_TYPENAME.set(name.getLocalPart());
        try {
            getSQLDialect().ensureViewExists(name.getLocalPart());
            SimpleFeatureType schema = delegate.getSchema(name);
            return getSQLDialect().fixGeometryTypes(schema);
        } finally {
            GeoParquetDialect.CURRENT_TYPENAME.remove();
        }
    }

    /**
     * Returns a feature source for the given qualified name.
     *
     * <p>This implementation ensures the database view exists for the requested feature source before delegating to the
     * JDBC datastore. It sets the CURRENT_TYPENAME thread-local variable to provide context for geometry type handling
     * during the operation and wraps the result in an OverridingFeatureSource that provides the correct geometry types.
     *
     * @param typeName The qualified name of the feature type
     * @return A feature source for reading features with correct geometry types
     * @throws IOException If there is a problem creating the feature source
     */
    @Override
    public SimpleFeatureSource getFeatureSource(Name typeName) throws IOException {
        GeoParquetDialect.CURRENT_TYPENAME.set(typeName.getLocalPart());
        try {
            getSQLDialect().ensureViewExists(typeName.getLocalPart());
            SimpleFeatureSource featureSource = delegate.getFeatureSource(typeName);
            SimpleFeatureType geometryTypeNarrowedSchema = getSQLDialect().fixGeometryTypes(featureSource.getSchema());
            return new OverridingFeatureSource(featureSource, this, geometryTypeNarrowedSchema);
        } finally {
            GeoParquetDialect.CURRENT_TYPENAME.remove();
        }
    }

    /**
     * Helper method to get the GeoParquet SQL dialect from the delegate datastore.
     *
     * @return The GeoParquet SQL dialect
     */
    GeoParquetDialect getSQLDialect() {
        return (GeoParquetDialect) delegate.getSQLDialect();
    }
}
