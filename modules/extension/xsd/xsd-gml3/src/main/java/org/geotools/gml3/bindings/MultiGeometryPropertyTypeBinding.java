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
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

import com.vividsolutions.jts.geom.GeometryCollection;

/**
 * Binding object for the type
 * http://www.opengis.net/gml/3.2:MultiGeometryPropertyType.
 * 
 * <p>
 * 
 * <pre>
 * &lt;complexType name="MultiGeometryPropertyType">
 *   &lt;annotation>
 *     &lt;documentation>A property that has a geometric aggregate as its value domain can either be an appropriate geometry element encapsulated in an element of this type or an XLink reference to a remote geometry element (where remote includes geometry elements located elsewhere in the same document). Either the reference or the contained element must be given, but neither both nor none.&lt;/documentation>
 *   &lt;/annotation>
 *   &lt;sequence minOccurs="0">
 *      &lt;element ref="gml:_GeometricAggregate"/>
 *   &lt;/sequence>
 *   &lt;attributeGroup ref="gml:AssociationAttributeGroup">
 *     &lt;annotation>
 *        &lt;documentation>This attribute group includes the XLink attributes (see xlinks.xsd). XLink is used in GML to reference remote resources (including those elsewhere in the same document). A simple link element can be constructed by including a specific set of XLink attributes. The XML Linking Language (XLink) is currently a Proposed Recommendation of the World Wide Web Consortium. XLink allows elements to be inserted into XML documents so as to create sophisticated links between resources; such links can be used to reference remote properties.
 *              A simple link element can be used to implement pointer functionality, and this functionality has been built into various GML 3 elements by including the gml:AssociationAttributeGroup.&lt;/documentation>
 *     &lt;/annotation>
 *   &lt;/attributeGroup>
 * &lt;/complexType>
 * </pre>
 * 
 * </p>
 * 
 * @generated
 *
 *
 * @source $URL$
 */
public class MultiGeometryPropertyTypeBinding extends AbstractComplexBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.MultiGeometryPropertyType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return GeometryCollection.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
            throws Exception {

        return node.getChildValue(GeometryCollection.class);
    }
    
    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        if ( GML._GeometricAggregate.equals( name ) ) {
            return (GeometryCollection)object;
        }
        return null;
    }

}
