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
import org.geotools.se.v1_1.SE;
import org.geotools.sld.bindings.SLDAnchorPointBinding;
import org.geotools.styling.StyleFactory;
import org.geotools.xml.*;

/**
 * Binding object for the element http://www.opengis.net/se:AnchorPoint.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;xsd:element name="AnchorPoint" type="se:AnchorPointType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *          An "AnchorPoint" identifies the location inside of a text label to
 *          use an an 'anchor' for positioning it relative to a point geometry.
 *        &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *  &lt;/xsd:element&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class AnchorPointBinding extends SLDAnchorPointBinding {

    public AnchorPointBinding(StyleFactory styleFactory) {
        super(styleFactory);
    }

    /** @generated */
    public QName getTarget() {
        return SE.AnchorPoint;
    }
}
