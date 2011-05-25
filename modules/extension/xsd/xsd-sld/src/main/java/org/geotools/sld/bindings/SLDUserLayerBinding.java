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
package org.geotools.sld.bindings;

import org.picocontainer.MutablePicoContainer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.util.Iterator;
import javax.xml.namespace.QName;
import org.geotools.styling.LayerFeatureConstraints;
import org.geotools.styling.RemoteOWS;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.UserLayer;
import org.geotools.xml.*;


/**
 * Binding object for the element http://www.opengis.net/sld:UserLayer.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:element name="UserLayer"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;         A UserLayer allows a user-defined
 *              layer to be built from WFS and         WCS data.       &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexType&gt;
 *          &lt;xsd:sequence&gt;
 *              &lt;xsd:element ref="sld:Name" minOccurs="0"/&gt;
 *              &lt;xsd:element ref="sld:RemoteOWS" minOccurs="0"/&gt;
 *              &lt;xsd:element ref="sld:LayerFeatureConstraints"/&gt;
 *              &lt;xsd:element ref="sld:UserStyle" maxOccurs="unbounded"/&gt;
 *          &lt;/xsd:sequence&gt;
 *      &lt;/xsd:complexType&gt;
 *  &lt;/xsd:element&gt;
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
public class SLDUserLayerBinding extends AbstractComplexBinding {
    StyleFactory styleFactory;

    public SLDUserLayerBinding(StyleFactory styleFactory) {
        this.styleFactory = styleFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return SLD.USERLAYER;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public int getExecutionMode() {
        return AFTER;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return UserLayer.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public void initialize(ElementInstance instance, Node node, MutablePicoContainer context) {
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        UserLayer userLayer = styleFactory.createUserLayer();

        //&lt;xsd:element ref="sld:Name" minOccurs="0"/&gt;
        if (node.hasChild("Name")) {
            userLayer.setName((String) node.getChildValue("Name"));
        }

        //&lt;xsd:element ref="sld:RemoteOWS" minOccurs="0"/&gt;
        if (node.hasChild("RemoteOWS")) {
            userLayer.setRemoteOWS((RemoteOWS) node.getChildValue("RemoteOWS"));
        }

        //&lt;xsd:element ref="sld:LayerFeatureConstraints"/&gt;
        if (node.hasChild("LayerFeatureConstraints")) {
            LayerFeatureConstraints lfc = (LayerFeatureConstraints) node.getChildValue(
                    "LayerFeatureConstraints");
            userLayer.setLayerFeatureConstraints(lfc.getFeatureTypeConstraints());
        }

        //&lt;xsd:element ref="sld:UserStyle" maxOccurs="unbounded"/&gt;
        for (Iterator i = node.getChildValues("UserStyle").iterator(); i.hasNext();) {
            userLayer.addUserStyle((Style) i.next());
        }

        return userLayer;
    }
}
