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
 * The "LinePlacement" specifies where and how a text label should be rendered
 * relative to a line.
 *
 * <p>
 * The details of this object are taken from the <a
 * href="https://portal.opengeospatial.org/files/?artifact_id=1188"> OGC
 * Styled-Layer Descriptor Report (OGC 02-070) version 1.0.0.</a>:
 * <pre><code>
 * &lt;xsd:element name="LinePlacement"&gt;
 *   &lt;xsd:annotation&gt;
 *     &lt;xsd:documentation&gt;
 *       A "LinePlacement" specifies how a text label should be rendered
 *       relative to a linear geometry.
 *     &lt;/xsd:documentation&gt;
 *   &lt;/xsd:annotation&gt;
 *   &lt;xsd:complexType&gt;
 *     &lt;xsd:sequence&gt;
 *       &lt;xsd:element ref="sld:PerpendicularOffset" minOccurs="0"/&gt;
 *     &lt;/xsd:sequence&gt;
 *   &lt;/xsd:complexType&gt;
 * &lt;/xsd:element&gt;
 * </code></pre>
 * </p>
 *
 * <p>
 * $Id$
 * </p>
 *
 * @author Ian Turton, CCG
 *
 * @source $URL$
 */
public interface LinePlacement extends org.opengis.style.LinePlacement, LabelPlacement {
    /**
     * @deprecated
     */
    boolean IsAligned();
    
    /**
     * Correct method name violation from GeoAPI.
     * @return
     */
    boolean isAligned();
    
    /**
     * Returns the expression that is used to compute how far from the lines
     * the text will be drawn.  The distance must evaluate to a non-negative
     * number.
     *
     * @return compute how far from the line the text will be drawn
     */
    Expression getPerpendicularOffset();

    /**
     * Sets the expression that is used to compute how far from the lines the
     * text will be drawn. See {@link #getPerpendicularOffset} for details.
     *
     * @param offset 
     */
    void setPerpendicularOffset(Expression offset);
    
    public void setRepeated(boolean repeated);
    public void setGeneralized(boolean generalized);
    public void setAligned(boolean aligned);
    public void setGap(Expression gap);
    public void setInitialGap(Expression initialGap);
}
