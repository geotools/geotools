/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import com.vividsolutions.jts.geom.Geometry;

import static mil.nga.giat.data.elasticsearch.ElasticLayerConfiguration.DATE_FORMAT;
import static mil.nga.giat.data.elasticsearch.ElasticLayerConfiguration.FULL_NAME;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.base.Joiner;
import org.elasticsearch.common.joda.Joda;
import org.elasticsearch.common.joda.time.format.DateTimeFormatter;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.geotools.data.FeatureReader;
import org.geotools.data.store.ContentState;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * FeatureReader access to the Elasticsearch index.
 */
public class ElasticFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {

    private final ContentState state;

    private final SimpleFeatureType featureType;

    private final float maxScore;

    private SimpleFeatureBuilder builder;

    private Iterator<SearchHit> searchHitIterator;

    private ElasticParserUtil parserUtil;

    public ElasticFeatureReader(ContentState contentState, SearchResponse response) {
    	this.state = contentState;
    	this.featureType = state.getFeatureType();
    	this.searchHitIterator = response.getHits().iterator();
    	this.builder = new SimpleFeatureBuilder(featureType);
    	this.parserUtil = new ElasticParserUtil();
    	this.maxScore = response.getHits().getMaxScore();
    }
    
    public ElasticFeatureReader(ContentState contentState, Iterator<SearchHit> searchHitIterator) {
    	this.state = contentState;
        this.featureType = state.getFeatureType();
        this.searchHitIterator = searchHitIterator;
        this.builder = new SimpleFeatureBuilder(featureType);
        this.parserUtil = new ElasticParserUtil();
        this.maxScore = 0;
    }    

    @Override
    public SimpleFeatureType getFeatureType() {
        return state.getFeatureType();
    }

    @Override
    public SimpleFeature next() {
        final SearchHit hit = searchHitIterator.next();
        final SimpleFeatureType type = getFeatureType();
        final Map<String, Object> source = hit.getSource();

        final Float score;
        final Float relativeScore;
        if (!Float.isNaN(hit.getScore()) && maxScore>0) {
            score = hit.getScore();
            relativeScore = score / maxScore;
        } else {
            score = null;
            relativeScore = null;
        }

        for (final AttributeDescriptor descriptor : type.getAttributeDescriptors()) {
            final String name = descriptor.getType().getName().getLocalPart();
            final String sourceName = (String) descriptor.getUserData().get(FULL_NAME);

            final SearchHitField field = hit.field(sourceName);
            List<Object> values = null;
            if (field != null) {
                // hit field
                values = field.values();
            }
            if (values == null && source != null) {
                // read field from source
                values = parserUtil.readField(source, sourceName);
            }

            if (values == null && name.equals("_id")) {
                builder.set(name, hit.getId());
            } else if (values == null && name.equals("_index")) {
                builder.set(name, hit.getIndex());
            } else if (values == null && name.equals("_type")) {
                builder.set(name, hit.getType());
            } else if (values == null && name.equals("_score")) {
                builder.set(name, score);
            } else if (values == null && name.equals("_relative_score")) {
                builder.set(name, relativeScore);
            } else if (values == null) {
                // skip missing attribute
            } else if (Geometry.class.isAssignableFrom(descriptor.getType().getBinding())) {
                builder.set(name, parserUtil.createGeometry(values.get(0)));
            } else if (Date.class.isAssignableFrom(descriptor.getType().getBinding())) {
                Object dataVal = values.get(0);
                if (dataVal instanceof Double) {
                    builder.set(name, new Date(Math.round((Double) dataVal)));
                } else if (dataVal instanceof Integer) {
                    builder.set(name, new Date((Integer) dataVal));
                } else if (dataVal instanceof Long) {
                    builder.set(name, new Date((long) dataVal));
                } else {
                    final String format = (String) descriptor.getUserData().get(DATE_FORMAT);
                    final DateTimeFormatter dateFormatter = Joda.forPattern(format).parser();
                    Date date = dateFormatter.parseDateTime((String) dataVal).toDate();
                    builder.set(name, date);
                }
            } else if (values.size() == 1) {
                builder.set(name, values.get(0));
            } else if (String.class.isAssignableFrom(descriptor.getType().getBinding())) {
                builder.set(name, Joiner.on(';').join(values));
            } else {
                builder.set(name, values);
            }
        }

        final String typeName = state.getEntry().getTypeName();
        final SimpleFeature feature;
        feature = builder.buildFeature(typeName + "." + hit.getId());
        return feature;
    }

    @Override
    public boolean hasNext() {
        return searchHitIterator.hasNext();
    }

    @Override
    public void close() {
        builder = null;
        searchHitIterator = null;
    }

}
