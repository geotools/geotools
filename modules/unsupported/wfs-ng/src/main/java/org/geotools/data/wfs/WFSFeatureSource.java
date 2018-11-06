/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2014, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.DiffFeatureReader;
import org.geotools.data.EmptyFeatureReader;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.FilteringFeatureReader;
import org.geotools.data.Query;
import org.geotools.data.ReTypeFeatureReader;
import org.geotools.data.ResourceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.Transaction.State;
import org.geotools.data.crs.ForceCoordinateSystemFeatureReader;
import org.geotools.data.crs.ReprojectFeatureReader;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.data.wfs.internal.GetFeatureParser;
import org.geotools.data.wfs.internal.GetFeatureRequest;
import org.geotools.data.wfs.internal.GetFeatureRequest.ResultType;
import org.geotools.data.wfs.internal.GetFeatureResponse;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.data.wfs.internal.WFSConfig;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.gml2.bindings.GML2EncodingUtils;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.impl.PackedCoordinateSequenceFactory;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

class WFSFeatureSource extends ContentFeatureSource {

    private static final Logger LOGGER = Logging.getLogger(WFSFeatureSource.class);

    private final WFSClient client;

    public WFSFeatureSource(final ContentEntry entry, final WFSClient client) {
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
        return false; // TODO: check with the WFS client
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
    public WFSDataStore getDataStore() {
        return (WFSDataStore) super.getDataStore();
    }

    /**
     * @return the WFS advertised bounds of the feature type if {@code Filter.INCLUDE ==
     *     query.getFilter()}, reprojected to the Query's crs, or {@code null} otherwise as it would
     *     be too expensive to calculate.
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
     *     filter is fully supported AND the wfs returns that information in as an attribute of the
     *     FeatureCollection (since the request is performed with resultType=hits), otherwise {@code
     *     -1} as it would be too expensive to calculate.
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

    /**
     * Invert axis order in the given query filter, if needed.
     *
     * @param query
     */
    private void invertAxisInFilterIfNeeded(Query query, SimpleFeatureType featureType) {
        CoordinateReferenceSystem crs = query.getCoordinateSystem();
        if (crs == null) {
            crs = featureType.getCoordinateReferenceSystem();
        }
        boolean invertXY = WFSConfig.invertAxisNeeded(client.getAxisOrderFilter(), crs);
        if (invertXY) {
            invertAxisInFilter(query);
        }
    }

    private void invertAxisInFilter(Query query) {
        Filter filter = query.getFilter();

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        InvertAxisFilterVisitor visitor = new InvertAxisFilterVisitor(ff, new GeometryFactory());
        filter = (Filter) filter.accept(visitor, null);

        query.setFilter(filter);
    }

    protected GetFeatureRequest createGetFeature(Query query, ResultType resultType)
            throws IOException {
        GetFeatureRequest request = client.createGetFeatureRequest();

        final WFSDataStore dataStore = getDataStore();

        final QName remoteTypeName = dataStore.getRemoteTypeName(getEntry().getName());
        final SimpleFeatureType remoteSimpleFeatureType;
        remoteSimpleFeatureType = dataStore.getRemoteSimpleFeatureType(remoteTypeName);

        request.setTypeName(remoteTypeName);
        request.setFullType(remoteSimpleFeatureType);
        invertAxisInFilterIfNeeded(query, remoteSimpleFeatureType);
        request.setFilter(query.getFilter());
        request.setResultType(resultType);
        request.setHints(query.getHints());

        int maxFeatures = query.getMaxFeatures();
        if (Integer.MAX_VALUE > maxFeatures) {
            request.setMaxFeatures(maxFeatures);
        }
        // let the request decide request.setOutputFormat(outputFormat);
        request.setPropertyNames(query.getPropertyNames());
        request.setSortBy(query.getSortBy());

        String srsName = getSupportedSrsName(request, query);

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

        final SimpleFeatureType destType = getQueryType(localQuery, getSchema());
        final SimpleFeatureType contentType =
                getQueryType(localQuery, (SimpleFeatureType) request.getFullType());
        request.setQueryType(contentType);

        GetFeatureResponse response = client.issueRequest(request);

        GeometryFactory geometryFactory = findGeometryFactory(localQuery.getHints());
        GetFeatureParser features = response.getSimpleFeatures(geometryFactory);

        FeatureReader<SimpleFeatureType, SimpleFeature> reader;
        reader = new WFSFeatureReader(features);

        if (request.getUnsupportedFilter() != null
                && request.getUnsupportedFilter() != Filter.INCLUDE) {
            reader =
                    new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(
                            reader, request.getUnsupportedFilter());
        }

        if (!reader.hasNext()) {
            return new EmptyFeatureReader<SimpleFeatureType, SimpleFeature>(contentType);
        }

        final SimpleFeatureType readerType = reader.getFeatureType();
        if (!destType.equals(readerType)) {
            final boolean cloneContents = false;
            reader = new ReTypeFeatureReader(reader, destType, cloneContents);
        }

        reader = applyReprojectionDecorator(reader, localQuery, request);

        Transaction transaction = getTransaction();
        if (!Transaction.AUTO_COMMIT.equals(transaction)) {
            ContentEntry entry = getEntry();
            State state = transaction.getState(entry);
            WFSLocalTransactionState wfsState = (WFSLocalTransactionState) state;
            if (wfsState != null) {
                WFSDiff diff = wfsState.getDiff();
                reader =
                        new DiffFeatureReader<SimpleFeatureType, SimpleFeature>(
                                reader, diff, localQuery.getFilter());
            }
        }
        return reader;
    }

    protected String getSupportedSrsName(GetFeatureRequest request, Query query) {
        String epsgCode = GML2EncodingUtils.epsgCode(query.getCoordinateSystem());
        Set<String> supported =
                request.getStrategy().getSupportedCRSIdentifiers(request.getTypeName());
        for (String supportedSrs : supported) {
            if (supportedSrs.endsWith(":" + epsgCode)) {
                return supportedSrs;
            }
        }
        return null;
    }

    protected FeatureReader<SimpleFeatureType, SimpleFeature> applyReprojectionDecorator(
            FeatureReader<SimpleFeatureType, SimpleFeature> reader,
            Query query,
            GetFeatureRequest request) {
        FeatureReader<SimpleFeatureType, SimpleFeature> tmp = reader;
        if (query.getCoordinateSystem() != null
                && !query.getCoordinateSystem()
                        .equals(reader.getFeatureType().getCoordinateReferenceSystem())) {
            if (request.getSrsName() != null) {
                try {
                    reader =
                            new ForceCoordinateSystemFeatureReader(
                                    reader, query.getCoordinateSystem());
                } catch (SchemaException e) {
                    LOGGER.warning(e.toString());
                    reader = tmp;
                }
            } else {
                try {
                    reader = new ReprojectFeatureReader(reader, query.getCoordinateSystem());
                } catch (Exception e) {
                    LOGGER.warning(e.toString());
                    reader = tmp;
                }
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
        final WFSDataStore dataStore = getDataStore();

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

    /**
     * Returns the feature type that shall result of issueing the given request, adapting the
     * original feature type for the request's type name in terms of the query CRS and requested
     * attributes.
     *
     * @param query
     * @return
     * @throws IOException
     */
    SimpleFeatureType getQueryType(final Query query, SimpleFeatureType featureType)
            throws IOException {

        final CoordinateReferenceSystem coordinateSystemReproject =
                query.getCoordinateSystemReproject();

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
                queryType =
                        DataUtilities.createSubType(
                                queryType, propertyNames, coordinateSystemReproject);
            } catch (SchemaException e) {
                throw new DataSourceException(e);
            }
        }

        return queryType;
    }

    @Override
    public ResourceInfo getInfo() {
        try {
            return client.getInfo(getRemoteTypeName());
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Unexpected error getting ResourceInfo: ", e);
            return super.getInfo();
        }
    }
}
