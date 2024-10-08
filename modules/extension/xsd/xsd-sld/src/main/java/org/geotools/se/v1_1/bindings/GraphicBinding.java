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
package org.geotools.se.v1_1.bindings;

import javax.xml.namespace.QName;
import org.geotools.api.style.AnchorPoint;
import org.geotools.api.style.Displacement;
import org.geotools.api.style.Graphic;
import org.geotools.api.style.StyleFactory;
import org.geotools.se.v1_1.SE;
import org.geotools.sld.bindings.SLDGraphicBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the element http://www.opengis.net/se:Graphic.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;xsd:element name="Graphic" type="se:GraphicType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *          A "Graphic" specifies or refers to a "graphic Symbolizer" with inherent
 *          shape, size, and coloring.
 *        &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *  &lt;/xsd:element&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class GraphicBinding extends SLDGraphicBinding {

    public GraphicBinding(StyleFactory styleFactory) {
        super(styleFactory);
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return SE.Graphic;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        Graphic g = (Graphic) super.parse(instance, node, value);
        if (node.hasChild(AnchorPoint.class)) {
            g.setAnchorPoint(node.getChildValue(AnchorPoint.class));
        }
        if (node.hasChild(Displacement.class)) {
            g.setDisplacement(node.getChildValue(Displacement.class));
        }
        return g;
    }
}
