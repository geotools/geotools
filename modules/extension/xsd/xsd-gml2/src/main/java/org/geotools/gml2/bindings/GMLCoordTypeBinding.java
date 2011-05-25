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
package org.geotools.gml2.bindings;

import java.math.BigDecimal;

import javax.xml.namespace.QName;

import org.geotools.gml2.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;


/**
 * Binding object for the type http://www.opengis.net/gml:CoordType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="CoordType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;         Represents a coordinate tuple in one,
 *              two, or three dimensions.       &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element name="X" type="decimal"/&gt;
 *          &lt;element name="Y" type="decimal" minOccurs="0"/&gt;
 *          &lt;element name="Z" type="decimal" minOccurs="0"/&gt;
 *      &lt;/sequence&gt;
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
public class GMLCoordTypeBinding extends AbstractComplexBinding {
    CoordinateSequenceFactory csFactory;

    public GMLCoordTypeBinding(CoordinateSequenceFactory csFactory) {
        this.csFactory = csFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.CoordType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Coordinate.class;
    }

    /**
     * <!-- begin-user-doc -->
     * Returns a coordinate sequence with a single coordinate in it.
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        int dimension = 1;
        double x;
        double y;
        double z;
        x = y = z = Double.NaN;

        x = ((BigDecimal) node.getChild("X").getValue()).doubleValue();

        if (!node.getChildren("Y").isEmpty()) {
            dimension++;
            y = ((BigDecimal) node.getChild("Y").getValue()).doubleValue();
        }

        if (!node.getChildren("Z").isEmpty()) {
            dimension++;
            z = ((BigDecimal) node.getChild("Z").getValue()).doubleValue();
        }

        return new Coordinate(x, y, z);
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        Coordinate c = (Coordinate) object;

        if ("X".equals(name.getLocalPart())) {
            return new Double(c.x);
        }

        if ("Y".equals(name.getLocalPart())) {
            return new Double(c.y);
        }

        if ("Z".equals(name.getLocalPart()) && !new Double(c.z).isNaN()) {
            return new Double(c.z);
        }

        return null;
    }
}
