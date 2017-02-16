package org.geotools.mbstyle.source;

import org.geotools.mbstyle.MBFormatException;
import org.geotools.mbstyle.parse.MBObjectParser;
import org.json.simple.JSONObject;

/**
 * 
 * Wrapper around a {@link JSONObject} containing the "sources" in a Mapbox style. Mapbox sources supply data to be shown on the map. The type of
 * source is specified by the "type" property, and must be one of vector, raster, geojson, image, video, canvas.
 * 
 * "Layers refer to a source and give it a visual representation. This makes it possible to style the same source in different ways, like differentiating between types of roads in a highways layer."
 * 
 * @see <a href="https://www.mapbox.com/mapbox-gl-js/style-spec/#sources">https://www.mapbox.com/mapbox-gl-js/style-spec/#sources</a>
 *
 */
public abstract class MBSource {
    
    protected JSONObject json;
    protected MBObjectParser parser;

    public MBSource(JSONObject json) {
        this(json, null);
    }
    
    public MBSource(JSONObject json, MBObjectParser parser) {
        this.json = json != null ? json : new JSONObject();
        this.parser = parser != null ? parser : new MBObjectParser();
    }
    
    public static MBSource create(JSONObject json, MBObjectParser parser ) {
        if (!json.containsKey("type") || !(json.get("type") instanceof String)) {
            throw new MBFormatException("Mapbox source \"type\" is required and must be one of: vector, raster, geojson, image, video, or canvas.");
        }
        
        String type = ((String) json.get("type")).toLowerCase().trim();

        if ("vector".equalsIgnoreCase(type)) {
            return new VectorMBSource(json, parser);
        }
        if ("raster".equalsIgnoreCase(type)) {
            return new RasterMBSource(json, parser);
        }
        if ("geojson".equalsIgnoreCase(type)) {
            return new GeoJsonMBSource(json, parser);
        }
        if ("image".equalsIgnoreCase(type)) {
            return new ImageMBSource(json, parser);
        }
        if ("video".equalsIgnoreCase(type)) {
            return new VideoMBSource(json, parser);
        }
        if ("canvas".equalsIgnoreCase(type)) {
            return new CanvasMBSource(json, parser);
        } else {
            throw new MBFormatException("Mapbox source \"type\" is required and must be one of: vector, raster, geojson, image, video, or canvas.");
        }
    }
    
    /**
     * Must be one of "vector", "raster", "geojson", "image", "video", "canvas"
     */
    public abstract String getType();

}
