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
package org.geotools.data.vpf;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.Name;
import org.geotools.data.store.ContentState;
import org.geotools.data.vpf.file.VPFFile;

/**
 * @author <a href="mailto:knuterik@onemap.org">Knut-Erik Johnsen</a>, Project OneMap
 * @author Chris Holmes, Fulbright.
 * @source $URL$
 */
public class VPFFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {
    private boolean resetCalled = false;
    private SimpleFeature currentFeature = null;
    private final VPFFeatureType featureType;

    /** State used when reading file */
    protected ContentState state;

    /** Creates a new instance of VPFFeatureReader */
    public VPFFeatureReader(VPFFeatureType type) {
        this.featureType = type;
    }

    public VPFFeatureReader(ContentState contentState, VPFFeatureType featureType) throws IOException {
        this.state = contentState;
        this.featureType = featureType;
    }

    /* (non-Javadoc)
     * @see org.geotools.api.data.FeatureReader#close()
     */
    @Override
    public synchronized void close() throws IOException {
        reset();
    }

    /* (non-Javadoc)
     * @see org.geotools.api.data.FeatureReader#getFeatureType()
     */
    @Override
    public SimpleFeatureType getFeatureType() {
        return featureType;
    }

    /* (non-Javadoc)
     * @see org.geotools.api.data.FeatureReader#hasNext()
     */
    @Override
    public synchronized boolean hasNext() throws IOException {
        VPFFeatureClass featureClass = featureType.getFeatureClass();
        if (!resetCalled) {
            this.reset();
        }
        return featureClass.hasNext();
    }

    /* (non-Javadoc)
     * @see org.geotools.api.data.FeatureReader#next()
     */
    @Override
    public synchronized SimpleFeature next() throws IOException, IllegalAttributeException, NoSuchElementException {
        readNext();
        return currentFeature;
    }

    /**
     * Read a row and determine if it matches the feature type Three possibilities here: row is null -- hasNext = false,
     * do not try again row matches -- hasNext = true, do not try again row does not match -- hasNext is undefined
     * because we must try again
     *
     * @return Whether we need to read again
     */
    private synchronized boolean readNext() throws IOException {
        VPFFeatureClass featureClass = featureType.getFeatureClass();
        if (!resetCalled) {
            this.reset();
        }
        currentFeature = featureClass.readNext(featureType);
        return currentFeature != null;
    }

    /**
     * Returns the VPFFile for a particular column. It will only find the first match, but that should be okay because
     * duplicate columns will cause even bigger problems elsewhere.
     *
     * @param column the column to search for
     * @return the VPFFile that owns this column
     */
    public synchronized VPFFile getVPFFile(VPFColumn column) {
        String columnName = column.getName();
        VPFFile result = null;
        VPFFile temp;
        Iterator<VPFFile> iter = featureType.getFeatureClass().getFileList().iterator();
        while (iter.hasNext()) {
            temp = iter.next();
            if (temp != null && temp.getColumn(columnName) != null) {
                result = temp;
                break;
            }
        }
        return result;
    }
    /**
     * Returns the VPFFile for a particular column. It will only find the first match, but that should be okay because
     * duplicate columns will cause even bigger problems elsewhere.
     *
     * @param column the column to search for
     * @return the VPFFile that owns this column
     */
    public synchronized VPFFile getVPFFile(AttributeDescriptor column) {
        Name columnName = column.getName();
        VPFFile result = null;
        VPFFile temp;
        Iterator<VPFFile> iter = featureType.getFeatureClass().getFileList().iterator();
        while (iter.hasNext()) {
            temp = iter.next();
            if (temp != null && temp.getColumn(columnName.getLocalPart()) != null) {
                result = temp;
                break;
            }
        }
        return result;
    }
    /** Need to reset the stream for the next time Resets the iterator by resetting the stream. */
    public synchronized void reset() {
        VPFFeatureClass featureClass = featureType.getFeatureClass();
        featureClass.reset();
        this.resetCalled = true;
    }
}
