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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import org.geotools.geometry.jts.CompoundCurvedGeometry;
import org.geotools.geometry.jts.CurvedGeometry;
import org.geotools.geometry.jts.CurvedGeometryFactory;
import org.geotools.geometry.jts.CurvedRing;
import org.geotools.gml3.ArcParameters;
import org.geotools.gml3.GML;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

/**
 * @author Erik van de Pol. B3Partners BV.
 * @author Andrea Aime
 */
public class RingTypeBinding extends AbstractComplexBinding implements Comparable {
    protected GeometryFactory gf;

    protected ArcParameters arcParameters;

    public RingTypeBinding(GeometryFactory gf) {
        this.gf = gf;
    }

    /** @generated */
    public QName getTarget() {
        return GML.RingType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return CurvedRing.class;
    }

    @Override
    public int getExecutionMode() {
        return OVERRIDE;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        List members = node.getChildValues("curveMember");

        if (members.isEmpty()) {
            return null;
        } else if (members.size() == 1) {
            Object o = members.get(0);
            if (o.getClass() == LineString.class) {
                LineString ls = (LineString) o;
                return this.gf.createLinearRing(ls.getCoordinates());
            } else {
                return members.get(0);
            }
        } else {
            LineString curved = null;
            List<LineString> components = new ArrayList<>();
            for (Iterator it = members.iterator(); it.hasNext(); ) {
                LineString ls = (LineString) it.next();
                if (ls instanceof CurvedGeometry<?>) {
                    curved = ls;
                }
                components.add(ls);
            }
            CurvedGeometryFactory factory =
                    GML3ParsingUtils.getCurvedGeometryFactory(
                            arcParameters,
                            gf,
                            curved != null ? curved.getCoordinateSequence() : null);
            return factory.createCurvedGeometry(components);
        }
    }

    public Object getProperty(Object object, QName name) throws Exception {
        // System.out.println(name.getLocalPart());
        if ("curveMember".equals(name.getLocalPart())) {
            if (object instanceof CompoundCurvedGeometry<?>) {
                CompoundCurvedGeometry<?> curve = (CompoundCurvedGeometry<?>) object;
                List<LineString> components = curve.getComponents();
                return components;
            } else {
                return object;
            }
        }

        return null;
    }

    public void setArcParameters(ArcParameters arcParameters) {
        this.arcParameters = arcParameters;
    }

    public int compareTo(Object o) {
        if (o instanceof LinearRingTypeBinding) {
            return -1;
        } else {
            return 0;
        }
    }
}
