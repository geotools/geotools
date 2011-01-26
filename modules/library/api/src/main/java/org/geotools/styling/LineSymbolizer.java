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
package org.geotools.styling;

import org.opengis.filter.expression.Expression;


/**
 * A symbolizer describes how a feature should appear on a map.
 *
 * <p>
 * The symbolizer describes not just the shape that should appear but also
 * such graphical properties as color and opacity.
 * </p>
 *
 * <p>
 * A symbolizer is obtained by specifying one of a small number of different
 * types of symbolizer and then supplying parameters to override its default
 * behaviour.
 * </p>
 *
 * <p>
 * The details of this object are taken from the <a
 * href="https://portal.opengeospatial.org/files/?artifact_id=1188"> OGC
 * Styled-Layer Descriptor Report (OGC 02-070) version 1.0.0.</a>:
 * <pre><code>
 * &lt;xsd:element name="LineSymbolizer" substitutionGroup="sld:Symbolizer">
 *   &lt;xsd:annotation>
 *     &lt;xsd:documentation>
 *       A LineSymbolizer is used to render a "stroke" along a linear geometry.
 *     &lt;/xsd:documentation>
 *   &lt;/xsd:annotation>
 *   &lt;xsd:complexType>
 *     &lt;xsd:complexContent>
 *       &lt;xsd:extension base="sld:SymbolizerType">
 *         &lt;xsd:sequence>
 *           &lt;xsd:element ref="sld:Geometry" minOccurs="0"/>
 *           &lt;xsd:element ref="sld:Stroke" minOccurs="0"/>
 *         &lt;/xsd:sequence>
 *       &lt;/xsd:extension>
 *     &lt;/xsd:complexContent>
 *   &lt;/xsd:complexType>
 * &lt;/xsd:element>
 * </code></pre>
 * </p>
 *
 * <p>
 * Renderers can use this information when displaying styled features, though
 * it must be remembered that not all renderers will be able to fully
 * represent strokes as set out by this interface.  For example, opacity may
 * not be supported.
 * </p>
 *
 * <p>
 * Notes:
 *
 * <ul>
 * <li>
 * The graphical parameters and their values are derived from SVG/CSS2
 * standards with names and semantics which are as close as possible.
 * </li>
 * </ul>
 * </p>
 *
 * @author James Macgill, CCG
 * @source $URL$
 * @version $Id$
 */
public interface LineSymbolizer extends org.opengis.style.LineSymbolizer, Symbolizer {
    /**
     * Provides the graphical-symbolization parameter to use for the  linear
     * geometry.
     *
     * @return The Stroke style to use when rendering lines.
     */
    Stroke getStroke();

    /**
     * Provides the graphical-symbolization parameter to use for the  linear
     * geometry.
     *
     * @param stroke The Stroke style to use when rendering lines.
     */
    void setStroke(org.opengis.style.Stroke stroke);

    /**
     * Define an offset to draw lines in parallel to the original geometry.
     * 
     * @param offset
     *            Distance in UOMs to offset line; left-hand side is positive; right-hand side is
     *            negative; the default value is 0
     */
    void setPerpendicularOffset(Expression offset);
}
