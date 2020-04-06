/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014-2016, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.solr;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.CursorMarkParams;
import org.geotools.data.FeatureReader;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;

/** Reader for SOLR datastore */
public class SolrFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {

    private Iterator<SolrDocument> solrDocIterator;

    private SimpleFeatureType featureType;

    private Boolean next;

    private SimpleFeatureBuilder builder;

    private SolrAttribute pkey;

    private HttpSolrClient server;

    private SolrDataStore solrDataStore;

    private String cursorMark;

    private String nextCursorMark;

    private SolrQuery solrQuery;

    private long counter;

    private Map<Name, SolrSpatialStrategy> geometryReaders;

    /**
     * Creates the feature reader for SOLR store <br>
     * The feature reader use SOLR CURSOR to paginate request, so multiple SOLR query will be
     * executed
     *
     * @param featureType the feature type to query
     * @param server The SOLR server
     * @param solrQuery the SOLR query to execute
     * @param solrDataStore the SOLR store
     * @throws java.io.IOException
     */
    public SolrFeatureReader(
            SimpleFeatureType featureType,
            HttpSolrClient server,
            SolrQuery solrQuery,
            SolrDataStore solrDataStore)
            throws SolrServerException, IOException {
        this.featureType = featureType;
        this.solrQuery = solrQuery;
        this.solrDataStore = solrDataStore;
        this.pkey = solrDataStore.getPrimaryKey(featureType.getTypeName());

        this.builder = new SimpleFeatureBuilder(featureType);

        this.server = server;

        // Add always pk as field if not already present
        if (solrQuery.getFields() != null && !solrQuery.getFields().contains(pkey.getName())) {
            solrQuery.addField(pkey.getName());
        }

        // Can't use pagination with start, then first query for cursor mark of start
        if (solrQuery.getStart() != null && solrQuery.getStart() > 0) {
            cursorMark = getCursorMarkForStart(server, solrQuery);
        } else {
            cursorMark = CursorMarkParams.CURSOR_MARK_START;
        }
        solrQuery.set(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark);
        QueryResponse rsp = server.query(solrQuery);
        if (this.solrDataStore.getLogger().isLoggable(Level.FINE)) {
            this.solrDataStore
                    .getLogger()
                    .log(Level.FINE, "SOLR query done: " + solrQuery.toString());
        }
        this.solrDocIterator = rsp.getResults().iterator();
        nextCursorMark = rsp.getNextCursorMark();
        counter = 0;

        // create readers for different geometry types
        geometryReaders = new HashMap<>();
        for (AttributeDescriptor att : featureType.getAttributeDescriptors()) {
            if (att instanceof GeometryDescriptor) {
                SolrSpatialStrategy spatialStrategy =
                        SolrSpatialStrategy.createStrategy((GeometryDescriptor) att);
                geometryReaders.put(att.getName(), spatialStrategy);
            }
        }
    }

    /*
     * Can't use CURSOR MARK with "start" parameter, so get initial SOLR CURSOR MARK to positioning
     * CURSOR at the row specified by start query parameter
     */
    private String getCursorMarkForStart(HttpSolrClient server, SolrQuery solrQuery)
            throws SolrServerException, IOException {
        Integer prevRows = solrQuery.getRows();
        solrQuery.setRows(solrQuery.getStart());
        solrQuery.setStart(0);
        solrQuery.set(CursorMarkParams.CURSOR_MARK_PARAM, CursorMarkParams.CURSOR_MARK_START);
        QueryResponse rsp = server.query(solrQuery);
        if (this.solrDataStore.getLogger().isLoggable(Level.FINE)) {
            this.solrDataStore
                    .getLogger()
                    .log(Level.FINE, "SOLR query done: " + solrQuery.toString());
        }
        String nextC = rsp.getNextCursorMark();
        solrQuery.setRows(prevRows);
        return nextC;
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return this.featureType;
    }

    /**
     * SOLR multiValues fields are returned as single String field, with values concatenated and
     * separated by ";"
     */
    @Override
    public SimpleFeature next()
            throws IOException, IllegalArgumentException, NoSuchElementException {
        String fid = "";
        try {
            if (!hasNext()) {
                throw new NoSuchElementException(
                        "No more features in this reader, you should call "
                                + "hasNext() to check for feature availability");
            }
            SolrDocument doc = this.solrDocIterator.next();
            fid = featureType.getTypeName() + "." + doc.getFieldValue(pkey.getName());

            final int attributeCount = featureType.getAttributeCount();
            for (int i = 0; i < attributeCount; i++) {
                AttributeDescriptor type = featureType.getDescriptor(i);
                Object value = doc.getFieldValue(type.getLocalName());
                if (value instanceof List<?>) {
                    value = StringUtils.join(((List) value).toArray(), ";");
                }
                if (type instanceof GeometryDescriptor) {
                    GeometryDescriptor gatt = (GeometryDescriptor) type;
                    if (value != null) {
                        SolrSpatialStrategy spatialStrategy = geometryReaders.get(gatt.getName());
                        if (spatialStrategy == null) {
                            // should ever happen but being defensive here
                            spatialStrategy = SolrSpatialStrategy.DEFAULT;
                        }

                        Geometry geometry = spatialStrategy.decode(value.toString());

                        if (geometry != null && geometry.getUserData() == null) {
                            geometry.setUserData(gatt.getCoordinateReferenceSystem());
                        }
                        builder.add(geometry);
                    }
                } else {
                    builder.add(value);
                }
            }
            return builder.buildFeature(fid);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            if (this.solrDataStore.getLogger().isLoggable(Level.FINE)) {
                this.solrDataStore.getLogger().log(Level.FINE, "Created " + fid);
            }
            next = null;
            counter++;
        }
    }

    /**
     * SOLR CURSOR MARK is used to retrieve data until no more cursor and no more data is available
     */
    @Override
    public boolean hasNext() throws IOException {
        if (next == null) {
            if (this.solrDocIterator.hasNext()) {
                next = true;
            } else {
                if (counter < solrQuery.getRows() && !cursorMark.equals(nextCursorMark)) {
                    cursorMark = nextCursorMark;
                    solrQuery.set(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark);
                    try {
                        QueryResponse rsp = server.query(solrQuery);
                        if (this.solrDataStore.getLogger().isLoggable(Level.FINE)) {
                            this.solrDataStore
                                    .getLogger()
                                    .log(
                                            Level.FINE,
                                            server.getBaseURL()
                                                    + "/select?"
                                                    + solrQuery.toString());
                        }
                        this.solrDocIterator = rsp.getResults().iterator();
                        nextCursorMark = rsp.getNextCursorMark();
                        next = this.solrDocIterator.hasNext();
                    } catch (SolrServerException e) {
                        this.solrDataStore.getLogger().log(Level.SEVERE, e.getMessage(), e);
                        next = false;
                    }
                } else {
                    next = false;
                }
            }
        }
        return next.booleanValue();
    }

    @Override
    public void close() throws IOException {
        // nothing to do
    }
}
