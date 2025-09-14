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
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.wfs.impl;

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.geotools.api.data.DataAccess;
import org.geotools.api.data.FeatureListener;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.Query;
import org.geotools.api.data.QueryCapabilities;
import org.geotools.api.data.ResourceInfo;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.wfs.internal.GetFeatureRequest;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.data.wfs.internal.WFSContentComplexFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.logging.Logging;

/**
 * Combines the WFSClient and DataAccess objects and exposes methods to access the features by using the
 * XmlComplexFeatureParser.
 */
public class WFSContentComplexFeatureSource implements FeatureSource<FeatureType, Feature> {
    /** The name of the feature type of the source. */
    private Name typeName;

    /** The wfs client object. */
    private WFSClient client;

    /** The data access object. */
    private WFSContentDataAccess dataAccess;

    private static Logger LOGGER = Logging.getLogger(WFSContentComplexFeatureSource.class);

    /**
     * Initialises a new instance of the class WFSContentComplexFeatureSource.
     *
     * @param typeName The name of the feature you want to retrieve.
     * @param client The WFSClient responsible for making the WFS requests.
     * @param dataAccess The data access object.
     */
    public WFSContentComplexFeatureSource(Name typeName, WFSClient client, WFSContentDataAccess dataAccess) {
        this.typeName = typeName;
        this.client = client;
        this.dataAccess = dataAccess;
    }

    /** Get features based on the specified filter. */
    @Override
    public FeatureCollection<FeatureType, Feature> getFeatures(Filter filter) throws IOException {
        return getFeatures(new Query(this.typeName.getLocalPart(), filter));
    }

    /** Get features using the default Query.ALL. */
    @Override
    public FeatureCollection<FeatureType, Feature> getFeatures() throws IOException {
        return getFeatures(Query.ALL);
    }

    /** Get features based on the query specified. */
    @Override
    public FeatureCollection<FeatureType, Feature> getFeatures(Query query) throws IOException {
        if (query.getTypeName() != null && !typeName.getLocalPart().equals(query.getTypeName())) {
            throw new IllegalArgumentException(
                    "Query's local typeName %s doesn't match the one of this feature source %s."
                            .formatted(query.getTypeName(), typeName.getLocalPart()));
        }
        GetFeatureRequest request = client.createGetFeatureRequest();
        FeatureType schema = dataAccess.getSchema(typeName);
        QName name = dataAccess.getRemoteTypeName(typeName);
        request.setTypeName(name);
        request.setFullType(schema);
        request.setFilter(query.getFilter());
        request.setPropertyNames(query.getPropertyNames());
        request.setSortBy(query.getSortBy());

        if (query.getCoordinateSystem() != null) {
            request.findSupportedSrsName(query.getCoordinateSystem());
            if (request.getSrsName() == null) {
                LOGGER.log(Level.WARNING, "WFS doesn't support the coordinate system: " + query.getCoordinateSystem());
            }
        }

        return new WFSContentComplexFeatureCollection(request, schema, name, client);
    }

    @Override
    public Name getName() {
        return typeName;
    }

    @Override
    public ResourceInfo getInfo() {
        return new ResourceInfo() {
            final Set<String> words = new HashSet<>();

            {
                words.add("features");
                words.add(WFSContentComplexFeatureSource.this.getName().getURI());
            }

            @Override
            public ReferencedEnvelope getBounds() {
                try {
                    return WFSContentComplexFeatureSource.this.getBounds();
                } catch (IOException e) {
                    return null;
                }
            }

            @Override
            public CoordinateReferenceSystem getCRS() {
                return WFSContentComplexFeatureSource.this.getSchema().getCoordinateReferenceSystem();
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public Set<String> getKeywords() {
                return words;
            }

            @Override
            public String getName() {
                return WFSContentComplexFeatureSource.this.getName().getURI();
            }

            @Override
            public URI getSchema() {
                Name name = WFSContentComplexFeatureSource.this.getSchema().getName();
                URI namespace;
                try {
                    namespace = new URI(name.getNamespaceURI());
                    return namespace;
                } catch (URISyntaxException e) {
                    return null;
                }
            }

            @Override
            public String getTitle() {
                Name name = WFSContentComplexFeatureSource.this.getSchema().getName();
                return name.getLocalPart();
            }
        };
    }

    @Override
    public DataAccess<FeatureType, Feature> getDataStore() {
        return dataAccess;
    }

    @Override
    public QueryCapabilities getQueryCapabilities() {
        return new QueryCapabilities();
    }

    @Override
    public void addFeatureListener(FeatureListener listener) {
        // not supported yet
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeFeatureListener(FeatureListener listener) {
        // not supported yet
        throw new UnsupportedOperationException();
    }

    @Override
    public FeatureType getSchema() {
        try {
            return dataAccess.getSchema(typeName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ReferencedEnvelope getBounds() throws IOException {
        return getBounds(Query.ALL);
    }

    @Override
    public ReferencedEnvelope getBounds(Query query) throws IOException {
        if (!Filter.INCLUDE.equals(query.getFilter())) {
            return null;
        }
        QName name = dataAccess.getRemoteTypeName(typeName);
        final CoordinateReferenceSystem targetCrs = query.getCoordinateSystemReproject() != null
                ? query.getCoordinateSystemReproject()
                : client.getDefaultCRS(name);

        return client.getBounds(name, targetCrs);
    }

    @Override
    public int getCount(Query query) throws IOException {
        return getFeatures(query).size();
    }

    @Override
    public Set<Key> getSupportedHints() {
        return Collections.emptySet();
    }
}
