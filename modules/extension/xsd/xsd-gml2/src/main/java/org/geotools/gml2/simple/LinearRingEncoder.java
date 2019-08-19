/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml2.simple;

import org.geotools.gml2.GML;
import org.geotools.xsd.Encoder;

/**
 * Encodes a GML2 linear ring
 *
 * @author Justin Deoliveira, OpenGeo
 * @author Andrea Aime - GeoSolutions
 */
class LinearRingEncoder extends LineStringEncoder {

    static final QualifiedName LINEAR_RING = new QualifiedName(GML.NAMESPACE, "LinearRing", "gml");

    protected LinearRingEncoder(Encoder encoder, String gmlPrefix) {
        super(encoder, LINEAR_RING.derive(gmlPrefix));
    }
}
