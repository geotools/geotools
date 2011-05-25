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
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.StyleFactory;
import org.geotools.xml.*;


/**
 * Binding object for the element http://www.opengis.net/sld:ChannelSelection.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *   &lt;xsd:element name=&quot;ChannelSelection&quot;&gt;
 *       &lt;xsd:annotation&gt;
 *           &lt;xsd:documentation&gt;         &quot;ChannelSelection&quot;
 *               specifies the false-color channel selection         for a
 *               multi-spectral raster source.       &lt;/xsd:documentation&gt;
 *       &lt;/xsd:annotation&gt;
 *       &lt;xsd:complexType&gt;
 *           &lt;xsd:choice&gt;
 *               &lt;xsd:sequence&gt;
 *                   &lt;xsd:element ref=&quot;sld:RedChannel&quot;/&gt;
 *                   &lt;xsd:element ref=&quot;sld:GreenChannel&quot;/&gt;
 *                   &lt;xsd:element ref=&quot;sld:BlueChannel&quot;/&gt;
 *               &lt;/xsd:sequence&gt;
 *               &lt;xsd:element ref=&quot;sld:GrayChannel&quot;/&gt;
 *           &lt;/xsd:choice&gt;
 *       &lt;/xsd:complexType&gt;
 *   &lt;/xsd:element&gt;
 *
 *
 * </code>
 *         </pre>
 *
 * </p>
 *
 * @generated
 *
 *
 * @source $URL$
 */
public class SLDChannelSelectionBinding extends AbstractComplexBinding {
    StyleFactory styleFactory;

    public SLDChannelSelectionBinding(StyleFactory styleFactory) {
        this.styleFactory = styleFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return SLD.CHANNELSELECTION;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public int getExecutionMode() {
        return AFTER;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return ChannelSelection.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public void initialize(ElementInstance instance, Node node, MutablePicoContainer context) {
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        
        ChannelSelection cs = styleFactory.createChannelSelection(null);
        
        if (node.hasChild("GrayChannel")) {
            cs.setGrayChannel((SelectedChannelType) node.getChildValue("GrayChannel"));
        }
        else {
            SelectedChannelType[] rgb = new SelectedChannelType[] {
                (SelectedChannelType) node.getChildValue("RedChannel"),
                (SelectedChannelType) node.getChildValue("GreenChannel"),
                (SelectedChannelType) node.getChildValue("BlueChannel")
            };
            cs.setRGBChannels(rgb);
        }
        
        return cs;
    }
}
