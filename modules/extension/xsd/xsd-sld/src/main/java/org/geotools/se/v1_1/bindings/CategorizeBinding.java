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

import java.util.List;

import org.geotools.se.v1_1.SE;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapEntry;
import org.geotools.styling.StyleFactory;
import org.geotools.xml.*;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

import javax.xml.namespace.QName;

/**
 * Binding object for the element http://www.opengis.net/se:Categorize.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:element name="Categorize" substitutionGroup="se:Function" type="se:CategorizeType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *         The transformation of continuous values to distinct values.
 *               &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *  &lt;/xsd:element&gt; 
 * 	
 *   </code>
 * </pre>
 * 
 * <pre>
 *       <code>
 *  &lt;xsd:complexType name="CategorizeType"&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="se:FunctionType"&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:element ref="se:LookupValue"/&gt;
 *                  &lt;xsd:element ref="se:Value"/&gt;
 *                  &lt;xsd:sequence maxOccurs="unbounded" minOccurs="0"&gt;
 *                      &lt;xsd:element ref="se:Threshold"/&gt;
 *                      &lt;xsd:element ref="se:Value"/&gt;
 *                  &lt;/xsd:sequence&gt;
 *              &lt;/xsd:sequence&gt;
 *              &lt;xsd:attribute name="threshholdsBelongTo"
 *                  type="se:ThreshholdsBelongToType" use="optional"/&gt;
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
public class CategorizeBinding extends AbstractComplexBinding {

    StyleFactory styleFactory;
    FilterFactory filterFactory;
    
    public CategorizeBinding(StyleFactory styleFactory, FilterFactory filterFactory) {
        this.styleFactory = styleFactory;
        this.filterFactory = filterFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return SE.Categorize;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return ColorMap.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        ColorMap map = styleFactory.createColorMap();
        
        List<Node> children = node.getChildren();
        int i = 0;
        while(!"Value".equals(children.get(i).getComponent().getName())) i++;
        
        ColorMapEntry entry = styleFactory.createColorMapEntry();
        entry.setColor((Expression)children.get(i++).getValue());
        map.addColorMapEntry(entry);
        
        while(i < children.size()) {
            entry = styleFactory.createColorMapEntry();
            entry.setQuantity((Expression)children.get(i).getValue());
            if (i+1 >= children.size()) {
                throw new IllegalArgumentException("Incorrectly specified color map Threshold/Value pair");
            }
            
            entry.setColor((Expression)children.get(i+1).getValue());
            map.addColorMapEntry(entry);

            i+=2;
        }
        
        return map;
    }

}