/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;


/**
 * Filtering is performed on this hasNext() method.
 * 
 * <p>
 * This implementation writes out content furing the hasNext() method. This
 * allows the implementation to "peek" ahead.
 * </p>
 * 
 * <p>
 * This FeatureWriter does not support the addition of new content.
 * </p>
 *
 * @author Jody Garnett, Refractions Research
 *
 * @source $URL$
 */
public class FilteringFeatureWriter implements FeatureWriter<SimpleFeatureType, SimpleFeature> {
    FeatureWriter<SimpleFeatureType, SimpleFeature> writer;
    Filter filter;
    SimpleFeature next = null; // next feature as peeked by hasNext()
    SimpleFeature current = null; // holds current Feature returned to user

    public FilteringFeatureWriter(FeatureWriter<SimpleFeatureType, SimpleFeature> writer, Filter filter) {
        this.writer = writer;
        this.filter = filter;
    }

    public SimpleFeatureType getFeatureType() {
        return writer.getFeatureType();
    }

    public SimpleFeature next() throws IOException {
        if (hasNext()) {
            // use hasNext() to and peek ahead
            // 
            current = next;
            next = null;

            return current;
        }
            // FilteringFeatureWriter Does not support the creation
            // of new content
            throw new NoSuchElementException(
                "FeatureWriter does not have additional content");
    }

    public void remove() throws IOException {
        if (writer == null) {
            throw new IOException("FeatureWriter has been closed");
        }

        if (current == null) {
            // We do not have a current Feature
            // Either:
            // - we have not started yet
            // - hasNext() has already skipped current
            // - write() has already writen current
            // - remove() has already deleted current
            throw new IOException("No feature available to remove");
        }

        current = null;
        writer.remove();
    }

    public void write() throws IOException {
        if (writer == null) {
            throw new IOException("FeatureWriter has been closed");
        }

        if (current == null) {
            // We do not have a current Feature
            // Either:
            // - we have not started yet
            // - hasNext() has already skipped current
            // - write() has already writen current
            // - remove() has already deleted current
            throw new IOException("No feature available to write");
        }

        writer.write();
        current = null;
    }

    /**
     * Query if we have more content.
     *
     * @return true if writer has additional content
     *
     * @throws IOException If writer we are filtering encounters a problem
     */
    public boolean hasNext() throws IOException {
        if (next != null) {
            return true; // we found next already
        }

        if (writer == null) {
            return false; // writer is closed
        }

        if (current != null) {
            // we are skipping ahead, the user will lose
            // any changes to current
            current = null;
        }

        SimpleFeature peek;

        while (writer.hasNext()) {
            peek = writer.next();

            if (filter.evaluate(peek)) {
                next = peek;

                return true; // we have a match!
            }
        }

        return false;
    }

    public void close() throws IOException {
        if (writer != null) {
            writer.close();
            writer = null;
        }

        if (filter != null) {
            filter = null;
        }

        current = null;
        next = null;
    }
}
