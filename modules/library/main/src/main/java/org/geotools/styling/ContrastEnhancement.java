/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Map;
import org.opengis.filter.expression.Expression;
import org.opengis.style.ContrastMethod;

/**
 * The ContrastEnhancement object defines contrast enhancement for a channel of a false-color image
 * or for a color image. Its format is:
 *
 * <pre>
 * &lt;xs:element name="ContrastEnhancement"&gt;
 *   &lt;xs:complexType&gt;
 *     &lt;xs:sequence&gt;
 *       &lt;xs:choice minOccurs="0"&gt;
 *         &lt;xs:element ref="sld:Normalize"/&gt;
 *         &lt;xs:element ref="sld:Histogram"/&gt;
 *       &lt;/xs:choice&gt;
 *       &lt;xs:element ref="sld:GammaValue" minOccurs="0"/&gt;
 *     &lt;/xs:sequence&gt;
 *   &lt;/xs:complexType&gt;
 * &lt;/xs:element&gt;
 * &lt;xs:element name="Normalize"&gt;
 *   &lt;xs:complexType/&gt;
 * &lt;/xs:element&gt;
 * &lt;xs:element name="Histogram"&gt;
 *   &lt;xs:complexType/&gt;
 * &lt;/xs:element&gt;
 * &lt;xs:element name="GammaValue" type="xs:double"/&gt;
 * </pre>
 *
 * In the case of a color image, the relative grayscale brightness of a pixel color is used.
 * ?Normalize? means to stretch the contrast so that the dimmest color is stretched to black and the
 * brightest color is stretched to white, with all colors in between stretched out linearly.
 * ?Histogram? means to stretch the contrast based on a histogram of how many colors are at each
 * brightness level on input, with the goal of producing equal number of pixels in the image at each
 * brightness level on output. This has the effect of revealing many subtle ground features. A
 * ?GammaValue? tells how much to brighten (value greater than 1.0) or dim (value less than 1.0) an
 * image. The default GammaValue is 1.0 (no change). If none of Normalize, Histogram, or GammaValue
 * are selected in a ContrastEnhancement, then no enhancement is performed.
 *
 * @author iant
 */
public interface ContrastEnhancement extends org.opengis.style.ContrastEnhancement {
    /** Used to set the contrast enhancement method used. */
    public void setMethod(ContrastMethod method);

    /**
     * @param gamma How much to brighten (greater than 1) or dim (less than 1) this channel; use 1.0
     *     to indicate no change.
     */
    public void setGammaValue(Expression gamma);

    /**
     * How much to brighten (values greater than 1.0) or dim (values less than 1.0) an image. The
     * default GammaValue is 1.0 (no change).
     *
     * @return Expression, if <code>null</code> a value of 1.0 is assumed indicating no change
     */
    public Expression getGammaValue();

    /**
     * Return vendor options relating to the enhancement method
     *
     * @return a Map containing expressions with string keys.
     */
    public Map<String, Expression> getOptions();
    /**
     * check if vendor option key is available
     *
     * @param key - the name of the option to check
     * @return true if present
     */
    public boolean hasOption(String key);

    /**
     * Store a vendor option
     *
     * @param key - the name of the option
     * @param value an expression that evaluates to it's value
     */
    public void addOption(String key, Expression value);

    /** Traversal of the style data structure. */
    public void accept(StyleVisitor visitor);

    /** @param options a Map of VendorOptions */
    public void setOptions(Map<String, Expression> options);

    /** @return An expression for the matching VendorOption */
    public Expression getOption(String string);

    /**
     * Convenience method to allow users to pass in a {@link ContrastEnhancementMethod} to update
     * {@link Method} and {@link Options} in one go.
     *
     * @param method the {@link ContrastEnhancementMethod} that underlies this enhancement
     */
    public void setMethod(ContrastMethodStrategy method);
}
