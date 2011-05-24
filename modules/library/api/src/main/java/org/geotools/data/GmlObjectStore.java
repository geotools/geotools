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
package org.geotools.data;

import java.io.IOException;

import org.geotools.factory.Hints;
import org.opengis.filter.identity.GmlObjectId;

/**
 * Interface providing lookup operations for gml objects.
 * <p>
 * This interface may be implemented by data stores to provide an additional operation 
 * for looking object a "gml object" directly. A gml object is typically a feature 
 * or a geometry. 
 * </p>
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 * @since 2.5
 *
 *
 * @source $URL$
 */
public interface GmlObjectStore {

    /**
     * Looks up an object by its gml id.
     * <p>
     * This method returns <code>null</code> if no such object exists.
     * </p>
     * @param id The id of the object, must not be <code>null</code>.
     * @param hints Any hints to use when looking up the gml object, this value 
     * may be <code>null</code>. 
     * 
     * @return The gml object, or <code>null</code> if one could not be found
     * matching the specified id.
     * 
     * @throws IOException Any I/O errors that occur.
     */
    Object getGmlObject( GmlObjectId id, Hints hints ) throws IOException;
}
