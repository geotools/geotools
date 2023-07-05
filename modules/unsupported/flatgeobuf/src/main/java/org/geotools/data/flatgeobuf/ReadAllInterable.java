/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.flatgeobuf;

import com.google.common.io.LittleEndianDataInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.wololo.flatgeobuf.HeaderMeta;

final class ReadAllInterable implements Iterable<SimpleFeature> {
    private final class IteratorImplementation implements Iterator<SimpleFeature> {
        long count = 0;
        SimpleFeature feature;
        SimpleFeature nextFeature;
        boolean done = false;

        @Override
        public boolean hasNext() {
            if (done) return false;
            if (nextFeature != null) {
                feature = nextFeature;
                nextFeature = null;
            } else {
                try {
                    nextFeature = next();
                } catch (NoSuchElementException e) {
                    done = true;
                    return false;
                }
                return true;
            }
            return true;
        }

        @Override
        public SimpleFeature next() {
            if (nextFeature != null) {
                feature = nextFeature;
                nextFeature = null;
            } else {
                try {
                    feature = FeatureConversions.deserialize(data, fb, headerMeta, count++);
                } catch (IOException e) {
                    throw new NoSuchElementException();
                }
            }
            return feature;
        }
    }

    private final HeaderMeta headerMeta;
    private final LittleEndianDataInputStream data;
    private final SimpleFeatureBuilder fb;

    ReadAllInterable(
            HeaderMeta headerMeta, LittleEndianDataInputStream data, SimpleFeatureBuilder fb) {
        this.headerMeta = headerMeta;
        this.data = data;
        this.fb = fb;
    }

    @Override
    public Iterator<SimpleFeature> iterator() {
        Iterator<SimpleFeature> it = new IteratorImplementation();
        return it;
    }
}
