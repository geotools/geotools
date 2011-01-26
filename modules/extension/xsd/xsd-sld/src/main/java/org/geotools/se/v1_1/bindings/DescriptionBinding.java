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
import org.geotools.styling.Description;
import org.geotools.styling.StyleFactory;
import org.geotools.util.SimpleInternationalString;
import org.geotools.xml.*;
import org.opengis.util.InternationalString;

import javax.xml.namespace.QName;

/**
 * Binding object for the element http://www.opengis.net/se:Description.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:element name="Description" type="se:DescriptionType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *          A "Description" gives human-readable descriptive information for
 *          the object it is included within.
 *        &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *  &lt;/xsd:element&gt; 
 * 	
 *   </code>
 * </pre>
 * 
 * </p>
 * 
 * @generated
 */
public class DescriptionBinding extends AbstractComplexBinding {

    StyleFactory styleFactory;
    
    public DescriptionBinding(StyleFactory styleFactory) {
        this.styleFactory = styleFactory;
    }
    
    /**
     * @generated
     */
    public QName getTarget() {
        return SE.Description;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return Description.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        
        InternationalString title = null, abstrct = null;
        
        //&lt;xsd:element minOccurs="0" name="Title" type="xsd:string"/&gt;
        if (node.hasChild("Title")) {
            title = new SimpleInternationalString((String) node.getChildValue("Title"));
        }
        //&lt;xsd:element minOccurs="0" name="Abstract" type="xsd:string"/&gt;
        if (node.hasChild("Abstract")) {
            abstrct = new SimpleInternationalString((String) node.getChildValue("Abstract"));
        }
        
        return styleFactory.description(title, abstrct);
    }

}