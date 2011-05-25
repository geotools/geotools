/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.se.v1_1.bindings;

import org.geotools.se.v1_1.SE;
import org.geotools.xml.*;

import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/se:directionType.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:simpleType name="directionType"&gt;
 *      &lt;xsd:restriction base="xsd:token"&gt;
 *          &lt;xsd:enumeration value="toUpper"/&gt;
 *          &lt;xsd:enumeration value="toLower"/&gt;
 *      &lt;/xsd:restriction&gt;
 *  &lt;/xsd:simpleType&gt; 
 * 	
 *   </code>
 * </pre>
 * 
 * </p>
 * 
 * @generated
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-sld/src/main/java/org/geotools/se/v1_1/bindings/DirectionTypeBinding.java $
 */
public class DirectionTypeBinding extends AbstractSimpleBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return SE.directionType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return String.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value) throws Exception {
        String val = (String) value;
        if (!"toUpper".equalsIgnoreCase(val) && !"toLower".equalsIgnoreCase(val)) {
            throw new IllegalStateException(val + " not supported, must be one of 'toUpper', 'toLower'");
        }
        return val;
    }

}
