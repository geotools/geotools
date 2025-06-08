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
package org.geotools.mbstyle.sprite;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.geotools.api.feature.Feature;
import org.geotools.api.filter.expression.Expression;
import org.geotools.data.ows.URLCheckers;
import org.geotools.http.HTTPClient;
import org.geotools.http.HTTPClientFinder;
import org.geotools.http.HTTPResponse;
import org.geotools.image.io.ImageIOExt;
import org.geotools.mbstyle.parse.MBFormatException;
import org.geotools.mbstyle.transform.MBStyleTransformer;
import org.geotools.renderer.style.ExternalGraphicFactory;
import org.geotools.renderer.style.GraphicCache;
import org.geotools.util.SoftValueHashMap;
import org.geotools.util.logging.Logging;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Implementation of an {@link ExternalGraphicFactory} that takes the address of a Mapbox-style sprite sheet resource
 * and an icon name, and retrieves the icon from the sprite sheet.
 *
 * <p>Note that this factory expects the {@link MBStyleTransformer} to produce {@link ExternalGraphic} instances with
 * slightly modified URLs using one of the following forms: <code>
 * {baseUrl}#{iconName}</code> <code>{baseUrl}#icon={iconName}&amp;size={sizeMultiplier}</code>
 *
 * <p>Only the baseUrl is used to retrieve the sprite sheet (at {baseUrl}.png) and sprite index (at {baseUrl}.json). The
 * iconName (required) is then used by this factory to select the correct icon from the spritesheet, and the size
 * (optional) is used to scale the icon. For example, for the following style:
 *
 * <pre>
 * {
 *  "version": 8,
 *  "name": "A Style",
 *  "sprite": "file:/GeoServerDataDirs/release/styles/testSpritesheet",
 *  "glyphs": "...",
 *  "sources": {...},
 *  "layers": [...]
 * }
 * </pre>
 *
 * <p>If a layer in this style references an icon in the spritesheet, e.g. iconName, then the constructed URL for the
 * external graphic should be <code>
 * file:/GeoServerDataDirs/release/styles/testSpritesheet#iconName</code>
 *
 * @see <a
 *     href="https://www.mapbox.com/mapbox-gl-js/style-spec/#sprite">https://www.mapbox.com/mapbox-gl-js/style-spec/#sprite</a>
 */
public class SpriteGraphicFactory implements ExternalGraphicFactory, GraphicCache {

    /** {@link ExternalGraphic} instances with this format will be handled by the {@link SpriteGraphicFactory}. */
    public static final String FORMAT = "mbsprite";

    final JSONParser jsonParser = new JSONParser();

    protected static final Map<URL, BufferedImage> imageCache = Collections.synchronizedMap(new SoftValueHashMap<>());

    protected static final Map<URL, SpriteIndex> indexCache = Collections.synchronizedMap(new SoftValueHashMap<>());

    private static final Logger LOGGER = Logging.getLogger(SpriteGraphicFactory.class);

    private static final String ICON_NAME_DELIMITER = "#";

    @Override
    public Icon getIcon(Feature feature, Expression url, String format, int size) throws Exception {

        // Only handle the correct format
        if (!FORMAT.equalsIgnoreCase(format.trim())) {
            return null;
        }

        URL loc = url.evaluate(feature, URL.class);
        URL baseUrl = parseBaseUrl(loc);

        // validate the icon can actually be fetched, it may go to a random
        // local filesystem location, or a remote server
        URLCheckers.confirm(loc);

        Map<String, String> paramsMap = parseFragmentParams(loc);
        String iconName = paramsMap.get("icon");
        String sizeStr = paramsMap.get("size");
        sizeStr = sizeStr == null ? "1.0" : sizeStr;

        Double sizeMultiplier;
        try {
            sizeMultiplier = Double.parseDouble(sizeStr);
            if (sizeMultiplier < 0) {
                sizeMultiplier = 1.0;
            }
        } catch (NumberFormatException e) {
            throw new MBSpriteException(
                    "Exception parsing size parameter from Sprite External Graphic URL. URL was: " + loc, e);
        }

        // Retrieve and parse the sprite index file.
        SpriteIndex spriteIndex = getSpriteIndex(baseUrl);

        SpriteIndex.IconInfo iconInfo = spriteIndex.getIcon(iconName);

        // Retrieve the sprite sheet and get the icon as a sub image
        BufferedImage spriteImg = getSpriteSheet(baseUrl);
        BufferedImage iconSubImg =
                spriteImg.getSubimage(iconInfo.getX(), iconInfo.getY(), iconInfo.getWidth(), iconInfo.getHeight());

        // Use "size" to scale the image, if > 0
        if (size > 0 && iconSubImg.getHeight() != size) {
            double scaleY = (double) size / iconSubImg.getHeight(); // >1 if you're magnifying
            double scaleX = scaleY; // keep aspect ratio!

            AffineTransform scaleTx = AffineTransform.getScaleInstance(scaleX, scaleY);
            AffineTransformOp ato = new AffineTransformOp(scaleTx, AffineTransformOp.TYPE_BILINEAR);
            iconSubImg = ato.filter(iconSubImg, null);
        }

        // Use the size multiplier, if any, to scale the image
        if (sizeMultiplier != 1.0) {
            AffineTransform scaleTx = AffineTransform.getScaleInstance(sizeMultiplier, sizeMultiplier);
            AffineTransformOp ato = new AffineTransformOp(scaleTx, AffineTransformOp.TYPE_BILINEAR);
            iconSubImg = ato.filter(iconSubImg, null);
        }

        return new ImageIcon(iconSubImg);
    }

    /**
     * Parse the parameters from the URL fragment in the provided URL (interpreting the fragment like a query string).
     * The "name" parameter is required and will cause an {@link MBFormatException} if missing. The "size" parameter is
     * optional and defaults to "1".
     *
     * @return Sprite parameters map providing name, icons, size values.
     */
    protected static Map<String, String> parseFragmentParams(URL url) {
        String urlStr = url.toExternalForm();
        int fragmentIdx = urlStr.indexOf(ICON_NAME_DELIMITER);

        if (fragmentIdx == -1) {
            throw new IllegalArgumentException(
                    "Sprite external graphics must have url with fragment of the form #icon=test&size=1.5. URL was: "
                            + urlStr);
        }

        String fragment = urlStr.substring(fragmentIdx + 1);

        if (fragment.trim().length() == 0) {
            throw new IllegalArgumentException(
                    "Sprite external graphics must have url with non-empty fragment of the form #icon=test&size=1.5. URL was: "
                            + urlStr);
        }

        String[] nvps = fragment.split("&");

        Map<String, String> paramsMap = new HashMap<>();
        for (String s : nvps) {
            try {
                String[] nvp = s.split("=");
                if (nvp.length == 1 && nvps.length == 1) {
                    // Allow the simple case url#iconName (omitting name=iconName)
                    paramsMap.put("icon", URLDecoder.decode(nvp[0], "utf-8"));
                } else {
                    String k = URLDecoder.decode(nvp[0], "utf-8").trim().toLowerCase();
                    String v = URLDecoder.decode(nvp[1], "utf-8");
                    paramsMap.put(k, v);
                }

            } catch (UnsupportedEncodingException uee) {
                throw new MBSpriteException(
                        "Exception decoding URL fragment for external graphic URL. URL was: " + urlStr, uee);
            }
        }

        if (paramsMap.get("icon") == null || paramsMap.get("icon").trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Sprite external graphics must provide an icon name using a URL fragment of the form #icon=test&size=1.5 . URL was: "
                            + urlStr);
        }

        return paramsMap;
    }

    /**
     * Parse the icon name from the provided {@link URL}. E.g., <code>/path/to/sprite#iconName
     * </code> will return <code>iconName</code>.
     *
     * @param url The url from which to parse the icon name.
     * @return The icon name.
     * @throws IllegalArgumentException If the icon name could not be parsed.
     */
    protected static String parseIconName(URL url) {
        String urlStr = url.toExternalForm();

        if (!urlStr.contains(ICON_NAME_DELIMITER)) {

            throw new IllegalArgumentException(
                    "Mapbox-style sprite external graphics must have url#{iconName}. URL was: " + urlStr);
        }

        String[] splitStr = url.toExternalForm().split(ICON_NAME_DELIMITER);
        String iconName = splitStr[splitStr.length - 1];

        if (iconName.trim().length() == 0) {
            throw new IllegalArgumentException(
                    "Mapbox-style sprite external graphics must have non-empty url#{iconName}. URL was: " + urlStr);
        }

        return iconName;
    }

    /**
     * Return the base URL (without an appended icon name) from the provided URL. <code>
     * /path/to/sprite#iconName</code> will return <code>path/to/sprite</code>
     *
     * @param loc The URL.
     * @return The URL, without an appended icon name.
     */
    protected static URL parseBaseUrl(URL loc) throws MalformedURLException {
        String urlStr = loc.toExternalForm();
        int idx = urlStr.indexOf(ICON_NAME_DELIMITER);
        if (idx == -1) {
            return new URL(urlStr);
        } else {
            return new URL(urlStr.substring(0, idx));
        }
    }

    /**
     * Retrieve the sprite sheet index for the provided sprite base url. The base url should have no extension.
     *
     * @param baseUrl The base URL of the Mapbox sprite source (no extension).
     * @return The sprite sheet index
     */
    protected SpriteIndex getSpriteIndex(URL baseUrl) throws IOException {
        SpriteIndex spriteIndex = indexCache.get(baseUrl);
        if (spriteIndex == null) {
            String indexUrlStr = baseUrl.toExternalForm() + ".json";
            URL url = new URL(indexUrlStr);
            HTTPClient client = getHttpClient();
            HTTPResponse response = client.get(url);
            String charset = Optional.ofNullable(response.getResponseCharset()).orElse("UTF-8");
            try (BufferedReader reader =
                    new BufferedReader(new InputStreamReader(response.getResponseStream(), charset))) {
                Object parsed = jsonParser.parse(reader);
                if (parsed instanceof JSONObject) {
                    spriteIndex = new SpriteIndex(indexUrlStr, (JSONObject) parsed);
                    indexCache.put(baseUrl, spriteIndex);
                } else {
                    throw new MBSpriteException("Exception parsing sprite index file from: "
                            + indexUrlStr
                            + ". Expected JSONObject, but was: "
                            + parsed.getClass().getSimpleName());
                }

            } catch (ParseException e) {
                throw new MBSpriteException("Exception parsing sprite index file from: " + indexUrlStr, e);
            }
        }

        return spriteIndex;
    }

    protected HTTPClient getHttpClient() {
        return HTTPClientFinder.createClient();
    }

    /**
     * Retrieve the sprite sheet for the provided sprite base url. The base url should have no extension.
     *
     * @param baseUrl The base URL of the Mapbox sprite source (no extension).
     * @return A {@link BufferedImage} for the sprite sheet.
     */
    private BufferedImage getSpriteSheet(URL baseUrl) {
        BufferedImage image = imageCache.get(baseUrl);
        if (image == null) {
            HTTPClient client = getHttpClient();
            try (InputStream is =
                    client.get(new URL(baseUrl.toExternalForm() + ".png")).getResponseStream()) {
                image = ImageIOExt.readBufferedImage(is);
            } catch (Exception e) {
                LOGGER.warning("Unable to retrieve sprite sheet from location: "
                        + baseUrl.toExternalForm()
                        + " ("
                        + e.getMessage()
                        + ")");
                throw new MBSpriteException(
                        "Failed to retrieve sprite sheet for baseUrl: " + baseUrl.toExternalForm(), e);
            }
            imageCache.put(baseUrl, image);
        }
        return image;
    }

    /** Images are cached by this factory. This method can be used to drop the cache. */
    @Override
    public void clearCache() {
        imageCache.clear();
        indexCache.clear();
    }
}
