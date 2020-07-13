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
 *
 */
package org.geotools.styling;

import org.opengis.filter.expression.Expression;

/**
 * A Halo fills an extended area outside the glyphs of a rendered textlabel to make it easier to
 * read over a background.
 *
 * <p>The details of this object are taken from the <a
 * href="https://portal.opengeospatial.org/files/?artifact_id=1188">OGC Styled-Layer Descriptor
 * Report (OGC 02-070) version 1.0.0.</a>:
 *
 * <pre><code>
 * &lt;xsd:element name="Halo"&gt;
 *   &lt;xsd:annotation&gt;
 *     &lt;xsd:documentation&gt;
 *      A "Halo" fills an extended area outside the glyphs of a rendered
 *       text label to make the label easier to read over a background.
 *     &lt;/xsd:documentation&gt;
 *   &lt;/xsd:annotation&gt;
 *   &lt;xsd:complexType&gt;
 *     &lt;xsd:sequence&gt;
 *       &lt;xsd:element ref="sld:Radius" minOccurs="0"/&gt;
 *       &lt;xsd:element ref="sld:Fill" minOccurs="0"/&gt;
 *     &lt;/xsd:sequence&gt;
 *   &lt;/xsd:complexType&gt;
 * &lt;/xsd:element&gt;
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
 * $Id$
 *
 * @author Ian Turton, CCG
 */
public interface Halo extends org.opengis.style.Halo {

    /** Expression that represents the the distance the halo extends from the text */
    void setRadius(Expression radius);

    /**
     * The fill (color) of the halo
     *
     * @return fill (color) of the halo
     */
    Fill getFill();

    /** The fill (color) of the halo */
    void setFill(org.opengis.style.Fill fill);

    void accept(org.geotools.styling.StyleVisitor visitor);
}
