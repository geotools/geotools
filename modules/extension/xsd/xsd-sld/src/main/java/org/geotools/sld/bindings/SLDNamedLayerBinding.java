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
import org.geotools.styling.NamedLayer;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.xml.*;


/**
 * Binding object for the element http://www.opengis.net/sld:NamedLayer.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:element name="NamedLayer"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;         A NamedLayer is a layer of data that
 *              has a name advertised by a WMS.       &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexType&gt;
 *          &lt;xsd:sequence&gt;
 *              &lt;xsd:element ref="sld:Name"/&gt;
 *              &lt;xsd:element ref="sld:LayerFeatureConstraints" minOccurs="0"/&gt;
 *              &lt;xsd:choice minOccurs="0" maxOccurs="unbounded"&gt;
 *                  &lt;xsd:element ref="sld:NamedStyle"/&gt;
 *                  &lt;xsd:element ref="sld:UserStyle"/&gt;
 *              &lt;/xsd:choice&gt;
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
public class SLDNamedLayerBinding extends AbstractComplexBinding {
    StyleFactory styleFactory;

    public SLDNamedLayerBinding(StyleFactory styleFactory) {
        this.styleFactory = styleFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return SLD.NAMEDLAYER;
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
        return NamedLayer.class;
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
        NamedLayer namedLayer = styleFactory.createNamedLayer();

        //&lt;xsd:element ref="sld:Name"/&gt;
        namedLayer.setName((String) node.getChildValue("Name"));

        //&lt;xsd:element ref="sld:LayerFeatureConstraints" minOccurs="0"/&gt;
        if (node.hasChild("LayerFeatureConstraints")) {
            LayerFeatureConstraints constraints = (LayerFeatureConstraints) node.getChildValue(
                    "LayerFeatureConstraints");
            namedLayer.setLayerFeatureConstraints(constraints.getFeatureTypeConstraints());
        }

        //&lt;xsd:choice minOccurs="0" maxOccurs="unbounded"&gt;
        //  &lt;xsd:element ref="sld:NamedStyle"/&gt;
        //  &lt;xsd:element ref="sld:UserStyle"/&gt;
        //&lt;/xsd:choice&gt;
        for (Iterator itr = node.getChildValues(Style.class).iterator(); itr.hasNext();) {
            namedLayer.addStyle((Style) itr.next());
        }

        return namedLayer;
    }
}
