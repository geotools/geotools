/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.style;

import org.geotools.api.filter.expression.Expression;

/**
 * The ShadedRelief element selects the application of relief shading (or “hill shading”) to an image for a
 * three-dimensional visual effect.
 *
 * <p>Exact parameters of the shading are system-dependent (for now). If the BrightnessOnly flag is “0” or “false”
 * (false, default), the shading is applied to the layer being rendered as the current RasterSymbolizer. If
 * BrightnessOnly is “1” or “true” (true), the shading is applied to the brightness of the colors in the rendering
 * canvas generated so far by other layers, with the effect of relief-shading these other layers. The default for
 * BrightnessOnly is “0” (false). The ReliefFactor gives the amount of exaggeration to use for the height of the
 * “hills.” A value of around 55 (times) gives reasonable results for Earth-based DEMs. The default value is
 * system-dependent.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding Implementation Specification
 *     1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Ian Turton, CCG
 * @author Johann Sorel (Geomatys)
 * @since GeoAPI 2.2
 */
public interface ShadedRelief {

    /**
     * indicates if brightnessOnly is true or false. Default is false.
     *
     * @return boolean brightnessOn.
     */
    public boolean isBrightnessOnly();

    /**
     * The ReliefFactor gives the amount of exaggeration to use for the height of the ?hills.? A value of around 55
     * (times) gives reasonable results for Earth-based DEMs. The default value is system-dependent.
     *
     * @return an expression which evaluates to a double.
     */
    public Expression getReliefFactor();

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    Object accept(TraversingStyleVisitor visitor, Object extraData);

    /**
     * turns brightnessOnly on or off depending on value of flag.
     *
     * @param flag boolean
     */
    void setBrightnessOnly(boolean flag);

    /**
     * The ReliefFactor gives the amount of exaggeration to use for the height of the ?hills.? A value of around 55
     * (times) gives reasonable results for Earth-based DEMs. The default value is system-dependent.
     *
     * @param reliefFactor an expression which evaluates to a double.
     */
    void setReliefFactor(Expression reliefFactor);

    void accept(StyleVisitor visitor);
}
