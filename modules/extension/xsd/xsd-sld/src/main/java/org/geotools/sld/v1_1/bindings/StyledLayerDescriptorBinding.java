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

import org.geotools.sld.bindings.SLDStyledLayerDescriptorBinding;
import org.geotools.sld.v1_1.SLD;
import org.geotools.styling.Description;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.xml.*;

import javax.xml.namespace.QName;

/**
 * Binding object for the element http://www.opengis.net/sld:StyledLayerDescriptor.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:element name="StyledLayerDescriptor"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *          A StyledLayerDescriptor is a sequence of styled layers, represented
 *          at the first level by NamedLayer and UserLayer elements.
 *        &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexType&gt;
 *          &lt;xsd:sequence&gt;
 *              &lt;xsd:element minOccurs="0" ref="se:Name"/&gt;
 *              &lt;xsd:element minOccurs="0" ref="se:Description"/&gt;
 *              &lt;xsd:element maxOccurs="unbounded" minOccurs="0" ref="sld:UseSLDLibrary"/&gt;
 *              &lt;xsd:choice maxOccurs="unbounded" minOccurs="0"&gt;
 *                  &lt;xsd:element ref="sld:NamedLayer"/&gt;
 *                  &lt;xsd:element ref="sld:UserLayer"/&gt;
 *              &lt;/xsd:choice&gt;
 *          &lt;/xsd:sequence&gt;
 *          &lt;xsd:attribute name="version" type="se:VersionType" use="required"/&gt;
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
public class StyledLayerDescriptorBinding extends SLDStyledLayerDescriptorBinding {

    public StyledLayerDescriptorBinding(StyleFactory styleFactory) {
        super(styleFactory);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        StyledLayerDescriptor sld =  (StyledLayerDescriptor) super.parse(instance, node, value);
        if (node.hasChild("Description")) {
            Description desc = (Description) node.getChildValue("Description");
            if (desc.getAbstract() != null) {
                sld.setAbstract(desc.getAbstract().toString());
            }
            if (desc.getTitle() != null) {
                sld.setTitle(desc.getTitle().toString());
            }
        }
        return sld;
    }

}