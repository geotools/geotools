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
package org.geotools.ows.bindings;

import javax.xml.namespace.QName;
import net.opengis.ows10.Ows10Factory;
import org.geotools.xsd.AbstractComplexEMFBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.geotools.xsd.ows.OWS;

/**
 * Binding object for the type http://www.opengis.net/ows:MetadataType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;complexType name="MetadataType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;This element either references or contains more metadata about the element that includes this element. To reference metadata stored remotely, at least the xlinks:href attribute in xlink:simpleLink shall be included. Either at least one of the attributes in xlink:simpleLink or a substitute for the AbstractMetaData element shall be included, but not both. An Implementation Specification can restrict the contents of this element to always be a reference or always contain metadata. (Informative: This element was adapted from the metaDataProperty element in GML 3.0.) &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element minOccurs="0" ref="ows:AbstractMetaData"/&gt;
 *      &lt;/sequence&gt;
 *      &lt;attributeGroup ref="xlink:simpleLink"&gt;
 *          &lt;annotation&gt;
 *              &lt;documentation&gt;Reference to metadata recorded elsewhere, either external to this XML document or within it. Whenever practical, the xlink:href attribute with type anyURI should include a URL from which this metadata can be electronically retrieved. &lt;/documentation&gt;
 *          &lt;/annotation&gt;
 *      &lt;/attributeGroup&gt;
 *      &lt;attribute name="about" type="anyURI" use="optional"&gt;
 *          &lt;annotation&gt;
 *              &lt;documentation&gt;Optional reference to the aspect of the element which includes this "metadata" element that this metadata provides more information about. &lt;/documentation&gt;
 *          &lt;/annotation&gt;
 *      &lt;/attribute&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class MetadataTypeBinding extends AbstractComplexEMFBinding {
    public MetadataTypeBinding(Ows10Factory factory) {
        super(factory);
    }

    /** @generated */
    public QName getTarget() {
        return OWS.MetadataType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return super.getType();
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
