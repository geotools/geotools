/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbstyle.layer;

import java.util.Collections;
import java.util.List;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.Rule;
import org.geotools.api.style.SemanticType;
import org.geotools.api.style.StyleFactory;
import org.geotools.mbstyle.MBStyle;
import org.geotools.mbstyle.parse.MBFilter;
import org.geotools.mbstyle.parse.MBFormatException;
import org.geotools.mbstyle.parse.MBObjectParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * MBLayer wrapper (around one of the MBStyle layers).
 *
 * <p>All methods act as accessors on provided JSON layer, no other state is maintained. This allows modifications to be
 * made cleanly with out chance of side-effect.
 *
 * <ul>
 *   <li>get methods: access the json directly
 *   <li>query methods: provide logic / transforms to GeoTools classes as required.
 * </ul>
 *
 * <p>In the normal course of events MBLayer is constructed as a flyweight object by MBStyle to provide easy access to
 * its layers list.
 *
 * @author Torben Barsballe (Boundless)
 */
public abstract class MBLayer {
    /** Helper class used to provide json access and expression / filter conversions */
    protected final MBObjectParser parse;

    /** Shared filter factory */
    protected final FilterFactory ff;

    /** Shared style factory */
    protected final StyleFactory sf;

    /** JSON layer being wrapped. */
    protected final JSONObject json;

    /** field access for "id" key (due to its use in error messages) */
    protected final String id;

    /**
     * Whether this layer is displayed.
     *
     * <p>Optional enum. One of visible, none. Defaults to visible.
     */
    public enum Visibility {
        /** The layer is shown. */
        VISIBLE,
        /** The layer is not shown. */
        NONE
    }

    public MBLayer(JSONObject json, MBObjectParser parse) {
        this.json = json;
        this.parse = parse;
        this.ff = parse.getFilterFactory();
        this.sf = parse.getStyleFactory();

        this.id = (String) json.get("id");
    }
    //
    // Factory methods
    //

    /**
     * Factory method creating the appropriate MBStyle based on the "type" indicated in the layer JSON.
     *
     * @param layer JSON Layer definition
     * @return Generated MBLayer subclass
     */
    public static MBLayer create(JSONObject layer) {
        if (layer.containsKey("type") && layer.get("type") instanceof String) {
            String type = (String) layer.get("type");
            switch (type) {
                case "line":
                    return new LineMBLayer(layer);
                case "fill":
                    return new FillMBLayer(layer);
                case "raster":
                    return new RasterMBLayer(layer);
                case "circle":
                    return new CircleMBLayer(layer);
                case "background":
                    return new BackgroundMBLayer(layer);
                case "symbol":
                    return new SymbolMBLayer(layer);
                case "fill-extrusion":
                    return new FillExtrusionMBLayer(layer);
                default:
                    throw new MBFormatException("\"type\" "
                            + type
                            + " is not a valid layer type. Must be one of: "
                            + "background, fill, line, symbol, raster, circle, fill-extrusion");
            }
        }
        // technically we may be able to do this via a ref
        throw new MBFormatException("\"type\" required to create layer.");
    }

    /**
     * Rendering type of this layer.
     *
     * <p>One of:
     *
     * <ul>
     *   <li>fill:A filled polygon with an optional stroked border.
     *   <li>line: A stroked line.
     *   <li>symbol: An icon or a text label.
     *   <li>circle:A filled circle.
     *   <li>fill-extrusion: An extruded (3D) polygon.
     *   <li>raster: Raster map textures such as satellite imagery.
     *   <li>background: The background color or pattern of the map.
     * </ul>
     *
     * @return One of fill, line, symbol, circle, fill-extrusion, raster, background.
     */
    public abstract String getType();

    /**
     * Arbitrary properties useful to track with the layer, but do not influence rendering. Properties should be
     * prefixed to avoid collisions, like 'mapbox:' and 'gt:`.
     *
     * @return Arbitrary properties useful to track with the layer.
     */
    public JSONObject getMetadata() {
        return parse.getJSONObject(json, "metadata", new JSONObject());
    }

    /**
     * References another layer to copy type, source, source-layer, minzoom, maxzoom, filter, and layout properties
     * from. This allows the layers to share processing and be more efficient.
     *
     * @return References another layer to copy type, source, source-layer, minzoom, maxzoom, filter, and layout
     *     properties from.
     */
    public String getRef() {
        // We should update getType(), getSource(), getSourceLayer(), getMinZoom(), getMaxZoom(),
        // getFilter() to look up value provided by getRef() if needed.
        return parse.optional(String.class, json, "ref", null);
    }

    public JSONObject getJson() {
        return json;
    }

    /**
     * Unique layer name.
     *
     * @return layer name, required field
     */
    public String getId() {
        return id;
    }

    /**
     * Name of a source description to be used for this layer.
     *
     * <p>While this value is optional, it may be obtained via {@link #getRef()} if needed.
     *
     * @return name of source description to be used for this layer, or null if the style has no source.
     */
    public String getSource() {
        return parse.optional(String.class, json, "source", null);
    }

    /**
     * Layer to use from a vector tile source. Required if the source supports multiple layers.
     *
     * <p>While this value is optional, it may be obtained via {@link #getRef()} if needed.
     *
     * @return layer to use from a vector tile source, or null if the style has no source-layer.
     */
    public String getSourceLayer() {
        return parse.optional(String.class, json, "source-layer", null);
    }

    /**
     * The minimum zoom level on which the layer gets parsed and appears on.
     *
     * @return minimum zoom level, or Integer.MIN_VALUE if the style has no minzoom.
     */
    public int getMinZoom() {
        Integer min = parse.optional(Integer.class, json, "minzoom", null);
        return min == null ? Integer.MIN_VALUE : min;
    }

    /**
     * The maximum zoom level on which the layer gets parsed and appears on.
     *
     * @return maximum zoom level, or Integer.MAX_VALUE if the style has no maxzoom.
     */
    public int getMaxZoom() {
        Integer max = parse.optional(Integer.class, json, "maxzoom", null);
        return max == null ? Integer.MAX_VALUE : max;
    }

    /**
     * A MBFilter wrapping optional json specifying conditions on source features. Only features that match the filter
     * are displayed. This is available as a GeoTools {@link Filter} via {@link #filter()}.
     *
     * @return MBFilter expression specifying conditions on source features.
     */
    public MBFilter getFilter() {
        JSONArray array = parse.getJSONArray(json, "filter", null);
        MBFilter filter = new MBFilter(array, parse, defaultSemanticType());
        return filter;
    }

    /**
     * Default {@link SemanticType} to use when generating {@link #getFilter()}.
     *
     * <p>Use ANY to match all geometry, or fill in LINE, POINT, POLYGON if needed.
     *
     * @return Appropriate LINE, POINT, POLYGON value, or ANY to match any geometry.
     */
    abstract SemanticType defaultSemanticType();

    /**
     * The "filter" as a GeoTools {@link Filter} suitable for feature selection, as defined by {@link #getFilter()}.
     *
     * @return Filter, or Filter.INCLUDE if the style has no filter.
     */
    public Filter filter() {
        MBFilter mbFilter = getFilter();
        if (mbFilter == null) {
            return Filter.INCLUDE;
        }
        return mbFilter.filter();
    }

    /**
     * Layout properties for the layer.
     *
     * <p><em>Layout properties</em> appear in the layer's "layout" object. They are applied early in the rendering
     * process and define how data for that layer is passed to the renderer. For efficiency, a layer can share layout
     * properties with another layer via the "ref" layer property, and should do so where possible. This will decrease
     * processing time and allow the two layers will share GPU memory and other resources associated with the layer.
     *
     * @return Layout properties defined for layer, or an empty {@link JSONObject} if no layout properties are defined
     *     for the style.
     */
    public JSONObject getLayout() {
        return parse.layout(json);
    }

    /**
     * Query for layout information (making use of {@link #getRef()} if available).
     *
     * @return Layout properties to use for this layer.
     */
    public JSONObject layout() {
        return getLayout();
    }

    /**
     * Default paint properties for this layer.
     *
     * <p><em>Paint properties</em> are applied later in the rendering process. A layer that shares layout properties
     * with another layer can have independent paint properties. Paint properties appear in the layer's "paint" object.
     *
     * @return Default paint properties for this layer, or an empty {@link JSONObject} if no paint properties are
     *     defined for the style.
     */
    public JSONObject getPaint() {
        return parse.paint(json);
    }

    /**
     * Layout setting - whether this layer is displayed.
     *
     * @return One of visible, none. Defaults to visible if not defined in the style.
     */
    public Visibility getVisibility() {
        JSONObject layout = layout();
        return parse.getEnum(layout, "visibility", Visibility.class, Visibility.VISIBLE);
    }

    /**
     * Whether this layer is displayed.
     *
     * @return Whether the layout is visible. Defaults to true.
     */
    public boolean visibility() {
        return getVisibility() == Visibility.VISIBLE;
    }

    /**
     * Query for paint information (making use of {@link #getRef()} if available).
     *
     * @return Paint properties to use for this layer.
     */
    public JSONObject paint() {
        return getPaint();
    }

    /**
     * Transforms a given {@link MBLayer} to a GeoTools {@link FeatureTypeStyle}.
     *
     * @param styleContext The MBStyle to which this layer belongs, used to resolving sprite and glyph names to full
     *     urls.
     * @param minScaleDenominator Used to determine zoom level restrictions for generated rules
     * @param maxScaleDenominator Used to determine zoom level restrictions for generated rules
     * @return A feature type style from the provided layer, or null if the visibility of that layer is false.
     */
    public List<FeatureTypeStyle> transform(
            MBStyle styleContext, Double minScaleDenominator, Double maxScaleDenominator) {
        // Would prefer to accept zoom levels here (less concepts in our API)
        // If we accept zoom levels we may be able to reduce, and return a list of FeatureTypeStyles
        // (with the understanding that the list may be empty if the MBLayer does not contribute any
        // content
        //  at a specific zoom level range)
        List<FeatureTypeStyle> style = transform(styleContext);
        if (style == null) {
            return Collections.emptyList();
        }
        for (FeatureTypeStyle fts : style) {
            for (Rule rule : fts.rules()) {
                if (minScaleDenominator != null) {
                    rule.setMinScaleDenominator(minScaleDenominator);
                }
                if (maxScaleDenominator != null) {
                    rule.setMaxScaleDenominator(maxScaleDenominator);
                }
            }
        }
        return style;
    }
    /**
     * Transforms a given {@link MBLayer} to a GeoTools {@link FeatureTypeStyle}.
     *
     * @param styleContext The MBStyle to which this layer belongs, used as a context for things like resolving sprite
     *     and glyph names to full urls.
     * @return A feature type style from the provided layer, or null if the visibility of that layer is false.
     */
    public final List<FeatureTypeStyle> transform(MBStyle styleContext) {
        MBLayer layer = this;
        if (!layer.visibility()) {
            return null; // layer layout visibility 'none'
        }
        return transformInternal(styleContext);
    }

    public abstract List<FeatureTypeStyle> transformInternal(MBStyle styleContext);

    //
    // Data Object based on wrapped json
    //

    /** Hashcode based on wrapped {@link #json}. */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (json == null ? 0 : json.hashCode());
        return result;
    }

    /** Equality based on wrapped {@link #json}. */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        MBLayer other = (MBLayer) obj;
        if (json == null) {
            if (other.json != null) {
                return false;
            }
        } else if (!json.equals(other.json)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " id=" + id;
    }
}
