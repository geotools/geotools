/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.store;

import java.io.IOException;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.collection.DelegateFeatureReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.collection.DecoratingSimpleFeatureCollection;
import org.geotools.feature.visitor.CountVisitor;
import org.geotools.feature.visitor.FeatureAttributeVisitor;
import org.geotools.filter.FilterAttributeExtractor;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;

/**
 * SimpleFeatureCollection decorator which decorates a feature collection "re-typing" its schema
 * based on attributes specified in a query.
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public class ReTypingFeatureCollection extends DecoratingSimpleFeatureCollection {

    SimpleFeatureType featureType;

    public ReTypingFeatureCollection(
            FeatureCollection<SimpleFeatureType, SimpleFeature> delegate,
            SimpleFeatureType featureType) {
        this(DataUtilities.simple(delegate), featureType);
    }

    public ReTypingFeatureCollection(
            SimpleFeatureCollection delegate, SimpleFeatureType featureType) {
        super(delegate);
        this.featureType = featureType;
    }

    public SimpleFeatureType getSchema() {
        return featureType;
    }

    public FeatureReader<SimpleFeatureType, SimpleFeature> reader() throws IOException {
        return new DelegateFeatureReader<SimpleFeatureType, SimpleFeature>(getSchema(), features());
    }

    public SimpleFeatureIterator features() {
        return new ReTypingFeatureIterator(delegate.features(), delegate.getSchema(), featureType);
    }

    @Override
    protected boolean canDelegate(FeatureVisitor visitor) {
        return isTypeCompatible(visitor, featureType);
    }

    /**
     * Checks if the visitor is accessing only properties available in the specified feature type,
     * or as a special case, if it's a count visitor accessing no properties at all
     */
    public static boolean isTypeCompatible(FeatureVisitor visitor, SimpleFeatureType featureType) {
        if (visitor instanceof FeatureAttributeVisitor) {
            // pass through if the target schema contains all the necessary attributes
            FilterAttributeExtractor extractor = new FilterAttributeExtractor(featureType);
            for (Expression e : ((FeatureAttributeVisitor) visitor).getExpressions()) {
                e.accept(extractor, null);
            }

            for (PropertyName pname : extractor.getPropertyNameSet()) {
                AttributeDescriptor att = (AttributeDescriptor) pname.evaluate(featureType);
                if (att == null) {
                    return false;
                }
            }
            return true;
        } else if (visitor instanceof CountVisitor) {
            return true;
        }
        return false;
    }
}
