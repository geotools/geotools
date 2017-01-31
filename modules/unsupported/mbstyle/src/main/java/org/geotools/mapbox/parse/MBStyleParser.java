package org.geotools.mapbox.parse;

import org.geotools.mapbox.MBStyle;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Reader;

/**
 * Given JSON input (as a {@link String} or {@link Reader}, parses and returns a {@link MBStyle}
 */
public class MBStyleParser {

    JSONParser parser;

    public MBStyleParser() {
        JSONParser parser = new JSONParser();
    }

    public MBStyle parse(String json) throws ParseException, IllegalArgumentException {
        return new MBStyle(asJSONObject(parser.parse(json)));
    }

    public MBStyle parse(Reader json) throws ParseException, IOException, IllegalArgumentException {
        return new MBStyle(asJSONObject(parser.parse(json)));
    }

    private JSONObject asJSONObject(Object obj) {
        if (obj instanceof JSONObject) {
            return (JSONObject) obj;
        }
        throw new IllegalArgumentException("Invalid MapBox Style JSON: Root must be a JSON Object");
    }
}
