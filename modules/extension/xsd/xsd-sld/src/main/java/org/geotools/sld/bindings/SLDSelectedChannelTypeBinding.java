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
import javax.xml.namespace.QName;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.StyleFactory;
import org.geotools.xml.*;


/**
 * Binding object for the type http://www.opengis.net/sld:SelectedChannelType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="SelectedChannelType"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element ref="sld:SourceChannelName"/&gt;
 *          &lt;xsd:element ref="sld:ContrastEnhancement" minOccurs="0"/&gt;
 *      &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt;
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
public class SLDSelectedChannelTypeBinding extends AbstractComplexBinding {
    StyleFactory styleFactory;

    public SLDSelectedChannelTypeBinding(StyleFactory styleFactory) {
        this.styleFactory = styleFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return SLD.SELECTEDCHANNELTYPE;
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
        return SelectedChannelType.class;
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
        return styleFactory.createSelectedChannelType((String) node.getChildValue(
                "SourceChannelName"),
            (ContrastEnhancement) node.getChildValue("ContrastEnhancement"));
    }
}
