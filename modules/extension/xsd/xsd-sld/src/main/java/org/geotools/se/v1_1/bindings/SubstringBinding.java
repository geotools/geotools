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
 * Binding object for the element http://www.opengis.net/se:Substring.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:element name="Substring" substitutionGroup="se:Function" type="se:SubstringType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *  Returns substrings.
 *               &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *  &lt;/xsd:element&gt; 
 * 	
 *   </code>
 * </pre>
 * 
 * <pre>
 *       <code>
 *  &lt;xsd:complexType name="SubstringType"&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="se:FunctionType"&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:element ref="se:StringValue"/&gt;
 *                  &lt;xsd:element minOccurs="0" ref="se:Position"/&gt;
 *                  &lt;xsd:element minOccurs="0" ref="se:Length"/&gt;
 *              &lt;/xsd:sequence&gt;
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
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-sld/src/main/java/org/geotools/se/v1_1/bindings/SubstringBinding.java $
 */
public class SubstringBinding extends AbstractComplexBinding {

    FilterFactory filterFactory;
    
    public SubstringBinding(FilterFactory filterFactory) {
        this.filterFactory = filterFactory;
    }
    
    /**
     * @generated
     */
    public QName getTarget() {
        return SE.Substring;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return Expression.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        //&lt;xsd:element ref="se:StringValue"/&gt;
        Expression string = (Expression) node.getChildValue("StringValue");
        
        //&lt;xsd:element minOccurs="0" ref="se:Position"/&gt;
        Expression position = (Expression) node.getChildValue("Position");
        
        //function is one based
        position = filterFactory.subtract(position, filterFactory.literal(1));
        
        //&lt;xsd:element minOccurs="0" ref="se:Length"/&gt;
        Expression length = (Expression) node.getChildValue("Length");

        //our strSubstring function works with begin,end rather than begin,length
        Expression end = filterFactory.add(position, length);
        
        return filterFactory.function("strSubstring", string, position, end);
    }

}
