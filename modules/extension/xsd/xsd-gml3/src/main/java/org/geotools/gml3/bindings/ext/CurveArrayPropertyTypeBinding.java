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

import org.geotools.gml3.bindings.CurvePropertyTypeBinding;
import org.geotools.gml3.bindings.CurveTypeBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

public class CurveArrayPropertyTypeBinding 
    extends org.geotools.gml3.bindings.CurveArrayPropertyTypeBinding 
    implements Comparable {
    
    public CurveArrayPropertyTypeBinding(GeometryFactory gf) {
        super(gf);
    }

    @Override
    public Class getType() {
        return MultiLineString.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        
        List<LineString> lineStrings = new ArrayList<LineString>();

        // This property element contains a list of curves.
        // The order of the elements is significant and shall be preserved when processing the array.
        for (Node child : (List<Node>)node.getChildren()) {
            Object nodeValue = child.getValue();
            if (nodeValue instanceof MultiLineString) {
                MultiLineString curve = (MultiLineString)nodeValue;
                for (int i = 0; i < curve.getNumGeometries(); i++) {
                    LineString lineString = (LineString)curve.getGeometryN(i);
                    lineStrings.add(lineString);
                }
            } else if (nodeValue instanceof LineString) {
                LineString lineString = (LineString)nodeValue;
                lineStrings.add(lineString);
            }
        }
        
        return gf.createMultiLineString(GeometryFactory.toLineStringArray(lineStrings));
    }
    
    public int compareTo(Object o) {
        if (o instanceof CurveTypeBinding || o instanceof CurvePropertyTypeBinding) {
            return 1;
        } else {
            return 0;
        }
    }
}
