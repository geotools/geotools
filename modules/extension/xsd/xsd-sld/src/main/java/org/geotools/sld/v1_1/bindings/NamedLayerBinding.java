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
package org.geotools.sld.v1_1.bindings;

import org.geotools.sld.bindings.SLDNamedLayerBinding;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.StyleFactory;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the element http://www.opengis.net/sld:NamedLayer.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;xsd:element name="NamedLayer"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *          A NamedLayer is a layer of data that has a name advertised by a WMS.
 *        &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexType&gt;
 *          &lt;xsd:sequence&gt;
 *              &lt;xsd:element ref="se:Name"/&gt;
 *              &lt;xsd:element minOccurs="0" ref="se:Description"/&gt;
 *              &lt;xsd:element minOccurs="0" ref="sld:LayerFeatureConstraints"/&gt;
 *              &lt;xsd:choice maxOccurs="unbounded" minOccurs="0"&gt;
 *                  &lt;xsd:element ref="sld:NamedStyle"/&gt;
 *                  &lt;xsd:element ref="sld:UserStyle"/&gt;
 *              &lt;/xsd:choice&gt;
 *          &lt;/xsd:sequence&gt;
 *      &lt;/xsd:complexType&gt;
 *  &lt;/xsd:element&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class NamedLayerBinding extends SLDNamedLayerBinding {

    public NamedLayerBinding(StyleFactory styleFactory) {
        super(styleFactory);
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        NamedLayer layer = (NamedLayer) super.parse(instance, node, value);

        // TODO: description
        return layer;
    }
}
