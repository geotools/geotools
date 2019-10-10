/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.wcs.bindings;

import javax.xml.namespace.QName;
import org.geotools.wcs.WCS;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the type http://www.opengis.net/wcs:MetadataLinkType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;complexType name="MetadataLinkType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Refers to a metadata package that contains metadata properties for an object. The metadataType attribute indicates the type of metadata referred to. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="wcs:MetadataAssociationType"&gt;
 *              &lt;attribute name="metadataType" use="required"&gt;
 *                  &lt;simpleType&gt;
 *                      &lt;restriction base="NMTOKEN"&gt;
 *                          &lt;enumeration value="TC211"&gt;
 *                              &lt;annotation&gt;
 *                                  &lt;documentation&gt;This metadata uses a profile of ISO TC211s Geospatial Metadata Standard 19115. &lt;/documentation&gt;
 *                              &lt;/annotation&gt;
 *                          &lt;/enumeration&gt;
 *                          &lt;enumeration value="FGDC"&gt;
 *                              &lt;annotation&gt;
 *                                  &lt;documentation&gt;This metadata uses a profile of the US FGDC Content Standard for Digital Geospatial Metadata. &lt;/documentation&gt;
 *                              &lt;/annotation&gt;
 *                          &lt;/enumeration&gt;
 *                          &lt;enumeration value="other"&gt;
 *                              &lt;annotation&gt;
 *                                  &lt;documentation&gt;This metadata uses some other metadata standard(s) and/or no standard. &lt;/documentation&gt;
 *                              &lt;/annotation&gt;
 *                          &lt;/enumeration&gt;
 *                      &lt;/restriction&gt;
 *                  &lt;/simpleType&gt;
 *              &lt;/attribute&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class MetadataLinkTypeBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return WCS.MetadataLinkType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return null;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        // TODO: implement and remove call to super
        return super.parse(instance, node, value);
    }
}
