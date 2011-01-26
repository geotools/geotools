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
import org.geotools.sld.bindings.SLDCssParameterBinding;
import org.geotools.xml.*;
import org.opengis.filter.FilterFactory;

import javax.xml.namespace.QName;

/**
 * Binding object for the element http://www.opengis.net/se:SvgParameter.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:element name="SvgParameter" type="se:SvgParameterType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *          A "SvgParameter" refers to an SVG/CSS graphical-formatting
 *          parameter.  The parameter is identified using the "name" attribute
 *          and the content of the element gives the SVG/CSS-coded value.
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
public class SvgParameterBinding extends SLDCssParameterBinding {

    public SvgParameterBinding(FilterFactory filterFactory) {
        super(filterFactory);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return SE.SvgParameter;
    }
}