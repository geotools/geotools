/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Allows to evaluate the differences in the state of the data between two revisions
 * 
 * @author Andrea Aime - OpenGeo
 * 
 */
public interface FeatureDiffReader {

    /**
     * The first version used to compute the difference
     */
    public String getFromVersion();

    /**
     * The second version used to computed the difference
     */
    public String getToVersion();

    /**
     * Returns the feature type whose features are diffed with this reader
     * 
     * @return
     */
    public SimpleFeatureType getSchema();

    /**
     * Reads the next FeatureDifference
     * 
     * @return The next FeatureDifference
     * 
     * @throws IOException
     *             If an error occurs reading the FeatureDifference.
     * @throws NoSuchElementException
     *             If there are no more Features in the Reader.
     */
    public FeatureDiff next() throws IOException, NoSuchElementException;

    /**
     * Query whether this FeatureDiffReader has another FeatureDiff.
     * 
     * @return True if there are more differences to be read. In other words, true if calls to next
     *         would return a feature rather than throwing an exception.
     * 
     * @throws IOException
     *             If an error occurs determining if there are more Features.
     */
    public boolean hasNext() throws IOException;

    /**
     * Release the underlying resources associated with this reader.
     * 
     * @throws IOException
     */
    public void close() throws IOException;
}
