package org.geotools.map;

import org.geotools.map.event.MapLayerEvent;
import org.geotools.styling.Style;

/**
 * Layer responsible for rendering under control of a user supplied Style object.
 * <p>
 * The StyleLayer is expected to be subclassed; and is responsible for:
 * <ul>
 * <li>style: Style</li>
 * </ul>
 * Please note that a StyleLayerDescriptor (defined by SLD) document is usually used to describe the
 * rendering requirements for an entire Map; while a Style (defined by SE) is focused on a single
 * layer of content
 */
public abstract class StyleLayer extends Layer {
    /** Style used for rendering */
    protected Style style;

    /**
     * Creates a new instance of StyleLayer
     * 
     * @param style
     *            the style used to control drawing of this layer
     */
    public StyleLayer(Style style) {
        this.style = style;
    }

    public StyleLayer(Style style, String title) {
        this.style = style;
        setTitle(title);
    }

    @Override
    public void dispose() {
        style = null;
        super.dispose();
    }

    /**
     * Get the style for this layer. If style has not been set, then null is returned.
     * 
     * @return The style (SLD).
     */
    public Style getStyle() {
        return style;
    }

    /**
     * Sets the style for this layer. If a style has not been defined a default one is used.
     * 
     * @param style
     *            The new style
     */
    public void setStyle(Style style) {
        if (style == null) {
            throw new NullPointerException("Style is required");
        }
        this.style = style;
        fireMapLayerListenerLayerChanged(MapLayerEvent.STYLE_CHANGED);
    }

}
