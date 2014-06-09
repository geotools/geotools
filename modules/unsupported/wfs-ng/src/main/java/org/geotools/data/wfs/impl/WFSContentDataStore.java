package org.geotools.data.wfs.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.XMLConstants;
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
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.impl.PackedCoordinateSequenceFactory;

public class WFSContentDataStore extends ContentDataStore {

    public static final String STORED_QUERY_CONFIGURATION_HINT = "WFS_NG_STORED_QUERY_CONFIGURATION";

    private final WFSClient client;

    private final Map<Name, QName> names;

    private final Map<QName, FeatureType> remoteFeatureTypes;
    private final Map<String, StoredQueryDescriptionType> storedQueryDescriptionTypes;

    private ListStoredQueriesResponseType remoteStoredQueries;

    protected Map<String, String> configuredStoredQueries =
            new ConcurrentHashMap<String, String>();

    public WFSContentDataStore(final WFSClient client) {
        this.client = client;
        this.names = new ConcurrentHashMap<Name, QName>();
        this.remoteFeatureTypes = new ConcurrentHashMap<QName, FeatureType>();
        this.storedQueryDescriptionTypes = new ConcurrentHashMap<String, StoredQueryDescriptionType>();
        // default factories
        setFilterFactory(CommonFactoryFinder.getFilterFactory(null));
        setGeometryFactory(new GeometryFactory(PackedCoordinateSequenceFactory.DOUBLE_FACTORY));
        setFeatureTypeFactory(new FeatureTypeFactoryImpl());
        setFeatureFactory(CommonFactoryFinder.getFeatureFactory(null));
    }

    /**
     * @see WFSDataStore#getInfo()
     */
    @Override
    public WFSServiceInfo getInfo() {
        return client.getInfo();
    }

    @Override
    protected WFSContentState createContentState(ContentEntry entry) {
        return new WFSContentState(entry);
    }

    /**
     * @see org.geotools.data.store.ContentDataStore#createTypeNames()
     */
    @Override
    protected List<Name> createTypeNames() throws IOException {
        String namespaceURI = getNamespaceURI();

        Set<QName> remoteTypeNames = client.getRemoteTypeNames();
        List<Name> names = new ArrayList<Name>(remoteTypeNames.size());
        for (QName remoteTypeName : remoteTypeNames) {
            String localTypeName = remoteTypeName.getLocalPart();
            if (!XMLConstants.DEFAULT_NS_PREFIX.equals(remoteTypeName.getPrefix())) {
                localTypeName = remoteTypeName.getPrefix() + "_" + localTypeName;
            }
            Name typeName = new NameImpl(namespaceURI, localTypeName);
            names.add(typeName);
            this.names.put(typeName, remoteTypeName);
        }

        for(Entry<String, String> e : configuredStoredQueries.entrySet()) {
            String name = e.getKey();
            String storedQueryId = e.getValue();
            Name typeName = new NameImpl(namespaceURI, name);
            names.add(typeName);
            this.names.put(typeName, getStoredQueryReturnType(storedQueryId));
        }

        return names;
    }

    /**
     * @see WFSContentFeatureSource
     * @see WFSContentFeatureStore
     * @see WFSClient#supportsTransaction(QName)
     * @see org.geotools.data.store.ContentDataStore#createFeatureSource(org.geotools.data.store.ContentEntry)
     */
    @Override
    protected ContentFeatureSource createFeatureSource(final ContentEntry entry) throws IOException {
        ContentFeatureSource source;

        if (!isStoredQuery(entry.getName())) {
            final QName remoteTypeName = getRemoteTypeName(entry.getName());

            source = new WFSContentFeatureSource(entry, client);

            // TODO: revisit. Transactions disabled by now until resolving the strategy to use as much
            // from ContentDataStore and related classes as possible
            // if (client.supportsTransaction(remoteTypeName)) {
            // source = new WFSContentFeatureStore((WFSContentFeatureSource) source);
            // }

        } else {
            String storedQueryId = configuredStoredQueries.get(entry.getName().getLocalPart());
            StoredQueryDescriptionType desc = getStoredQueryDescriptionType(storedQueryId);

            source = new WFSStoredQueryContentFeatureSource(entry, client, desc);
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

        synchronized(this) {

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

    public StoredQueryDescriptionType getStoredQueryDescriptionType(String storedQueryId) throws IOException {

        StoredQueryDescriptionType desc = null;

        final String lockObj = storedQueryId.intern();

        synchronized (lockObj) {
            desc = storedQueryDescriptionTypes.get(storedQueryId);
            if (desc == null) {
                DescribeStoredQueriesRequest request = client.createDescribeStoredQueriesRequest();

                URI id;
                try {
                    id = new URI(storedQueryId);
                } catch(URISyntaxException use) {
                    throw new IOException(use);
                }

                request.getStoredQueryIds().add(id);

                DescribeStoredQueriesResponse response = client.issueRequest(request);

                desc = response.getStoredQueryDescriptions().get(0);
                storedQueryDescriptionTypes.put(storedQueryId, desc);
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
        throw new IOException("Unknown stored query "+storedQueryId);
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

}
