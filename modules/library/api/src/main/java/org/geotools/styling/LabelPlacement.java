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
 * The "LabelPlacement" specifies where and how a text label should be rendered
 * relative to a geometry.
 *
 * <p>
 * The details of this object are taken from the <a
 * href="https://portal.opengeospatial.org/files/?artifact_id=1188"> OGC
 * Styled-Layer Descriptor Report (OGC 02-070) version 1.0.0.</a>:
 * <pre><code>
 * &lt;xsd:element name="LabelPlacement"&gt;
 *   &lt;xsd:annotation&gt;
 *     &lt;xsd:documentation&gt;
 *       The "LabelPlacement" specifies where and how a text label should
 *       be rendered relative to a geometry.  The present mechanism is
 *       poorly aligned with CSS/SVG.
 *     &lt;/xsd:documentation&gt;
 *   &lt;/xsd:annotation&gt;
 *   &lt;xsd:complexType&gt;
 *     &lt;xsd:choice&gt;
 *       &lt;xsd:element ref="sld:PointPlacement"/&gt;
 *       &lt;xsd:element ref="sld:LinePlacement"/&gt;
 *     &lt;/xsd:choice&gt;
 *   &lt;/xsd:complexType&gt;
 * &lt;/xsd:element&gt;
 * </code></pre>
 * </p>
 *
 * <p></p>
 *
 * @author Ian Turton, CCG
 *
 * @source $URL$
 */
public interface LabelPlacement extends org.opengis.style.LabelPlacement{
    
    void accept(StyleVisitor visitor);
}
