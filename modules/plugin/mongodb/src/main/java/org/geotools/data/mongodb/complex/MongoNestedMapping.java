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

import com.mongodb.DBObject;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.geotools.data.DataAccess;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.ResourceInfo;
import org.geotools.data.complex.AppSchemaDataAccess;
import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.data.complex.MappingFeatureCollection;
import org.geotools.data.complex.MappingFeatureSource;
import org.geotools.data.complex.NestedAttributeMapping;
import org.geotools.data.complex.filter.XPathUtil;
import org.geotools.data.memory.MemoryFeatureCollection;
import org.geotools.data.mongodb.MongoFeature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
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
        FeatureSource fSource = buildMappingFeatureSource(feature, features);
        ArrayList<Feature> matchingFeatures = new ArrayList<Feature>();
        // get all the mapped nested features based on the link values
        FeatureCollection<FeatureType, Feature> fCollection = fSource.getFeatures(Query.ALL);
        if (fCollection instanceof MappingFeatureCollection) {
            FeatureIterator<Feature> iterator = fCollection.features();
            while (iterator.hasNext()) {
                Feature nestedFeature = iterator.next();
                String parentPath =
                        MongoComplexUtilities.resolvePath(
                                (Feature) feature, linkCollection.getCollectionPath());
                MongoComplexUtilities.setParentPath(nestedFeature, parentPath);
                matchingFeatures.add(nestedFeature);
            }
            iterator.close();
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
        MongoStaticFeatureSource staticSource =
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
        feature = MongoComplexUtilities.extractFeature(feature, collectionPath);
        if (feature instanceof MongoFeature) {
            DBObject mongoObject = ((MongoFeature) feature).getMongoObject();
            return getSubCollection(mongoObject, collectionPath, Collections.emptyMap());
        } else if (feature instanceof MongoCollectionFeature) {
            MongoCollectionFeature collectionFeature = (MongoCollectionFeature) feature;
            return getSubCollection(
                    collectionFeature.getMongoFeature().getMongoObject(),
                    collectionPath,
                    collectionFeature.getCollectionsIndexes());
        }
        throw new RuntimeException("MongoDB nesting only works with MongoDB features.");
    }

    private List getSubCollection(
            DBObject mongoObject, String collectionPath, Map<String, Integer> collectionsIndexes) {
        Object value =
                MongoComplexUtilities.getValue(mongoObject, collectionsIndexes, collectionPath);
        if (value == null) {
            return Collections.emptyList();
        }
        if (value instanceof List) {
            return (List) value;
        }
        throw new RuntimeException("Could not extract collection from path.");
    }

    private static final class MongoStaticFeatureSource implements FeatureSource {

        private final FeatureCollection features;
        private final FeatureSource originalFeatureSource;

        public MongoStaticFeatureSource(
                FeatureCollection features, FeatureSource originalFeatureSource) {
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
        public DataAccess getDataStore() {
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
        public FeatureCollection getFeatures(Filter filter) throws IOException {
            return features;
        }

        @Override
        public FeatureCollection getFeatures(Query query) throws IOException {
            return features;
        }

        @Override
        public FeatureCollection getFeatures() throws IOException {
            return features;
        }

        @Override
        public FeatureType getSchema() {
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
