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
 * Binding object for the element http://www.opengis.net/se:BaseSymbolizer.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:element name="BaseSymbolizer" type="se:BaseSymbolizerType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *          A "BaseSymbolizer" defines the default properties of a Symbolizer to
 *          be those of an external Symbolizer, which will frequently be inside
 *          of an OGC Symbolizer repository.  The Symbolizer properties given
 *          in-line override the base-Symbolizer properties.
 *        &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *  &lt;/xsd:element&gt; 
 * 	
 *   </code>
 * </pre>
 * 
 * <pre>
 *       <code>
 *  &lt;xsd:complexType name="BaseSymbolizerType"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element ref="se:OnlineResource"/&gt;
 *      &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt; 
 *              
 *        </code>
 * </pre>
 * 
 * </p>
 * 
 * @generated
 */
public class BaseSymbolizerBinding extends AbstractComplexBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return SE.BaseSymbolizer;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        // TODO: implement and remove call to super
        return super.parse(instance, node, value);
    }

}