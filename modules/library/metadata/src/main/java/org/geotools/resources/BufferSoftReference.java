/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.resources;

import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;

import org.geotools.resources.NIOUtilities;
import org.geotools.util.WeakCollectionCleaner;

/**
 * A soft reference that will clear the contained byte buffer before getting garbage collected 
 * @author Andrea Aime - OpenGeo
 *
 */
class BufferSoftReference extends SoftReference<ByteBuffer> {

    public BufferSoftReference(ByteBuffer referent) {
        super(referent, WeakCollectionCleaner.DEFAULT.getReferenceQueue());
    }

    @Override
    public void clear() {
        ByteBuffer buffer = get();
        NIOUtilities.clean(buffer);
        super.clear();
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof BufferSoftReference)) {
            return false;
        }
        
        ByteBuffer buffer = get();
        if(buffer == null) {
            return false;
        } else {
            ByteBuffer otherBuffer = ((BufferSoftReference) other).get();
            if(otherBuffer == null) {
                return false;
            }
            return otherBuffer == buffer;
        }
    }
}
