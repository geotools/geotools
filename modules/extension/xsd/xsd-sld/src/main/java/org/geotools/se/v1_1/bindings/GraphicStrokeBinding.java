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

import org.geotools.se.v1_1.SE;
import org.geotools.sld.bindings.SLDGraphicStrokeBinding;
import org.geotools.styling.Graphic;
import org.geotools.xml.*;
import org.opengis.filter.expression.Expression;

import javax.xml.namespace.QName;

/**
 * Binding object for the element http://www.opengis.net/se:GraphicStroke.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:element name="GraphicStroke" type="se:GraphicStrokeType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *          A "GraphicStroke" defines a repeated-linear graphic pattern to be used
 *          for stroking a line.
 *        &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *  &lt;/xsd:element&gt; 
 * 	
 *   </code>
 * </pre>
 * 
 * </p>
 * 
 * @generated
 */
public class GraphicStrokeBinding extends SLDGraphicStrokeBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return SE.GraphicStroke;
    }

    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        Graphic g = (Graphic) super.parse(instance, node, value);
        
        //&lt;xsd:element minOccurs="0" ref="se:InitialGap"/&gt;
        if (node.hasChild("InitialGap")) {
            g.setInitialGap((Expression)node.getChildValue("InitialGap"));
        }
        //&lt;xsd:element minOccurs="0" ref="se:Gap"/&gt;
        if (node.hasChild("Gap")) {
            g.setGap((Expression)node.getChildValue("Gap"));
        }
        return g;
    }

}