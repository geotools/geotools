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
package org.geotools.gml3.bindings.ext;

import javax.xml.namespace.QName;

import org.geotools.gml3.GML;
import org.geotools.gml3.bindings.PolygonTypeBinding;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

public class PolygonPatchTypeBinding extends org.geotools.gml3.bindings.PolygonPatchTypeBinding
    implements Comparable {

    public PolygonPatchTypeBinding(GeometryFactory gf) {
        super(gf);
    }
    
    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        return new PolygonTypeBinding( gf ).getProperty(object, name);
    }

    public int compareTo(Object o) {
        if (o instanceof PolygonTypeBinding) {
            return 1;
        } else {
            return 0;
        }
    }
}
