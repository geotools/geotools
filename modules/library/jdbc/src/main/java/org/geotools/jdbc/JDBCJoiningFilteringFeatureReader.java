/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.jdbc;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.geotools.data.DelegatingFeatureReader;
import org.geotools.data.FeatureReader;
import org.geotools.jdbc.JoinInfo.JoinPart;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Feature reader that wraps multiple feature readers in a joining / post filtered query.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class JDBCJoiningFilteringFeatureReader implements DelegatingFeatureReader<SimpleFeatureType, SimpleFeature> {

    FeatureReader<SimpleFeatureType,SimpleFeature> delegate;
    JoinInfo join;
    SimpleFeature next;
    
    public JDBCJoiningFilteringFeatureReader(FeatureReader delegate, JoinInfo join) {
        this.delegate = delegate;
        this.join = join;
    }

    public FeatureReader<SimpleFeatureType, SimpleFeature> getDelegate() {
        return delegate;
    }

    public SimpleFeatureType getFeatureType() {
        return delegate.getFeatureType();
    }

    public boolean hasNext() throws IOException {
        if (next != null) {
            return true;
        }

        //scroll through the delegate reader until we get a feature whose joined features match
        // all the post features
        while(delegate.hasNext()) {
            SimpleFeature peek = delegate.next();

            for (JoinPart part : join.getParts()) {
                if (part.getPostFilter() != null) {
                    SimpleFeature f = (SimpleFeature) peek.getAttribute(part.getAttributeName());
                    if (!part.getPostFilter().evaluate(f)) {
                        peek = null;
                        break;
                    }
                }
            }
            
            if (peek != null) {
                next = peek;
                break;
            }
        }
        
        return next != null;
    }

    public SimpleFeature next() throws IOException, IllegalArgumentException,
            NoSuchElementException {
        if (!hasNext()) {
            throw new NoSuchElementException("No more features");
        }

        SimpleFeature f = next;
        next = null;
        return f;
    }

    public void close() throws IOException {
        delegate.close();
    }
}
