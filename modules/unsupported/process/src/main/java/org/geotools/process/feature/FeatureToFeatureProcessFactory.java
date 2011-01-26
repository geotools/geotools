/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.Parameter;
import org.geotools.feature.FeatureCollection;
import org.geotools.process.ProcessFactory;
import org.geotools.text.Text;

/**
 * Base class for process factories which perform an operation on each feature in a feature 
 * collection with the result being a feature collection (the original collection modified
 * or a new collection).
 * <p>
 * <b>Note</b>: This base class is intended to be used for processes which operate on each feature in a feature 
 * collection, resulting in a new feature collection which has the same schema as the original.
 * </p>
 * <p>
 * Subclasses must implement:
 * <ul>
 *   <li>{@link ProcessFactory#getTitle()}
 *   <li>{@link ProcessFactory#getDescription()}
 *   <li>{@link #addParameters(Map)}
 *   <li>
 * </ul>
 * </p>
 * 
 * @author Justin Deoliveira, OpenGEO
 * @since 2.6
 *
 * @source $URL$
 */
public abstract class FeatureToFeatureProcessFactory extends AbstractFeatureCollectionProcessFactory {

    private static final String VERSION = "1.0.0";

    /** 
     * Result of the operation is a FeatureCollection.
     * This can be the input FeatureCollection, modified by the process
     * or a new FeatureCollection.
     */
    static final Parameter<FeatureCollection> RESULT = new Parameter<FeatureCollection>(
            "result", FeatureCollection.class, Text.text("Result"), Text
                    .text("Buffered features"));
    
    static final Map<String,Parameter<?>> resultInfo = new HashMap<String, Parameter<?>>();

    static {
        resultInfo.put( RESULT.key, RESULT );
    }
    
    public final Map<String, Parameter<?>> getResultInfo(
            Map<String, Object> parameters) throws IllegalArgumentException {
        return Collections.unmodifiableMap( resultInfo );
    }
    
    public final boolean supportsProgress() {
        return true;
    }

    public String getVersion() {
        return VERSION;
    }
}
