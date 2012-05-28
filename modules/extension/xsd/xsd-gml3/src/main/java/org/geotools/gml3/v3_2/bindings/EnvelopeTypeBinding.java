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
package org.geotools.gml3.v3_2.bindings;

import javax.xml.namespace.QName;

import org.geotools.gml3.v3_2.GML;
import org.geotools.xml.Configuration;

public class EnvelopeTypeBinding extends org.geotools.gml3.bindings.EnvelopeTypeBinding {

    public EnvelopeTypeBinding(Configuration config) {
        super(config);
    }

    @Override
    public QName getTarget() {
        return GML.EnvelopeType;
    }
}
