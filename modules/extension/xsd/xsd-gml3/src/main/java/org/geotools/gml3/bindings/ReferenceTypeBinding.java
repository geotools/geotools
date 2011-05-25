/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml3.bindings;

import javax.xml.namespace.QName;

import org.geotools.gml3.GML;
import org.geotools.xlink.XLINK;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.feature.Property;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * Binding object for the type http://www.opengis.net/gml:ReferenceType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="ReferenceType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;
 *              A pattern or base for derived types used to specify complex
 *              types corresponding to a UML aggregation association. An
 *              instance of this type serves as a pointer to a remote Object.
 *           &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 *
 * @source $URL$
 */
public class ReferenceTypeBinding extends AbstractComplexBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return GML.ReferenceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Property.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        //TODO: implement and remove call to super
        return super.parse(instance, node, value);
    }
    
    public Element encode(Object object, Document document, Element value) throws Exception {
        Property property = (Property) object;
        // I don't think we can assign values to this type, so only encode client properties
        // for the mean time
        GML3EncodingUtils.encodeClientProperties(property, value);

        return value;
    }

    public Object getProperty(Object object, QName name) throws Exception {
        Property association = (Property) object;
    
        //non resolved, return the xlink:href
        if (XLINK.HREF.equals(name)) {
            String id = (String) association.getUserData().get("gml:id");
    
            return "#" + id;
        }
    
        return null;
    }
}
