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
import org.geotools.sld.bindings.SLDFillBinding;
import org.geotools.styling.StyleFactory;
import org.geotools.xml.*;
import org.opengis.filter.FilterFactory;

/**
 * Binding object for the element http://www.opengis.net/se:Fill.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;xsd:element name="Fill" type="se:FillType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *          A "Fill" specifies the pattern for filling an area geometry.
 *          The allowed SvgParameters are: "fill" (color) and "fill-opacity".
 *        &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *  &lt;/xsd:element&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 * @source $URL$
 */
public class FillBinding extends SLDFillBinding {

    public FillBinding(StyleFactory styleFactory, FilterFactory filterFactory) {
        super(styleFactory, filterFactory);
    }

    /** @generated */
    public QName getTarget() {
        return SE.Fill;
    }
}
