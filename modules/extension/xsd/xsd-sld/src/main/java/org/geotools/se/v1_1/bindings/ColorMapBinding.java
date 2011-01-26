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
import org.geotools.styling.ColorMap;
import org.geotools.xml.*;

import javax.xml.namespace.QName;

/**
 * Binding object for the element http://www.opengis.net/se:ColorMap.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:element name="ColorMap" type="se:ColorMapType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *          A "ColorMap" defines either the colors of a pallette-type raster
 *          source or the mapping of numeric pixel values to colors.
 *        &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *  &lt;/xsd:element&gt; 
 * 	
 *   </code>
 * </pre>
 * 
 * <pre>
 *       <code>
 *  &lt;xsd:complexType name="ColorMapType"&gt;
 *      &lt;xsd:choice&gt;
 *          &lt;xsd:element ref="se:Categorize"/&gt;
 *          &lt;xsd:element ref="se:Interpolate"/&gt;
 *      &lt;/xsd:choice&gt;
 *  &lt;/xsd:complexType&gt; 
 *              
 *        </code>
 * </pre>
 * 
 * 
 * </p>
 * 
 * @generated
 */
public class ColorMapBinding extends AbstractComplexBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return SE.ColorMap;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return ColorMap.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        ColorMap map = (ColorMap) node.getChildValue("Categorize");
        if (map == null) {
            map = (ColorMap) node.getChildValue("Interpolate");
        }
        
        if (map == null) {
            throw new IllegalArgumentException("Categorize or Interpolate not specified");
        }
        return map;
    }

}