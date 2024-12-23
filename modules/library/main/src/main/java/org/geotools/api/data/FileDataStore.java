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
package org.geotools.api.data;

import java.io.IOException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;

/**
 * DataStore represents a single file of content.
 *
 * <p>Allows developer to skip refering to the typeName when a file contains only a single set of content.
 */
public interface FileDataStore extends DataStore {

    /**
     * FeatureType for the file being read.
     *
     * <p>This is the same as getSchema( getTypeName[0] )
     *
     * @return FeatureType of the file being read
     * @see DataStore#getSchema(java.lang.String)
     */
    SimpleFeatureType getSchema() throws IOException;

    /** @see DataStore#updateSchema(java.lang.String,SimpleFeatureType) */
    void updateSchema(SimpleFeatureType featureType) throws IOException;

    /** @see DataStore#getFeatureSource(java.lang.String) */
    SimpleFeatureSource getFeatureSource() throws IOException;

    /** @see DataStore#getFeatureReader(Query,Transaction) */
    FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader() throws IOException;

    /** @see DataStore#getFeatureWriter(String,Filter,Transaction) */
    FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(Filter filter, Transaction transaction)
            throws IOException;

    /** @see DataStore#getFeatureWriter(java.lang.String, Transaction) */
    FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(Transaction transaction) throws IOException;

    /** @see DataStore#getFeatureWriterAppend(java.lang.String, Transaction) */
    FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(Transaction transaction) throws IOException;
}
