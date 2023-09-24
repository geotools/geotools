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
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.collection.BaseFeatureCollection;
import org.geotools.util.logging.Logging;

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
        Objects.requireNonNull(client);
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
            ComplexGetFeatureResponse response = client.issueComplexRequest(request);
            response.getParser().setFilter(filter);
            return response.features();
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
