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
import org.geotools.api.data.FeatureReader;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.store.ContentState;

/**
 * A feature reader for the VPFFile object
 *
 * @author <a href="mailto:jeff@ionicenterprise.com">Jeff Yutzler</a>
 * @source $URL$
 */
public class VPFFileFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {
    private final VPFFile file;
    private SimpleFeature currentFeature;

    /** State used when reading file */
    protected ContentState state;

    public VPFFileFeatureReader(VPFFile file) {
        this.file = file;
        currentFeature = null;
        if (this.file != null) this.file.reset();
    }

    public VPFFileFeatureReader(ContentState contentState, VPFFile file) throws IOException {
        this.state = contentState;
        this.file = file;
        this.currentFeature = null;
        if (this.file != null) this.file.reset();
    }

    /* (non-Javadoc)
     * @see org.geotools.api.data.FeatureReader#getFeatureType()
     */
    @Override
    public SimpleFeatureType getFeatureType() {
        return file != null ? file.getFeatureType() : null;
    }

    /* (non-Javadoc)
     * @see org.geotools.api.data.FeatureReader#next()
     */
    @Override
    public SimpleFeature next() throws IOException, NoSuchElementException {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        currentFeature = file != null ? file.readFeature() : null;

        return currentFeature;
    }

    /* (non-Javadoc)
     * @see org.geotools.api.data.FeatureReader#hasNext()
     */
    @Override
    public boolean hasNext() throws IOException {

        // Ask the stream if it has space for another object
        boolean result = file != null ? file.hasNext() : false;
        return result;
    }

    /* (non-Javadoc)
     * @see org.geotools.api.data.FeatureReader#close()
     */
    @Override
    public void close() throws IOException {
        // TODO Auto-generated method stub
        if (this.file != null) this.file.close();
    }
}
