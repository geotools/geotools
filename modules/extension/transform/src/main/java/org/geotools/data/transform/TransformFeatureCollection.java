/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.transform;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.sort.SortedFeatureIterator;
import org.geotools.data.store.EmptyFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.collection.AbstractFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.visitor.CountVisitor;
import org.geotools.feature.visitor.FeatureAttributeVisitor;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.feature.visitor.UniqueVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.logging.Logging;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.geometry.BoundingBox;
import org.opengis.util.ProgressListener;

/**
 * A transforming collection based on the {@link TransformFeatureSource} definitions
 *
 * @author Andrea Aime - GeoSolution
 */
class TransformFeatureCollection extends AbstractFeatureCollection {

    static final Logger LOGGER = Logging.getLogger(TransformFeatureCollection.class);

    SimpleFeatureSource source;

    Transformer transformer;

    Query query;

    public TransformFeatureCollection(
            SimpleFeatureSource source, Transformer transformer, Query query) {
        super(retypeSchema(source.getSchema(), query));
        this.source = source;
        this.transformer = transformer;
        this.query = query;
    }

    /** Creates a sub-schema with only the selected attributes */
    static SimpleFeatureType retypeSchema(SimpleFeatureType schema, Query query) {
        if (query.getPropertyNames() == Query.ALL_NAMES) {
            return schema;
        } else {
            return SimpleFeatureTypeBuilder.retype(schema, query.getPropertyNames());
        }
    }

    @Override
    @SuppressWarnings("PMD.CloseResource") // convoluted logic, but "fi" is closed or returned
    protected Iterator<SimpleFeature> openIterator() {
        SimpleFeatureIterator fi = null;
        Iterator<SimpleFeature> result = null;
        try {
            // build the query against the original store
            Query txQuery = transformer.transformQuery(query);

            // let the world know about the query re-shaping
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(
                        Level.FINE,
                        "The original query for feature extraction {0} has been transformed to {1}",
                        new Object[] {query, txQuery});
            }

            // grab the original features
            fi = transformer.getSource().getFeatures(txQuery).features();
            SimpleFeatureIterator transformed =
                    new TransformFeatureIteratorWrapper(fi, transformer);

            // see if we have to apply sort
            if (query.getSortBy() != null && txQuery.getSortBy() == null) {
                transformed =
                        new SortedFeatureIterator(transformed, getSchema(), query.getSortBy(), -1);
            }

            // see if we have to apply the offset manually
            if (query.getStartIndex() != null && txQuery.getStartIndex() == null) {
                for (int i = 0; i < query.getStartIndex() && transformed.hasNext(); i++) {
                    transformed.next();
                }
            }

            // do we also have to apply limits?
            if (txQuery.getMaxFeatures() > query.getMaxFeatures()) {
                transformed = new MaxFeaturesIterator(transformed, query.getMaxFeatures());
            }

            result = new SimpleFeatureIteratorIterator(transformed);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // if result is null, an exception has occurred, close the wrapped iterator
            if (result == null) {
                fi.close();
            }
        }

        return result;
    }

    @Override
    public int size() {
        try {
            Query txQuery = transformer.transformQuery(query);
            int size = source.getCount(query);
            if (size >= 0) {
                // see if we had to hide paging to the wrapped store
                if (query.getStartIndex() != null && txQuery.getStartIndex() == null) {
                    size -= query.getStartIndex();
                }
                return Math.min(size, query.getMaxFeatures());
            }

            // sigh, fall back to brute force computation
            SimpleFeatureIterator fi = null;
            try {
                size = 0;
                fi = source.getFeatures(query).features();
                while (fi.hasNext()) {
                    fi.next();
                    size++;
                }

                return size;
            } finally {
                if (fi != null) {
                    fi.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ReferencedEnvelope getBounds() {
        try {
            ReferencedEnvelope re = source.getBounds(query);
            if (re != null) {
                return re;
            }

            // sigh, fall back to brute force computation
            SimpleFeatureIterator fi = null;
            try {
                fi = source.getFeatures(query).features();
                while (fi.hasNext()) {
                    SimpleFeature f = fi.next();
                    BoundingBox bb = f.getBounds();
                    if (bb != null) {
                        ReferencedEnvelope ref = ReferencedEnvelope.reference(bb);
                        if (re == null) {
                            re = ref;
                        } else {
                            re.expandToInclude(ref);
                        }
                    }
                }

                return re;
            } finally {
                if (fi != null) {
                    fi.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SimpleFeatureCollection subCollection(Filter filter) {
        if (filter == Filter.INCLUDE) {
            return this;
        } else if (filter == Filter.EXCLUDE || query.getFilter() == Filter.EXCLUDE) {
            return new EmptyFeatureCollection(getSchema());
        }

        Query q = new Query(query);
        if (query.getFilter() == Filter.INCLUDE) {
            q.setFilter(filter);
        } else {
            FilterFactory ff = CommonFactoryFinder.getFilterFactory();
            Filter combined = ff.and(filter, q.getFilter());
            q.setFilter(combined);
        }
        return new TransformFeatureCollection(source, transformer, q);
    }

    /**
     * Checks if the visitor is accessing only properties available in the specified feature type,
     * checks if the target schema contains transformed attributes or as a special case, if it's a
     * count visitor accessing no properties at all
     */
    protected boolean isTypeCompatible(FeatureVisitor visitor, SimpleFeatureType featureType) {
        if (visitor instanceof CountVisitor) {
            // pass through if the CountVisitor has been recognized
            return true;
        } else if (visitor instanceof FeatureAttributeVisitor) {
            // allow passing down if the properties requested are not computed not renamed,
            // thus can be passed to the delegate collection as is
            for (Expression e : ((FeatureAttributeVisitor) visitor).getExpressions()) {
                if (!(e instanceof PropertyName)) {
                    return false;
                }
                PropertyName externalName = (PropertyName) e;
                Expression attributeExpression =
                        transformer.getExpression(externalName.getPropertyName());
                if (!(attributeExpression instanceof PropertyName)) {
                    return false;
                }
                if (!((PropertyName) attributeExpression)
                        .getPropertyName()
                        .equals(externalName.getPropertyName())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void accepts(
            org.opengis.feature.FeatureVisitor visitor, org.opengis.util.ProgressListener progress)
            throws IOException {
        if (isTypeCompatible(visitor, transformer.getSchema())) {
            delegateVisitor(visitor, progress);
        } else if (visitor instanceof MinVisitor) {
            MinVisitor original = (MinVisitor) visitor;
            Expression transformedExpression =
                    transformer.transformExpression(original.getExpression());
            MinVisitor transformedVisitor = new MinVisitor(transformedExpression);
            delegateVisitor(transformedVisitor, progress);
            original.setValue(transformedVisitor.getResult().getValue());
        } else if (visitor instanceof MaxVisitor) {
            MaxVisitor original = (MaxVisitor) visitor;
            Expression transformedExpression =
                    transformer.transformExpression(original.getExpression());
            MaxVisitor transformedVisitor = new MaxVisitor(transformedExpression);
            delegateVisitor(transformedVisitor, progress);
            original.setValue(transformedVisitor.getResult().getValue());
        } else if (visitor instanceof UniqueVisitor) {
            UniqueVisitor original = (UniqueVisitor) visitor;
            Expression transformedExpression =
                    transformer.transformExpression(original.getExpression());
            UniqueVisitor transformedVisitor = new UniqueVisitor(transformedExpression);
            transformedVisitor.setMaxFeatures(original.getMaxFeatures());
            transformedVisitor.setStartIndex(original.getStartIndex());
            transformedVisitor.setPreserveOrder(original.isPreserveOrder());
            delegateVisitor(transformedVisitor, progress);
            original.setValue(transformedVisitor.getResult().getValue());
        } else {
            super.accepts(visitor, progress);
        }
    }

    protected void delegateVisitor(FeatureVisitor visitor, ProgressListener progress)
            throws IOException {
        Name typeName = transformer.getSource().getName();
        Query txQuery = transformer.transformQuery(query);
        source.getDataStore()
                .getFeatureSource(typeName)
                .getFeatures(txQuery)
                .accepts(visitor, progress);
    }
}
