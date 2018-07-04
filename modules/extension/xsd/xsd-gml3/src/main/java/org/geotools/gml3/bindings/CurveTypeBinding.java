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
package org.geotools.gml3.bindings;

import java.util.Arrays;
import java.util.List;
import javax.xml.namespace.QName;
import org.geotools.geometry.jts.CompoundCurvedGeometry;
import org.geotools.geometry.jts.CurvedGeometry;
import org.geotools.geometry.jts.CurvedGeometryFactory;
import org.geotools.gml3.ArcParameters;
import org.geotools.gml3.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

/**
 * Binding object for the type http://www.opengis.net/gml:CurveType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;complexType name="CurveType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Curve is a 1-dimensional primitive. Curves are continuous, connected, and have a measurable length in terms of the coordinate system.
 *                                  A curve is composed of one or more curve segments. Each curve segment within a curve may be defined using a different interpolation method. The curve segments are connected to one another, with the end point of each segment except the last being the start point of the next segment in the segment list.
 *                                  The orientation of the curve is positive.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="gml:AbstractCurveType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element ref="gml:segments"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;This element encapsulates the segments of the curve.&lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *              &lt;/sequence&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 * @source $URL$
 */
public class CurveTypeBinding extends AbstractComplexBinding implements Comparable {
    protected GeometryFactory gf;

    ArcParameters arcParameters;

    public CurveTypeBinding(GeometryFactory gf) {
        this.gf = gf;
    }

    public void setArcParameters(ArcParameters arcParameters) {
        this.arcParameters = arcParameters;
    }

    /** @generated */
    public QName getTarget() {
        return GML.CurveType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return CurvedGeometry.class;
    }

    public int getExecutionMode() {
        return BEFORE;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        LineString[] segments = (LineString[]) node.getChildValue("segments");

        if (segments.length == 0) {
            return null;
        } else if (segments.length == 1) {
            return segments[0];
        } else {
            LineString curved = null;
            for (LineString ls : segments) {
                if (ls instanceof CurvedGeometry<?>) {
                    curved = ls;
                }
            }
            CurvedGeometryFactory factory =
                    GML3ParsingUtils.getCurvedGeometryFactory(
                            arcParameters,
                            gf,
                            curved != null ? curved.getCoordinateSequence() : null);
            return factory.createCurvedGeometry(Arrays.asList(segments));
        }
    }

    public Object getProperty(Object object, QName name) throws Exception {
        if ("segments".equals(name.getLocalPart())) {
            if (object instanceof CompoundCurvedGeometry<?>) {
                CompoundCurvedGeometry<?> curve = (CompoundCurvedGeometry<?>) object;
                List<LineString> components = curve.getComponents();
                return components;
            } else {
                return object;
            }
        } else {
            super.getProperty(object, name);
        }

        return null;
    }

    public int compareTo(Object o) {
        if (o instanceof LineStringTypeBinding) {
            return -1;
        } else {
            return 0;
        }
    }
}
