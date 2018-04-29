/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.sort;

import java.io.IOException;
import java.io.RandomAccessFile;
import org.opengis.feature.simple.SimpleFeature;

/**
 * Reads the features stored in the specified block of a {@link RandomAccessFile}
 *
 * @author Andrea Aime - GeoSolutions
 */
class FeatureBlockReader {

    SimpleFeature curr;

    long offset;

    int count;

    SimpleFeatureIO io;

    public FeatureBlockReader(SimpleFeatureIO io, long start, int count) {
        this.offset = start;
        this.count = count;
        this.io = io;
    }

    public SimpleFeature feature() throws IOException {
        if (curr == null && count > 0) {
            curr = readNextFeature();
        }
        return curr;
    }

    public SimpleFeature next() throws IOException {
        curr = readNextFeature();
        return curr;
    }

    private SimpleFeature readNextFeature() throws IOException {
        if (count <= 0) {
            return null;
        }

        // move to the next feature offset
        io.seek(offset);

        // read the feature
        SimpleFeature sf = io.read();

        // update the offset for the next feature
        offset = io.getOffset();
        count--;

        // return the feature
        return sf;
    }
}
