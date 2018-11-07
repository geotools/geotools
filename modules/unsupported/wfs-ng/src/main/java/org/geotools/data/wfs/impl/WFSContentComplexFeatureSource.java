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
import javax.xml.namespace.QName;
import org.geotools.data.DataAccess;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.ResourceInfo;
import org.geotools.data.wfs.internal.GetFeatureRequest;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.data.wfs.internal.WFSContentComplexFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Combines the WFSClient and DataAccess objects and exposes methods to access the features by using
 * the XmlComplexFeatureParser.
 */
public class WFSContentComplexFeatureSource implements FeatureSource<FeatureType, Feature> {
    /** The name of the feature type of the source. */
    private Name typeName;

    /** The wfs client object. */
    private WFSClient client;

    /** The data access object. */
    private WFSContentDataAccess dataAccess;

    /**
     * Initialises a new instance of the class WFSContentComplexFeatureSource.
     *
     * @param typeName The name of the feature you want to retrieve.
     * @param client The WFSClient responsible for making the WFS requests.
     * @param dataAccess The data access object.
     */
    public WFSContentComplexFeatureSource(
            Name typeName, WFSClient client, WFSContentDataAccess dataAccess) {
        this.typeName = typeName;
        this.client = client;
        this.dataAccess = dataAccess;
    }

    /** Get features based on the specified filter. */
    @Override
    public FeatureCollection<FeatureType, Feature> getFeatures(Filter filter) throws IOException {
        return getFeatures(new Query(this.typeName.toString(), filter));
    }

    /** Get features using the default Query.ALL. */
    @Override
    public FeatureCollection<FeatureType, Feature> getFeatures() throws IOException {
        return getFeatures(Query.ALL);
    }

    /** Get features based on the query specified. */
    @Override
    public FeatureCollection<FeatureType, Feature> getFeatures(Query query) throws IOException {

        GetFeatureRequest request = client.createGetFeatureRequest();
        FeatureType schema = dataAccess.getSchema(typeName);
        QName name = dataAccess.getRemoteTypeName(typeName);
        request.setTypeName(new QName(query.getTypeName()));

        request.setFullType(schema);
        request.setFilter(query.getFilter());
        request.setPropertyNames(query.getPropertyNames());
        request.setSortBy(query.getSortBy());

        String srsName = null;
        CoordinateReferenceSystem crs = query.getCoordinateSystem();
        if (null != crs) {
            // System.err.println("Warning: don't forget to set the query CRS");
        }

        request.setSrsName(srsName);

        return new WFSContentComplexFeatureCollection(request, schema, name);
    }

    @Override
    public Name getName() {
        return typeName;
    }

    @Override
    public ResourceInfo getInfo() {
        return new ResourceInfo() {
            final Set<String> words = new HashSet<String>();

            {
                words.add("features");
                words.add(WFSContentComplexFeatureSource.this.getName().getURI());
            }

            public ReferencedEnvelope getBounds() {
                try {
                    return WFSContentComplexFeatureSource.this.getBounds();
                } catch (IOException e) {
                    return null;
                }
            }

            public CoordinateReferenceSystem getCRS() {
                return WFSContentComplexFeatureSource.this
                        .getSchema()
                        .getCoordinateReferenceSystem();
            }

            public String getDescription() {
                return null;
            }

            public Set<String> getKeywords() {
                return words;
            }

            public String getName() {
                return WFSContentComplexFeatureSource.this.getName().getURI();
            }

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
        return dataAccess.getFeatureSource(typeName).getBounds();
    }

    @Override
    public ReferencedEnvelope getBounds(Query query) throws IOException {
        return dataAccess.getFeatureSource(typeName).getBounds(query);
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
