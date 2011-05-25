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
 * Binding object for the element http://www.opengis.net/se:ChangeCase.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:element name="ChangeCase" substitutionGroup="se:Function" type="se:ChangeCaseType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *  Changes the case of strings.
 *               &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *  &lt;/xsd:element&gt; 
 * 	
 *   </code>
 * </pre>
 * 
 * <pre>
 *       <code>
 *  &lt;xsd:complexType name="ChangeCaseType"&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="se:FunctionType"&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:element ref="se:StringValue"/&gt;
 *              &lt;/xsd:sequence&gt;
 *              &lt;xsd:attribute name="direction" type="se:directionType"/&gt;
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
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-sld/src/main/java/org/geotools/se/v1_1/bindings/ChangeCaseBinding.java $
 */
public class ChangeCaseBinding extends AbstractComplexBinding {

    FilterFactory filterFactory;
    
    public ChangeCaseBinding(FilterFactory filterFactory) {
        this.filterFactory = filterFactory;
    }
    
    /**
     * @generated
     */
    public QName getTarget() {
        return SE.ChangeCase;
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
        
        String direction = (String) node.getAttributeValue("direction");
        if ("toUpper".equalsIgnoreCase(direction)) {
            return filterFactory.function("strToUpperCase", str);
        }
        else {
            return filterFactory.function("strToLowerCase", str);
        }
    }

}
