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
import org.opengis.style.OverlapBehavior;

/**
 * The RasterSymbolizer describes how to render raster/matrix-coverage data (e.g., satellite photos,
 * DEMs).
 *
 * <p>The details of this object are taken from the <a
 * href="https://portal.opengeospatial.org/files/?artifact_id=1188">OGC Styled-Layer Descriptor
 * Report (OGC 02-070) version 1.0.0.</a>:
 *
 * <pre><code>
 * &lt;xs:element name="RasterSymbolizer"&gt;
 *   &lt;xs:complexType&gt;
 *     &lt;xs:sequence&gt;
 *       &lt;xs:element ref="sld:Geometry" minOccurs="0"/&gt;
 *       &lt;xs:element ref="sld:Opacity" minOccurs="0"/&gt;
 *       &lt;xs:element ref="sld:ChannelSelection" minOccurs="0"/&gt;
 *       &lt;xs:element ref="sld:OverlapBehavior" minOccurs="0"/&gt;
 *       &lt;xs:element ref="sld:ColorMap" minOccurs="0"/&gt;
 *       &lt;xs:element ref="sld:ContrastEnhancement" minOccurs="0"/&gt;
 *       &lt;xs:element ref="sld:ShadedRelief" minOccurs="0"/&gt;
 *       &lt;xs:element ref="sld:ImageOutline" minOccurs="0"/&gt;
 *     &lt;/xs:sequence&gt;
 *   &lt;/xs:complexType&gt;
 * &lt;/xs:element&gt;
 * </code></pre>
 *
 * The following example applies a coloring to elevation (DEM) data (quantities are in meters):
 *
 * <pre>
 * &lt;RasterSymbolizer&gt;
 *    &lt;Opacity&gt;1.0&lt;/Opacity&gt;
 *    &lt;ColorMap&gt;
 *       &lt;ColorMapEntry color="#00ff00" quantity="-500"/&gt;
 *       &lt;ColorMapEntry color="#00fa00" quantity="-417"/&gt;
 *       &lt;ColorMapEntry color="#14f500" quantity="-333"/&gt;
 *       &lt;ColorMapEntry color="#28f502" quantity="-250"/&gt;
 *       &lt;ColorMapEntry color="#3cf505" quantity="-167"/&gt;
 *       &lt;ColorMapEntry color="#50f50a" quantity="-83"/&gt;
 *       &lt;ColorMapEntry color="#64f014" quantity="-1"/&gt;
 *       &lt;ColorMapEntry color="#7deb32" quantity="0"/&gt;
 *       &lt;ColorMapEntry color="#78c818" quantity="30"/&gt;
 *       &lt;ColorMapEntry color="#38840c" quantity="105"/&gt;
 *       &lt;ColorMapEntry color="#2c4b04" quantity="300"/&gt;
 *       &lt;ColorMapEntry color="#ffff00" quantity="400"/&gt;
 *       &lt;ColorMapEntry color="#dcdc00" quantity="700"/&gt;
 *       &lt;ColorMapEntry color="#b47800" quantity="1200"/&gt;
 *       &lt;ColorMapEntry color="#c85000" quantity="1400"/&gt;
 *       &lt;ColorMapEntry color="#be4100" quantity="1600"/&gt;
 *       &lt;ColorMapEntry color="#963000" quantity="2000"/&gt;
 *       &lt;ColorMapEntry color="#3c0200" quantity="3000"/&gt;
 *       &lt;ColorMapEntry color="#ffffff" quantity="5000"/&gt;
 *       &lt;ColorMapEntry color="#ffffff" quantity="13000"/&gt;
 *    &lt;/ColorMap&gt;
 *    &lt;OverlapBehavior&gt;
 *       &lt;AVERAGE/&gt;
 *    &lt;/OverlapBehavior&gt;
 *    &lt;ShadedRelief/&gt;
 * &lt;/RasterSymbolizer&gt;
 * </pre>
 *
 * Here is a rather artificial mutli-band raster symbol:
 *
 * <pre>
 * &lt;RasterSymbolizer&gt;
 *    &lt;Opacity&gt;1.0&lt;/Opacity&gt;
 *    &lt;ColorMap&gt;
 *       &lt;ColorMapEntry color="#000000" quantity="0"/&gt;
 *       &lt;ColorMapEntry color="#ffffff" quantity="255"/&gt;
 *    &lt;/ColorMap&gt;
 *    &lt;ChannelSelection&gt;
 *       &lt;RedChannel&gt;
 *          &lt;SourceChannelName&gt;1&lt;/SourceChannelName&gt;
 *          &lt;ContrastEnhancement&gt;
 *             &lt;Histogram/&gt;
 *          &lt;/ContrastEnhancement&gt;
 *       &lt;/RedChannel&gt;
 *       &lt;GreenChannel&gt;
 *          &lt;SourceChannelName&gt;2&lt;/SourceChannelName&gt;
 *          &lt;ContrastEnhancement&gt;
 *             &lt;GammaValue&gt;2.5&lt;/GammaValue&gt;
 *          &lt;/ContrastEnhancement&gt;
 *       &lt;/GreenChannel&gt;
 *       &lt;BlueChannel&gt;
 *          &lt;SourceChannelName&gt;3&lt;/SourceChannelName&gt;
 *          &lt;ContrastEnhancement&gt;
 *             &lt;Normalize/&gt;
 *          &lt;/ContrastEnhancement&gt;
 *       &lt;/BlueChannel&gt;
 *    &lt;/ChannelSelection&gt;
 *    &lt;OverlapBehavior&gt;
 *       &lt;LATEST_ON_TOP/&gt;
 *    &lt;/OverlapBehavior&gt;
 *    &lt;ContrastEnhancement&gt;
 *       &lt;GammaValue&gt;1.0&lt;/GammaValue&gt;
 *   &lt;/ContrastEnhancement&gt;
 * &lt;/RasterSymbolizer&gt;
 * </PRE>
 *
 * $Id$
 *
 * @author Ian Turton, CCG
 */
public interface RasterSymbolizer extends org.opengis.style.RasterSymbolizer, Symbolizer {
    /**
     * sets the opacity for the coverage, it has the usual meaning.
     *
     * @param opacity An expression which evaluates to the the opacity (0-1)
     */
    void setOpacity(Expression opacity);

    /**
     * The ChannelSelection element specifies the false-color channel selection for a multi-spectral
     * raster source (such as a multi-band satellite-imagery source). Either a channel may be
     * selected to display in each of red, green, and blue, or a single channel may be selected to
     * display in grayscale. (The spelling ?gray? is used since it seems to be more common on the
     * Web than ?grey? by a ratio of about 3:1.) Contrast enhancement may be applied to each channel
     * in isolation. Channels are identified by a system and data-dependent character identifier.
     * Commonly, channels will be labelled as ?1?, ?2?, etc.
     *
     * @param channel the channel selected
     */
    void setChannelSelection(org.opengis.style.ChannelSelection channel);

    /**
     * The ChannelSelection element specifies the false-color channel selection for a multi-spectral
     * raster source (such as a multi-band satellite-imagery source). Either a channel may be
     * selected to display in each of red, green, and blue, or a single channel may be selected to
     * display in grayscale. (The spelling ?gray? is used since it seems to be more common on the
     * Web than ?grey? by a ratio of about 3:1.) Contrast enhancement may be applied to each channel
     * in isolation. Channels are identified by a system and data-dependent character identifier.
     * Commonly, channels will be labelled as ?1?, ?2?, etc.
     *
     * @return the ChannelSelection object set or null if none is available.
     */
    ChannelSelection getChannelSelection();

    /**
     * The OverlapBehavior element tells a system how to behave when multiple raster images in a
     * layer overlap each other, for example with satellite-image scenes. LATEST_ON_TOP and
     * EARLIEST_ON_TOP refer to the time the scene was captured. AVERAGE means to average multiple
     * scenes together. This can produce blurry results if the source images are not perfectly
     * aligned in their geo-referencing. RANDOM means to select an image (or piece thereof) randomly
     * and place it on top. This can produce crisper results than AVERAGE potentially more
     * efficiently than LATEST_ON_TOP or EARLIEST_ON_TOP. The default behaviour is system-dependent.
     *
     * @param overlap the expression which evaluates to LATEST_ON_TOP, EARLIEST_ON_TOP, AVERAGE or
     *     RANDOM
     */
    void setOverlap(Expression overlap);

    /**
     * The OverlapBehavior element tells a system how to behave when multiple raster images in a
     * layer overlap each other, for example with satellite-image scenes. LATEST_ON_TOP and
     * EARLIEST_ON_TOP refer to the time the scene was captured. AVERAGE means to average multiple
     * scenes together. This can produce blurry results if the source images are not perfectly
     * aligned in their geo-referencing. RANDOM means to select an image (or piece thereof) randomly
     * and place it on top. This can produce crisper results than AVERAGE potentially more
     * efficiently than LATEST_ON_TOP or EARLIEST_ON_TOP. The default behaviour is system-dependent.
     *
     * @return The expression which evaluates to LATEST_ON_TOP, EARLIEST_ON_TOP, AVERAGE or RANDOM
     */
    Expression getOverlap();

    /** Set the overlap behavior. */
    void setOverlapBehavior(OverlapBehavior overlapBehavior);

    /**
     * The ColorMap element defines either the colors of a palette-type raster source or the mapping
     * of fixed-numeric pixel values to colors. For example, a DEM raster giving elevations in
     * meters above sea level can be translated to a colored image with a ColorMap. The quantity
     * attributes of a color-map are used for translating between numeric matrixes and color rasters
     * and the ColorMap entries should be in order of increasing numeric quantity so that
     * intermediate numeric values can be matched to a color (or be interpolated between two
     * colors). Labels may be used for legends or may be used in the future to match character
     * values. Not all systems can support opacity in colormaps. The default opacity is 1.0 (fully
     * opaque). Defaults for quantity and label are system-dependent.
     *
     * @param colorMap the ColorMap for the raster
     */
    void setColorMap(org.opengis.style.ColorMap colorMap);

    /**
     * The ColorMap element defines either the colors of a palette-type raster source or the mapping
     * of fixed-numeric pixel values to colors. For example, a DEM raster giving elevations in
     * meters above sea level can be translated to a colored image with a ColorMap. The quantity
     * attributes of a color-map are used for translating between numeric matrixes and color rasters
     * and the ColorMap entries should be in order of increasing numeric quantity so that
     * intermediate numeric values can be matched to a color (or be interpolated between two
     * colors). Labels may be used for legends or may be used in the future to match character
     * values. Not all systems can support opacity in colormaps. The default opacity is 1.0 (fully
     * opaque). Defaults for quantity and label are system-dependent.
     *
     * @return the ColorMap for the raster
     */
    ColorMap getColorMap();

    /**
     * The ContrastEnhancement element defines contrast enhancement for a channel of a false-color
     * image or for a color image. In the case of a color image, the relative grayscale brightness
     * of a pixel color is used. ?Normalize? means to stretch the contrast so that the dimmest color
     * is stretched to black and the brightest color is stretched to white, with all colors in
     * between stretched out linearly. ?Histogram? means to stretch the contrast based on a
     * histogram of how many colors are at each brightness level on input, with the goal of
     * producing equal number of pixels in the image at each brightness level on output. This has
     * the effect of revealing many subtle ground features. A ?GammaValue? tells how much to
     * brighten (value greater than 1.0) or dim (value less than 1.0) an image. The default
     * GammaValue is 1.0 (no change). If none of Normalize, Histogram, or GammaValue are selected in
     * a ContrastEnhancement, then no enhancement is performed.
     *
     * @param ce the contrastEnhancement
     */
    void setContrastEnhancement(org.opengis.style.ContrastEnhancement ce);

    /**
     * The ContrastEnhancement element defines contrast enhancement for a channel of a false-color
     * image or for a color image. In the case of a color image, the relative grayscale brightness
     * of a pixel color is used. ?Normalize? means to stretch the contrast so that the dimmest color
     * is stretched to black and the brightest color is stretched to white, with all colors in
     * between stretched out linearly. ?Histogram? means to stretch the contrast based on a
     * histogram of how many colors are at each brightness level on input, with the goal of
     * producing equal number of pixels in the image at each brightness level on output. This has
     * the effect of revealing many subtle ground features. A ?GammaValue? tells how much to
     * brighten (value greater than 1.0) or dim (value less than 1.0) an image. The default
     * GammaValue is 1.0 (no change). If none of Normalize, Histogram, or GammaValue are selected in
     * a ContrastEnhancement, then no enhancement is performed.
     *
     * @return the ContrastEnhancement
     */
    ContrastEnhancement getContrastEnhancement();

    /**
     * The ShadedRelief element selects the application of relief shading (or ?hill shading?) to an
     * image for a three-dimensional visual effect. It is defined as: Exact parameters of the
     * shading are system-dependent (for now). If the BrightnessOnly flag is ?0? (false, default),
     * the shading is applied to the layer being rendered as the current RasterSymbol. If
     * BrightnessOnly is ?1? (true), the shading is applied to the brightness of the colors in the
     * rendering canvas generated so far by other layers, with the effect of relief-shading these
     * other layers. The default for BrightnessOnly is ?0? (false). The ReliefFactor gives the
     * amount of exaggeration to use for the height of the ?hills.? A value of around 55 (times)
     * gives reasonable results for Earth-based DEMs. The default value is system-dependent.
     *
     * @param relief the shadedrelief object
     */
    void setShadedRelief(org.opengis.style.ShadedRelief relief);

    /**
     * The ShadedRelief element selects the application of relief shading (or ?hill shading?) to an
     * image for a three-dimensional visual effect. It is defined as: Exact parameters of the
     * shading are system-dependent (for now). If the BrightnessOnly flag is ?0? (false, default),
     * the shading is applied to the layer being rendered as the current RasterSymbol. If
     * BrightnessOnly is ?1? (true), the shading is applied to the brightness of the colors in the
     * rendering canvas generated so far by other layers, with the effect of relief-shading these
     * other layers. The default for BrightnessOnly is ?0? (false). The ReliefFactor gives the
     * amount of exaggeration to use for the height of the ?hills.? A value of around 55 (times)
     * gives reasonable results for Earth-based DEMs. The default value is system-dependent.
     *
     * @return the shadedrelief object
     */
    ShadedRelief getShadedRelief();

    /**
     * The ImageOutline element specifies that individual source rasters in a multi-raster set (such
     * as a set of satellite-image scenes) should be outlined with either a LineStringSymbol or
     * PolygonSymbol. It is defined as:
     *
     * <pre>
     * &lt;xs:element name="ImageOutline"&gt;
     *   &lt;xs:complexType&gt;
     *     &lt;xs:choice&gt;
     *       &lt;xs:element ref="sld:LineSymbolizer"/&gt;
     *       &lt;xs:element ref="sld:PolygonSymbolizer"/&gt;
     *     &lt;/xs:choice&gt;
     *   &lt;/xs:complexType&gt;
     * &lt;/xs:element&gt;
     * </pre>
     *
     * An Opacity of 0.0 can be selected for the main raster to avoid rendering the main-raster
     * pixels, or an opacity can be used for a PolygonSymbolizer Fill to allow the main-raster data
     * be visible through the fill.
     *
     * @param symbolizer the symbolizer to be used. If this is <B>not</B> a polygon or a line
     *     symbolizer an unexpected argument exception may be thrown by an implementing class.
     */
    void setImageOutline(org.opengis.style.Symbolizer symbolizer);

    /**
     * The ImageOutline element specifies that individual source rasters in a multi-raster set (such
     * as a set of satellite-image scenes) should be outlined with either a LineStringSymbol or
     * PolygonSymbol. It is defined as:
     *
     * <pre>
     * &lt;xs:element name="ImageOutline"&gt;
     *   &lt;xs:complexType&gt;
     *     &lt;xs:choice&gt;
     *       &lt;xs:element ref="sld:LineSymbolizer"/&gt;
     *       &lt;xs:element ref="sld:PolygonSymbolizer"/&gt;
     *     &lt;/xs:choice&gt;
     *   &lt;/xs:complexType&gt;
     * &lt;/xs:element&gt;
     * </pre>
     *
     * An Opacity of 0.0 can be selected for the main raster to avoid rendering the main-raster
     * pixels, or an opacity can be used for a PolygonSymbolizer Fill to allow the main-raster data
     * be visible through the fill.
     *
     * @return The relevent symbolizer
     */
    Symbolizer getImageOutline();
}
