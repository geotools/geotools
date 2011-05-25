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


/**
 *  FeatureReader<SimpleFeatureType, SimpleFeature> customized for FeatureID handling.
 * 
 * <p>
 * An experimental method for doing FIDs.   I'd like to see it and
 * AttributeReader extend a similar base.  Perhaps BaseReader or something?
 * And perhaps have  FeatureReader<SimpleFeatureType, SimpleFeature> extend it too? This reader just returns an
 * incrementing index.  May be sufficient for files, representing rows in a
 * file.  For jdbc datasources another fid reader should be used.
 * </p>
 * 
 * <p>
 * We could have FIDReader implement AttributeReader, but it doesn't seem to
 * make sense, as the getAttributeType doesn't make much sense, as our
 * featureID is just a string.  Or we could consider having a special FID
 * attribute in our hierarchy.
 * </p>
 *
 * @author Chris Holmes
 *
 * @source $URL$
 * @version $Id$
 */
public interface FIDReader {
    /**
     * Release any resources associated with this reader
     */
    void close() throws IOException;

    /**
     * Returns whether another fid exists for this reader.
     *
     * @return <code>true</code> if more content exists
     */
    boolean hasNext() throws IOException;

    /**
     * Gets the next FID from the Reader.
     *
     * @return Next featureID
     */
    String next() throws IOException;
}
