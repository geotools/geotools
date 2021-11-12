/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.geotools.data.FeatureReader;
import org.geotools.data.elasticsearch.date.ElasticsearchDateConverter;
import org.geotools.data.store.ContentState;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

/** FeatureReader access to the Elasticsearch index. */
class ElasticFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {

    private static final Logger LOGGER = Logging.getLogger(ElasticFeatureReader.class);

    private final ContentState state;

    private final SimpleFeatureType featureType;

    private final float maxScore;

    private final ObjectMapper mapper;

    private final ElasticDataStore.ArrayEncoding arrayEncoding;

    private SimpleFeatureBuilder builder;

    private Iterator<ElasticHit> searchHitIterator;

    private Iterator<Map<String, Object>> aggregationIterator;

    private final ElasticParserUtil parserUtil;

    public ElasticFeatureReader(ContentState contentState, ElasticResponse response) {
        this(contentState, response.getHits(), response.getAggregations(), response.getMaxScore());
    }

    public ElasticFeatureReader(
            ContentState contentState,
            List<ElasticHit> hits,
            Map<String, ElasticAggregation> aggregations,
            float maxScore) {
        this.state = contentState;
        this.featureType = state.getFeatureType();
        this.searchHitIterator = hits.iterator();
        this.builder = new SimpleFeatureBuilder(featureType);
        this.parserUtil = new ElasticParserUtil();
        this.maxScore = maxScore;

        this.aggregationIterator = Collections.emptyIterator();
        if (aggregations != null && !aggregations.isEmpty()) {
            String aggregationName = aggregations.keySet().stream().findFirst().orElse(null);
            if (aggregations.size() > 1) {
                LOGGER.info("Result has multiple aggregations. Using " + aggregationName);
            }
            if (aggregations.get(aggregationName).getBuckets() != null) {
                this.aggregationIterator =
                        aggregations.get(aggregationName).getBuckets().iterator();
            }
        }

        if (contentState.getEntry() != null && contentState.getEntry().getDataStore() != null) {
            final ElasticDataStore dataStore =
                    (ElasticDataStore) contentState.getEntry().getDataStore();
            this.arrayEncoding = dataStore.getArrayEncoding();
        } else {
            this.arrayEncoding =
                    ElasticDataStore.ArrayEncoding.valueOf(
                            (String) ElasticDataStoreFactory.ARRAY_ENCODING.getDefaultValue());
        }

        this.mapper = new ObjectMapper();
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return this.featureType;
    }

    @Override
    public SimpleFeature next() {
        final String id;
        if (searchHitIterator.hasNext()) {
            id = nextHit();
        } else {
            nextAggregation();
            id = null;
        }
        return builder.buildFeature(id);
    }

    private String nextHit() {
        final ElasticHit hit = searchHitIterator.next();
        final SimpleFeatureType type = getFeatureType();
        final Map<String, Object> source = hit.getSource();

        final Float score;
        final Float relativeScore;
        if (hit.getScore() != null && !Float.isNaN(hit.getScore()) && maxScore > 0) {
            score = hit.getScore();
            relativeScore = score / maxScore;
        } else {
            score = null;
            relativeScore = null;
        }

        for (final AttributeDescriptor descriptor : type.getAttributeDescriptors()) {
            final String name = descriptor.getType().getName().getLocalPart();
            final String sourceName =
                    (String) descriptor.getUserData().get(ElasticConstants.FULL_NAME);

            List<Object> values = hit.field(sourceName);
            if (values == null && source != null) {
                // read field from source
                values = parserUtil.readField(source, sourceName);
            }

            if (values == null && sourceName.equals("_id")) {
                builder.set(name, hit.getId());
            } else if (values == null && sourceName.equals("_index")) {
                builder.set(name, hit.getIndex());
            } else if (values == null && sourceName.equals("_type")) {
                builder.set(name, hit.getType());
            } else if (values == null && sourceName.equals("_score")) {
                builder.set(name, score);
            } else if (values == null && sourceName.equals("_relative_score")) {
                builder.set(name, relativeScore);
            } else if (values != null
                    && Geometry.class.isAssignableFrom(descriptor.getType().getBinding())) {
                if (values.size() == 1) {
                    builder.set(name, parserUtil.createGeometry(values.get(0)));
                } else {
                    builder.set(name, parserUtil.createGeometry(values));
                }
            } else if (values != null
                    && Date.class.isAssignableFrom(descriptor.getType().getBinding())) {
                Object dataVal = values.get(0);
                if (dataVal instanceof Double) {
                    builder.set(name, new Date(Math.round((Double) dataVal)));
                } else if (dataVal instanceof Integer) {
                    builder.set(name, new Date(((Integer) dataVal).longValue()));
                } else if (dataVal instanceof Long) {
                    builder.set(name, new Date((long) dataVal));
                } else {
                    @SuppressWarnings("unchecked")
                    final List<String> validFormats =
                            (List<String>)
                                    descriptor.getUserData().get(ElasticConstants.DATE_FORMAT);
                    ElasticsearchDateConverter dateFormatter = null;
                    Date date = null;
                    if (!(validFormats == null) && !(validFormats.isEmpty())) {
                        for (String format : validFormats) {
                            try {
                                dateFormatter = ElasticsearchDateConverter.forFormat(format);
                                date = dateFormatter.parse((String) dataVal);
                                break;
                            } catch (Exception e) {
                                LOGGER.fine(
                                        "Unable to parse date format ('"
                                                + format
                                                + "') for "
                                                + descriptor);
                            }
                        }
                    }
                    if (dateFormatter == null) {
                        LOGGER.fine("Unable to find any valid date format for " + descriptor);
                        dateFormatter = ElasticsearchDateConverter.forFormat("date_optional_time");
                    }
                    if (date == null) {
                        LOGGER.fine("Unable to find any valid date format for " + dataVal);
                        date = dateFormatter.parse((String) dataVal);
                    }
                    builder.set(name, date);
                }
            } else if (values != null && values.size() == 1) {
                builder.set(name, values.get(0));
            } else if (values != null && !name.equals("_aggregation")) {
                final Object value;
                if (arrayEncoding == ElasticDataStore.ArrayEncoding.CSV) {
                    // only include first array element when using CSV array encoding
                    value = values.get(0);
                } else {
                    value = values;
                }
                builder.set(name, value);
            }
        }

        return state.getEntry().getTypeName() + "." + hit.getId();
    }

    private void nextAggregation() {
        final Map<String, Object> aggregation = aggregationIterator.next();
        try {
            final byte[] data = mapper.writeValueAsBytes(aggregation);
            builder.set("_aggregation", data);
        } catch (IOException e) {
            LOGGER.warning("Unable to set aggregation. Try reloading layer.");
        }
    }

    @Override
    public boolean hasNext() {
        return searchHitIterator.hasNext() || aggregationIterator.hasNext();
    }

    @Override
    public void close() {
        builder = null;
        searchHitIterator = null;
    }
}
