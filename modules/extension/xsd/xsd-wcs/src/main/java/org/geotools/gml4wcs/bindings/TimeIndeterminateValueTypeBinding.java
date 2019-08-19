/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.gml4wcs.bindings;

import javax.xml.namespace.QName;
import org.geotools.gml4wcs.GML;
import org.geotools.xsd.AbstractSimpleBinding;
import org.geotools.xsd.InstanceComponent;
import org.opengis.temporal.IndeterminateValue;

/**
 * Binding object for the type http://www.opengis.net/gml:TimeIndeterminateValueType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;simpleType name=&quot;TimeIndeterminateValueType&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation xml:lang=&quot;en&quot;&gt;
 *          This enumerated data type specifies values for indeterminate positions.
 *        &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;restriction base=&quot;string&quot;&gt;
 *          &lt;enumeration value=&quot;after&quot;/&gt;
 *          &lt;enumeration value=&quot;before&quot;/&gt;
 *          &lt;enumeration value=&quot;now&quot;/&gt;
 *          &lt;enumeration value=&quot;unknown&quot;/&gt;
 *      &lt;/restriction&gt;
 *  &lt;/simpleType&gt;
 *
 * </code>
 *  </pre>
 *
 * @generated
 */
public class TimeIndeterminateValueTypeBinding extends AbstractSimpleBinding {

    /** @generated */
    public QName getTarget() {
        return GML.TimeIndeterminateValueType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return IndeterminateValue.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value) throws Exception {
        IndeterminateValue timeValue = IndeterminateValue.valueOf((String) value);
        return timeValue;
    }
}
