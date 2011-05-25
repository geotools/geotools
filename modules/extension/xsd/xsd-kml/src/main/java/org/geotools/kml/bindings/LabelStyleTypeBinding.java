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
import org.geotools.kml.KML;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.TextSymbolizer;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.Binding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;


/**
 * Binding object for the type http://earth.google.com/kml/2.1:LabelStyleType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType final="#all" name="LabelStyleType"&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="kml:ColorStyleType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element default="1" minOccurs="0" name="scale" type="float"/&gt;
 *              &lt;/sequence&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 *
 * @source $URL$
 */
public class LabelStyleTypeBinding extends AbstractComplexBinding {
    StyleBuilder sb;

    public LabelStyleTypeBinding(StyleBuilder sb) {
        this.sb = sb;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return KML.LabelStyleType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return TextSymbolizer.class;
    }

    public int getExecutionMode() {
        return Binding.AFTER;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        Color color = (Color) value;

        TextSymbolizer textSymbolizer = sb.createTextSymbolizer();
        textSymbolizer.setFill(sb.createFill(color));

        return textSymbolizer;
    }
}
