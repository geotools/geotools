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
package org.geotools.geometry.jts;

import java.awt.geom.PathIterator;

/**
 * An iterator for empty geometries
 * 
 * @since 2.4
 *
 * @source $URL$
 */
public class EmptyIterator extends AbstractLiteIterator {
    
    public static final EmptyIterator INSTANCE = new EmptyIterator();
    
    public int getWindingRule() {
        return WIND_NON_ZERO;
    }

    public boolean isDone() {
        return true;
    }

    public void next() {
        throw new IllegalStateException();
    }

    public int currentSegment(double[] coords) {
        return 0;
    }
    
    public int currentSegment(float[] coords) {
        return 0;
    }
}
