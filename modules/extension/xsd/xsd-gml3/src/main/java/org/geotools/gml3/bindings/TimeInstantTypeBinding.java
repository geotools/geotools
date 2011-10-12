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

import org.geotools.gml3.GML;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.xml.*;
import org.opengis.temporal.Instant;
import org.opengis.temporal.Position;

import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/gml:TimeInstantType.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;complexType name="TimeInstantType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Omit back-pointers begunBy, endedBy.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="gml:AbstractTimeGeometricPrimitiveType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element ref="gml:timePosition"/&gt;
 *              &lt;/sequence&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt; 
 * 	
 *   </code>
 * </pre>
 * 
 * </p>
 * 
 * @generated
 */
public class TimeInstantTypeBinding extends AbstractComplexBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.TimeInstantType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return Instant.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        Position pos = (Position) node.getChildValue(Position.class);
        return new DefaultInstant(pos);
    }

}