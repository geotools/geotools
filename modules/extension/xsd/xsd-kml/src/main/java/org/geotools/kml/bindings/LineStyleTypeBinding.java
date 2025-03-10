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
package org.geotools.kml.bindings;

import java.awt.Color;
import javax.xml.namespace.QName;
import org.geotools.api.style.LineSymbolizer;
import org.geotools.kml.KML;
import org.geotools.styling.StyleBuilder;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.Binding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the type http://earth.google.com/kml/2.1:LineStyleType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;complexType final="#all" name="LineStyleType"&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="kml:ColorStyleType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element default="1" minOccurs="0" name="width" type="float"/&gt;
 *              &lt;/sequence&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class LineStyleTypeBinding extends AbstractComplexBinding {
    StyleBuilder sb;

    public LineStyleTypeBinding(StyleBuilder sb) {
        this.sb = sb;
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return KML.LineStyleType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Class getType() {
        return LineSymbolizer.class;
    }

    @Override
    public int getExecutionMode() {
        return Binding.AFTER;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        Color color = (Color) value;
        Float width = (Float) node.getChildValue("width", Float.valueOf(1f));

        return sb.createLineSymbolizer(color, width);
    }
}
