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
 * Binding object for the type http://www.opengis.net/se:searchDirectionType.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:simpleType name="searchDirectionType"&gt;
 *      &lt;xsd:restriction base="xsd:token"&gt;
 *          &lt;xsd:enumeration value="frontToBack"/&gt;
 *          &lt;xsd:enumeration value="backToFront"/&gt;
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
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-sld/src/main/java/org/geotools/se/v1_1/bindings/SearchDirectionTypeBinding.java $
 */
public class SearchDirectionTypeBinding extends AbstractSimpleBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return SE.searchDirectionType;
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
        
        //&lt;xsd:enumeration value="frontToBack"/&gt;
        //&lt;xsd:enumeration value="backToFront"/&gt;
        if (!"frontToBack".equalsIgnoreCase(val) && !"backToFront".equalsIgnoreCase(val)) {
            throw new IllegalArgumentException(
                val + " not supported, must be one of 'frontToBack', 'backToFront'");
        }

        return val;
    }

}
