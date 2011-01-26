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

import org.geotools.filter.function.math.FilterFunction_abs;
import org.geotools.se.v1_1.SE;
import org.geotools.xml.*;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

import javax.xml.namespace.QName;

/**
 * Binding object for the element http://www.opengis.net/se:StringPosition.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:element name="StringPosition" substitutionGroup="se:Function" type="se:StringPositionType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *  Returns position of first occurence of a substring
 *               &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *  &lt;/xsd:element&gt; 
 * 	
 *   </code>
 * </pre>
 * 
 * <pre>
 *       <code>
 *  &lt;xsd:complexType name="StringPositionType"&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="se:FunctionType"&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:element ref="se:LookupString"/&gt;
 *                  &lt;xsd:element ref="se:StringValue"/&gt;
 *              &lt;/xsd:sequence&gt;
 *              &lt;xsd:attribute name="searchDirection" type="se:searchDirectionType"/&gt;
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
public class StringPositionBinding extends AbstractComplexBinding {
    FilterFactory filterFactory;
    
    public StringPositionBinding(FilterFactory filterFactory) {
        this.filterFactory = filterFactory;
    }
    /**
     * @generated
     */
    public QName getTarget() {
        return SE.StringPosition;
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

        //&lt;xsd:element ref="se:LookupString"/&gt;
        Expression lookup = (Expression) node.getChildValue("LookupString");
        
        //&lt;xsd:element ref="se:StringValue"/&gt;
        Expression string = (Expression) node.getChildValue("StringValue");
        
        //&lt;xsd:attribute name="searchDirection" type="se:searchDirectionType"/&gt;
        Literal direction = filterFactory.literal(node.getAttributeValue("searchDirection"));
        
        return filterFactory.function("strPosition", lookup, string, direction);
    }

}