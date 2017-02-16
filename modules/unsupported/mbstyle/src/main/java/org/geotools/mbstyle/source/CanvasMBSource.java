package org.geotools.mbstyle.source;

import org.geotools.mbstyle.parse.MBObjectParser;
import org.json.simple.JSONObject;

/**
 * 
 * Wrapper around a {@link JSONObject} containing a Mapbox canvas source.
 * 
 * @see <a href="https://www.mapbox.com/mapbox-gl-js/style-spec/#sources-canvas">https://www.mapbox.com/mapbox-gl-js/style-spec/#sources-canvas</a>
 *
 */
public class CanvasMBSource extends MediaMBSource {

    public CanvasMBSource(JSONObject json) {
        this(json, null);
    }

    public CanvasMBSource(JSONObject json, MBObjectParser parser) {
        super(json, parser);
    }

    /**
     * (Required) HTML ID of the canvas from which to read pixels.
     * 
     * @return String value for the HTML ID
     */
    public String getCanvas() {
        return parser.get(json, "canvas");
    }

    /**
     * (Optional)  Defaults to true. Whether the canvas source is animated. If the canvas is static, animate should be set to false to improve performance.
     * 
     * @return Boolean for whether the source is animated, defaulting to true.
     */
    public Boolean getAnimate() {
        return parser.optional(Boolean.class, json, "animate", true);
    }

    @Override
    public String getType() {
        return "canvas";
    }

}
