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
package org.geotools.geopkg.wps.xml;

import java.net.URI;
import javax.xml.namespace.QName;
import org.geotools.geopkg.wps.GeoPackageProcessRequest;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.locationtech.jts.geom.Envelope;

/**
 * Binding object for the type http://www.opengis.net/gpkg:layertype.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;xs:complexType name="layertype" xmlns:xs="http://www.w3.org/2001/XMLSchema"&gt;
 *      &lt;xs:sequence&gt;
 *        &lt;xs:element minOccurs="0" name="identifier" type="xs:string"/&gt;
 *        &lt;xs:element minOccurs="0" name="description" type="xs:string"/&gt;
 *        &lt;xs:element minOccurs="0" name="bbox"&gt;
 *          &lt;xs:complexType name="layertype_bbox"&gt;
 *            &lt;xs:sequence&gt;
 *              &lt;xs:element name="minx" type="xs:decimal"/&gt;
 *              &lt;xs:element name="miny" type="xs:decimal"/&gt;
 *              &lt;xs:element name="maxx" type="xs:decimal"/&gt;
 *              &lt;xs:element name="maxy" type="xs:decimal"/&gt;
 *            &lt;/xs:sequence&gt;
 *          &lt;/xs:complexType&gt;
 *        &lt;/xs:element&gt;
 *        &lt;xs:element minOccurs="0" name="srs" type="xs:string"/&gt;
 *      &lt;/xs:sequence&gt;
 *      &lt;xs:attribute name="name" use="required"/&gt;
 *    &lt;/xs:complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public abstract class LayertypeBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return GPKG.layertype;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return GeoPackageProcessRequest.Layer.class;
    }

    public abstract GeoPackageProcessRequest.Layer parseLayer(
            ElementInstance instance, Node node, Object value) throws Exception;

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        GeoPackageProcessRequest.Layer layer = parseLayer(instance, node, value);

        layer.setName((String) node.getAttributeValue("name"));
        layer.setIdentifier((String) node.getAttributeValue("identifier"));
        layer.setBbox((Envelope) node.getChildValue("bbox"));
        layer.setDescription((String) node.getChildValue("description"));
        layer.setSrs((URI) node.getChildValue("srs"));

        return layer;
    }
}
