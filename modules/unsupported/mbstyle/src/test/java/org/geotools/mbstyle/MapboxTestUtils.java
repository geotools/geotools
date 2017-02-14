package org.geotools.mbstyle;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MapboxTestUtils {

    static JSONParser jsonParser = new JSONParser();

    /**
     * 
     * Read a test Mapbox Style file (json) and parse it into a {@link JSONObject}.
     */
    public static JSONObject parseTestStyle(String filename) throws IOException, ParseException {
        InputStream is = MapboxTestUtils.class.getResourceAsStream(filename);
        String fileContents = IOUtils.toString(is, "utf-8");
        return (JSONObject) jsonParser.parse(fileContents);
    }

}
