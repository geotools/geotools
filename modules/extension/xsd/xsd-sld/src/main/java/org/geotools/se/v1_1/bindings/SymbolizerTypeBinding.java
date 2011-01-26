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

import java.net.URI;

import org.geotools.se.v1_1.SE;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.UomOgcMapping;
import org.geotools.xml.*;
import org.opengis.style.Description;

import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/se:SymbolizerType.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:complexType abstract="true" name="SymbolizerType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *          A "SymbolizerType" is an abstract type for encoding the graphical
 *          properties used to portray geographic information.  Concrete Symbolizer
 *          types are derived from this base type.
 *        &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element minOccurs="0" ref="se:Name"/&gt;
 *          &lt;xsd:element minOccurs="0" ref="se:Description"/&gt;
 *          &lt;xsd:element minOccurs="0" ref="se:BaseSymbolizer"/&gt;
 *      &lt;/xsd:sequence&gt;
 *      &lt;xsd:attribute name="version" type="se:VersionType"/&gt;
 *      &lt;xsd:attribute name="uom" type="xsd:anyURI"/&gt;
 *  &lt;/xsd:complexType&gt; 
 * 	
 *   </code>
 * </pre>
 * 
 * </p>
 * 
 * @generated
 */
public class SymbolizerTypeBinding extends AbstractComplexBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return SE.SymbolizerType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return Symbolizer.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        Symbolizer sym = (Symbolizer) value;
        //      &lt;xsd:sequence&gt;
        //          &lt;xsd:element minOccurs="0" ref="se:Name"/&gt;
        //          &lt;xsd:element minOccurs="0" ref="se:Description"/&gt;
        //          &lt;xsd:element minOccurs="0" ref="se:BaseSymbolizer"/&gt;
        //      &lt;/xsd:sequence&gt;
        //      &lt;xsd:attribute name="version" type="se:VersionType"/&gt;
        //      &lt;xsd:attribute name="uom" type="xsd:anyURI"/&gt;
        if (node.hasChild("Name")) {
            sym.setName((String)node.getChildValue("Name"));
        }
        if (node.hasChild("Description")) {
            sym.setDescription((Description) node.getChildValue("Description"));
        }
        if (node.hasChild("BaseSymbolizer")) {
            //throw new IllegalArgumentException("BaseSymbolizer not supported");
        }
        if (node.hasAttribute("version")) {
            //throw new IllegalArgumentException("version not supported");
        }
        if (node.hasAttribute("uom")) {
            String uom = ((URI) node.getAttributeValue("uom")).toString();
            if (UomOgcMapping.get(uom) == null) {
                throw new IllegalArgumentException("uom " + uom + " not supported");
            }
            
            sym.setUnitOfMeasure(UomOgcMapping.get(uom).getUnit());
        }
        
        return sym;
    }

}