/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;

/**
 * Interface for wrapping feature writers which delegate to another feature writer.
 * 
 * @author Justin Deoliveira, OpenGEO
 * @since 2.5
 *
 *
 * @source $URL$
 */
public interface DelegatingFeatureWriter<T extends FeatureType,F extends Feature> extends FeatureWriter<T, F> {

    /**
     * @return The delegate writer.
     */
    FeatureWriter<T,F> getDelegate();
}
