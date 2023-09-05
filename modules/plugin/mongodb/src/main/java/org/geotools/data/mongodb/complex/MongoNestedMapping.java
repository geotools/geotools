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

import static org.geotools.data.mongodb.complex.MongoComplexUtilities.extractFeature;
import static org.geotools.data.mongodb.complex.MongoComplexUtilities.getTransformer;
import static org.geotools.data.mongodb.complex.MongoComplexUtilities.invalidFeature;

import com.mongodb.DBObject;
import java.awt.RenderingHints;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import org.geotools.api.data.DataAccess;
import org.geotools.api.data.FeatureListener;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.Query;
import org.geotools.api.data.QueryCapabilities;
import org.geotools.api.data.ResourceInfo;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.complex.AppSchemaDataAccess;
import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.data.complex.MappingFeatureCollection;
import org.geotools.data.complex.MappingFeatureSource;
import org.geotools.data.complex.NestedAttributeMapping;
import org.geotools.data.complex.util.XPathUtil;
import org.geotools.data.memory.MemoryFeatureCollection;
import org.geotools.data.mongodb.MongoFeature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.xml.sax.helpers.NamespaceSupport;

/** MongoDB custom nested attribute mapping for app-schema. */
public class MongoNestedMapping extends NestedAttributeMapping {

    public MongoNestedMapping(
            Expression idExpression,
            Expression parentExpression,
            XPathUtil.StepList targetXPath,
            boolean isMultiValued,
            Map<Name, Expression> clientProperties,
            Expression sourceElement,
            XPathUtil.StepList sourcePath,
            NamespaceSupport namespaces)
            throws IOException {
        super(
                idExpression,
                parentExpression,
                targetXPath,
                isMultiValued,
                clientProperties,
                sourceElement,
                sourcePath,
                namespaces);
    }

    @Override
    public List<Feature> getFeatures(
            Object source,
            Object foreignKeyValue,
            List<Object> idValues,
            CoordinateReferenceSystem reprojection,
            Object feature,
            List<PropertyName> selectedProperties,
            boolean includeMandatory,
            int resolveDepth,
            Integer resolveTimeOut)
            throws IOException {
        if (!(foreignKeyValue instanceof CollectionLinkFunction.LinkCollection)) {
            throw new RuntimeException(
                    "MongoDB nesting only supports foreign keys of 'CollectionLink' type.");
        }
        CollectionLinkFunction.LinkCollection linkCollection =
                (CollectionLinkFunction.LinkCollection) foreignKeyValue;
        String collectionPath = linkCollection.getCollectionPath();
        if (feature instanceof MongoCollectionFeature) {
            String parentPath = ((MongoCollectionFeature) feature).getCollectionPath();
            collectionPath = parentPath + "." + collectionPath;
        }
        List collection = getSubCollection(feature, collectionPath);
        if (collection == null) {
            return Collections.emptyList();
        }
        List<SimpleFeature> features = new ArrayList<>();
        for (int i = 0; i < collection.size(); i++) {
            features.add(MongoCollectionFeature.build(feature, collectionPath, i));
        }
        MappingFeatureSource fSource = buildMappingFeatureSource(feature, features);
        ArrayList<Feature> matchingFeatures = new ArrayList<>();
        // get all the mapped nested features based on the link values
        FeatureCollection<FeatureType, Feature> fCollection = fSource.getFeatures(Query.ALL);
        if (fCollection instanceof MappingFeatureCollection) {
            try (FeatureIterator<Feature> iterator = fCollection.features()) {
                while (iterator.hasNext()) {
                    Feature nestedFeature = iterator.next();
                    String parentPath =
                            MongoComplexUtilities.resolvePath(
                                    (Feature) feature, linkCollection.getCollectionPath());
                    MongoComplexUtilities.setParentPath(nestedFeature, parentPath);
                    matchingFeatures.add(nestedFeature);
                }
            }
        }
        return matchingFeatures;
    }

    private MappingFeatureSource buildMappingFeatureSource(
            Object feature, List<SimpleFeature> features) throws IOException {
        MappingFeatureSource originalFeatureSource =
                (MappingFeatureSource) getMappingSource(feature);
        FeatureTypeMapping mapping = originalFeatureSource.getMapping();
        AppSchemaDataAccess dataAccess = (AppSchemaDataAccess) originalFeatureSource.getDataStore();
        MemoryFeatureCollection collection = new MemoryFeatureCollection(null);
        collection.addAll(features);
        @SuppressWarnings("unchecked")
        MongoStaticFeatureSource<?, ?> staticSource =
                new MongoStaticFeatureSource(collection, mapping.getSource());
        FeatureTypeMapping staticMapping =
                new FeatureTypeMapping(
                        staticSource,
                        mapping.getTargetFeature(),
                        mapping.getDefaultGeometryXPath(),
                        mapping.getAttributeMappings(),
                        mapping.getNamespaces(),
                        mapping.isDenormalised());
        return new MappingFeatureSource(dataAccess, staticMapping);
    }

    private List getSubCollection(Object feature, String collectionPath) {
        // let's make sure we have a feature
        // we should have a feature
        if (!(feature instanceof Feature)) {
            // not a feature so nothing to do
            throw invalidFeature(feature, collectionPath);
        }
        Feature f = (Feature) feature;
        Feature extracted = extractFeature(feature, collectionPath);

        if (extracted instanceof MongoFeature) {
            MongoFeature mongoFeature = (MongoFeature) extracted;
            DBObject mongoObject = mongoFeature.getMongoObject();
            Supplier<GeometryCoordinateSequenceTransformer> transformer =
                    getTransformer(f, mongoFeature);

            return getSubCollection(
                    mongoObject, collectionPath, Collections.emptyMap(), transformer);
        } else if (extracted instanceof MongoCollectionFeature) {
            MongoCollectionFeature collectionFeature = (MongoCollectionFeature) extracted;
            MongoFeature mongoFeature = collectionFeature.getMongoFeature();
            Supplier<GeometryCoordinateSequenceTransformer> transformer =
                    getTransformer(f, mongoFeature);

            return getSubCollection(
                    mongoFeature.getMongoObject(),
                    collectionPath,
                    collectionFeature.getCollectionsIndexes(),
                    transformer);
        }
        throw new RuntimeException("MongoDB nesting only works with MongoDB features.");
    }

    private List getSubCollection(
            DBObject mongoObject,
            String collectionPath,
            Map<String, Integer> collectionsIndexes,
            Supplier<GeometryCoordinateSequenceTransformer> transformer) {
        Object value =
                MongoComplexUtilities.getValue(
                        mongoObject, collectionsIndexes, collectionPath, transformer);
        if (value == null) {
            return Collections.emptyList();
        }
        if (value instanceof List) {
            return (List) value;
        }
        throw new RuntimeException("Could not extract collection from path.");
    }

    private static final class MongoStaticFeatureSource<T extends FeatureType, F extends Feature>
            implements FeatureSource<T, F> {

        private final FeatureCollection<T, F> features;
        private final FeatureSource<T, F> originalFeatureSource;

        public MongoStaticFeatureSource(
                FeatureCollection<T, F> features, FeatureSource<T, F> originalFeatureSource) {
            this.features = features;
            this.originalFeatureSource = originalFeatureSource;
        }

        @Override
        public Name getName() {
            return originalFeatureSource.getName();
        }

        @Override
        public ResourceInfo getInfo() {
            return originalFeatureSource.getInfo();
        }

        @Override
        public DataAccess<T, F> getDataStore() {
            return originalFeatureSource.getDataStore();
        }

        @Override
        public QueryCapabilities getQueryCapabilities() {
            return originalFeatureSource.getQueryCapabilities();
        }

        @Override
        public void addFeatureListener(FeatureListener listener) {}

        @Override
        public void removeFeatureListener(FeatureListener listener) {}

        @Override
        public FeatureCollection<T, F> getFeatures(Filter filter) throws IOException {
            return features;
        }

        @Override
        public FeatureCollection<T, F> getFeatures(Query query) throws IOException {
            return features;
        }

        @Override
        public FeatureCollection<T, F> getFeatures() throws IOException {
            return features;
        }

        @Override
        public T getSchema() {
            return originalFeatureSource.getSchema();
        }

        @Override
        public ReferencedEnvelope getBounds() throws IOException {
            return features.getBounds();
        }

        @Override
        public ReferencedEnvelope getBounds(Query query) throws IOException {
            return features.getBounds();
        }

        @Override
        public int getCount(Query query) throws IOException {
            return features.size();
        }

        @Override
        public Set<RenderingHints.Key> getSupportedHints() {
            return originalFeatureSource.getSupportedHints();
        }
    }
}
