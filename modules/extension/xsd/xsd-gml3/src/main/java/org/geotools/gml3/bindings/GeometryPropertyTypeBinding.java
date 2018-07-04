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
import org.geotools.gml3.XSDIdRegistry;

/**
 * Binding object for the type http://www.opengis.net/gml:GeometryPropertyType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;complexType name="GeometryPropertyType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;A geometric property can either be any geometry element encapsulated in an element of this type or an XLink reference
 *                          to a remote geometry element (where remote includes geometry elements located elsewhere in the same document). Note that either
 *                          the reference or the contained element must be given, but not both or none.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence minOccurs="0"&gt;
 *          &lt;element ref="gml:_Geometry"/&gt;
 *      &lt;/sequence&gt;
 *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"&gt;
 *          &lt;annotation&gt;
 *              &lt;documentation&gt;This attribute group includes the XLink attributes (see xlinks.xsd). XLink is used in GML to reference
 *                                  remote resources (including those elsewhere in the same document). A simple link element can be constructed by
 *                                  including a specific set of XLink attributes. The XML Linking Language (XLink) is currently a Proposed Recommendation
 *                                  of the World Wide Web Consortium. XLink allows elements to be inserted into XML documents so as to create
 *                                  sophisticated links between resources; such links can be used to reference remote properties. A simple link element
 *                                  can be used to implement pointer functionality, and this functionality has been built into various GML 3 elements by
 *                                  including the gml:AssociationAttributeGroup.&lt;/documentation&gt;
 *          &lt;/annotation&gt;
 *      &lt;/attributeGroup&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 * </pre>
 *
 * @generated
 * @source $URL$
 *     http://svn.osgeo.org/geotools/trunk/modules/extension/xsd/xsd-gml3/src/main/java/org
 *     /geotools/gml3/bindings/GeometryPropertyTypeBinding.java $
 */
public class GeometryPropertyTypeBinding extends GeometryPropertyTypeBindingBase {

    public GeometryPropertyTypeBinding(GML3EncodingUtils encodingUtils, XSDIdRegistry idRegistry) {
        super(encodingUtils, idRegistry);
        // TODO Auto-generated constructor stub
    }

    /** @generated */
    public QName getTarget() {
        return GML.GeometryPropertyType;
    }
}
