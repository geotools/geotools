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
package org.geotools.xs.bindings;

import javax.xml.namespace.QName;
import org.geotools.xml.InstanceComponent;
import org.geotools.xml.SimpleBinding;
import org.geotools.xs.XS;


/**
 * Binding object for the type http://www.w3.org/2001/XMLSchema:double.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xs:simpleType name="double" id="double"&gt;
 *      &lt;xs:annotation&gt;
 *          &lt;xs:appinfo&gt;
 *              &lt;hfp:hasFacet name="pattern"/&gt;
 *              &lt;hfp:hasFacet name="enumeration"/&gt;
 *              &lt;hfp:hasFacet name="whiteSpace"/&gt;
 *              &lt;hfp:hasFacet name="maxInclusive"/&gt;
 *              &lt;hfp:hasFacet name="maxExclusive"/&gt;
 *              &lt;hfp:hasFacet name="minInclusive"/&gt;
 *              &lt;hfp:hasFacet name="minExclusive"/&gt;
 *              &lt;hfp:hasProperty name="ordered" value="total"/&gt;
 *              &lt;hfp:hasProperty name="bounded" value="true"/&gt;
 *              &lt;hfp:hasProperty name="cardinality" value="finite"/&gt;
 *              &lt;hfp:hasProperty name="numeric" value="true"/&gt;
 *          &lt;/xs:appinfo&gt;
 *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#double"/&gt;
 *      &lt;/xs:annotation&gt;
 *      &lt;xs:restriction base="xs:anySimpleType"&gt;
 *          &lt;xs:whiteSpace value="collapse" fixed="true" id="double.whiteSpace"/&gt;
 *      &lt;/xs:restriction&gt;
 *  &lt;/xs:simpleType&gt;
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
public class XSDoubleBinding implements SimpleBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return XS.DOUBLE;
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
     * This binding returns objects of type {@link java.lang.Double}.
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Double.class;
    }

    /**
     * <!-- begin-user-doc -->
     * This binding returns objects of type {@link java.lang.Double}. This is an
     * override so value is null.
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value)
        throws Exception {
        if ("INF".equals(value)) {
            return new Double(Double.POSITIVE_INFINITY);
        }

        return new Double((String) value);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public String encode(Object object, String value) {
        Double d = (Double) object;

        if (Double.POSITIVE_INFINITY == d.doubleValue()) {
            return "INF";
        }

        return d.toString();
    }
}
