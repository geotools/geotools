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
import org.geotools.gml3.XSDIdRegistry;
import org.geotools.gml3.bindings.GML3EncodingUtils;
import org.geotools.gml3.bindings.SurfaceTypeBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

public class SurfacePropertyTypeBinding extends org.geotools.gml3.bindings.SurfacePropertyTypeBinding 
    implements Comparable {

    GeometryFactory gf;
    
    public SurfacePropertyTypeBinding(GML3EncodingUtils encodingUtils, XSDIdRegistry idRegistry, GeometryFactory gf) {
        super(encodingUtils, idRegistry);
        this.gf = gf;
    }

    public Class<? extends Geometry> getGeometryType() {
        return MultiPolygon.class;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        Polygon polygon = (Polygon)node.getChildValue(Polygon.class);
        MultiPolygon surface = (MultiPolygon)node.getChildValue(MultiPolygon.class);

        if (polygon != null) {
            return gf.createMultiPolygon(new Polygon[] {polygon});
        } else {
            return surface;
        }
    }

    @Override
    public Object getProperty(Object object, QName name)
        throws Exception {
        if ("_Surface".equals(name.getLocalPart()) || "AbstractSurface".equals(name.getLocalPart())) {
            MultiPolygon multiPolygon = (MultiPolygon) object;
            // this MultiPolygon consists of a single Polygon wrapped in a MultiPolygon:
            return multiPolygon.getGeometryN(0);
        }

        return super.getProperty(object, name);
    }

    public int compareTo(Object o) {
        if (o instanceof SurfaceTypeBinding) {
            return 1;
        } else {
            return 0;
        }
    }
}
