/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.data.simple.SimpleFeatureWriter;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Feature utility class.
 * 
 * @author Justin Deoliveira, OpenGeo
 */
public class Features {

    public static SimpleFeatureReader simple(final FeatureReader r) {
        if (r instanceof SimpleFeatureReader) {
            return (SimpleFeatureReader) r;
        }
        return new SimpleFeatureReader() {
            @Override
            public SimpleFeatureType getFeatureType() {
                return (SimpleFeatureType) r.getFeatureType();
            }

            @Override
            public boolean hasNext() throws IOException {
                return r.hasNext();
            }

            @Override
            public void close() throws IOException {
                r.close();
            }

            @Override
            public SimpleFeature next() throws IOException, IllegalArgumentException,
                    NoSuchElementException {
                return (SimpleFeature) r.next();
            }
        };
    }

    public static SimpleFeatureWriter simple(final FeatureWriter w) {
        if (w instanceof SimpleFeatureWriter) {
            return (SimpleFeatureWriter) w;
        }

        return new SimpleFeatureWriter() {

            @Override
            public SimpleFeatureType getFeatureType() {
                return (SimpleFeatureType) w.getFeatureType();
            }

            @Override
            public SimpleFeature next() throws IOException {
                return (SimpleFeature) w.next();
            }
            
            @Override
            public boolean hasNext() throws IOException {
                return w.hasNext();
            }

            @Override
            public void write() throws IOException {
                w.write();
            }

            @Override
            public void remove() throws IOException {
                w.remove();
            }
            
            @Override
            public void close() throws IOException {
                w.close();
            }
        };
    }
}
