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
import org.geotools.xml.*;
import org.opengis.temporal.Position;

import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/gml:TimePositionType.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;complexType final="#all" name="TimePositionType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation xml:lang="en"&gt;Direct representation of a temporal position. 
 *        Indeterminate time values are also allowed, as described in ISO 19108. The indeterminatePosition 
 *        attribute can be used alone or it can qualify a specific value for temporal position (e.g. before 
 *        2002-12, after 1019624400). 
 *        For time values that identify position within a calendar, the calendarEraName attribute provides 
 *        the name of the calendar era to which the date is referenced (e.g. the Meiji era of the Japanese calendar).&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;simpleContent&gt;
 *          &lt;extension base="gml:TimePositionUnion"&gt;
 *              &lt;attribute default="#ISO-8601" name="frame" type="anyURI" use="optional"/&gt;
 *              &lt;attribute name="calendarEraName" type="string" use="optional"/&gt;
 *              &lt;attribute name="indeterminatePosition"
 *                  type="gml:TimeIndeterminateValueType" use="optional"/&gt;
 *          &lt;/extension&gt;
 *      &lt;/simpleContent&gt;
 *  &lt;/complexType&gt; 
 * 	
 *   </code>
 * </pre>
 * 
 * </p>
 * 
 * @generated
 */
public class TimePositionTypeBinding extends AbstractComplexBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.TimePositionType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return Position.class;
    }
    
    @Override
    public int getExecutionMode() {
        return AFTER;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        return value;
    }

}