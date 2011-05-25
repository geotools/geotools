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

import java.util.ArrayList;

import javax.xml.namespace.QName;

import org.geotools.gml3.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;


/**
 * Binding object for the type http://www.opengis.net/gml:MultiPointType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="MultiPointType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;A MultiPoint is defined by one or more Points, referenced through pointMember elements.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="gml:AbstractGeometricAggregateType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;annotation&gt;
 *                      &lt;documentation&gt;The members of the geometric aggregate can be specified either using the "standard" property or the array property style. It is also valid to use both the "standard" and the array property style in the same collection.
 *  NOTE: Array properties cannot reference remote geometry elements.&lt;/documentation&gt;
 *                  &lt;/annotation&gt;
 *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:pointMember"/&gt;
 *                  &lt;element minOccurs="0" ref="gml:pointMembers"/&gt;
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
 *
 * @source $URL$
 */
public class MultiPointTypeBinding extends AbstractComplexBinding {
    GeometryFactory gFactory;

    public MultiPointTypeBinding(GeometryFactory gFactory) {
        this.gFactory = gFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.MultiPointType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return MultiPoint.class;
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
        ArrayList points = new ArrayList();

        if (node.hasChild(Point.class)) {
            points.addAll(node.getChildValues(Point.class));
        }

        if (node.hasChild(Point[].class)) {
            Point[] p = (Point[]) node.getChildValue(Point[].class);

            for (int i = 0; i < p.length; i++)
                points.add(p[i]);
        }

        return gFactory.createMultiPoint((Point[]) points.toArray(new Point[points.size()]));
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        if ("pointMember".equals(name.getLocalPart())) {
            MultiPoint multiPoint = (MultiPoint) object;
            Point[] members = new Point[multiPoint.getNumGeometries()];

            for (int i = 0; i < members.length; i++) {
                members[i] = (Point) multiPoint.getGeometryN(i);
            }

            return members;
        }

        return null;
    }
}
