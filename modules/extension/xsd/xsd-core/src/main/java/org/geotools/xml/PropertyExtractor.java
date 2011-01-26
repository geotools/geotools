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
package org.geotools.xml;

import org.eclipse.xsd.XSDElementDeclaration;
import java.util.List;


/**
 * Factory used by the encoder to obtain child values from objects being encoded.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public interface PropertyExtractor {
    /**
     * Determines if this extractor can handle objects of the given type.
     *
     * @param object The object being encoded.
     *
     * @return <code>true</code> if the extractor can handle the object,
     *         otherwise <code>false<code>.
     */
    boolean canHandle(Object object);

    /**
     * Exracts the properties from the object being encoded.
     * <p>
     * This method should return a set of tuples made up of
     * ({@link org.eclipse.xsd.XSDParticle},Object).
     * </p>
     *
     * @param object The object being encoded.
     * @param element The element declaration corresponding to the object being encoded.
     *
     * @return A set of element, object tuples.
     */
    List /*Object[2]*/ properties(Object object, XSDElementDeclaration element);
}
