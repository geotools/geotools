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

import org.geotools.geometry.DirectPosition1D;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.gml3.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.vividsolutions.jts.geom.GeometryFactory;


/**
 * Binding object for the type http://www.opengis.net/gml:DirectPositionType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="DirectPositionType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;DirectPosition instances hold the coordinates for a position within some coordinate reference system (CRS). Since
 *                          DirectPositions, as data types, will often be included in larger objects (such as geometry elements) that have references to CRS, the
 *                          "srsName" attribute will in general be missing, if this particular DirectPosition is included in a larger element with such a reference to a
 *                          CRS. In this case, the CRS is implicitly assumed to take on the value of the containing object's CRS.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;simpleContent&gt;
 *          &lt;extension base="gml:doubleList"&gt;
 *              &lt;attributeGroup ref="gml:SRSReferenceGroup"/&gt;
 *          &lt;/extension&gt;
 *      &lt;/simpleContent&gt;
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
public class DirectPositionTypeBinding extends AbstractComplexBinding {
    GeometryFactory factory;

    public DirectPositionTypeBinding(GeometryFactory factory) {
        this.factory = factory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.DirectPositionType;
    }

    public int getExecutionMode() {
        return AFTER;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return DirectPosition2D.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        CoordinateReferenceSystem crs = GML3ParsingUtils.crs(node);

        //double[] position = (double[]) value;
        Double[] position = (Double[]) value;
        DirectPosition dp = null;

        if (position.length < 2) {
            dp = (crs != null) ? new DirectPosition1D(crs) : new DirectPosition1D();
            dp.setOrdinate(0, position[0].doubleValue());
        } else {
            dp = (crs != null) ? new DirectPosition2D(crs) : new DirectPosition2D();
            dp.setOrdinate(0, position[0].doubleValue());
            dp.setOrdinate(1, position[1].doubleValue());
        }

        return dp;
    }

    public Element encode(Object object, Document document, Element value)
        throws Exception {
        DirectPosition dp = (DirectPosition) object;

        double[] coordinates = dp.getCoordinates();

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < coordinates.length; i++) {
            sb.append(String.valueOf(coordinates[i]));

            if (i != (coordinates.length - 1)) {
                sb.append(" ");
            }
        }

        //Element pos = document.createElementNS(GML.pos.getNamespaceURI(), GML.pos.getLocalPart());
        value.appendChild(document.createTextNode(sb.toString()));

        return value;
    }
}
