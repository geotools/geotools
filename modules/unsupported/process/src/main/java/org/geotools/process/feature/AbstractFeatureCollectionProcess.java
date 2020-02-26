/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.feature;

import java.util.Map;
import org.geotools.process.impl.AbstractProcess;
import org.opengis.feature.simple.SimpleFeature;

/**
 * A Process for feature collections.
 *
 * @author Justin Deoliveira, OpenGEO
 * @author Michael Bedward
 * @since 2.6
 */
public abstract class AbstractFeatureCollectionProcess extends AbstractProcess {

    /** Constructor */
    public AbstractFeatureCollectionProcess(AbstractFeatureCollectionProcessFactory factory) {
        super(factory);
    }

    /**
     * Performs an operation on a single feature in the collection.
     *
     * <p>This method should do some work based on the feature and then set any attributes on the
     * feature as necessary. Example of a simple buffering operation:
     *
     * <pre>
     * protected void processFeature(SimpleFeature feature, Map<String, Object> input) throws Exception {
     *    Double buffer = (Double) input.get( BufferFeatureCollectionFactory.BUFFER.key );
     *
     *    Geometry g = (Geometry) feature.getDefaultGeometry();
     *    g = g.buffer( buffer );
     *
     *    feature.setDefaultGeometry( g );
     * }
     * </pre>
     *
     * @param feature the feature being processed
     * @param input a Map of input parameters
     */
    protected abstract void processFeature(SimpleFeature feature, Map<String, Object> input)
            throws Exception;
}
