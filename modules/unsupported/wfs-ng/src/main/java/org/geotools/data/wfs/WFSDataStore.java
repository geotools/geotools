/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.xml.namespace.QName;
import net.opengis.wfs20.ListStoredQueriesResponseType;
import net.opengis.wfs20.StoredQueryDescriptionType;
import net.opengis.wfs20.StoredQueryListItemType;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.data.wfs.internal.DescribeFeatureTypeRequest;
import org.geotools.data.wfs.internal.DescribeFeatureTypeResponse;
import org.geotools.data.wfs.internal.DescribeStoredQueriesRequest;
import org.geotools.data.wfs.internal.DescribeStoredQueriesResponse;
import org.geotools.data.wfs.internal.ListStoredQueriesRequest;
import org.geotools.data.wfs.internal.ListStoredQueriesResponse;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.data.wfs.internal.parsers.EmfAppSchemaParser;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.impl.PackedCoordinateSequenceFactory;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;

public class WFSDataStore extends ContentDataStore {

    public static final String STORED_QUERY_CONFIGURATION_HINT =
            "WFS_NG_STORED_QUERY_CONFIGURATION";

    private final WFSClient client;

    private final Map<Name, QName> names;

    private final Map<QName, FeatureType> remoteFeatureTypes;
    private final Map<String, StoredQueryDescriptionType> storedQueryDescriptionTypes;
    private ReadWriteLock storedQueryDescriptionTypesLock;

    private ListStoredQueriesResponseType remoteStoredQueries;

    protected Map<String, String> configuredStoredQueries = new ConcurrentHashMap<String, String>();

    public WFSDataStore(final WFSClient client) {
        this.client = client;
        this.names = new ConcurrentHashMap<Name, QName>();
        this.remoteFeatureTypes = new ConcurrentHashMap<QName, FeatureType>();
        this.storedQueryDescriptionTypes =
                new ConcurrentHashMap<String, StoredQueryDescriptionType>();
        this.storedQueryDescriptionTypesLock = new ReentrantReadWriteLock();
        // default factories
        setFilterFactory(CommonFactoryFinder.getFilterFactory(null));
        setGeometryFactory(new GeometryFactory(PackedCoordinateSequenceFactory.DOUBLE_FACTORY));
        setFeatureTypeFactory(new FeatureTypeFactoryImpl());
        setFeatureFactory(CommonFactoryFinder.getFeatureFactory(null));
    }

    /** @see WFSDataStore#getInfo() */
    @Override
    public WFSServiceInfo getInfo() {
        return client.getInfo();
    }

    @Override
    protected WFSContentState createContentState(ContentEntry entry) {
        return new WFSContentState(entry);
    }

    /** @see org.geotools.data.store.ContentDataStore#createTypeNames() */
    @Override
    protected List<Name> createTypeNames() throws IOException {
        String namespaceURI = getNamespaceURI();

        Set<QName> remoteTypeNames = client.getRemoteTypeNames();
        List<Name> names = new ArrayList<Name>(remoteTypeNames.size());
        for (QName remoteTypeName : remoteTypeNames) {
            String localTypeName = client.getConfig().localTypeName(remoteTypeName);
            Name typeName =
                    new NameImpl(
                            namespaceURI == null ? remoteTypeName.getNamespaceURI() : namespaceURI,
                            localTypeName);

            names.add(typeName);
            this.names.put(typeName, remoteTypeName);
        }

        for (Entry<String, String> e : configuredStoredQueries.entrySet()) {
            String name = e.getKey();
            String storedQueryId = e.getValue();
            Name typeName = new NameImpl(namespaceURI, name);
            names.add(typeName);
            this.names.put(typeName, getStoredQueryReturnType(storedQueryId));
        }

        return names;
    }

    /**
     * @see WFSFeatureSource
     * @see WFSFeatureStore
     * @see WFSClient#supportsTransaction(QName)
     * @see
     *     org.geotools.data.store.ContentDataStore#createFeatureSource(org.geotools.data.store.ContentEntry)
     */
    @Override
    protected ContentFeatureSource createFeatureSource(final ContentEntry entry)
            throws IOException {
        ContentFeatureSource source;

        if (!isStoredQuery(entry.getName())) {
            final QName remoteTypeName = getRemoteTypeName(entry.getName());

            source = new WFSFeatureSource(entry, client);

            if (client.supportsTransaction(remoteTypeName)) {
                source = new WFSFeatureStore((WFSFeatureSource) source);
            }

        } else {
            String storedQueryId = configuredStoredQueries.get(entry.getName().getLocalPart());
            StoredQueryDescriptionType desc = getStoredQueryDescriptionType(storedQueryId);

            source = new WFSStoredQueryFeatureSource(entry, client, desc);
        }

        return source;
    }

    private boolean isStoredQuery(Name name) {
        return configuredStoredQueries.containsKey(name.getLocalPart());
    }

    public QName getRemoteTypeName(Name localTypeName) throws IOException {
        if (names.isEmpty()) {
            createTypeNames();
        }
        QName qName = names.get(localTypeName);
        if (null == qName) {
            throw new NoSuchElementException(localTypeName.toString());
        }
        return qName;
    }

    public ListStoredQueriesResponseType getStoredQueryListResponse() throws IOException {

        synchronized (this) {
            if (remoteStoredQueries == null) {

                ListStoredQueriesRequest request = client.createListStoredQueriesRequest();

                ListStoredQueriesResponse response = client.issueRequest(request);

                remoteStoredQueries = response.getListStoredQueriesResponse();
            }
        }

        return remoteStoredQueries;
    }

    public boolean supportsStoredQueries() {
        return client.supportsStoredQueries();
    }

    public FeatureType getRemoteFeatureType(final QName remoteTypeName) throws IOException {

        FeatureType remoteFeatureType;

        final String lockObj = remoteTypeName.toString().intern();

        synchronized (lockObj) {
            remoteFeatureType = remoteFeatureTypes.get(remoteTypeName);
            if (remoteFeatureType == null) {

                DescribeFeatureTypeRequest request = client.createDescribeFeatureTypeRequest();
                request.setTypeName(remoteTypeName);

                DescribeFeatureTypeResponse response = client.issueRequest(request);

                remoteFeatureType = response.getFeatureType();

                remoteFeatureTypes.put(remoteTypeName, remoteFeatureType);
            }
        }

        return remoteFeatureType;
    }

    public StoredQueryDescriptionType getStoredQueryDescriptionType(String storedQueryId)
            throws IOException {

        StoredQueryDescriptionType desc = null;

        storedQueryDescriptionTypesLock.readLock().lock();
        desc = storedQueryDescriptionTypes.get(storedQueryId);
        storedQueryDescriptionTypesLock.readLock().unlock();

        if (desc == null) {
            storedQueryDescriptionTypesLock.writeLock().lock();

            // Make sure another thread has not retrieved this information while waiting for locks
            desc = storedQueryDescriptionTypes.get(storedQueryId);

            try {
                // If desc is still null, retrieve it
                if (desc == null) {
                    DescribeStoredQueriesRequest request =
                            client.createDescribeStoredQueriesRequest();

                    URI id;
                    try {
                        id = new URI(storedQueryId);
                    } catch (URISyntaxException use) {
                        throw new IOException(use);
                    }

                    request.getStoredQueryIds().add(id);

                    DescribeStoredQueriesResponse response = client.issueRequest(request);

                    desc = response.getStoredQueryDescriptions().get(0);
                    storedQueryDescriptionTypes.put(storedQueryId, desc);
                }
            } finally {
                storedQueryDescriptionTypesLock.writeLock().unlock();
            }
        }
        return desc;
    }

    public SimpleFeatureType getRemoteSimpleFeatureType(final QName remoteTypeName)
            throws IOException {

        final FeatureType remoteFeatureType = getRemoteFeatureType(remoteTypeName);
        final SimpleFeatureType remoteSimpleFeatureType;
        // remove GML properties
        remoteSimpleFeatureType = EmfAppSchemaParser.toSimpleFeatureType(remoteFeatureType);

        return remoteSimpleFeatureType;
    }

    public WFSClient getWfsClient() {
        return client;
    }

    public Name addStoredQuery(String localName, String storedQueryId) throws IOException {
        Name name = new NameImpl(namespaceURI, localName);
        try {
            configuredStoredQueries.put(localName, storedQueryId);
            // the new stored query might be overriding a previous definition
            removeEntry(name);
            getStoredQuerySchema(storedQueryId);

            this.names.put(name, getStoredQueryReturnType(storedQueryId));

            return name;
        } catch (IOException e) {
            configuredStoredQueries.remove(localName);
            throw e;
        }
    }

    public QName getStoredQueryReturnType(String storedQueryId) throws IOException {
        ListStoredQueriesResponseType list = getStoredQueryListResponse();
        for (StoredQueryListItemType query : list.getStoredQuery()) {

            if (query.getId().equals(storedQueryId)) {
                return query.getReturnFeatureType().get(0);
            }
        }
        throw new IOException("Unknown stored query " + storedQueryId);
    }

    public SimpleFeatureType getStoredQuerySchema(String storedQueryId) throws IOException {
        QName returnType = getStoredQueryReturnType(storedQueryId);
        return getRemoteSimpleFeatureType(returnType);
    }

    public Map<String, String> getConfiguredStoredQueries() {
        return configuredStoredQueries;
    }

    public void removeStoredQuery(String localName) {
        Name name = new NameImpl(namespaceURI, localName);
        this.names.remove(name);
        configuredStoredQueries.remove(localName);
    }

    public URL getCapabilitiesURL() {
        return client.getCapabilitiesURL();
    }
}
