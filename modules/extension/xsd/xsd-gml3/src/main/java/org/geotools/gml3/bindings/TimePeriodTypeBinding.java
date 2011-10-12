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
import org.geotools.temporal.object.DefaultPeriod;
import org.geotools.xml.*;
import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;

import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/gml:TimePeriodType.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;complexType name="TimePeriodType"&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="gml:AbstractTimeGeometricPrimitiveType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;choice&gt;
 *                      &lt;element name="beginPosition" type="gml:TimePositionType"/&gt;
 *                      &lt;element name="begin" type="gml:TimeInstantPropertyType"/&gt;
 *                  &lt;/choice&gt;
 *                  &lt;choice&gt;
 *                      &lt;element name="endPosition" type="gml:TimePositionType"/&gt;
 *                      &lt;element name="end" type="gml:TimeInstantPropertyType"/&gt;
 *                  &lt;/choice&gt;
 *                  &lt;group minOccurs="0" ref="gml:timeLength"/&gt;
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
public class TimePeriodTypeBinding extends AbstractComplexBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.TimePeriodType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return Period.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        Instant begin = null, end = null;
        if (node.hasChild("begin")) {
            begin = (Instant) node.getChildValue("begin");
        }
        else {
            begin = (Instant) node.getChildValue("beginPosition");
        }
        
        if (node.hasChild("end")) {
            end = (Instant) node.getChildValue("end");
        }
        else {
            end = (Instant) node.getChildValue("endPosition");
        }
        
        if (begin == null || end == null) {
            throw new IllegalArgumentException("Time period begin/end not specified");
        }
        
        return new DefaultPeriod(begin, end);
    }

}