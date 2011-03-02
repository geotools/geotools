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

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.geotools.gml3.GML;
import org.geotools.gml3.bindings.SurfaceTypeBinding;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;


public class SurfaceArrayPropertyTypeBinding 
    extends org.geotools.gml3.bindings.SurfaceArrayPropertyTypeBinding 
    implements Comparable {
    
    protected GeometryFactory gf;

    public SurfaceArrayPropertyTypeBinding(GeometryFactory gf) {
        this.gf = gf;
    }

    
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return MultiPolygon.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        
        List<Polygon> polygons = new ArrayList<Polygon>();
        
        // This property element contains a list of surfaces.
        // The order of the elements is significant and shall be preserved when processing the array.
        for (Node child : (List<Node>)node.getChildren()) {
            Object nodeValue = child.getValue();
            if (nodeValue instanceof MultiPolygon) { // Surface
                MultiPolygon surface = (MultiPolygon)nodeValue;
                for (int i = 0; i < surface.getNumGeometries(); i++) {
                    Polygon polygon = (Polygon)surface.getGeometryN(i);
                    polygons.add(polygon);
                }
            } else if (nodeValue instanceof Polygon) { // Polygon
                Polygon polygon = (Polygon)nodeValue;
                polygons.add(polygon);
            }
        }

        return gf.createMultiPolygon((Polygon[])polygons.toArray(new Polygon[polygons.size()]));
    }
    
    public int compareTo(Object o) {
        if (o instanceof SurfaceTypeBinding) {
            return 1;
        } else {
            return 0;
        }
    }
}
