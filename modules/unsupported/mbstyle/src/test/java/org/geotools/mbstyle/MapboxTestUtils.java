package org.geotools.mbstyle;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

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

    /**
     * 
     * Read a test Mapbox Style file (json) and parse it into a {@link JSONObject}.
     */
    public static JSONObject parseTestStyle(String filename) throws IOException, ParseException {
        InputStream is = MapboxTestUtils.class.getResourceAsStream(filename);
        String fileContents = IOUtils.toString(is, "utf-8");
        return (JSONObject) jsonParser.parse(fileContents);
    }
    
    public static BufferedImage showRender(String testName, GTRenderer renderer, long timeOut,
            ReferencedEnvelope[] bounds, RenderListener listener) throws Exception {
        BufferedImage[] images = new BufferedImage[bounds.length];
        for (int i = 0; i < images.length; i++) {
            images[i] = RendererBaseTest.renderImage(renderer, bounds[i], listener);
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

}
