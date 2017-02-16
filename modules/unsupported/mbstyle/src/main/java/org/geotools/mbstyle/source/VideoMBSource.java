package org.geotools.mbstyle.source;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.geotools.mbstyle.parse.MBObjectParser;
import org.json.simple.JSONObject;

/**
 * 
 * Wrapper around a {@link JSONObject} containing a Mapbox video source.
 * 
 * @see <a href="https://www.mapbox.com/mapbox-gl-js/style-spec/#sources-video">https://www.mapbox.com/mapbox-gl-js/style-spec/#sources-video</a>
 *
 */
public class VideoMBSource extends MediaMBSource {

    public VideoMBSource(JSONObject json) {
        this(json, null);
    }

    public VideoMBSource(JSONObject json, MBObjectParser parser) {
        super(json, parser);
    }

    /**
     * (Required) URLs to video content in order of preferred format.
     * 
     * @return List of String urls
     */
    public List<String> getUrls() {
        // TODO I don't think this works
        String[] arr = parser.array(String.class, json, "urls", null);
        if (arr != null) {
            return Arrays.asList(arr);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public String getType() {
        return "video";
    }

}
