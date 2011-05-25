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
package org.geotools.feature;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

/**
 * Utility class used by feature model implementation.
 * <p>
 * This class is only for use internally and is not meant to be called by 
 * client code.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 * @source $URL$
 */
public class FeatureImplUtils {

    /**
     * Wraps a collection in an umodifiable collection based on the interface
     * the collection implements.
     * <p>
     * A list will result in an umodifiable list, a set in an unmodifiable set, 
     * etc..
     * </p>
     * 
     */
    public static Collection unmodifiable( Collection original ) {
        
        if ( original instanceof Set ) {
            if ( original instanceof SortedSet ) {
                return Collections.unmodifiableSortedSet((SortedSet) original);
            }
            
            return Collections.unmodifiableSet((Set)original);
        }
        else if ( original instanceof List ) {
            return Collections.unmodifiableList((List)original);
        }
        
        return Collections.unmodifiableCollection(original);
    }
}
