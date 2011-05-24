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


/**
 * A symbolizer describes how a feature should appear on a map.
 *
 * <p>
 * The symbolizer defines not just the shape that should appear but also  such
 * graphical properties as color and opacity.
 * </p>
 *
 * <p>
 * A symbolizer is obtained by specifying one of a small number of different
 * types of symbolizer and then supplying parameters to overide its default
 * behaviour.
 * </p>
 *
 * <p>
 * The details of this object are taken from the <a
 * href="https://portal.opengeospatial.org/files/?artifact_id=1188"> OGC
 * Styled-Layer Descriptor Report (OGC 02-070) version 1.0.0.</a>:
 * <pre><code>
 * &lt;xsd:element name="PointSymbolizer" substitutionGroup="sld:Symbolizer">
 *   &lt;xsd:annotation>
 *     &lt;xsd:documentation>
 *       A "PointSymbolizer" specifies the rendering of a "graphic symbol"
 *       at a point.
 *     &lt;/xsd:documentation>
 *   &lt;/xsd:annotation>
 *   &lt;xsd:complexType>
 *     &lt;xsd:complexContent>
 *       &lt;xsd:extension base="sld:SymbolizerType">
 *         &lt;xsd:sequence>
 *           &lt;xsd:element ref="sld:Geometry" minOccurs="0"/>
 *           &lt;xsd:element ref="sld:Graphic" minOccurs="0"/>
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
 * @author James Macgill
 *
 * @source $URL$
 * @version $Id$
 */
public interface PointSymbolizer extends org.opengis.style.PointSymbolizer, Symbolizer {
    /**
     * Provides the graphical-symbolization parameter to use for the  point
     * geometry.
     *
     * @return The Graphic to be used when drawing a point.
     */
    Graphic getGraphic();

    /**
     * Provides the graphical-symbolization parameter to use for the  point
     * geometry.
     */
    void setGraphic(org.opengis.style.Graphic graphic);

    
}
