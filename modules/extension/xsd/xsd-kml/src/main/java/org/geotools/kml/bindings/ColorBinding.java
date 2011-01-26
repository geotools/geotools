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
import org.geotools.xml.AbstractSimpleBinding;
import org.geotools.xml.Binding;
import org.geotools.xml.InstanceComponent;


/**
 * Binding object for the type http://earth.google.com/kml/2.1:color.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;simpleType name="color"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;&lt;![CDATA[
 *
 *          aabbggrr
 *
 *          ffffffff: opaque white
 *          ff000000: opaque black
 *
 *          ]]&gt;&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;restriction base="hexBinary"&gt;
 *          &lt;length value="4"/&gt;
 *      &lt;/restriction&gt;
 *  &lt;/simpleType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class ColorBinding extends AbstractSimpleBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return KML.color;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Color.class;
    }

    public int getExecutionMode() {
        return Binding.OVERRIDE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value)
        throws Exception {
        if (value == null) {
            return null;
        }

        String color = (String) value;

        if (color.startsWith("#")) {
            color = color.substring(1);
        }

        if ((color.length() < 6) || (color.length() > 8)) {
            throw new IllegalArgumentException("illegal color: " + color);
        }

        int i = 0;
        String a = null;

        if (color.length() > 6) {
            a = color.substring(i, 2);
            i = 2;
        }

        String r = color.substring(i, i + 2);
        String g = color.substring(i + 2, i + 4);
        String b = color.substring(i + 4, i + 6);

        if (a != null) {
            return new Color(Integer.parseInt(r, 16), Integer.parseInt(g, 16),
                Integer.parseInt(b, 16), Integer.parseInt(a, 16));
        } else {
            return new Color(Integer.parseInt(r, 16), Integer.parseInt(g, 16),
                Integer.parseInt(b, 16));
        }
    }
}
