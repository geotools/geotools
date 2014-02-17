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
package org.geotools.gml3.bindings.ext;

import com.vividsolutions.jts.geom.CoordinateList;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import java.util.List;
import javax.xml.namespace.QName;
import org.geotools.gml3.GML;
import org.geotools.gml3.bindings.LineStringTypeBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

/**
 * Simple type binding for Composite Curve GML elements.
 * @source $URL$
 */
public class CompositeCurveTypeBinding extends LineStringTypeBinding {

    private final GeometryFactory gFactory;
    
    public CompositeCurveTypeBinding(GeometryFactory gFactory, CoordinateSequenceFactory csFactory) {
        super(gFactory, csFactory);
        this.gFactory = gFactory;
    }

    @Override
    public QName getTarget() {
        return GML.CompositeCurveType;
    }

    @Override
    public Class getType() {
        return LineString.class;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value)
            throws Exception {
        CoordinateList clist = extractCurveMemberCoordinates(node);
        LineString lineString = gFactory
                .createLineString(clist.toCoordinateArray());
        return lineString;
    }

    /**
     * Construct a line string from CurveMembers coordinates.
     * 
     * @param node
     * @return
     */
    public static CoordinateList extractCurveMemberCoordinates(Node node) {
        List curveMembers = node.getChildren("curveMember");
        CoordinateList clist = new CoordinateList();
        for (int i = 0; i < curveMembers.size(); i++) {
            List curves = ((Node) curveMembers.get(i))
                    .getChildren(MultiLineString.class);
            for (int j = 0; j < curves.size(); j++) {
                MultiLineString mls = (MultiLineString) ((Node) curves.get(j))
                        .getValue();
                clist.add(mls.getCoordinates(), false);
            }
        }
        return clist;
    }
}
