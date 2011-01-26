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
import org.geotools.sld.bindings.SLDLinePlacementBinding;
import org.geotools.styling.LinePlacement;
import org.geotools.styling.StyleFactory;
import org.geotools.xml.*;
import org.opengis.filter.expression.Expression;

import javax.xml.namespace.QName;

/**
 * Binding object for the element http://www.opengis.net/se:LinePlacement.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:element name="LinePlacement" type="se:LinePlacementType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *          A "LinePlacement" specifies how a text label should be rendered
 *          relative to a linear geometry.
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
public class LinePlacementBinding extends SLDLinePlacementBinding {

    public LinePlacementBinding(StyleFactory styleFactory) {
        super(styleFactory);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return SE.LinePlacement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        //&lt;xsd:element minOccurs="0" ref="se:PerpendicularOffset"/&gt;
        LinePlacement lp = (LinePlacement) super.parse(instance, node, value);
        
        //&lt;xsd:element minOccurs="0" ref="se:IsRepeated"/&gt;
        if (node.hasChild("IsRepeated")) {
            lp.setRepeated((Boolean) node.getChildValue("IsRepeated"));
        }
        //&lt;xsd:element minOccurs="0" ref="se:InitialGap"/&gt;
        if (node.hasChild("InitialGap")) {
            lp.setInitialGap((Expression)node.getChildValue("InitialGap"));
        }
        //&lt;xsd:element minOccurs="0" ref="se:Gap"/&gt;
        if (node.hasChild("Gap")) {
            lp.setGap((Expression)node.getChildValue("Gap"));
        }
        //&lt;xsd:element minOccurs="0" ref="se:IsAligned"/&gt;
        if (node.hasChild("IsAligned")) {
            lp.setAligned((Boolean) node.getChildValue("IsAligned"));
        }       
        //&lt;xsd:element minOccurs="0" ref="se:GeneralizeLine"/&gt;
        if (node.hasChild("GeneralizeLine")) {
            lp.setGeneralized((Boolean) node.getChildValue("GeneralizeLine"));
        }
        
        return lp;
    }

}