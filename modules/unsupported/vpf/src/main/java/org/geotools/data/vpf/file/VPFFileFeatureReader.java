/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.vpf.file;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.geotools.data.FeatureReader;
import org.geotools.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;


/**
 * A feature reader for the VPFFile object
 *
 * @author <a href="mailto:jeff@ionicenterprise.com">Jeff Yutzler</a>
 *
 * @source $URL$
 */
public class VPFFileFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {
    private final VPFFile featureType;
    private SimpleFeature currentFeature;

    public VPFFileFeatureReader(VPFFile type) {
        featureType = type;
        currentFeature = null;

        featureType.reset();
    }

    /* (non-Javadoc)
     * @see org.geotools.data.FeatureReader#getFeatureType()
     */
    public SimpleFeatureType getFeatureType() {
        return featureType;
    }

    /* (non-Javadoc)
     * @see org.geotools.data.FeatureReader#next()
     */
    public SimpleFeature next()
        throws IOException, IllegalAttributeException, NoSuchElementException {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        currentFeature = featureType.readFeature();

        return currentFeature;
    }

    /* (non-Javadoc)
     * @see org.geotools.data.FeatureReader#hasNext()
     */
    public boolean hasNext() throws IOException {
        boolean result = false;

        // Ask the stream if it has space for another object
        result = featureType.hasNext();
        return result;
    }

    /* (non-Javadoc)
     * @see org.geotools.data.FeatureReader#close()
     */
    public void close() throws IOException {
        // TODO Auto-generated method stub
    }
}
