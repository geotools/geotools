/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.wfs.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.geotools.api.data.DataAccess;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.ServiceInfo;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.data.complex.feature.type.ComplexFeatureTypeFactoryImpl;
import org.geotools.data.complex.feature.type.FeatureTypeRegistry;
import org.geotools.data.complex.util.EmfComplexFeatureReader;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.feature.NameImpl;
import org.geotools.gml3.complex.GmlFeatureTypeRegistryConfiguration;
import org.geotools.util.logging.Logging;
import org.geotools.xml.resolver.SchemaCache;
import org.geotools.xml.resolver.SchemaResolver;
import org.geotools.xsd.SchemaIndex;

/**
 * DataAccess for WFS with complex features.
 *
 * <p>Using a schema parser with local storage. Could be set to a file location to avoid repeated
 * downloads. To do this use the parameter {@link WFSDataAccessFactory#SCHEMA_CACHE_LOCATION}, or
 * call the function {@link #setCacheLocation(File)}.
 *
 * @author Adam Brown (Curtin University of Technology) Inspired by code from WFSContentDataStore &
 *     ContentDataStore.
 */
public class WFSContentDataAccess implements DataAccess<FeatureType, Feature> {
    /** The Web feature service client object. */
    private final WFSClient client;

    /** The schema reader used to take describe feature URL and turn it into a schema index. */
    private EmfComplexFeatureReader schemaParser;

    /** Collection of feature type descriptors. */
    private FeatureTypeRegistry typeRegistry;

    /** Backing field for the cache location file. */
    private File cacheLocation;

    /** A map of names and their QName equivalent. */
    private final Map<Name, QName> names;

    /** namespace URL of the datastore itself, or default namespace */
    protected String namespaceURI;

    private static Logger LOGGER = Logging.getLogger(WFSContentDataAccess.class);

    /**
     * The namespace URL of the datastore.
     *
     * @return The namespace URL, may be <code>null</code>.
     */
    public String getNamespaceURI() {
        return namespaceURI;
    }

    /**
     * Sets the namespace URI of the datastore.
     *
     * <p>This will be used to qualify the entries or types of the datastore.
     *
     * @param namespaceURI The namespace URI, may be <code>null</code>.
     */
    public void setNamespaceURI(String namespaceURI) {
        this.namespaceURI = namespaceURI;
    }

    /** The WFS capabilities document. */
    public WFSContentDataAccess(final WFSClient client) {
        this.client = client;
        this.names = new ConcurrentHashMap<>();
    }

    @Override
    public ServiceInfo getInfo() {
        return this.client.getInfo();
    }

    @Override
    public void createSchema(FeatureType featureType) throws IOException {
        throw new UnsupportedOperationException(
                "WFSContentDataAccess does not support createSchema.");
    }

    @Override
    public void updateSchema(Name typeName, FeatureType featureType) throws IOException {
        throw new UnsupportedOperationException(
                "WFSContentDataAccess does not support update schema.");
    }

    /** Populates the names map and returns a list of names. */
    @Override
    public List<Name> getNames() throws IOException {
        // the WFSContentDataStore version inherits an implementation of this
        // method from ContentDataStore, that method calls getTypeNames which
        // calls an abstract method (i.e. one that's implemented in
        // WFSContentDataStore) called createTypeNames(). createTypeNames, as
        // implemented in WFSContentDataStore, uses client to
        // 'getRemoteTypeNames()'.
        Set<QName> remoteTypeNames = client.getRemoteTypeNames();
        List<Name> namesList = new ArrayList<>(remoteTypeNames.size());
        for (QName remoteTypeName : remoteTypeNames) {
            Name typeName = new NameImpl(remoteTypeName);
            namesList.add(typeName);
            this.names.put(typeName, remoteTypeName);
        }

        return namesList;
    }

    /**
     * Look up a QName based on a given Name.
     *
     * @param localTypeName The local type name whose QName equivalent you'd like.
     * @return The QName that corresponds to the Name you passed in.
     */
    public QName getRemoteTypeName(Name localTypeName) throws IOException {
        if (names.isEmpty()) {
            getNames();
        }

        QName qName = names.get(localTypeName);
        if (null == qName) {
            throw new NoSuchElementException(localTypeName.toString());
        }

        return qName;
    }

    /**
     * Create the FeatureType based on a call to DescribeFeatureType. Using gt-complex to parse the
     * xsd-document, and use the schema cache.
     */
    @Override
    public FeatureType getSchema(Name name) throws IOException {
        // If there are no values in this.names it probably means that getNames
        // hasn't been called yet.
        if (this.names.isEmpty()) {
            this.getNames();
        }

        // Generate the URL for the feature request
        // (should be for a GET request):
        // -----------------------------------------
        QName qname = this.names.get(name);
        URL describeRequestURL = client.getDescribeFeatureTypeGetURL(qname);

        // Create type registry and add the schema to it:
        // ----------------------------------------------
        FeatureTypeRegistry typeRegistry = this.getFeatureTypeRegistry();
        SchemaIndex schemaIndex = this.getSchemaParser().parse(describeRequestURL);
        typeRegistry.addSchemas(schemaIndex);

        // Create the attribute type and cast it as a FeatureType:
        // -------------------------------------------------------
        AttributeDescriptor attributeDescriptor = typeRegistry.getDescriptor(name, null);
        return (FeatureType) attributeDescriptor.getType();
    }

    /**
     * Sets the location of the cache folder to be used by app-schema-resolver.
     *
     * @param cacheLocation the folder to use as the cache.
     */
    public void setCacheLocation(File cacheLocation) {
        if (schemaParser != null && this.cacheLocation == null) {
            deleteTemporaryCache();
            schemaParser = null;
            typeRegistry = null;
        }
        this.cacheLocation = cacheLocation;
    }

    @Override
    public FeatureSource<FeatureType, Feature> getFeatureSource(Name typeName) throws IOException {
        // There is an implementation of this in ContentDataStore which gets
        // inherited by WFSContentDataStore.
        FeatureSource<FeatureType, Feature> contentComplexFeatureSource =
                new WFSContentComplexFeatureSource(typeName, this.client, this);

        return contentComplexFeatureSource;
    }

    private void deleteTemporaryCache() {
        if (this.schemaParser.getResolver() != null) {
            SchemaCache cache = this.schemaParser.getResolver().getCache();
            if (cache != null) {
                try {
                    FileUtils.deleteDirectory(cache.getDirectory());
                } catch (IOException e) {
                    LOGGER.log(
                            Level.SEVERE,
                            "Failed to delete temporary directory used for wfs schema cache.",
                            e);
                }
            }
        }
    }

    @Override
    public void dispose() {
        if (this.cacheLocation == null && this.schemaParser != null) {
            deleteTemporaryCache();
        }
        this.schemaParser = null;
        this.typeRegistry = null;
    }

    /**
     * Get the schema parser, creating it first if necessary.
     *
     * @return the schema parser. Guaranteed non-null.
     */
    protected EmfComplexFeatureReader getSchemaParser() {
        if (this.schemaParser == null) {
            this.schemaParser = EmfComplexFeatureReader.newInstance();

            SchemaResolver appSchemaResolver = null;
            if (this.cacheLocation == null) {
                File temporaryCache =
                        new File(
                                FileUtils.getTempDirectory(),
                                "wfs_cache_" + RandomStringUtils.randomAlphanumeric(5));
                if (temporaryCache.mkdir()) {
                    appSchemaResolver =
                            new SchemaResolver(new SchemaCache(temporaryCache, true, true));
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("Using temporary cache: " + temporaryCache.getAbsolutePath());
                    }
                } else {
                    LOGGER.warning(
                            "Couldn't create temporary directory for wfs cache at location: "
                                    + temporaryCache.getAbsolutePath());
                }
            } else {
                appSchemaResolver =
                        new SchemaResolver(
                                new SchemaCache(
                                        this.cacheLocation,
                                        /* download: */ true,
                                        /* keepQuery: */ true));
            }

            if (appSchemaResolver != null) {
                this.schemaParser.setResolver(appSchemaResolver);
            }
        }

        return this.schemaParser;
    }

    /**
     * Get the type registry, creating it first if necessary.
     *
     * @return the type registry. Guaranteed non-null.
     */
    private FeatureTypeRegistry getFeatureTypeRegistry() {
        if (this.typeRegistry == null) {
            this.typeRegistry =
                    new FeatureTypeRegistry(
                            new ComplexFeatureTypeFactoryImpl(),
                            new GmlFeatureTypeRegistryConfiguration(null));
        }

        return this.typeRegistry;
    }

    @Override
    public void removeSchema(Name typeName) throws IOException {
        throw new UnsupportedOperationException(
                "WFSContentDataAccess does not support remove schema.");
    }
}
