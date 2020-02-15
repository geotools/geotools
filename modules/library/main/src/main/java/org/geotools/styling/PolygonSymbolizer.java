/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
 * A symbolizer describes how a polygon feature should appear on a map.
 *
 * <p>The symbolizer describes not just the shape that should appear but also such graphical
 * properties as color and opacity.
 *
 * <p>A symbolizer is obtained by specifying one of a small number of different types of symbolizer
 * and then supplying parameters to overide its default behaviour.
 *
 * <p>The details of this object are taken from the <a
 * href="https://portal.opengeospatial.org/files/?artifact_id=1188">OGC Styled-Layer Descriptor
 * Report (OGC 02-070) version 1.0.0.</a>:
 *
 * <pre><code>
 * &lt;xsd:element name="PolygonSymbolizer" substitutionGroup="sld:Symbolizer">
 *    &lt;xsd:annotation>
 *      &lt;xsd:documentation>
 *        A "PolygonSymbolizer" specifies the rendering of a polygon or
 *        area geometry, including its interior fill and border stroke.
 *      &lt;/xsd:documentation>
 *    &lt;/xsd:annotation>
 *    &lt;xsd:complexType>
 *      &lt;xsd:complexContent>
 *       &lt;xsd:extension base="sld:SymbolizerType">
 *         &lt;xsd:sequence>
 *           &lt;xsd:element ref="sld:Geometry" minOccurs="0"/>
 *           &lt;xsd:element ref="sld:Fill" minOccurs="0"/>
 *           &lt;xsd:element ref="sld:Stroke" minOccurs="0"/>
 *         &lt;/xsd:sequence>
 *       &lt;/xsd:extension>
 *     &lt;/xsd:complexContent>
 *   &lt;/xsd:complexType>
 * &lt;/xsd:element>
 * </code></pre>
 *
 * <p>Renderers can use this information when displaying styled features, though it must be
 * remembered that not all renderers will be able to fully represent strokes as set out by this
 * interface. For example, opacity may not be supported.
 *
 * <p>Notes:
 *
 * <ul>
 *   <li>The graphical parameters and their values are derived from SVG/CSS2 standards with names
 *       and semantics which are as close as possible.
 * </ul>
 *
 * @author James Macgill
 * @version $Id$
 */
public interface PolygonSymbolizer extends org.opengis.style.PolygonSymbolizer, Symbolizer {

    /** Pixels between each graphic of a polygon fill */
    public static String GRAPHIC_MARGIN_KEY = "graphic-margin";

    /**
     * Provides the graphical-symbolization parameter to use to fill the area of the geometry. Note
     * that the area should be filled first before the outline is rendered.
     *
     * @return The Fill style to use when rendering the area.
     */
    Fill getFill();

    /**
     * Provides the graphical-symbolization parameter to use to fill the area of the geometry. Note
     * that the area should be filled first before the outline is rendered.
     *
     * @param fill The Fill style to use when rendering the area.
     */
    void setFill(org.opengis.style.Fill fill);

    /**
     * Provides the graphical-symbolization parameter to use for the outline of the Polygon.
     *
     * @return The Stroke style to use when rendering lines.
     */
    Stroke getStroke();

    /**
     * Provides the graphical-symbolization parameter to use for the outline of the Polygon.
     *
     * @param stroke The Stroke style to use when rendering lines.
     */
    void setStroke(org.opengis.style.Stroke stroke);

    /**
     * PerpendicularOffset works as defined for LineSymbolizer, allowing to draw polygons smaller or
     * larger than their actual geometry.
     *
     * @param offset Offset from the edge polygon positive outside; negative to the inside with a
     *     default of 0.
     */
    public void setPerpendicularOffset(Expression offset);

    /**
     * Displacement from the original geometry in pixels.
     *
     * @return Displacement above and to the right of the indicated point; default x=0, y=0
     */
    public Displacement getDisplacement();

    /** Provide x / y offset in pixels used to crate shadows. */
    public void setDisplacement(org.opengis.style.Displacement displacement);
}
