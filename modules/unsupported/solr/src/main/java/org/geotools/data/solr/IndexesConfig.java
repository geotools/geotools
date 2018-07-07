/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/** This class contains the configuration used by Apache Solr store to query Apache Solr indexes. */
public final class IndexesConfig {

    // Apache Solr index configuration indexed by the index name
    private final ConcurrentHashMap<String, IndexConfig> indexesConfig = new ConcurrentHashMap<>();

    // list of Apache Solr indexes for which a configuration was provided
    private final List<String> indexesNames = new ArrayList<>();

    /**
     * Adds the specification of a geometry attribute of the index to the current index
     * configuration.
     *
     * @param attributeName the index attribute name
     * @param srid the SIRD of the geometry, e.g. EPSG:4326
     * @param type the type of the geometry, e.g. POINT
     * @param isDefault TRUE fi this attribute contains the default geometry of the feature type,
     *     otherwise FALSE
     */
    public void addGeometry(
            String indexName, String attributeName, String srid, String type, String isDefault) {
        GeometryConfig geometry = GeometryConfig.create(attributeName, srid, type, isDefault);
        getIndexConfig(indexName).addGeometry(geometry);
    }

    /**
     * Add the list of attributes that should be considered when querying the Apache Solr index
     * identified byt the provided index name.
     *
     * @param indexName the Apache Solr index name
     * @param attributes the list of attributes names
     */
    public void addAttributes(String indexName, Collection<String> attributes) {
        IndexConfig indexConfig = getIndexConfig(indexName);
        indexConfig.getAttributes().addAll(attributes);
    }

    /**
     * Build a simple feature type based on the Apache Solr index configuration and on the provided
     * Apache Solr schema attributes. Note that if no configuration existing for the requested
     * Apache Solr index, all attributes will be used to build the simple feature type and no
     * geometries fields will be identified.
     *
     * @param indexName the Apache Solr index name
     * @param solrAttributes Apache Solr schema attributes associated with the index
     * @return the built simple feature type
     */
    public SimpleFeatureType buildFeatureType(
            String indexName, List<SolrAttribute> solrAttributes) {
        return getIndexConfig(indexName).buildFeatureType(solrAttributes);
    }

    /**
     * Returns a list with the available indexes names.
     *
     * @return a list with the available indexes names
     */
    public List<String> getIndexesNames() {
        return indexesNames;
    }

    /**
     * Returns the configuration associated with the provided apache Solr index name. If not
     * configuration is available a new one will be created and associated with the index.
     */
    private IndexConfig getIndexConfig(String indexName) {
        IndexConfig indexConfig = indexesConfig.get(indexName);
        if (indexConfig == null) {
            // no configuration available, create a new one
            indexConfig = new IndexConfig(indexName);
            IndexConfig existing = indexesConfig.putIfAbsent(indexName, indexConfig);
            if (existing == null) {
                indexesNames.add(indexName);
            } else {
                indexConfig = existing;
            }
        }
        return indexConfig;
    }

    /** This class holds the configuration of an Apache Solr index. */
    private static final class IndexConfig {

        private static final Logger LOGGER = Logging.getLogger(IndexConfig.class);

        private final String indexName;
        private final List<GeometryConfig> geometries = new ArrayList<>();
        private final List<String> attributes = new ArrayList<>();

        IndexConfig(String indexName) {
            this.indexName = indexName;
        }

        void addGeometry(GeometryConfig geometry) {
            geometries.add(geometry);
        }

        List<String> getAttributes() {
            return attributes;
        }

        GeometryConfig searchGeometry(String attributeName) {
            return search(
                    geometries,
                    geometry -> Objects.equals(geometry.getAttributeName(), attributeName));
        }

        GeometryConfig searchDefaultGeometry() {
            return search(geometries, GeometryConfig::isDefault);
        }

        SimpleFeatureType buildFeatureType(List<SolrAttribute> solrAttributes) {
            SimpleFeatureTypeBuilder featureTypeBuilder = new SimpleFeatureTypeBuilder();
            featureTypeBuilder.setName(indexName);
            for (String attributeName : attributes) {
                AttributeDescriptor attribute =
                        buildAttributeDescriptor(attributeName, solrAttributes);
                if (attribute == null) {
                    continue;
                }
                featureTypeBuilder.add(attribute);
            }
            GeometryConfig defaultGeometry = searchDefaultGeometry();
            if (defaultGeometry != null) {
                featureTypeBuilder.setDefaultGeometry(defaultGeometry.getAttributeName());
            }
            return featureTypeBuilder.buildFeatureType();
        }

        private AttributeDescriptor buildAttributeDescriptor(
                String attributeName, List<SolrAttribute> solrAttributes) {
            SolrAttribute solrAttribute = searchAttribute(attributeName, solrAttributes);
            if (solrAttribute == null) {
                LOGGER.log(
                        Level.WARNING,
                        String.format(
                                "Could not find attribute '%s' in Solar index '%s' schema.",
                                attributeName, indexName));
                return null;
            }
            AttributeDescriptor attribute =
                    buildAttributeDescriptor(solrAttribute, searchGeometry(attributeName));
            attribute
                    .getUserData()
                    .put(SolrFeatureSource.KEY_SOLR_TYPE, solrAttribute.getSolrType());
            return attribute;
        }

        private AttributeDescriptor buildAttributeDescriptor(
                SolrAttribute solrAttribute, GeometryConfig geometry) {
            AttributeTypeBuilder attributeBuilder = new AttributeTypeBuilder();
            if (geometry == null) {
                attributeBuilder.setName(solrAttribute.getName());
                attributeBuilder.setBinding(solrAttribute.getType());
                return attributeBuilder.buildDescriptor(
                        solrAttribute.getName(), attributeBuilder.buildType());
            }
            attributeBuilder.setCRS(geometry.getCrs());
            attributeBuilder.setName(geometry.getAttributeName());
            attributeBuilder.setBinding(geometry.getType());
            return attributeBuilder.buildDescriptor(
                    geometry.getAttributeName(), attributeBuilder.buildGeometryType());
        }

        private static SolrAttribute searchAttribute(
                String attributeName, List<SolrAttribute> solrAttributes) {
            return search(
                    solrAttributes,
                    solrAttribute -> Objects.equals(solrAttribute.getName(), attributeName));
        }

        private static <T> T search(List<T> objects, Function<T, Boolean> predicate) {
            for (T object : objects) {
                if (predicate.apply(object)) {
                    return object;
                }
            }
            return null;
        }
    }

    /** Holder class that contains the information of a geometry attribute. */
    private static final class GeometryConfig {

        private final String attributeName;
        private final CoordinateReferenceSystem crs;
        private final Class<? extends Geometry> type;
        private final boolean isDefault;

        GeometryConfig(
                String attributeName,
                CoordinateReferenceSystem crs,
                Class<? extends Geometry> type,
                boolean isDefault) {
            this.attributeName = attributeName;
            this.crs = crs;
            this.type = type;
            this.isDefault = isDefault;
        }

        public String getAttributeName() {
            return attributeName;
        }

        public CoordinateReferenceSystem getCrs() {
            return crs;
        }

        public Class<? extends Geometry> getType() {
            return type;
        }

        public boolean isDefault() {
            return isDefault;
        }

        private static GeometryConfig create(
                String attributeName, String srid, String type, String isDefault) {
            CoordinateReferenceSystem crs;
            try {
                crs = CRS.decode("EPSG:" + srid);
            } catch (Exception exception) {
                throw new RuntimeException(
                        String.format("Error decoding CRS 'EPSG:%s'.", srid), exception);
            }
            return new GeometryConfig(
                    attributeName, crs, matchGeometryType(type), Boolean.parseBoolean(isDefault));
        }

        private static Class<? extends Geometry> matchGeometryType(String geometryTypeName) {
            switch (geometryTypeName) {
                case "POINT":
                    return Point.class;
                case "LINESTRING":
                    return LineString.class;
                case "POLYGON":
                    return Polygon.class;
                case "MULTIPOINT":
                    return MultiPoint.class;
                case "MULTILINESTRING":
                    return MultiLineString.class;
                case "MULTIPOLYGON":
                    return MultiPolygon.class;
                default:
                    return Geometry.class;
            }
        }
    }
}
