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
import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;

import org.geotools.gml3.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

public class MultiCurveTypeBinding extends org.geotools.gml3.bindings.MultiCurveTypeBinding
    implements Comparable {
    
    public MultiCurveTypeBinding(GeometryFactory gf) {
        super(gf);
    }

    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
      //&lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:curveMember"/&gt;
        List<MultiLineString> curveMemberList = node.getChildValues("curveMember");
        //&lt;element minOccurs="0" ref="gml:curveMembers"/&gt;
        MultiLineString curveMembers = (MultiLineString)node.getChildValue("curveMembers");

        List<LineString> lineStrings = new ArrayList<LineString>();

        if (curveMemberList != null) {
            for (MultiLineString curveMember : curveMemberList) {
                for (int i = 0; i < curveMember.getNumGeometries(); i++) {
                    LineString lineString = (LineString)curveMember.getGeometryN(i);
                    lineStrings.add(lineString);
                }
            }
        }

        if (curveMembers != null) {
            for (int i = 0; i < curveMembers.getNumGeometries(); i++) {
                LineString lineString = (LineString)curveMembers.getGeometryN(i);
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
