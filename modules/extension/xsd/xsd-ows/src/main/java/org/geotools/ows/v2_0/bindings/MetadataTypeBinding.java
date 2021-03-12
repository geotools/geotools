/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.v2_0.bindings;

import javax.xml.namespace.QName;
import net.opengis.ows20.MetadataType;
import net.opengis.ows20.Ows20Factory;
import org.geotools.ows.v2_0.OWS;
import org.geotools.xsd.AbstractComplexEMFBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the type http://www.opengis.net/ows/2.0:MetadataType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;complexType name="MetadataType" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *
 *      &lt;annotation&gt;
 *
 *        &lt;documentation&gt;This element either references or contains more metadata
 *
 *        about the element that includes this element. To reference metadata
 *
 *        stored remotely, at least the xlinks:href attribute in xlink:simpleAttrs
 *
 *        shall be included. Either at least one of the attributes in
 *
 *        xlink:simpleAttrs or a substitute for the AbstractMetaData element shall
 *
 *        be included, but not both. An Implementation Specification can restrict
 *
 *        the contents of this element to always be a reference or always contain
 *
 *        metadata. (Informative: This element was adapted from the
 *
 *        metaDataProperty element in GML 3.0.)&lt;/documentation&gt;
 *
 *      &lt;/annotation&gt;
 *
 *      &lt;sequence&gt;
 *
 *        &lt;element minOccurs="0" ref="ows:AbstractMetaData"/&gt;
 *
 *      &lt;/sequence&gt;
 *
 *      &lt;attributeGroup ref="xlink:simpleAttrs"&gt;
 *
 *        &lt;annotation&gt;
 *
 *          &lt;documentation&gt;Reference to metadata recorded elsewhere, either
 *
 *          external to this XML document or within it. Whenever practical, the
 *
 *          xlink:href attribute with type anyURI should include a URL from which
 *
 *          this metadata can be electronically retrieved.&lt;/documentation&gt;
 *
 *        &lt;/annotation&gt;
 *
 *      &lt;/attributeGroup&gt;
 *
 *      &lt;attribute name="about" type="anyURI" use="optional"&gt;
 *
 *        &lt;annotation&gt;
 *
 *          &lt;documentation&gt;Optional reference to the aspect of the element which
 *
 *          includes this "metadata" element that this metadata provides more
 *
 *          information about.&lt;/documentation&gt;
 *
 *        &lt;/annotation&gt;
 *
 *      &lt;/attribute&gt;
 *
 *    &lt;/complexType&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class MetadataTypeBinding extends AbstractComplexEMFBinding {

    public MetadataTypeBinding(Ows20Factory factory) {
        super(factory);
    }

    /** @generated */
    @Override
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
    @Override
    public Class getType() {
        return MetadataType.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        // TODO: implement and remove call to super
        return super.parse(instance, node, value);
    }

    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        // TODO Auto-generated method stub
        return super.getProperty(object, name);
    }
}
