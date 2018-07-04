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

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;
import org.json.simple.JSONObject;

/**
 * Wrapper that takes the sprite index file (as a JSONObject) for a Mapbox Sprite Sheet and parses
 * the all the individual icons as {@link IconInfo} objects. For example:
 *
 * <pre>
 * <code>
 * {
 *    "goldfish": {
 *      "height": 32,
 *      "pixelRatio": 1,
 *      "width": 32,
 *      "x": 64,
 *      "y": 64
 *    },
 *    "owl": {
 *      "height": 64,
 *      "pixelRatio": 1,
 *      "width": 64,
 *      "x": 0,
 *      "y": 0
 *    }
 * }
 * </code>
 * </pre>
 *
 * @see <a
 *     href="https://www.mapbox.com/mapbox-gl-js/style-spec/#sprite">https://www.mapbox.com/mapbox-gl-js/style-spec/#sprite</a>
 */
public class SpriteIndex {

    private String spriteIndexUrl;

    private JSONObject json;

    private Map<String, IconInfo> icons;

    private static final Logger LOGGER = Logging.getLogger(SpriteIndex.class);

    /**
     * @param spriteIndexUrl The URL of the sprite index file (used for error messages).
     * @param json The sprite index file as a {@link JSONObject}.
     */
    public SpriteIndex(String spriteIndexUrl, JSONObject json) {
        this.spriteIndexUrl = spriteIndexUrl;
        this.json = json;
        this.icons = new HashMap<>();

        for (Object key : this.json.keySet()) {
            if (key instanceof String) {
                String iconName = (String) key;
                try {
                    IconInfo iconInfo = parseIconInfoFromIndex(this.json, iconName);
                    icons.put(iconName, iconInfo);
                } catch (Exception e) {
                    LOGGER.warning(
                            "Mapbox sprite icon index file "
                                    + this.spriteIndexUrl
                                    + " contained invalid value for key \""
                                    + iconName
                                    + "\". Exception was: "
                                    + e.getMessage());
                }
            }
        }
    }

    /**
     * Parse the {@link IconInfo} for the provided iconName in the provided icon index.
     *
     * @param iconIndex The icon index file.
     * @param iconName The name of the icon in the index file.
     * @return An {@link IconInfo} for the icon.
     */
    protected static IconInfo parseIconInfoFromIndex(JSONObject iconIndex, String iconName) {
        if (!iconIndex.containsKey(iconName)) {
            throw new MBSpriteException(
                    "Sprite index file does not contain entry for icon with name: " + iconName);
        }

        Object o = iconIndex.get(iconName);
        if (!(o instanceof JSONObject)) {
            throw new MBSpriteException(
                    "Error parsing sprite index for \""
                            + iconName
                            + "\": Expected JSONObject, but is "
                            + o.getClass().getSimpleName());
        }
        return new IconInfo(iconName, (JSONObject) o);
    }

    /**
     * Get the names and data of all icons in the index
     *
     * @return An immutable map of the icon name to the corresponding {@link IconInfo}
     */
    public ImmutableMap<String, IconInfo> getIcons() {
        return ImmutableMap.copyOf(icons);
    }

    /**
     * Get information about a single icon from the index
     *
     * @param iconName Name of the icon
     * @return Info object describing the icons
     */
    public IconInfo getIcon(String iconName) {
        if (!icons.containsKey(iconName)) {
            throw new MBSpriteException(
                    "Mapbox sprite icon index file "
                            + this.spriteIndexUrl
                            + " does not contain icon with name: "
                            + iconName);
        } else {
            return icons.get(iconName);
        }
    }

    /**
     * Wrapper for parsing the properties of an individual sprite index entry (JSONObject) for a
     * single icon. For example:
     *
     * <pre>
     * <code>
     * {
     *  "width": 32,
     *  "height": 32,
     *  "x": 0,
     *  "y": 0,
     *  "pixelRatio": 1
     * }
     * </code>
     * </pre>
     *
     * @see <a
     *     href="https://www.mapbox.com/mapbox-gl-js/style-spec/#sprite">https://www.mapbox.com/mapbox-gl-js/style-spec/#sprite</a>
     */
    public static class IconInfo {

        private String iconName;

        private JSONObject json;

        /**
         * @param iconName The name of this sprite icon (used for error messages)
         * @param json The sprite index entry for this icon, as a {@link JSONObject}
         */
        public IconInfo(String iconName, JSONObject json) {
            this.iconName = iconName;
            this.json = json;
        }

        public int getWidth() {
            return intOrException("width");
        }

        public int getHeight() {
            return intOrException("height");
        }

        public int getX() {
            return intOrException("x");
        }

        public int getY() {
            return intOrException("y");
        }

        public int getPixelRatio() {
            return intOrException("pixelRatio");
        }

        private int intOrException(String k) {
            if (!json.containsKey(k)) {
                throw new MBSpriteException(
                        "Mapbox sprite icon with name \""
                                + iconName
                                + "\" is missing required property: "
                                + k);
            }
            Object o = json.get(k);

            try {
                if (o instanceof Number) {
                    return ((Number) o).intValue();
                } else if (o instanceof String) {
                    return Integer.valueOf((String) o);
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (Exception e) {
                throw new MBSpriteException(
                        "Mapbox sprite icon with name \""
                                + iconName
                                + "\" contains invalid value for property \""
                                + k
                                + "\". Expected integer, but was: "
                                + o.getClass().getSimpleName(),
                        e);
            }
        }
    }
}
