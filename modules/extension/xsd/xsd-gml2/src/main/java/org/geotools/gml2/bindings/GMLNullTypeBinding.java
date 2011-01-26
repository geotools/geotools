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

import javax.xml.namespace.QName;

import org.geotools.gml2.GML;
import org.geotools.xml.InstanceComponent;
import org.geotools.xml.SimpleBinding;

import com.vividsolutions.jts.geom.Envelope;


/**
 * Binding object for the type http://www.opengis.net/gml:NullType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;simpleType name="NullType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;         If a bounding shape is not provided for
 *              a feature collection,          explain why. Allowable values
 *              are:         innapplicable - the features do not have
 *              geometry         unknown - the boundingBox cannot be
 *              computed         unavailable - there may be a boundingBox
 *              but it is not divulged         missing - there are no
 *              features       &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;restriction base="string"&gt;
 *          &lt;enumeration value="inapplicable"/&gt;
 *          &lt;enumeration value="unknown"/&gt;
 *          &lt;enumeration value="unavailable"/&gt;
 *          &lt;enumeration value="missing"/&gt;
 *      &lt;/restriction&gt;
 *  &lt;/simpleType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class GMLNullTypeBinding implements SimpleBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return GML.NULLTYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public int getExecutionMode() {
        return OVERRIDE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Envelope.class;
    }

    /**
     * <!-- begin-user-doc -->
     * Returns an object of type @link com.vividsolutions.jts.geom.Envelope. In
     * the event that a <b>null</b> element is given, a null Envelope is
     * returned by calling @link com.vividsolutions.jts.geom.Envelope#setToNull().
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value)
        throws Exception {
        //dont do anything special, here just return the string
        return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public String encode(Object object, String value) {
        return "unknown";
    }
}
