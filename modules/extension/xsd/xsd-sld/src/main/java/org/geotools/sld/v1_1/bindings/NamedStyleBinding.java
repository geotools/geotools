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
package org.geotools.sld.v1_1.bindings;

import org.geotools.sld.bindings.SLDNamedStyleBinding;
import org.geotools.styling.Description;
import org.geotools.styling.NamedStyle;
import org.geotools.styling.StyleFactory;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

/**
 * Binding object for the element http://www.opengis.net/sld:NamedStyle.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:element name="NamedStyle"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *          A NamedStyle is used to refer to a style that has a name in a WMS.
 *        &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexType&gt;
 *          &lt;xsd:sequence&gt;
 *              &lt;xsd:element ref="se:Name"/&gt;
 *              &lt;xsd:element minOccurs="0" ref="se:Description"/&gt;
 *          &lt;/xsd:sequence&gt;
 *      &lt;/xsd:complexType&gt;
 *  &lt;/xsd:element&gt; 
 * 	
 *   </code>
 * </pre>
 * 
 * </p>
 * 
 * @generated
 */
public class NamedStyleBinding extends SLDNamedStyleBinding {

    public NamedStyleBinding(StyleFactory styleFactory) {
        super(styleFactory);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        NamedStyle style = (NamedStyle) super.parse(instance, node, value);
        
        if (node.hasChild("Description")) {
            Description desc = (Description) node.getChildValue("Description");
            style.getDescription().setAbstract(desc.getAbstract());
            style.getDescription().setTitle(desc.getTitle());
        }
        
        return style;
    }

}