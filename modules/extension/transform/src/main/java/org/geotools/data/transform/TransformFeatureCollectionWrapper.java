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
import java.util.List;

import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.collection.AbstractFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.geometry.BoundingBox;

/**
 * A reshaping collection based on a user provided feature collection
 * 
 * @author Andrea Aime - GeoSolution
 */
class TransformFeatureCollectionWrapper extends AbstractFeatureCollection {

    FeatureCollection<SimpleFeatureType, SimpleFeature> wrapped;

    Transformer transformer;

    public TransformFeatureCollectionWrapper(
            FeatureCollection<SimpleFeatureType, SimpleFeature> wrapped, Transformer transformer) {
        super(wrapped.getSchema());
        this.wrapped = wrapped;
        this.transformer = transformer;
    }

    @Override
    protected Iterator<SimpleFeature> openIterator() {
        try {
            return new SimpleFeatureIteratorIterator(new TransformFeatureIteratorWrapper(
                    wrapped.features(), transformer));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int size() {
        return wrapped.size();
    }

    @Override
    public ReferencedEnvelope getBounds() {
        // see if we can just reflect the original collection bounds
        List<String> names = transformer.getGeometryPropertyNames();
        if (names == null) {
            return null;
        }
        boolean geometryTransformed = false;
        for (String name : names) {
            Expression expression = transformer.getExpression(name);
            if (expression != null && !(expression instanceof PropertyName)) {
                geometryTransformed = true;

            }
        }
        if (!geometryTransformed) {
            return wrapped.getBounds();
        }

        // sigh, fall back to brute force computation
        SimpleFeatureIterator fi = null;
        ReferencedEnvelope re = null;
        try {
            fi = features();
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
    }
}
