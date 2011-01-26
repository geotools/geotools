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

import javax.xml.namespace.QName;

import org.geotools.gml3.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;


/**
 * Binding object for the type http://www.opengis.net/gml:LineStringType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="LineStringType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;A LineString is a special curve that consists of a single segment with linear interpolation. It is defined by two or more coordinate
 *                          tuples, with linear interpolation between them. It is backwards compatible with the LineString of GML 2, GM_LineString of ISO 19107 is
 *                          implemented by LineStringSegment.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="gml:AbstractCurveType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;choice&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;GML supports two different ways to specify the control points of a line string. 1. A sequence of "pos"
 *                                                          (DirectPositionType) or "pointProperty" (PointPropertyType) elements. "pos" elements are control points that are only part
 *                                                          of this curve, "pointProperty" elements contain a point that may be referenced from other geometry elements or reference
 *                                                          another point defined outside of this curve (reuse of existing points). 2. The "posList" element allows for a compact way to
 *                                                          specifiy the coordinates of the control points, if all control points are in the same coordinate reference systems and belong
 *                                                          to this curve only. The number of direct positions in the list must be at least two.&lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                      &lt;choice maxOccurs="unbounded" minOccurs="2"&gt;
 *                          &lt;element ref="gml:pos"/&gt;
 *                          &lt;element ref="gml:pointProperty"/&gt;
 *                          &lt;element ref="gml:pointRep"&gt;
 *                              &lt;annotation&gt;
 *                                  &lt;documentation&gt;Deprecated with GML version 3.1.0. Use "pointProperty" instead. Included for backwards compatibility
 *                                                                          with GML 3.0.0.&lt;/documentation&gt;
 *                              &lt;/annotation&gt;
 *                          &lt;/element&gt;
 *                          &lt;element ref="gml:coord"&gt;
 *                              &lt;annotation&gt;
 *                                  &lt;documentation&gt;Deprecated with GML version 3.0. Use "pos" instead. The "coord" element is included for backwards
 *                                                                          compatibility with GML 2.&lt;/documentation&gt;
 *                              &lt;/annotation&gt;
 *                          &lt;/element&gt;
 *                      &lt;/choice&gt;
 *                      &lt;element ref="gml:posList"/&gt;
 *                      &lt;element ref="gml:coordinates"&gt;
 *                          &lt;annotation&gt;
 *                              &lt;documentation&gt;Deprecated with GML version 3.1.0. Use "posList" instead.&lt;/documentation&gt;
 *                          &lt;/annotation&gt;
 *                      &lt;/element&gt;
 *                  &lt;/choice&gt;
 *              &lt;/sequence&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class LineStringTypeBinding extends AbstractComplexBinding {
    GeometryFactory gFactory;
    CoordinateSequenceFactory csFactory;

    public LineStringTypeBinding(GeometryFactory gFactory, CoordinateSequenceFactory csFactory) {
        this.gFactory = gFactory;
        this.csFactory = csFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.LineStringType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return LineString.class;
    }

    public int getExecutionMode() {
        return BEFORE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        return GML3ParsingUtils.lineString(node, gFactory, csFactory);
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        if ("posList".equals(name.getLocalPart())) {
            return GML3EncodingUtils.positions((LineString) object);
        }

        return null;
    }
}
