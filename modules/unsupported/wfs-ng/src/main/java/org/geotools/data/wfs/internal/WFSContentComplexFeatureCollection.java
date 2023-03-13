/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012 - 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.internal;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.geotools.data.wfs.internal.parsers.XmlComplexFeatureParser;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.collection.BaseFeatureCollection;
import org.geotools.util.logging.Logging;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;

/**
 * Feature collection for parsing complex features.
 *
 * @author Adam Brown (Curtin University of Technology)
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 */
public class WFSContentComplexFeatureCollection
        extends BaseFeatureCollection<FeatureType, Feature> {
    private static final Logger LOGGER =
            Logging.getLogger(WFSContentComplexFeatureCollection.class);

    private final WFSClient client;

    private final FeatureType schema;

    private final GetFeatureRequest request;

    private final QName name;

    private final Filter filter;

    /** @deprecated - Will not work when using POST */
    @Deprecated
    public WFSContentComplexFeatureCollection(
            GetFeatureRequest request, FeatureType schema, QName name) throws IOException {
        this(request, schema, name, Filter.INCLUDE, null);
    }

    /** @deprecated - Will not work when using POST */
    @Deprecated
    public WFSContentComplexFeatureCollection(
            GetFeatureRequest request, FeatureType schema, QName name, Filter filter)
            throws IOException {
        this(request, schema, name, filter, null);
    }

    /** Making a feature collection based on a request for a type without any filter. */
    public WFSContentComplexFeatureCollection(
            GetFeatureRequest request, FeatureType schema, QName name, WFSClient client) {
        this(request, schema, name, null, client);
    }

    /** Making a feature collection based on a request for a type with a filter. */
    public WFSContentComplexFeatureCollection(
            GetFeatureRequest request,
            FeatureType schema,
            QName name,
            Filter filter,
            WFSClient client) {
        this.request = request;
        this.name = name;
        this.schema = schema;
        this.client = client;
        this.filter = filter;
    }

    /** Make sure to close the iterator. */
    @SuppressWarnings("PMD.CloseResource") // wrapped and returned
    @Override
    public FeatureIterator<Feature> features() {
        try {
            if (client == null) {
                InputStream stream = request.getFinalURL().openStream();
                XmlComplexFeatureParser parser =
                        new XmlComplexFeatureParser(
                                stream, schema, name, filter, request.getStrategy());
                return new ComplexFeatureIteratorImpl(parser);
            } else {
                ComplexGetFeatureResponse response = client.issueComplexRequest(request);
                response.getParser().setFilter(filter);
                return response.features();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new RuntimeException("Couldn't read features of collection.", e);
        }
    }

    @Override
    public FeatureType getSchema() {
        return schema;
    }

    /** Issue a new request for subCollection */
    @Override
    public FeatureCollection<FeatureType, Feature> subCollection(Filter filter) {
        return new WFSContentComplexFeatureCollection(request, schema, name, filter, client);
    }
}
