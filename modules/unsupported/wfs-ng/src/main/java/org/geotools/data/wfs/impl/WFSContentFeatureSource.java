package org.geotools.data.wfs.impl;

import java.io.IOException;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.DiffFeatureReader;
import org.geotools.data.EmptyFeatureReader;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.ReTypeFeatureReader;
import org.geotools.data.Transaction;
import org.geotools.data.Transaction.State;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.data.wfs.internal.GetFeatureParser;
import org.geotools.data.wfs.internal.GetFeatureRequest;
import org.geotools.data.wfs.internal.GetFeatureRequest.ResultType;
import org.geotools.data.wfs.internal.GetFeatureResponse;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.factory.Hints;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.logging.Logging;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.impl.PackedCoordinateSequenceFactory;

class WFSContentFeatureSource extends ContentFeatureSource {

    private static final Logger LOGGER = Logging.getLogger(WFSContentFeatureSource.class);

    private final WFSClient client;

    public WFSContentFeatureSource(final ContentEntry entry, final WFSClient client) {
        super(entry, null);
        this.client = client;
    }

    WFSClient getWfs() {
        return client;
    }

    @Override
    protected boolean handleVisitor(Query query, FeatureVisitor visitor) throws IOException {
        return false;
    }

    @Override
    protected boolean canReproject() {
        return true;
    }

    @Override
    protected boolean canOffset() {
        return false;// TODO: check with the WFS client
    }

    /**
     * @return {@code true}
     * @see org.geotools.data.store.ContentFeatureSource#canSort()
     */
    @Override
    protected boolean canSort() {
        return client.canSort();
    }

    /**
     * @return {@code true}
     * @see org.geotools.data.store.ContentFeatureSource#canRetype()
     */
    @Override
    protected boolean canRetype() {
        return client.canRetype();
    }

    /**
     * @return {@code true}
     * @see org.geotools.data.store.ContentFeatureSource#canFilter()
     */
    @Override
    protected boolean canFilter() {
        return client.canFilter();
    }

    /**
     * @return {@code true}
     * @see org.geotools.data.store.ContentFeatureSource#canLimit()
     */
    @Override
    protected boolean canLimit() {
        return client.canLimit();
    }

    @Override
    public WFSContentDataStore getDataStore() {
        return (WFSContentDataStore) super.getDataStore();
    }

    /**
     * @return the WFS advertised bounds of the feature type if
     *         {@code Filter.INCLUDE ==  query.getFilter()}, reprojected to the Query's crs, or
     *         {@code null} otherwise as it would be too expensive to calculate.
     * @see FeatureSource#getBounds(Query)
     * @see org.geotools.data.store.ContentFeatureSource#getBoundsInternal(org.geotools.data.Query)
     */
    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        if (!Filter.INCLUDE.equals(query.getFilter())) {
            return null;
        }

        final QName remoteTypeName = getRemoteTypeName();

        final CoordinateReferenceSystem targetCrs;
        if (null == query.getCoordinateSystem()) {
            targetCrs = client.getDefaultCRS(remoteTypeName);
        } else {
            targetCrs = query.getCoordinateSystem();
        }

        ReferencedEnvelope bounds = client.getBounds(remoteTypeName, targetCrs);
        return bounds;
    }

    /**
     * @return the remote WFS advertised number of features for the given query only if the query
     *         filter is fully supported AND the wfs returns that information in as an attribute of
     *         the FeatureCollection (since the request is performed with resultType=hits),
     *         otherwise {@code -1} as it would be too expensive to calculate.
     * @see FeatureSource#getCount(Query)
     * @see org.geotools.data.store.ContentFeatureSource#getCountInternal(org.geotools.data.Query)
     */
    @Override
    protected int getCountInternal(Query query) throws IOException {
        if (!client.canCount()) {
            return -1;
        }

        GetFeatureRequest request = createGetFeature(query, ResultType.HITS);

        GetFeatureResponse response = client.issueRequest(request);
        GetFeatureParser featureParser = response.getFeatures(null);
        int resultCount = featureParser.getNumberOfFeatures();
        return resultCount;
    }

    private GetFeatureRequest createGetFeature(Query query, ResultType resultType)
            throws IOException {
        GetFeatureRequest request = client.createGetFeatureRequest();

        final WFSContentDataStore dataStore = getDataStore();

        final QName remoteTypeName = dataStore.getRemoteTypeName(getEntry().getName());
        final SimpleFeatureType remoteSimpleFeatureType;
        remoteSimpleFeatureType = dataStore.getRemoteSimpleFeatureType(remoteTypeName);

        request.setTypeName(remoteTypeName);
        request.setFullType(remoteSimpleFeatureType);

        request.setFilter(query.getFilter());
        request.setResultType(resultType);
        int maxFeatures = query.getMaxFeatures();
        if (Integer.MAX_VALUE > maxFeatures) {
            request.setMaxFeatures(maxFeatures);
        }
        // let the request decide request.setOutputFormat(outputFormat);
        request.setPropertyNames(query.getPropertyNames());
        request.setSortBy(query.getSortBy());

        String srsName = null;
        CoordinateReferenceSystem crs = query.getCoordinateSystem();
        if (null != crs) {
            System.err.println("TODO: don't forget to set the query CRS");
        }
        request.setSrsName(srsName);
        return request;
    }

    /**
     * @see FeatureSource#getFeatures(Query)
     * @see org.geotools.data.store.ContentFeatureSource#getReaderInternal(org.geotools.data.Query)
     */
    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query localQuery)
            throws IOException {

        if (Filter.EXCLUDE.equals(localQuery.getFilter())) {
            return new EmptyFeatureReader<SimpleFeatureType, SimpleFeature>(getSchema());
        }

        GetFeatureRequest request = createGetFeature(localQuery, ResultType.RESULTS);

        final SimpleFeatureType contentType = getQueryType(localQuery);
        request.setQueryType(contentType);

        GetFeatureResponse response = client.issueRequest(request);

        GeometryFactory geometryFactory = findGeometryFactory(localQuery.getHints());
        GetFeatureParser features = response.getSimpleFeatures(geometryFactory);

        FeatureReader<SimpleFeatureType, SimpleFeature> reader;
        reader = new WFSFeatureReader(features);

        if (!reader.hasNext()) {
            return new EmptyFeatureReader<SimpleFeatureType, SimpleFeature>(contentType);
        }

        final SimpleFeatureType readerType = reader.getFeatureType();
        if (!contentType.equals(readerType)) {
            final boolean cloneContents = false;
            reader = new ReTypeFeatureReader(reader, contentType, cloneContents);
        }

        Transaction transaction = getTransaction();
        if (!Transaction.AUTO_COMMIT.equals(transaction)) {
            ContentEntry entry = getEntry();
            State state = transaction.getState(entry);
            WFSLocalTransactionState wfsState = (WFSLocalTransactionState) state;
            if (wfsState != null) {
                WFSDiff diff = wfsState.getDiff();
                reader = new DiffFeatureReader<SimpleFeatureType, SimpleFeature>(reader, diff);
            }
        }
        return reader;
    }

    private GeometryFactory findGeometryFactory(Hints hints) {
        GeometryFactory geomFactory = (GeometryFactory) hints.get(Hints.JTS_GEOMETRY_FACTORY);
        if (geomFactory == null) {
            CoordinateSequenceFactory seqFac;
            seqFac = (CoordinateSequenceFactory) hints.get(Hints.JTS_COORDINATE_SEQUENCE_FACTORY);
            if (seqFac == null) {
                seqFac = PackedCoordinateSequenceFactory.DOUBLE_FACTORY;
            }
            geomFactory = new GeometryFactory(seqFac);
        }
        return geomFactory;
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        final WFSContentDataStore dataStore = getDataStore();

        final Name localTypeName = getEntry().getName();
        final QName remoteTypeName = dataStore.getRemoteTypeName(localTypeName);

        final SimpleFeatureType remoteSimpleFeatureType;
        remoteSimpleFeatureType = dataStore.getRemoteSimpleFeatureType(remoteTypeName);

        // adapt the feature type name
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.init(remoteSimpleFeatureType);
        builder.setName(localTypeName);
        String namespaceOverride = entry.getName().getNamespaceURI();

        if (namespaceOverride != null) {
            builder.setNamespaceURI(namespaceOverride);
        }

        GeometryDescriptor defaultGeometry = remoteSimpleFeatureType.getGeometryDescriptor();
        if (defaultGeometry != null) {
            builder.setDefaultGeometry(defaultGeometry.getLocalName());
            builder.setCRS(defaultGeometry.getCoordinateReferenceSystem());
        }
        final SimpleFeatureType adaptedFeatureType = builder.buildFeatureType();
        return adaptedFeatureType;
    }

    public QName getRemoteTypeName() throws IOException {
        Name localTypeName = getEntry().getName();
        QName remoteTypeName = getDataStore().getRemoteTypeName(localTypeName);
        return remoteTypeName;
    }

    //
    // /**
    // * Checks if the query requested CRS is supported by the query feature type and if not, adapts
    // * the query to the feature type default CRS, returning the CRS identifier to use for the WFS
    // * query.
    // * <p>
    // * If the query CRS is not advertised as supported in the WFS capabilities for the requested
    // * feature type, the query filter is modified so that any geometry literal is reprojected to
    // the
    // * default CRS for the feature type, otherwise the query is not modified at all. In any case,
    // * the crs identifier to actually use in the WFS GetFeature operation is returned.
    // * </p>
    // *
    // * @param query
    // * @return
    // * @throws IOException
    // */
    // private String adaptQueryForSupportedCrs(Query query) throws IOException {
    //
    // final String localTypeName = getEntry().getTypeName();
    // // The CRS the query is performed in
    // final CoordinateReferenceSystem queryCrs = query.getCoordinateSystem();
    // final String defaultCrs = client.getDefaultCRS(localTypeName);
    //
    // if (queryCrs == null) {
    // LOGGER.warning("Query does not provide a CRS, using default: " + query);
    // return defaultCrs;
    // }
    //
    // String epsgCode;
    //
    // final CoordinateReferenceSystem crsNative = getFeatureTypeCRS(localTypeName);
    //
    // if (CRS.equalsIgnoreMetadata(queryCrs, crsNative)) {
    // epsgCode = defaultCrs;
    // LOGGER.fine("request and native crs for " + localTypeName + " are the same: "
    // + epsgCode);
    // } else {
    // boolean transform = false;
    // epsgCode = GML2EncodingUtils.epsgCode(queryCrs);
    // if (epsgCode == null) {
    // LOGGER.fine("Can't find the identifier for the request CRS, "
    // + "query will be performed in native CRS");
    // transform = true;
    // } else {
    // epsgCode = "EPSG:" + epsgCode;
    // LOGGER.fine("Request CRS is " + epsgCode + ", checking if its supported for "
    // + localTypeName);
    //
    // Set<String> supportedCRSIdentifiers = client
    // .getSupportedCRSIdentifiers(localTypeName);
    // if (supportedCRSIdentifiers.contains(epsgCode)) {
    // LOGGER.fine(epsgCode + " is supported, request will be performed asking "
    // + "for reprojection over it");
    // } else {
    // LOGGER.fine(epsgCode + " is not supported for " + localTypeName
    // + ". Query will be adapted to default CRS " + defaultCrs);
    // transform = true;
    // }
    // if (transform) {
    // epsgCode = defaultCrs;
    // FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    // SimpleFeatureType ftype = getSchema();
    // ReprojectingFilterVisitor visitor = new ReprojectingFilterVisitor(ff, ftype);
    // Filter filter = query.getFilter();
    // Filter reprojectedFilter = (Filter) filter.accept(visitor, null);
    // if (LOGGER.isLoggable(Level.FINER)) {
    // LOGGER.finer("Original Filter: " + filter + "\nReprojected filter: "
    // + reprojectedFilter);
    // }
    // LOGGER.fine("Query filter reprojected to native CRS for " + localTypeName);
    // query.setFilter(reprojectedFilter);
    // }
    // }
    // }
    // return epsgCode;
    // }

    /**
     * Returns the feature type that shall result of issueing the given request, adapting the
     * original feature type for the request's type name in terms of the query CRS and requested
     * attributes.
     * 
     * @param query
     * @return
     * @throws IOException
     */
    SimpleFeatureType getQueryType(final Query query) throws IOException {

        final SimpleFeatureType featureType = getSchema();
        final CoordinateReferenceSystem coordinateSystemReproject = query
                .getCoordinateSystemReproject();

        String[] propertyNames = query.getPropertyNames();

        SimpleFeatureType queryType = featureType;
        if (propertyNames != null && propertyNames.length > 0) {
            try {
                queryType = DataUtilities.createSubType(queryType, propertyNames);
            } catch (SchemaException e) {
                throw new DataSourceException(e);
            }
        } else {
            propertyNames = DataUtilities.attributeNames(featureType);
        }

        if (coordinateSystemReproject != null) {
            try {
                queryType = DataUtilities.createSubType(queryType, propertyNames,
                        coordinateSystemReproject);
            } catch (SchemaException e) {
                throw new DataSourceException(e);
            }
        }

        return queryType;
    }

}
