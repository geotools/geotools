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
 * Created on 13 November 2002, 13:47
 */
package org.geotools.styling;

import javax.measure.quantity.Length;
import javax.measure.unit.Unit;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.style.OverlapBehavior;
import org.opengis.style.StyleVisitor;

/**
 * Default implementation of RasterSymbolizer.
 *
 * @author iant
 * @author Johann Sorel (Geomatys)
 *
 * @source $URL$
 */
public class RasterSymbolizerImpl extends AbstractSymbolizer implements RasterSymbolizer {
    
    private OverlapBehavior behavior;
    
    // TODO: make container ready
    private FilterFactory filterFactory;
    private ChannelSelection channelSelection = new ChannelSelectionImpl();
    private ColorMapImpl colorMap = new ColorMapImpl();
    private ContrastEnhancementImpl contrastEnhancement = new ContrastEnhancementImpl();
    private ShadedReliefImpl shadedRelief;
    private String geometryName = "geom";
    private Symbolizer symbolizer;
    private Expression opacity;
    private Expression overlap;

    public RasterSymbolizerImpl(){
        this( CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints()));
    }

    public RasterSymbolizerImpl(FilterFactory factory) {
        this(factory,null,null,null,null);   
    }
    
    public RasterSymbolizerImpl(FilterFactory factory, Description desc, String name, Unit<Length> uom, OverlapBehavior behavior) {
        super(name, desc, "grid", uom);
        this.filterFactory = factory;
        this.opacity = filterFactory.literal(1.0);
        this.overlap = filterFactory.literal(OverlapBehavior.RANDOM);
        this.behavior = behavior;
    }
    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((behavior == null) ? 0 : behavior.hashCode());
        result = prime * result + ((channelSelection == null) ? 0 : channelSelection.hashCode());
        result = prime * result + ((colorMap == null) ? 0 : colorMap.hashCode());
        result = prime * result
                + ((contrastEnhancement == null) ? 0 : contrastEnhancement.hashCode());
        result = prime * result + ((filterFactory == null) ? 0 : filterFactory.hashCode());
        result = prime * result + ((opacity == null) ? 0 : opacity.hashCode());
        result = prime * result + ((overlap == null) ? 0 : overlap.hashCode());
        result = prime * result + ((shadedRelief == null) ? 0 : shadedRelief.hashCode());
        result = prime * result + ((symbolizer == null) ? 0 : symbolizer.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        RasterSymbolizerImpl other = (RasterSymbolizerImpl) obj;
        if (behavior == null) {
            if (other.behavior != null)
                return false;
        } else if (!behavior.equals(other.behavior))
            return false;
        if (channelSelection == null) {
            if (other.channelSelection != null)
                return false;
        } else if (!channelSelection.equals(other.channelSelection))
            return false;
        if (colorMap == null) {
            if (other.colorMap != null)
                return false;
        } else if (!colorMap.equals(other.colorMap))
            return false;
        if (contrastEnhancement == null) {
            if (other.contrastEnhancement != null)
                return false;
        } else if (!contrastEnhancement.equals(other.contrastEnhancement))
            return false;
        if (filterFactory == null) {
            if (other.filterFactory != null)
                return false;
        } else if (!filterFactory.equals(other.filterFactory))
            return false;
        if (opacity == null) {
            if (other.opacity != null)
                return false;
        } else if (!opacity.equals(other.opacity))
            return false;
        if (overlap == null) {
            if (other.overlap != null)
                return false;
        } else if (!overlap.equals(other.overlap))
            return false;
        if (shadedRelief == null) {
            if (other.shadedRelief != null)
                return false;
        } else if (!shadedRelief.equals(other.shadedRelief))
            return false;
        if (symbolizer == null) {
            if (other.symbolizer != null)
                return false;
        } else if (!symbolizer.equals(other.symbolizer))
            return false;
        return true;
    }

    /**
     * The ChannelSelection element specifies the false-color channel selection
     * for a multi-spectral raster source (such as a multi-band
     * satellite-imagery source). Either a channel may be selected to display
     * in each of red, green, and blue, or a single channel may be selected to
     * display in grayscale.  (The spelling ?gray? is used since it seems to
     * be more common on the Web than ?grey? by a ratio of about 3:1.)
     * Contrast enhancement may be applied to each channel in isolation.
     * Channels are identified by a system and data-dependent character
     * identifier.  Commonly, channels will be labelled as ?1?, ?2?, etc.
     *
     * @return the ChannelSelection object set or null if none is available.
     */
    public ChannelSelection getChannelSelection() {
        return channelSelection;
    }

    /**
     * The ColorMap element defines either the colors of a palette-type raster
     * source or the mapping of fixed-numeric pixel values to colors. For
     * example, a DEM raster giving elevations in meters above sea level can
     * be translated to a colored image with a ColorMap.  The quantity
     * attributes of a color-map are used for translating between numeric
     * matrixes and color rasters and the ColorMap entries should be in order
     * of increasing numeric quantity so that intermediate numeric values can
     * be matched to a color (or be interpolated between two colors). Labels
     * may be used for legends or may be used in the future to match character
     * values. Not all systems can support opacity in colormaps.  The default
     * opacity is 1.0 (fully opaque). Defaults for quantity and label are
     * system-dependent.
     *
     * @return the ColorMap for the raster
     */
    public ColorMapImpl getColorMap() {
        return colorMap;
    }

    /**
     * The ContrastEnhancement element defines contrast enhancement for a
     * channel of a false-color image or for a color image. In the case of a
     * color image, the relative grayscale brightness of a pixel color is
     * used. ?Normalize? means to stretch the contrast so that the dimmest
     * color is stretched to black and the brightest color is stretched to
     * white, with all colors in between stretched out linearly. ?Histogram?
     * means to stretch the contrast based on a histogram of how many colors
     * are at each brightness level on input, with the goal of producing equal
     * number of pixels in the image at each brightness level on output.  This
     * has the effect of revealing many subtle ground features. A ?GammaValue?
     * tells how much to brighten (value greater than 1.0) or dim (value less
     * than 1.0) an image. The default GammaValue is 1.0 (no change). If none
     * of Normalize, Histogram, or GammaValue are selected in a
     * ContrastEnhancement, then no enhancement is performed.
     *
     * @return the ContrastEnhancement
     */
    public ContrastEnhancementImpl getContrastEnhancement() {
        return contrastEnhancement;
    }


    /**
     * The ImageOutline element specifies that individual source rasters in a
     * multi-raster set (such as a set of satellite-image scenes) should be
     * outlined with either a LineStringSymbol or PolygonSymbol. It is defined
     * as:
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
     * An Opacity of 0.0 can be selected for the main raster to avoid rendering
     * the main-raster pixels, or an opacity can be used for a
     * PolygonSymbolizer Fill to allow the main-raster data be visible through
     * the fill.
     *
     * @return The relevent symbolizer
     */
    public Symbolizer getImageOutline() {
        return symbolizer;
    }

    /**
     * fetch the expresion which evaluates to the opacity fo rthis coverage
     *
     * @return The expression
     */
    public Expression getOpacity() {
        return opacity;
    }

    /**
     * The OverlapBehavior element tells a system how to behave when multiple
     * raster images in a layer overlap each other, for example with
     * satellite-image scenes. LATEST_ON_TOP and EARLIEST_ON_TOP refer to the
     * time the scene was captured. AVERAGE means to average multiple scenes
     * together. This can produce blurry results if the source images are not
     * perfectly aligned in their geo-referencing. RANDOM means to select an
     * image (or piece thereof) randomly and place it on top.  This can
     * produce crisper results than AVERAGE potentially more efficiently than
     * LATEST_ON_TOP or EARLIEST_ON_TOP. The default behaviour is
     * system-dependent.
     *
     * @return The expression which evaluates to LATEST_ON_TOP,
     *         EARLIEST_ON_TOP, AVERAGE or RANDOM
     */
    @Deprecated
    public Expression getOverlap() {
        return overlap;
    }
    
    public OverlapBehavior getOverlapBehavior() {
        return behavior;
    }
    
    public void setOverlapBehavior(OverlapBehavior overlapBehavior) {
        this.behavior = overlapBehavior;
    }

    /**
     * The ShadedRelief element selects the application of relief shading (or
     * ?hill shading?) to an image for a three-dimensional visual effect.  It
     * is defined as: Exact parameters of the shading are system-dependent
     * (for now).  If the BrightnessOnly flag is ?0? (false, default), the
     * shading is applied to the layer being rendered as the current
     * RasterSymbol. If BrightnessOnly is ?1? (true), the shading is applied
     * to the brightness of the colors in the rendering canvas generated so
     * far by other layers, with the effect of relief-shading these other
     * layers. The default for BrightnessOnly is ?0? (false).  The
     * ReliefFactor gives the amount of exaggeration to use for the height of
     * the ?hills.?  A value of around 55 (times) gives reasonable results for
     * Earth-based DEMs. The default value is system-dependent.
     *
     * @return the shadedrelief object
     */
    public ShadedReliefImpl getShadedRelief() {
        return shadedRelief;
    }

    /**
     * The ChannelSelection element specifies the false-color channel selection
     * for a multi-spectral raster source (such as a multi-band
     * satellite-imagery source). Either a channel may be selected to display
     * in each of red, green, and blue, or a single channel may be selected to
     * display in grayscale.  (The spelling ?gray? is used since it seems to
     * be more common on the Web than ?grey? by a ratio of about 3:1.)
     * Contrast enhancement may be applied to each channel in isolation.
     * Channels are identified by a system and data-dependent character
     * identifier.  Commonly, channels will be labelled as ?1?, ?2?, etc.
     *
     * @param channel the channel selected
     */
    public void setChannelSelection(org.opengis.style.ChannelSelection channel) {
        if (this.channelSelection == channel) {
            return;
        }
        this.channelSelection = ChannelSelectionImpl.cast( channel );
    }

    /**
     * The ColorMap element defines either the colors of a palette-type raster
     * source or the mapping of fixed-numeric pixel values to colors. For
     * example, a DEM raster giving elevations in meters above sea level can
     * be translated to a colored image with a ColorMap.  The quantity
     * attributes of a color-map are used for translating between numeric
     * matrixes and color rasters and the ColorMap entries should be in order
     * of increasing numeric quantity so that intermediate numeric values can
     * be matched to a color (or be interpolated between two colors). Labels
     * may be used for legends or may be used in the future to match character
     * values. Not all systems can support opacity in colormaps.  The default
     * opacity is 1.0 (fully opaque). Defaults for quantity and label are
     * system-dependent.
     *
     * @param colorMap the ColorMap for the raster
     */
    public void setColorMap(org.opengis.style.ColorMap colorMap) {
        if (this.colorMap == colorMap) {
            return;
        }
        this.colorMap = ColorMapImpl.cast( colorMap );
    }

    /**
     * The ContrastEnhancement element defines contrast enhancement for a
     * channel of a false-color image or for a color image. In the case of a
     * color image, the relative grayscale brightness of a pixel color is
     * used. ?Normalize? means to stretch the contrast so that the dimmest
     * color is stretched to black and the brightest color is stretched to
     * white, with all colors in between stretched out linearly. ?Histogram?
     * means to stretch the contrast based on a histogram of how many colors
     * are at each brightness level on input, with the goal of producing equal
     * number of pixels in the image at each brightness level on output.  This
     * has the effect of revealing many subtle ground features. A ?GammaValue?
     * tells how much to brighten (value greater than 1.0) or dim (value less
     * than 1.0) an image. The default GammaValue is 1.0 (no change). If none
     * of Normalize, Histogram, or GammaValue are selected in a
     * ContrastEnhancement, then no enhancement is performed.
     *
     * @param contrastEnhancement the contrastEnhancement
     */
    public void setContrastEnhancement(org.opengis.style.ContrastEnhancement contrastEnhancement) {
        if (this.contrastEnhancement == contrastEnhancement) {
            return;
        }
        this.contrastEnhancement = ContrastEnhancementImpl.cast( contrastEnhancement );
    }

    /**
     * The ImageOutline element specifies that individual source rasters in a
     * multi-raster set (such as a set of satellite-image scenes) should be
     * outlined with either a LineStringSymbol or PolygonSymbol. It is defined
     * as:
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
     * An Opacity of 0.0 can be selected for the main raster to avoid rendering
     * the main-raster pixels, or an opacity can be used for a
     * PolygonSymbolizer Fill to allow the main-raster data be visible through
     * the fill.
     *
     * @param symbolizer the symbolizer to be used. If this is <B>not</B> a
     *        polygon or a line symbolizer an unexpected argument exception
     *        may be thrown by an implementing class.
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setImageOutline(org.opengis.style.Symbolizer symbolizer) {
        if( symbolizer == null ){
            this.symbolizer = null;
        }
        else if (symbolizer instanceof LineSymbolizer
                || symbolizer instanceof PolygonSymbolizer) {
            if (this.symbolizer == symbolizer) {
                return;
            }
            this.symbolizer = StyleFactoryImpl2.cast( symbolizer );
        } else {
            throw new IllegalArgumentException(
                "Only a line or polygon symbolizer may be used to outline a raster");
        }
    }

    /**
     * sets the opacity for the coverage, it has the usual meaning.
     *
     * @param opacity An expression which evaluates to the the opacity (0-1)
     */
    public void setOpacity(Expression opacity) {
        if (this.opacity == opacity) {
            return;
        }
        this.opacity = opacity;
    }

    /**
     * The OverlapBehavior element tells a system how to behave when multiple
     * raster images in a layer overlap each other, for example with
     * satellite-image scenes. LATEST_ON_TOP and EARLIEST_ON_TOP refer to the
     * time the scene was captured. AVERAGE means to average multiple scenes
     * together. This can produce blurry results if the source images are not
     * perfectly aligned in their geo-referencing. RANDOM means to select an
     * image (or piece thereof) randomly and place it on top.  This can
     * produce crisper results than AVERAGE potentially more efficiently than
     * LATEST_ON_TOP or EARLIEST_ON_TOP. The default behaviour is
     * system-dependent.
     *
     * @param overlap the expression which evaluates to LATEST_ON_TOP,
     *        EARLIEST_ON_TOP, AVERAGE or RANDOM
     */
    @Deprecated
    public void setOverlap(Expression overlap) {
        if (this.overlap == overlap) {
            return;
        }
        this.overlap = overlap;
    }

    /**
     * The ShadedRelief element selects the application of relief shading (or
     * ?hill shading?) to an image for a three-dimensional visual effect.  It
     * is defined as: Exact parameters of the shading are system-dependent
     * (for now).  If the BrightnessOnly flag is ?0? (false, default), the
     * shading is applied to the layer being rendered as the current
     * RasterSymbol. If BrightnessOnly is ?1? (true), the shading is applied
     * to the brightness of the colors in the rendering canvas generated so
     * far by other layers, with the effect of relief-shading these other
     * layers. The default for BrightnessOnly is ?0? (false).  The
     * ReliefFactor gives the amount of exaggeration to use for the height of
     * the ?hills.?  A value of around 55 (times) gives reasonable results for
     * Earth-based DEMs. The default value is system-dependent.
     *
     * @param shadedRelief the shadedrelief object
     */
    public void setShadedRelief(org.opengis.style.ShadedRelief shadedRelief) {
        if (this.shadedRelief == shadedRelief) {
            return;
        }
        this.shadedRelief = ShadedReliefImpl.cast( shadedRelief );
    }

    public Object accept(StyleVisitor visitor,Object data) {
        return visitor.visit(this,data);
    }
        
    public void accept(org.geotools.styling.StyleVisitor visitor) {
        visitor.visit(this);
    }
    

    /**
     * Creates a deep copy clone.   TODO: Need to complete the deep copy,
     * currently only shallow copy.
     *
     * @return The deep copy clone.
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public Object clone() {
        Object clone;

        try {
            clone = super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e); // this should never happen.
        }

        return clone;
    }

    static RasterSymbolizerImpl cast(org.opengis.style.Symbolizer symbolizer) {
        if( symbolizer == null ){
            return null;
        }
        if( symbolizer instanceof RasterSymbolizerImpl ){
            return (RasterSymbolizerImpl) symbolizer;
        }
        else if (symbolizer instanceof org.opengis.style.RasterSymbolizer ){
            org.opengis.style.RasterSymbolizer rasterSymbolizer = (org.opengis.style.RasterSymbolizer) symbolizer;
            RasterSymbolizerImpl copy = new RasterSymbolizerImpl();
            copy.setChannelSelection( rasterSymbolizer.getChannelSelection());
            copy.setColorMap( rasterSymbolizer.getColorMap() );
            copy.setContrastEnhancement( rasterSymbolizer.getContrastEnhancement() );
            copy.setDescription( rasterSymbolizer.getDescription());
            copy.setGeometryPropertyName( rasterSymbolizer.getGeometryPropertyName() );
            copy.setImageOutline( rasterSymbolizer.getImageOutline() );
            copy.setName( rasterSymbolizer.getName());
            copy.setOpacity( rasterSymbolizer.getOpacity());
            copy.setOverlapBehavior( rasterSymbolizer.getOverlapBehavior() );
            copy.setShadedRelief( rasterSymbolizer.getShadedRelief());
            copy.setUnitOfMeasure( rasterSymbolizer.getUnitOfMeasure());
            
            return copy;
        }
        return null; // must not be a raster symbolizer
    }

}
