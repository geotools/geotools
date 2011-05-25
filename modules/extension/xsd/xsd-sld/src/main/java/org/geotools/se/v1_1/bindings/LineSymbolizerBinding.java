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
import org.geotools.sld.bindings.SLDLineSymbolizerBinding;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.StyleFactory;
import org.geotools.xml.*;
import org.opengis.filter.expression.Expression;


import javax.xml.namespace.QName;

/**
 * Binding object for the element http://www.opengis.net/se:LineSymbolizer.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;xsd:element name="LineSymbolizer" substitutionGroup="se:Symbolizer" type="se:LineSymbolizerType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *          A LineSymbolizer is used to render a "stroke" along a linear geometry.
 *        &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *  &lt;/xsd:element&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-sld/src/main/java/org/geotools/se/v1_1/bindings/LineSymbolizerBinding.java $
 */
public class LineSymbolizerBinding extends SLDLineSymbolizerBinding {

    public LineSymbolizerBinding(StyleFactory styleFactory) {
        super(styleFactory);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return SE.LineSymbolizer;
    }

    @Override
    public int getExecutionMode() {
        return BEFORE;
    }
    
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        LineSymbolizer sym = (LineSymbolizer) super.parse(instance, node, value);
        
        //&lt;xsd:element minOccurs="0" ref="se:PerpendicularOffset"/&gt;
        if (node.hasChild("PerpendicularOffset")) {
            sym.setPerpendicularOffset((Expression) node.getChildValue("PerpendicularOffset"));
        }
        
        return sym;
    }

}
