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
 */
package org.geotools.mbstyle;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import org.apache.commons.io.IOUtils;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.RenderListener;
import org.geotools.renderer.lite.RendererBaseTest;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MapboxTestUtils {

    static JSONParser jsonParser = new JSONParser();

    /** Reader for a test Mapbox Style file (json). */
    public static Reader readerTestStyle(String filename) throws IOException, ParseException {
        InputStream is = MapboxTestUtils.class.getResourceAsStream(filename);
        String fileContents = IOUtils.toString(is, "utf-8");
        return new StringReader(fileContents);
    }

    /** Read a test Mapbox Style file (json) and parse it into a {@link JSONObject}. */
    public static JSONObject parseTestStyle(String filename) throws IOException, ParseException {
        InputStream is = MapboxTestUtils.class.getResourceAsStream(filename);
        String fileContents = IOUtils.toString(is, "utf-8");
        return (JSONObject) jsonParser.parse(fileContents);
    }

    public static BufferedImage showRender(
            String testName,
            GTRenderer renderer,
            long timeOut,
            ReferencedEnvelope[] bounds,
            RenderListener listener)
            throws Exception {
        return showRender(testName, renderer, timeOut, bounds, listener, 300, 300);
    }

    public static BufferedImage showRender(
            String testName,
            GTRenderer renderer,
            long timeOut,
            ReferencedEnvelope[] bounds,
            RenderListener listener,
            int width,
            int height)
            throws Exception {
        BufferedImage[] images = new BufferedImage[bounds.length];
        for (int i = 0; i < images.length; i++) {
            images[i] = RendererBaseTest.renderImage(renderer, bounds[i], listener, width, height);
        }
        final BufferedImage image = RendererBaseTest.mergeImages(images);

        RendererBaseTest.showImage(testName, timeOut, image);
        boolean hasData = false; // All I can seem to check reliably.

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (image.getRGB(x, y) != 0) {
                    hasData = true;
                }
            }
        }

        assert (hasData);
        return image;
    }

    /**
     * Parse JSONObject using ' rather than " for faster test case writing.
     *
     * @param json
     * @return parsed JSONArray
     * @throws ParseException
     */
    public static JSONObject object(String json) throws ParseException {
        JSONParser parser = new JSONParser();
        String text = json.replace('\'', '\"');
        Object object = parser.parse(text);
        return (JSONObject) object;
    }
}
