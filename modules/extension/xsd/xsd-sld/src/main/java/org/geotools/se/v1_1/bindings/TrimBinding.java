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
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

import javax.xml.namespace.QName;

/**
 * Binding object for the element http://www.opengis.net/se:Trim.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:element name="Trim" substitutionGroup="se:Function" type="se:TrimType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *  Strips off chars from a string value
 *               &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *  &lt;/xsd:element&gt; 
 * 	
 *   </code>
 * </pre>
 * 
 * <pre>
 *       <code>
 *  &lt;xsd:complexType name="TrimType"&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="se:FunctionType"&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:element ref="se:StringValue"/&gt;
 *              &lt;/xsd:sequence&gt;
 *              &lt;xsd:attribute name="stripOffPosition" type="se:stripOffPositionType"/&gt;
 *              &lt;xsd:attribute name="stripOffChar" type="xsd:string"/&gt;
 *          &lt;/xsd:extension&gt;
 *      &lt;/xsd:complexContent&gt;
 *  &lt;/xsd:complexType&gt; 
 *              
 *        </code>
 * </pre>
 * 
 * </p>
 * 
 * @generated
 */
public class TrimBinding extends AbstractComplexBinding {

    FilterFactory filterFactory;
    
    public TrimBinding(FilterFactory filterFactory) {
        this.filterFactory = filterFactory;
    }
    
    /**
     * @generated
     */
    public QName getTarget() {
        return SE.Trim;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return Function.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        
        //&lt;xsd:element ref="se:StringValue"/&gt;
        Expression str = (Expression) node.getChildValue("StringValue");
        
        //&lt;xsd:attribute name="stripOffPosition" type="se:stripOffPositionType"/&gt;
        Expression pos = filterFactory.literal(node.getAttributeValue("stripOffPosition"));
        
        //&lt;xsd:attribute name="stripOffChar" type="xsd:string"/&gt;
        Expression ch = filterFactory.literal(node.getAttributeValue("stripOffChar"));
        
        return filterFactory.function("strTrim2", str, pos, ch);
    }

}