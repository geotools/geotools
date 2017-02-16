package org.geotools.mbstyle.source;

import org.geotools.mbstyle.parse.MBObjectParser;
import org.json.simple.JSONObject;

/**
 * Wrapper around a {@link JSONObject} holding a Mapbox raser source. Tiled sources (vector and raster) must specify their details in terms of the
 * TileJSON specification.
 * 
 * @see {@link MBSource}
 * @see <a href="https://www.mapbox.com/mapbox-gl-js/style-spec/#sources-raster">https://www.mapbox.com/mapbox-gl-js/style-spec/#sources-raster</a>
 *
 */
public class RasterMBSource extends TileMBSource {
    
    public RasterMBSource(JSONObject json) {
        this(json, null);       
    }
    
    public RasterMBSource(JSONObject json, MBObjectParser parser) {
        super(json, parser);        
    }
    
    /**
     * (Optional) Units in pixels. Defaults to 512.
     * 
     * The minimum visual size to display tiles for this layer. Only configurable for raster layers.
     * 
     * @return Number for the tile size, defaulting to 512
     */
    public Number getTileSize() {
        return parser.optional(Number.class, json, "tileSize", 512);
    }

    @Override
    public String getType() {
        return "raster";
    }
        
}
