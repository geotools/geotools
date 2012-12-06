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
package org.geotools.kml.bindings;

import javax.xml.namespace.QName;
import org.geotools.kml.KML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;


/**
 * Binding object for the type http://earth.google.com/kml/2.1:RegionType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType final="#all" name="RegionType"&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="kml:ObjectType"&gt;
 *              &lt;all&gt;
 *                  &lt;element ref="kml:LatLonAltBox"/&gt;
 *                  &lt;element minOccurs="0" ref="kml:Lod"/&gt;
 *              &lt;/all&gt;
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
 *
 * @source $URL$
 */
public class RegionTypeBinding extends AbstractComplexBinding {

    private final GeometryFactory geometryFactory;

    /**
     * @generated
     */
    public QName getTarget() {
        return KML.RegionType;
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

    public RegionTypeBinding(GeometryFactory geometryFactory) {
        this.geometryFactory = geometryFactory;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        Object latLonChildElement = node.getChildValue("LatLonAltBox");
        if (latLonChildElement == null) {
            return null;
        }
        Envelope e = (Envelope) latLonChildElement;
        Coordinate bottomLeft = new Coordinate(e.getMinX(), e.getMinY());
        Coordinate topLeft = new Coordinate(e.getMinX(), e.getMaxY());
        Coordinate topRight = new Coordinate(e.getMaxX(), e.getMaxY());
        Coordinate bottomRight = new Coordinate(e.getMaxX(), e.getMinY());
        Coordinate[] cs = new Coordinate[] { bottomLeft, topLeft, topRight, bottomRight, bottomLeft };
        LinearRing linearRing = geometryFactory.createLinearRing(cs);
        return linearRing;
    }
}
