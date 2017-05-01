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
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.geotools.mbstyle.transform.MBStyleTransformer;
import org.geotools.renderer.style.GraphicCache;
import org.geotools.renderer.style.ExternalGraphicFactory;
import org.geotools.styling.ExternalGraphic;
import org.geotools.util.SoftValueHashMap;
import org.geotools.util.logging.Logging;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.opengis.feature.Feature;
import org.opengis.filter.expression.Expression;

/**
 * 
 * <p>
 * Implementation of an {@link ExternalGraphicFactory} that takes the address of a Mapbox-style sprite sheet resource and an icon name, and retrieves
 * the icon from the sprite sheet.
 * </p>
 * 
 * <p>
 * Note that this factory expects the {@link MBStyleTransformer} to produce {@link ExternalGraphic} instances with slightly modified URLs of the
 * following form:
 * </p>
 * 
 * <code>{baseUrl}#{iconName}</code>
 * 
 * <p>
 * Only the baseUrl is used to retrieve the sprite sheet (at {baseUrl}.png) and sprite index (at {baseUrl}.json). The iconName is then used by this
 * factory to select the correct icon from the spritesheet.
 * </p>
 * 
 * 
 * For example, for the following Mapbox style:
 * 
 * <pre>
 * {
 *  "version": 8,
 *  "name": "A Mapbox Style",
 *  "sprite": "file:/GeoServerDataDirs/release/styles/testSpritesheet",
 *  "glyphs": "...",
 *  "sources": {...},
 *  "layers": [...]
 * }
 * </pre>
 *
 * <p>
 * If a layer in this style references an icon in the spritesheet, e.g. iconName, then the constructed URL for the external graphic should be
 * <code>file:/GeoServerDataDirs/release/styles/testSpritesheet#iconName</code>
 * </p>
 * 
 * @see <a href="https://www.mapbox.com/mapbox-gl-js/style-spec/#sprite">https://www.mapbox.com/mapbox-gl-js/style-spec/#sprite</a>
 * 
 */
public class SpriteGraphicFactory implements ExternalGraphicFactory,GraphicCache {

    /**
     * {@link ExternalGraphic} instances with this format will be handled by the {@link SpriteGraphicFactory}.
     */
    public static final String FORMAT = "mbsprite";

    JSONParser jsonParser = new JSONParser();

    protected static Map<URL, BufferedImage> imageCache = Collections
            .synchronizedMap(new SoftValueHashMap<>());

    protected static Map<URL, SpriteIndex> indexCache = Collections
            .synchronizedMap(new SoftValueHashMap<>());

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
        String iconName = parseIconName(loc);

        // Retrieve and parse the sprite index file.
        SpriteIndex spriteIndex = getSpriteIndex(baseUrl);

        SpriteIndex.IconInfo iconInfo = spriteIndex.getIcon(iconName);

        // Retrieve the sprite sheet and get the icon as a sub image
        BufferedImage spriteImg = getSpriteSheet(baseUrl);
        BufferedImage iconSubImg = spriteImg.getSubimage(iconInfo.getX(), iconInfo.getY(),
                iconInfo.getWidth(), iconInfo.getHeight());

        // Use "size" to scale the image, if > 0
        if (size > 0 && iconSubImg.getHeight() != size) {
            double scaleY = ((double) size) / iconSubImg.getHeight(); // >1 if you're magnifying
            double scaleX = scaleY; // keep aspect ratio!

            AffineTransform scaleTx = AffineTransform.getScaleInstance(scaleX, scaleY);
            AffineTransformOp ato = new AffineTransformOp(scaleTx, AffineTransformOp.TYPE_BILINEAR);
            iconSubImg = ato.filter(iconSubImg, null);
        }

        return new ImageIcon(iconSubImg);
    }

    /**
     * Parse the icon name from the provided {@link URL}. E.g.,
     * 
     * <code>/path/to/sprite#iconName</code> will return <code>iconName</code>.
     * 
     * @param url The url from which to parse the icon name.
     * @return The icon name.
     * @throws IllegalArgumentException If the icon name could not be parsed.
     */
    protected static String parseIconName(URL url) {
        String urlStr = url.toExternalForm();

        if (urlStr.indexOf(ICON_NAME_DELIMITER) == -1) {

            throw new IllegalArgumentException(
                    "Mapbox-style sprite external graphics must have url#{iconName}. URL was: "
                            + urlStr);
        }

        String[] splitStr = url.toExternalForm().split(ICON_NAME_DELIMITER);
        String iconName = splitStr[splitStr.length - 1];

        if (iconName.trim().length() == 0) {
            throw new IllegalArgumentException(
                    "Mapbox-style sprite external graphics must have non-empty url#{iconName}. URL was: "
                            + urlStr);
        }

        return iconName;

    }

    /**
     * Return the base URL (without an appended icon name) from the provided URL.
     * 
     * <code>/path/to/sprite#iconName</code> will return <code>path/to/sprite</code>
     * 
     * @param loc The URL.
     * @return The URL, without an appended icon name.
     * @throws MalformedURLException
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
     * 
     * Retrieve the sprite sheet index for the provided sprite base url. The base url should have no extension.
     * 
     * @param baseUrl The base URL of the Mapbox sprite source (no extension).
     * @return The sprite sheet index
     * @throws IOException
     */
    protected SpriteIndex getSpriteIndex(URL baseUrl) throws IOException {
        SpriteIndex spriteIndex = indexCache.get(baseUrl);
        if (spriteIndex == null) {
            String indexUrlStr = baseUrl.toExternalForm() + ".json";
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new URL(indexUrlStr).openStream()))) {

                Object parsed = jsonParser.parse(reader);
                if (parsed instanceof JSONObject) {
                    spriteIndex = new SpriteIndex(indexUrlStr, (JSONObject) parsed);
                    indexCache.put(baseUrl, spriteIndex);
                } else {
                    throw new MBSpriteException("Exception parsing sprite index file from: "
                            + indexUrlStr + ". Expected JSONObject, but was: "
                            + parsed.getClass().getSimpleName());
                }

            } catch (ParseException e) {
                throw new MBSpriteException(
                        "Exception parsing sprite index file from: " + indexUrlStr, e);
            }
        }
        return spriteIndex;
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
            try {
                URL spriteSheetUrl = new URL(baseUrl.toExternalForm() + ".png");
                image = ImageIO.read(spriteSheetUrl);
            } catch (Exception e) {
                LOGGER.warning("Unable to retrieve sprite sheet from location: "
                        + baseUrl.toExternalForm() + " (" + e.getMessage() + ")");
                throw new MBSpriteException(
                        "Failed to retrieve sprite sheet for baseUrl: " + baseUrl.toExternalForm(),
                        e);
            }
            imageCache.put(baseUrl, image);
        }
        return image;
    }

    /**
     * Images are cached by this factory. This method can be used to drop the cache.
     */
    @Override
    public void clearCache() {
        imageCache.clear();
        indexCache.clear();
    }
}
