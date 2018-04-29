/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.mongodb.complex;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.geotools.data.mongodb.MongoFeature;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.filter.identity.FeatureIdImpl;

/** Contains information about a chained collection. */
final class MongoCollectionFeature extends SimpleFeatureImpl {

    private final MongoFeature mongoFeature;

    private final String collectionPath;

    private final Map<String, Integer> collectionsIndexes = new HashMap<>();

    static MongoCollectionFeature build(
            Object feature, String collectionPath, int collectionIndex) {
        feature = MongoComplexUtilities.extractFeature(feature, collectionPath);
        if (feature instanceof MongoFeature) {
            return new MongoCollectionFeature(
                    (MongoFeature) feature, collectionPath, collectionIndex);
        } else if (feature instanceof MongoCollectionFeature) {
            return new MongoCollectionFeature(
                    (MongoCollectionFeature) feature, collectionPath, collectionIndex);
        }
        throw new RuntimeException("The feature must be a mongo feature.");
    }

    private MongoCollectionFeature(
            MongoCollectionFeature collectionFeature, String collectionPath, int collectionIndex) {
        this(collectionFeature.getMongoFeature(), collectionPath, collectionIndex);
        this.collectionsIndexes.putAll(collectionFeature.getCollectionsIndexes());
    }

    private MongoCollectionFeature(
            MongoFeature feature, String collectionPath, int collectionIndex) {
        super(
                feature.getValues(),
                feature.getFeatureType(),
                new FeatureIdImpl(UUID.randomUUID().toString()),
                false);
        this.mongoFeature = feature;
        this.collectionsIndexes.put(collectionPath, collectionIndex);
        this.collectionPath = collectionPath;
    }

    MongoFeature getMongoFeature() {
        return mongoFeature;
    }

    Map<String, Integer> getCollectionsIndexes() {
        return collectionsIndexes;
    }

    public String getCollectionPath() {
        return collectionPath;
    }
}
