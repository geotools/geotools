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

import org.opengis.feature.type.AttributeDescriptor;

/**
 * Provides support for creating AttributeReaders.
 * @since 2.0
 * @source $URL$
 * @version $Id$
 * @author  Ian Schneider
 */
public abstract class AbstractAttributeIO {
    
    protected AttributeDescriptor[] metaData;
    
    protected AbstractAttributeIO(AttributeDescriptor[] metaData) {
        this.metaData = metaData;
    }
    
    /**
     * Copy the meta-data from this reader, but don't use the reader!!
     */
    protected AbstractAttributeIO(AttributeReader defs) {
        this(copy(defs));
    }
    
    public static AttributeDescriptor[] copy(AttributeReader defs) {
        AttributeDescriptor[] d = new AttributeDescriptor[defs.getAttributeCount()];
        for (int i = 0, ii = d.length; i < ii; i++) {
            d[i] = defs.getAttributeType(i);
        }
        return d;
    }
    
    public final int getAttributeCount() {
        return metaData.length;
    }
    
    public final AttributeDescriptor getAttributeType(int position) {
        return metaData[position];
    }
    
}
