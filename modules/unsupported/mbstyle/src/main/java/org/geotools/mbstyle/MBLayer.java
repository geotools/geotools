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
package org.geotools.mbstyle;

import org.geotools.mbstyle.parse.MBObjectParser;
import org.geotools.styling.StyleFactory2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

/**
 * MBLayer wrapper (around one of the MBStyle layers).
 * <p>
 * All methods act as accessors on provided JSON layer, no other state is maintained. This allows
 * modifications to be made cleanly with out chance of side-effect.
 * 
 * <ul>
 * <li>get methods: access the json directly</li>
 * <li>query methods: provide logic / transforms to GeoTools classes as required.</li>
 * </ul>
 * 
 * <p>
 * In the normal course of events MBLayer is constructed as a flighweight object by MBStyle to
 * provide easy access to its layers list.
 * </p>
 * 
 * @author Torben Barsballe (Boundless)
 */
public abstract class MBLayer {
    /** Helper class used to provide json access and expression / filter conversions */
    final protected MBObjectParser parse;

    /** Shared filter factory */
    final protected FilterFactory2 ff;

    /** Shared style factory */
    final protected StyleFactory2 sf;

    /** JSON layer being wrapped. */
    final protected JSONObject json;

    /** field access for "id" key (due to its use in error messages) */
    final protected String id;

    public MBLayer(JSONObject json) {
        this(json, new MBObjectParser());
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
     */
    public static MBLayer create(JSONObject layer ){
        if( layer.containsKey("type") && layer.get("type") instanceof String){
            String type = (String) layer.get("type");
            switch( type ){
            case "line": return new LineMBLayer(layer);
            case "fill": return new FillMBLayer(layer);
            case "raster": return new RasterMBLayer(layer);                       
            case "circle": return new CircleMBLayer(layer);
            case "background": return new BackgroundMBLayer(layer);
            case "symbol": return new SymbolMBLayer(layer);
            case "fill-extrusion":
                throw new UnsupportedOperationException("MBLayer type "+type+" not yet implemented");                
            default:
                throw new MBFormatException(("\"type\" " + type
                        + " is not a valid layer type. Must be one of: "
                        + "background, fill, line, symbol, raster, circle, fill-extrusion"));

            }
        }
        // technically we may be able to do this via a ref
        throw new MBFormatException("\"type\" required to create layer.");
    }
    
    /**
     * Rendering type of this layer.
     * <p>
     * One of:
     * <ul>
     * <li>fill:A filled polygon with an optional stroked border.</li>
     * <li>line: A stroked line.</li>
     * <li>symbol: An icon or a text label.</li>
     * <li>circle:A filled circle.</li>
     * <li>fill-extrusion: An extruded (3D) polygon.</li>
     * <li>raster: Raster map textures such as satellite imagery.</li>
     * <li>background: The background color or pattern of the map.</li>
     * </ul>
     * 
     * @return Optional field, one of fill, line, symbol, circle, fill-extrusion, raster,
     *         background.
     */
    public abstract String getType();
    
    /**
     * Arbitrary properties useful to track with the layer, but do not influence rendering.
     * Properties should be prefixed to avoid collisions, like 'mapbox:' and 'gt:`.
     * 
     * @return Arbitrary properties useful to track with the layer. 
     */
    public JSONObject getMetadata(){
        throw new UnsupportedOperationException(); 
    }

    /**
     * References another layer to copy type, source, source-layer, minzoom, maxzoom, filter, and
     * layout properties from. This allows the layers to share processing and be more efficient.
     * 
     * @return References another layer to copy type, source, source-layer, minzoom, maxzoom, filter, and
     * layout properties from.
     */
    public String getRef(){
        // We should update getType(), getSource(), getSourceLayer(), getMinZoom(), getMaxZoom(),
        // getFilter() to look up value provided by getRef() if needed.
        throw new UnsupportedOperationException();
    }
    
    
    public JSONObject getJson() {
        return json;
    }

    /**
     * Unique layer name.
     * @return layer name, required field
     */
    public String getId() {
        return id;
    }

    /**
     * Name of a source description to be used for this layer.
     * <p>
     * While this value is optional, it may be obtained via {@link #getRef()} if needed.
     * </p>
     * 
     * @return name of source description to be used for this layer
     */
    public String getSource() {
        return parse.optional(String.class, json, "source", null);
    }

    /**
     * Layer to use from a vector tile source. Required if the source supports multiple layers.
     * <p>
     * While this value is optional, it may be obtained via {@link #getRef()} if needed.
     * </p>
     * 
     * @return layer to use from a vector tile source
     */
    public String getSourceLayer() {
        return parse.optional(String.class, json, "source-layer", null);
    }

    /**
     * The minimum zoom level on which the layer gets parsed and appears on.
     * 
     * @return minimum zoom level, optional number may return null.
     */
    public Integer getMinZoom(){
        return parse.optional(Integer.class, json, "minzoom", null);
    }
    
    /**
     * The maximum zoom level on which the layer gets parsed and appears on.
     * 
     * @return maximum zoom level, optional number may return null.
     */
    public Integer getMaxZoom(){
        return parse.optional(Integer.class, json, "maxzoom", null);
    }
    
    /**
     * A JSONArray expression specifying conditions on source features. Only features that match the filter
     * are displayed. This is available as a GeoTools {@link Filter} via {@link #filter()}.
     * 
     * @return JSONArray expression specifying conditions on source features, optional may return null.
     */
    public JSONArray getFilter(){
        return parse.getJSONArray(json,"filter", null );
    }
    
    /**
     * The "filter" as a GeoTools {@link Filter} suitable for feature selection, as defined by
     * {@link #getFilter()}.
     * 
     * @return Filter
     */
    public Filter filter(){
        return Filter.INCLUDE; // TODO: Implemented based on getFilter, for now select everything
    }
    
    /**
     * Layout properties for the layer.
     * <p>
     * <em>Layout properties</em> appear in the layer's "layout" object. They are applied early in
     * the rendering process and define how data for that layer is passed to the renderer. For
     * efficiency, a layer can share layout properties with another layer via the "ref" layer
     * property, and should do so where possible. This will decrease processing time and allow the
     * two layers will share GPU memory and other resources associated with the layer.
     * </p>
     * 
     * @return Layout properties defined for layer, optional value may be empty.
     */
    public JSONObject getLayout(){
        return parse.layout(json);
    }
    
    /**
     * Query for layout information (making use of {@link #getRef()} if available).
     * 
     * @return Layout properties to use for this layer.
     */
    public JSONObject layout(){
        return getLayout(); // TODO: Lookup ref
    }
    
    /**
     * Default paint properties for this layer.
     * <p>
     * <em>Paint properties</em> are applied later in the rendering process. A layer that shares layout
     * properties with another layer can have independent paint properties. Paint properties appear
     * in the layer's "paint" object.
     * </p>
     * 
     * @return Default paint properties for this layer, optinal value may be empty.
     */
    public JSONObject getPaint(){
        return parse.paint(json);
    }
    /**
     * Query for paint information (making use of {@link #getRef()} if available).
     * 
     * @return Paint properties to use for this layer.
     */
    public JSONObject paint(){
        return getPaint(); // TODO: Lookup ref
    }

    /**
     * Class-specific "paint.*" properties for this layer. The class name is the part after the first
     * dot. 
     * 
     * @return class specific "paint.*" properties
     * @deprecated style classes are deprecated and will be removed in the next version of this spec.
     */
    public JSONObject getPaintProperties(){
        return new JSONObject();
    }
    
    //
    // Data Object based on wrapped json
    //
    
    /** Hashcode based on wrapped {@link #json}. */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((json == null) ? 0 : json.hashCode());
        return result;
    }

    /** Equality based on wrapped {@link #json}. */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MBLayer other = (MBLayer) obj;
        if (json == null) {
            if (other.json != null)
                return false;
        } else if (!json.equals(other.json))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + " id=" + id;
    }

}
