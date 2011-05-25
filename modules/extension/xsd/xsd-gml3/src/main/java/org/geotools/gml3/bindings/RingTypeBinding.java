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
package org.geotools.gml3.bindings;

import com.vividsolutions.jts.geom.CoordinateSequence;
import javax.xml.namespace.QName;

import org.geotools.gml3.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Erik van de Pol. B3Partners BV.
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-gml3/src/main/java/org/geotools/gml3/bindings/RingTypeBinding.java $
 */
public class RingTypeBinding extends AbstractComplexBinding {
    protected GeometryFactory gf;

    public RingTypeBinding(GeometryFactory gf) {
        this.gf = gf;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.RingType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return LinearRing.class;
    }

    @Override
    public int getExecutionMode() {
        return OVERRIDE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {

        List<MultiLineString> curveMemberList = node.getChildValues(MultiLineString.class);

        List<LineString> curveMembers = new ArrayList<LineString>();

        for (MultiLineString curveMember : curveMemberList) {
            for (int i = 0; i < curveMember.getNumGeometries(); i++) {
                LineString lineString = (LineString)curveMember.getGeometryN(i);
                curveMembers.add(lineString);
            }
        }

        MultiLineString multiLineString = gf.createMultiLineString(
                GeometryFactory.toLineStringArray(curveMembers));

        return gf.createLinearRing(multiLineString.getCoordinates());
    }

    @Override
    public Object getProperty(Object object, QName name)
        throws Exception {

        if ("curveMember".equals(name.getLocalPart())) {
            LinearRing ring = (LinearRing) object;

            return (MultiLineString)ring.getGeometryN(0);
        }

        return null;
    }
    
}
