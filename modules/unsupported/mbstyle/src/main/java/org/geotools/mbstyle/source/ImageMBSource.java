package org.geotools.mbstyle.source;

import org.geotools.mbstyle.parse.MBObjectParser;
import org.json.simple.JSONObject;

/**
 * 
 * Wrapper around a {@link JSONObject} containing a Mapbox image source.
 * 
 * @see <a href="https://www.mapbox.com/mapbox-gl-js/style-spec/#sources-image">https://www.mapbox.com/mapbox-gl-js/style-spec/#sources-image</a>
 *
 */
public class ImageMBSource extends MediaMBSource {

    public ImageMBSource(JSONObject json) {
        this(json, null);
    }

    public ImageMBSource(JSONObject json, MBObjectParser parser) {
        super(json, parser);
    }   

    /**
     * (Required) URL that points to an image.
     * 
     * @return String for the URL
     */
    public String getUrl() {
        return parser.get(json, "url");
    }

    @Override
    public String getType() {
        return "image";
    }

}
