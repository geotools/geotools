/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.efeature;

import org.eclipse.emf.ecore.EObject;
import org.opengis.feature.simple.SimpleFeature;

/**
 * This interface added EMF {@link EObject container} lookup to {@link SimpleFeature}.
 *  
 * @author kengu - 28. mai 2011 
 *
 */
public interface ESimpleFeature extends SimpleFeature {

    /**
     * Get EMF {@link EObject} containing this feature.
     * @return an {@link EObject} instance
     */
    public EObject eObject();
    
    /**
     * Get {@link EFeature} containing this feature.
     * @return an {@link EFeature} instance
     */
    public EFeature eFeature();
        
}
