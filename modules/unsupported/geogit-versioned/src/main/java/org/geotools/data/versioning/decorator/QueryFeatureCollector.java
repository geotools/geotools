/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.versioning.decorator;

import java.io.IOException;
import java.util.Iterator;

import org.geogit.api.GeoGIT;
import org.geogit.api.ObjectId;
import org.geogit.api.Ref;
import org.geogit.repository.Repository;
import org.geogit.storage.ObjectReader;
import org.geogit.storage.StagingDatabase;
import org.geogit.storage.WrappedSerialisingFactory;
import org.geotools.data.Query;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;

public class QueryFeatureCollector implements Iterable<Feature> {

    private final Repository repository;

    private final FeatureType featureType;

    private Query query;

    public QueryFeatureCollector(final Repository repository,
            final FeatureType featureType, Query query) {
        this.repository = repository;
        this.featureType = featureType;
        this.query = query;
    }

    @Override
    public Iterator<Feature> iterator() {

        GeoGIT ggit = new GeoGIT(repository);
        VersionQuery versionQuery = new VersionQuery(ggit,
                featureType.getName());
        Iterator<Ref> featureRefs;
        try {
            featureRefs = versionQuery.getByQuery(query);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Iterator<Feature> features = Iterators.transform(featureRefs,
                new RefToFeature(repository, featureType));

        return features;
    }

    private static class RefToFeature implements Function<Ref, Feature> {

        private final Repository repo;

        private final FeatureType type;

        private WrappedSerialisingFactory serialisingFactory;

        public RefToFeature(final Repository repo, final FeatureType type) {
            this.repo = repo;
            this.type = type;
            serialisingFactory = WrappedSerialisingFactory.getInstance();
        }

        @Override
        public Feature apply(final Ref featureRef) {
            String featureId = featureRef.getName();
            ObjectId contentId = featureRef.getObjectId();
            StagingDatabase database = repo.getIndex().getDatabase();
            Feature feature;
            try {
                ObjectReader<Feature> featureReader = serialisingFactory
                        .createFeatureReader(type, featureId);
                feature = database.get(contentId, featureReader);
                if (!feature.getType().equals(type)) {
                    throw new IOException("Invalid feature type returned.");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return VersionedFeatureWrapper.wrap(feature, featureRef
                    .getObjectId().toString());
        }

    }

}
