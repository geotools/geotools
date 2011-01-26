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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.geotools.data.Parameter;
import org.geotools.feature.FeatureCollection;
import org.geotools.process.ProcessFactory;
import org.geotools.process.impl.SingleProcessFactory;
import org.geotools.text.Text;

/**
 * Base class for process factories which perform an operation on each feature in a feature 
 * collection.
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
public abstract class AbstractFeatureCollectionProcessFactory extends SingleProcessFactory {
    /** Features for operation */
    public static final Parameter<FeatureCollection> FEATURES = new Parameter<FeatureCollection>(
        "features", FeatureCollection.class, Text.text("Features"), Text.text("Features to process"));

    /**
     * Adds the {@link #FEATURES} parameter and then delegates to {@link #addParameters(Map)}.
     */
    public final Map<String, Parameter<?>> getParameterInfo() {
        HashMap<String,Parameter<?>> parameterInfo = new LinkedHashMap<String, Parameter<?>>();
        parameterInfo.put( FEATURES.key, FEATURES );
        addParameters( parameterInfo );
        return parameterInfo;
    }
    
    /**
     * Method for subclasses to add parameter descriptors for the process.
     * <p>
     * Subclasses should not add a parameter for the input feature collection as this is done by 
     * the case class. Example implementation for a simple buffer example:
     * <pre>
     * protected void addParameters(Map<String, Parameter<?>> parameters) {
     *    parameters.put(BUFFER.key, BUFFER);
     * }
     * </pre>
     * </p>
     * 
     */
    protected abstract void addParameters( Map<String,Parameter<?>> parameters );
}
