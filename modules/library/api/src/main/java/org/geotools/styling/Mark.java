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
 * A Mark element defines a "shape" which has coloring applied to it.
 *
 * <p>
 * The details of this object are taken from the <a
 * href="https://portal.opengeospatial.org/files/?artifact_id=1188"> OGC
 * Styled-Layer Descriptor Report (OGC 02-070) version 1.0.0.</a>:
 * <pre><code>
 * &lt;xsd:element name="Mark"&gt;
 *   &lt;xsd:annotation&gt;
 *     &lt;xsd:documentation&gt;
 *       A "Mark" specifies a geometric shape and applies coloring to it.
 *     &lt;/xsd:documentation&gt;
 *   &lt;/xsd:annotation&gt;
 *   &lt;xsd:complexType&gt;
 *     &lt;xsd:sequence&gt;
 *       &lt;xsd:element ref="sld:WellKnownName" minOccurs="0"/&gt;
 *       &lt;xsd:element ref="sld:Fill" minOccurs="0"/&gt;
 *       &lt;xsd:element ref="sld:Stroke" minOccurs="0"/&gt;
 *     &lt;/xsd:sequence&gt;
 *   &lt;/xsd:complexType&gt;
 * &lt;/xsd:element&gt;
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
 * @source $URL$
 * @version $Id$
 */
public interface Mark extends org.opengis.style.Mark, Symbol {
    public static final Mark[] MARKS_EMPTY = new Mark[0];

    /**
     * This parameter gives the well-known name of the shape of the mark.<br>
     * Allowed names include at least "square", "circle", "triangle", "star",
     * "cross" and "x" though renderers may draw a different symbol instead if
     * they don't have a shape for all of these.<br>
     *
     * @return The well-known name of a shape.  The default value is "square".
     */
    Expression getWellKnownName();

    /**
     * This parameter gives the well-known name of the shape of the mark.<br>
     * Allowed names include at least "square", "circle", "triangle", "star",
     * "cross" and "x" though renderers may draw a different symbol instead if
     * they don't have a shape for all of these.<br>
     *
     * @param wellKnownName The well-known name of a shape.  The default value
     *        is "square".
     */
    void setWellKnownName(Expression wellKnownName);

    /**
     * This paramterer defines which stroke style should be used when rendering
     * the Mark.
     *
     * @return The Stroke definition to use when rendering the Mark.
     */
    Stroke getStroke();

    /**
     * This paramterer defines which stroke style should be used when rendering
     * the Mark.
     *
     * @param stroke The Stroke definition to use when rendering the Mark.
     */
    void setStroke(org.opengis.style.Stroke stroke);

    /**
     * This parameter defines which fill style to use when rendering the Mark.
     *
     * @return the Fill definition to use when rendering the Mark.
     */
    Fill getFill();

    /**
     * This parameter defines which fill style to use when rendering the Mark.
     *
     * @param fill the Fill definition to use when rendering the Mark.
     */
    void setFill(org.opengis.style.Fill fill);

    /**
     * @return size of mark
     * @deprecated please use graphic size
     */
    Expression getSize();

    /**
     * Size to display the mark.
     * <p>
     * If this value is not provided or is Expression.NIL then a default of 16x16
     * (or the natural size of the image) will be used.
     * 
     * @param size 
     * @deprecated please use graphic size
     */
    void setSize(Expression size);

    /**
     * @return rotation
     * @deprecated please use graphic rotation
     */
    Expression getRotation();

    /**
     * @param rotation
     * @deprecated please use graphic rotation
     */
    void setRotation(Expression rotation);

    /**
     * Mark defined by an external resource.
     *
     * @return ExternalMark or null if WellKNownName is being used
     */
    ExternalMark getExternalMark();
    
    /**
     * Mark defined by an external resource.
     * 
     * @param externalMark Indicate an mark defined by an external resource
     */
    void setExternalMark(org.opengis.style.ExternalMark externalMark);

    
    void accept(org.geotools.styling.StyleVisitor visitor);
}