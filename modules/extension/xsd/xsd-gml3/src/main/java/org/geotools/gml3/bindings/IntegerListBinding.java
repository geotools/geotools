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
import org.geotools.xml.AbstractSimpleBinding;
import org.geotools.xml.InstanceComponent;


/**
 * Binding object for the type http://www.opengis.net/gml:integerList.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;simpleType name="integerList"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;XML List based on XML Schema integer type.  An element of this type contains a space-separated list of integer values&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;list itemType="integer"/&gt;
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
public class IntegerListBinding extends AbstractSimpleBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return GML.integerList;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return int[].class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value)
        throws Exception {
        String[] values = ((String) value).split(" +");
        int[] integers = new int[values.length];

        for (int i = 0; i < values.length; i++) {
            integers[i] = Integer.parseInt(values[i]);
        }

        return integers;
    }
}
