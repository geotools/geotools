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
import com.vividsolutions.jts.geom.LinearRing;


/**
 * Binding object for the type http://www.opengis.net/gml:LinearRingType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="LinearRingType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;A LinearRing is defined by four or more coordinate tuples, with linear interpolation between them; the first and last coordinates must be coincident.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="gml:AbstractRingType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;choice&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;GML supports two different ways to specify the control points of a linear ring.
 *  1. A sequence of "pos" (DirectPositionType) or "pointProperty" (PointPropertyType) elements. "pos" elements are control points that are only part of this ring, "pointProperty" elements contain a point that may be referenced from other geometry elements or reference another point defined outside of this ring (reuse of existing points).
 *  2. The "posList" element allows for a compact way to specifiy the coordinates of the control points, if all control points are in the same coordinate reference systems and belong to this ring only. The number of direct positions in the list must be at least four.&lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                      &lt;choice maxOccurs="unbounded" minOccurs="4"&gt;
 *                          &lt;element ref="gml:pos"/&gt;
 *                          &lt;element ref="gml:pointProperty"/&gt;
 *                          &lt;element ref="gml:pointRep"&gt;
 *                              &lt;annotation&gt;
 *                                  &lt;documentation&gt;Deprecated with GML version 3.1.0. Use "pointProperty" instead. Included for backwards compatibility with GML 3.0.0.&lt;/documentation&gt;
 *                              &lt;/annotation&gt;
 *                          &lt;/element&gt;
 *                      &lt;/choice&gt;
 *                      &lt;element ref="gml:posList"/&gt;
 *                      &lt;element ref="gml:coordinates"&gt;
 *                          &lt;annotation&gt;
 *                              &lt;documentation&gt;Deprecated with GML version 3.1.0. Use "posList" instead.&lt;/documentation&gt;
 *                          &lt;/annotation&gt;
 *                      &lt;/element&gt;
 *                      &lt;element maxOccurs="unbounded" minOccurs="4" ref="gml:coord"&gt;
 *                          &lt;annotation&gt;
 *                              &lt;documentation&gt;Deprecated with GML version 3.0 and included for backwards compatibility with GML 2. Use "pos" elements instead.&lt;/documentation&gt;
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
public class LinearRingTypeBinding extends AbstractComplexBinding {
    GeometryFactory gFactory;
    CoordinateSequenceFactory csFactory;

    public LinearRingTypeBinding(GeometryFactory gFactory, CoordinateSequenceFactory csFactory) {
        this.gFactory = gFactory;
        this.csFactory = csFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.LinearRingType;
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
        return GML3ParsingUtils.linearRing(node, gFactory, csFactory);
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        if ("posList".equals(name.getLocalPart())) {
            return GML3EncodingUtils.positions((LineString) object);
        }

        return null;
    }
}
