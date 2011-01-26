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
package org.geotools.wfs.bindings;

import javax.xml.namespace.QName;

import net.opengis.wfs.MetadataURLType;
import net.opengis.wfs.WfsFactory;

import org.geotools.wfs.WFS;
import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;


/**
 * Binding object for the type http://www.opengis.net/wfs:MetadataURLType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="MetadataURLType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *              A Web Feature Server MAY use zero or more MetadataURL
 *              elements to offer detailed, standardized metadata about
 *              the data underneath a particular feature type.  The type
 *              attribute indicates the standard to which the metadata
 *              complies; the format attribute indicates how the metadata is
 *              structured.  Two types are defined at present:
 *              'TC211' or 'ISO19115' = ISO TC211 19115;
 *              'FGDC'                = FGDC CSDGM.
 *              'ISO19139'            = ISO 19139
 *           &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:simpleContent&gt;
 *          &lt;xsd:extension base="xsd:string"&gt;
 *              &lt;xsd:attribute name="type" use="required"&gt;
 *                  &lt;xsd:simpleType&gt;
 *                      &lt;xsd:restriction base="xsd:string"&gt;
 *                          &lt;xsd:enumeration value="TC211"/&gt;
 *                          &lt;xsd:enumeration value="FGDC"/&gt;
 *                          &lt;xsd:enumeration value="19115"/&gt;
 *                          &lt;xsd:enumeration value="19139"/&gt;
 *                      &lt;/xsd:restriction&gt;
 *                  &lt;/xsd:simpleType&gt;
 *              &lt;/xsd:attribute&gt;
 *              &lt;xsd:attribute name="format" use="required"&gt;
 *                  &lt;xsd:simpleType&gt;
 *                      &lt;xsd:restriction base="xsd:string"&gt;
 *                          &lt;xsd:enumeration value="text/xml"/&gt;
 *                          &lt;xsd:enumeration value="text/html"/&gt;
 *                          &lt;xsd:enumeration value="text/sgml"/&gt;
 *                          &lt;xsd:enumeration value="text/plain"/&gt;
 *                      &lt;/xsd:restriction&gt;
 *                  &lt;/xsd:simpleType&gt;
 *              &lt;/xsd:attribute&gt;
 *          &lt;/xsd:extension&gt;
 *      &lt;/xsd:simpleContent&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class MetadataURLTypeBinding extends AbstractComplexEMFBinding {
    public MetadataURLTypeBinding(WfsFactory factory) {
        super(factory);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.MetadataURLType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return MetadataURLType.class;
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
}
