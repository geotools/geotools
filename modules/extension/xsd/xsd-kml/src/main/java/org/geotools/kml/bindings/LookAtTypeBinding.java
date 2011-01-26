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
import com.vividsolutions.jts.geom.Coordinate;
import org.geotools.kml.KML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;


/**
 * Binding object for the type http://earth.google.com/kml/2.1:LookAtType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType final="#all" name="LookAtType"&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="kml:ObjectType"&gt;
 *              &lt;all&gt;
 *                  &lt;element default="0" minOccurs="0" name="longitude" type="kml:angle180"/&gt;
 *                  &lt;element default="0" minOccurs="0" name="latitude" type="kml:angle90"/&gt;
 *                  &lt;element default="0" minOccurs="0" name="altitude" type="double"/&gt;
 *                  &lt;element minOccurs="0" name="range" type="double"/&gt;
 *                  &lt;element default="0" minOccurs="0" name="tilt" type="kml:anglepos90"/&gt;
 *                  &lt;element default="0" minOccurs="0" name="heading" type="kml:angle360"/&gt;
 *                  &lt;element default="clampToGround" minOccurs="0"
 *                      name="altitudeMode" type="kml:altitudeModeEnum"/&gt;
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
 * @source $URL$
 */
public class LookAtTypeBinding extends AbstractComplexBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return KML.LookAtType;
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
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        Coordinate c = new Coordinate();

        //&lt;element default="0" minOccurs="0" name="longitude" type="kml:angle180"/&gt;
        c.y = ((Double) node.getChildValue("longitude", Double.valueOf(0d))).doubleValue();

        //&lt;element default="0" minOccurs="0" name="latitude" type="kml:angle90"/&gt;
        c.x = ((Double) node.getChildValue("latitude", Double.valueOf(0d))).doubleValue();

        //&lt;element default="0" minOccurs="0" name="altitude" type="double"/&gt;
        c.z = ((Double) node.getChildValue("altitude", Double.valueOf(0d))).doubleValue();

        return c;
    }
}
