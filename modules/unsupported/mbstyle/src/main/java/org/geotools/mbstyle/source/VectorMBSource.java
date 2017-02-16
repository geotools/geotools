package org.geotools.mbstyle.source;

import org.geotools.mbstyle.parse.MBObjectParser;
import org.json.simple.JSONObject;

/**
 * Wrapper around a {@link JSONObject} holding a Mapbox  vectorsource. Tiled sources (vector and raster) must specify their details in terms of the
 * TileJSON specification.
 * 
 * @see {@link MBSource}
 * @see <a href="https://www.mapbox.com/mapbox-gl-js/style-spec/#sources-vector">https://www.mapbox.com/mapbox-gl-js/style-spec/#sources-vector</a>
 *
 */
public class VectorMBSource extends TileMBSource {
    
    public VectorMBSource(JSONObject json) {
        this(json, null);       
    }
    
    public VectorMBSource(JSONObject json, MBObjectParser parser) {
        super(json, parser);        
    }

    @Override
    public String getType() {
        return "vector";
    }
        
}
