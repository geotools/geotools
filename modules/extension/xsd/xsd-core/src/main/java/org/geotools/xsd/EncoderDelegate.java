/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xsd;

import org.xml.sax.ContentHandler;

/**
 * An interface used to signal to the {@link Encoder} that it should delegate to the object itself
 * to encode, rather than work the object through the typical encoding routine.
 *
 * @author Justin Deoliveira, OpenGEO
 */
public interface EncoderDelegate {

    /** Encodes content to an output stream. */
    void encode(ContentHandler output) throws Exception;
}
