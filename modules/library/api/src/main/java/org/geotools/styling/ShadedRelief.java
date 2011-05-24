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
 * The ShadedRelief element selects the application of relief shading (or ?hill
 * shading?) to an image for a three-dimensional visual effect.  It is defined
 * as:
 * <pre>
 * &lt;xs:element name="ShadedRelief"&gt;
 *   &lt;xs:complexType&gt;
 *     &lt;xs:sequence&gt;
 *       &lt;xs:element ref="sld:BrightnessOnly" minOccurs="0"/&gt;
 *       &lt;xs:element ref="sld:ReliefFactor" minOccurs="0"/&gt;
 *     &lt;/xs:sequence&gt;
 *   &lt;/xs:complexType&gt;
 * &lt;/xs:element&gt;
 * &lt;xs:element name="BrightnessOnly" type="xs:boolean"/&gt;
 * &lt;xs:element name="ReliefFactor" type="xs:double"/&gt;
 * </pre>
 * Exact parameters of the shading are system-dependent (for now).  If the
 * BrightnessOnly flag is ?0? (false, default), the shading is applied to the
 * layer being rendered as the current RasterSymbol. If BrightnessOnly is ?1?
 * (true), the shading is applied to the brightness of the colors in the
 * rendering canvas generated so far by other layers, with the effect of
 * relief-shading these other layers. The default for BrightnessOnly is ?0?
 * (false).  The ReliefFactor gives the amount of exaggeration to use for the
 * height of the ?hills.?  A value of around 55 (times) gives reasonable
 * results for Earth-based DEMs. The default value is system-dependent.
 *
 * @author iant
 *
 * @source $URL$
 */
public interface ShadedRelief extends org.opengis.style.ShadedRelief{
    
    /**
     * turns brightnessOnly on or off depending on value of flag.
     *
     * @param flag boolean
     */
    public void setBrightnessOnly(boolean flag);

    /**
     * The ReliefFactor gives the amount of exaggeration to use for the height
     * of the ?hills.?  A value of around 55 (times) gives reasonable results
     * for Earth-based DEMs. The default value is system-dependent.
     *
     * @param reliefFactor an expression which evaluates to a double.
     */
    public void setReliefFactor(Expression reliefFactor);

    public void accept(org.geotools.styling.StyleVisitor visitor);
}
