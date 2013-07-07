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
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.geometry.BoundingBox;

/**
 * A transforming collection based on the {@link TransformFeatureSource} definitions
 * 
 * @author Andrea Aime - GeoSolution
 * 
 */
class TransformFeatureCollection extends AbstractFeatureCollection {
    
    static final Logger LOGGER = Logging.getLogger(TransformFeatureCollection.class);

    SimpleFeatureSource source;

    Transformer transformer;

    Query query;

    public TransformFeatureCollection(SimpleFeatureSource source, Transformer transformer, Query query) {
        super(retypeSchema(source.getSchema(), query));
        this.source = source;
        this.transformer = transformer;
        this.query = query;
    }

    /**
     * Creates a sub-schema with only the selected attributes
     * 
     * @param schema
     * @param query
     * @return
     */
    static SimpleFeatureType retypeSchema(SimpleFeatureType schema, Query query) {
        if (query.getPropertyNames() == Query.ALL_NAMES) {
            return schema;
        } else {
            return SimpleFeatureTypeBuilder.retype(schema, query.getPropertyNames());
        }
    }

    @Override
    protected Iterator<SimpleFeature> openIterator() {
        try {
            // build the query against the original store
            Query txQuery = transformer.transformQuery(query);
            
            // let the world know about the query re-shaping
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                        "The original query for feature extraction {0} has been transformed to {1}",
                        new Object[] { query, txQuery });
            }
            
            // grab the original features
            SimpleFeatureIterator fi = transformer.getSource().getFeatures(txQuery).features();
            SimpleFeatureIterator transformed = new TransformFeatureIteratorWrapper(fi, transformer);
            
            // see if we have to apply sort
            if(query.getSortBy() != null && txQuery.getSortBy() == null) {
                transformed = new SortedFeatureIterator(transformed, getSchema(), query.getSortBy(), -1);
            }
            
            // see if we have to apply the offset manually
            if(query.getStartIndex() != null && txQuery.getStartIndex() == null) {
                for (int i = 0; i < query.getStartIndex() && transformed.hasNext(); i++) {
                    transformed.next();
                }
            }
            
            // do we also have to apply limits?
            if(txQuery.getMaxFeatures() > query.getMaxFeatures()) {
                transformed = new MaxFeaturesIterator(transformed, query.getMaxFeatures());
            }
            
            return new SimpleFeatureIteratorIterator(transformed);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int size() {
        try {
            Query txQuery = transformer.transformQuery(query);
            int size = source.getCount(query);
            if (size >= 0) {
                // see if we had to hide paging to the wrapped store
                if(query.getStartIndex() != null && txQuery.getStartIndex() == null) {
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

}
