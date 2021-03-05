/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.gen;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultServiceInfo;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.LockingManager;
import org.geotools.data.Query;
import org.geotools.data.Repository;
import org.geotools.data.ServiceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.gen.info.GeneralizationInfo;
import org.geotools.data.gen.info.GeneralizationInfos;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureTypes;
import org.geotools.util.factory.Hints;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

/**
 * @author Christian Mueller
 *     <p>Datastore for multiple feature types with pregeneralized geometries
 *     <p>The data store is read only, all modifying methods throw an {@link
 *     UnsupportedOperationException}
 *     <p>This data store does business as usual with the following exception:
 *     <p>If a method has a {@link Query} parameter and {@link Query#getHints()} includes {@link
 *     Hints#GEOMETRY_DISTANCE} with a given distance, the datastore looks for the best fit
 *     pregeneralized geometries and returns these geometries instead of the original ones.
 *     <p>This process results in a lower memory usage, lower cpu usage for further processing and
 *     will decrease response time for the user.
 */
public class PreGeneralizedDataStore implements DataStore {

    public URI getNamespace() {
        return namespace;
    }

    Map<String, PreGeneralizedFeatureSource> featureSources;

    private URI namespace;

    /** */
    public PreGeneralizedDataStore(GeneralizationInfos infos, Repository repository) {
        this(infos, repository, null);
    }

    /** */
    public PreGeneralizedDataStore(
            GeneralizationInfos infos, Repository repository, URI namespace) {
        this.namespace = namespace;
        featureSources = new HashMap<>();
        for (GeneralizationInfo gi : infos.getGeneralizationInfoCollection()) {
            featureSources.put(
                    gi.getFeatureName(), new PreGeneralizedFeatureSource(gi, repository, this));
        }
    }

    @Override
    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(
            Query query, Transaction transaction) throws IOException {
        PreGeneralizedFeatureSource fs = featureSources.get(query.getTypeName());
        if (fs == null) throw new IOException(query.getTypeName() + " not found");
        return fs.getFeatureReader(query, transaction);
    }

    @Override
    public SimpleFeatureSource getFeatureSource(String typeName) throws IOException {
        SimpleFeatureSource fs = featureSources.get(typeName);
        if (fs == null) throw new IOException(typeName + " not found");
        return fs;
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
            String typeName, Transaction transaction) throws IOException {

        throw new UnsupportedOperationException("getFeatureWriter");
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
            String typeName, Filter filter, Transaction transaction) throws IOException {
        throw new UnsupportedOperationException("getFeatureWriter");
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(
            String typeName, Transaction transaction) throws IOException {
        throw new UnsupportedOperationException("getFeatureWriterAppend");
    }

    @Override
    public LockingManager getLockingManager() {
        throw new UnsupportedOperationException("getLockingManager");
    }

    @Override
    public SimpleFeatureType getSchema(String typeName) throws IOException {
        return getFeatureSource(typeName).getSchema();
    }

    @Override
    public String[] getTypeNames() throws IOException {
        Set<String> keys = featureSources.keySet();
        return keys.toArray(new String[keys.size()]);
    }

    @Override
    public void updateSchema(String typeName, SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException("updateSchema");
    }

    @Override
    public void createSchema(SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException("createSchema");
    }

    @Override
    public void dispose() {
        for (PreGeneralizedFeatureSource fs : featureSources.values()) {
            fs.reset();
        }
    }

    @Override
    public SimpleFeatureSource getFeatureSource(Name typeName) throws IOException {
        return getFeatureSource(typeName.getLocalPart());
    }

    @Override
    public ServiceInfo getInfo() {
        // TODO
        DefaultServiceInfo info = new DefaultServiceInfo();
        info.setDescription("Generalized Feature Store ");
        info.setSchema(FeatureTypes.DEFAULT_NAMESPACE);
        try {
            info.setPublisher(new URI(System.getProperty("user.name")));
            // TODO IBM SDK problem
            // info.setPublisher( new URI(System.getenv("user.name")) );
        } catch (URISyntaxException e) {
        }
        return info;
    }

    @Override
    public List<Name> getNames() throws IOException {
        List<Name> nameList = new ArrayList<>();
        for (PreGeneralizedFeatureSource fs : featureSources.values()) {
            nameList.add(fs.getName());
        }
        return nameList;
    }

    @Override
    public SimpleFeatureType getSchema(Name name) throws IOException {
        return getFeatureSource(name).getSchema();
    }

    @Override
    public void updateSchema(Name typeName, SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException("updateSchema");
    }

    @Override
    public void removeSchema(Name typeName) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeSchema(String typeName) throws IOException {
        throw new UnsupportedOperationException();
    }
}
