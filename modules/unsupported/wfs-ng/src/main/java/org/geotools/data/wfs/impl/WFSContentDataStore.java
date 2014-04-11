package org.geotools.data.wfs.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import net.opengis.wfs20.ListStoredQueriesResponseType;
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

    private static final String STORED_QUERY_LOCALNAME_PREFIX = "!StoredQuery_";

	private final WFSClient client;

    private final Map<Name, QName> names;

    private final Map<QName, FeatureType> remoteFeatureTypes;
  
    private ListStoredQueriesResponseType remoteStoredQueries;
    
    public WFSContentDataStore(final WFSClient client) {
        this.client = client;
        this.names = new ConcurrentHashMap<Name, QName>();
        this.remoteFeatureTypes = new ConcurrentHashMap<QName, FeatureType>();

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
        
        if (client.getInfo().getVersion().equals("2.0.0")) {
        	
        	for (StoredQueryListItemType query : getStoredQueryList().getStoredQuery()) {

        		String localTypeName = query.getId();
	        	QName remoteTypeName = new QName(namespaceURI, STORED_QUERY_LOCALNAME_PREFIX + localTypeName);
	        	
	        	Name typeName = new NameImpl(namespaceURI, localTypeName);
	        	names.add(typeName);
	        	this.names.put(typeName, remoteTypeName);
        	}
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

        source = new WFSContentFeatureSource(entry, client);

        final QName remoteTypeName = getRemoteTypeName(entry.getName());
        // TODO: revisit. Transactions disabled by now until resolving the strategy to use as much
        // from ContentDataStore and related classes as possible
        // if (client.supportsTransaction(remoteTypeName)) {
        // source = new WFSContentFeatureStore((WFSContentFeatureSource) source);
        // }

        return source;
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

    private ListStoredQueriesResponseType getStoredQueryList() throws IOException {
    	
    	synchronized(this) {

    		if (remoteStoredQueries == null) {
    			
    			ListStoredQueriesRequest request = client.createListStoredQueriesRequest();
    			
    			ListStoredQueriesResponse response = client.issueRequest(request);

    			remoteStoredQueries = response.getListStoredQueriesResponse();
	    	}
    	}

		return remoteStoredQueries;
	}
    
    public FeatureType getRemoteFeatureType(final QName remoteTypeName) throws IOException {

        FeatureType remoteFeatureType;

        final String lockObj = remoteTypeName.toString().intern();

        synchronized (lockObj) {
            remoteFeatureType = remoteFeatureTypes.get(remoteTypeName);
            if (remoteFeatureType == null) {

            	if (remoteTypeName.getLocalPart().startsWith(STORED_QUERY_LOCALNAME_PREFIX)) {
            		String storedQueryId = remoteTypeName.getLocalPart().substring(STORED_QUERY_LOCALNAME_PREFIX.length());
            		remoteFeatureType = resolveStoredQueryType(storedQueryId);
            	} else {
            		remoteFeatureType = resolveSimpleFeatureType(remoteTypeName);
            	}

                remoteFeatureTypes.put(remoteTypeName, remoteFeatureType);
            }
        }

        return remoteFeatureType;
    }

	private FeatureType resolveStoredQueryType(String storedQueryId) throws IOException {
		DescribeStoredQueriesRequest request = client.createDescribeStoredQueriesRequest();

		URI id;
		try {
			id = new URI(storedQueryId);
		} catch(URISyntaxException use) {
			throw new IOException(use);
		}

		request.getStoredQueryIds().add(id);

		DescribeStoredQueriesResponse response = client.issueRequest(request);

		List<FeatureType> featureTypes = response.getFeatureTypes(storedQueryId);

		// TODO: a selection mechanism would be nice...
		return featureTypes.get(0);
	}

	private FeatureType resolveSimpleFeatureType(final QName remoteTypeName)
	        throws IOException {
		FeatureType remoteFeatureType;
		DescribeFeatureTypeRequest request = client.createDescribeFeatureTypeRequest();
		request.setTypeName(remoteTypeName);

		DescribeFeatureTypeResponse response = client.issueRequest(request);

		remoteFeatureType = response.getFeatureType();
		return remoteFeatureType;
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

}
