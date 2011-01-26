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
import org.geotools.sld.bindings.SLDTextSymbolizerBinding;
import org.geotools.styling.StyleFactory;
import org.geotools.xml.*;

import javax.xml.namespace.QName;

/**
 * Binding object for the element http://www.opengis.net/se:TextSymbolizer.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:element name="TextSymbolizer" substitutionGroup="se:Symbolizer" type="se:TextSymbolizerType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *          A "TextSymbolizer" is used to render text labels according to
 *          various graphical parameters.
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
public class TextSymbolizerBinding extends SLDTextSymbolizerBinding {

    public TextSymbolizerBinding(StyleFactory styleFactory) {
        super(styleFactory);
    }
    
    @Override
    public int getExecutionMode() {
        return BEFORE;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return SE.TextSymbolizer;
    }

}