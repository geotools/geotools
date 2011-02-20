/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.swt.utils;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * A singleton cache for images.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 */
@SuppressWarnings("nls")
public class ImageCache {

    public static final String CHECKED = "/org/geotools/swt/icons/checked.gif";
    public static final String UNCHECKED = "/org/geotools/swt/icons/unchecked.gif";
    public static final String STYLE = "/org/geotools/swt/icons/style.gif";
    public static final String GRID = "/org/geotools/swt/icons/grid.gif";
    public static final String FEATURE = "/org/geotools/swt/icons/feature.gif";
    public static final String UP = "/org/geotools/swt/icons/up.gif";
    public static final String DOWN = "/org/geotools/swt/icons/down.gif";
    public static final String OPEN = "/org/geotools/swt/icons/open.gif";
    public static final String REMOVE_LAYER = "/org/geotools/swt/icons/remove_layer.gif";
    public static final String IMAGE_INFO = "/org/geotools/swt/icons/info_mode.gif"; 
    public static final String IMAGE_INFO_ICON = "/org/geotools/swt/icons/info_source.gif"; 
    public static final String IMAGE_PAN = "/org/geotools/swt/icons/pan_mode.gif"; 
    public static final String IMAGE_ZOOMIN = "/org/geotools/swt/icons/zoom_in_co.gif"; 
    public static final String IMAGE_ZOOMOUT = "/org/geotools/swt/icons/zoom_out_co.gif";
    public static final String IMAGE_FULLEXTENT = "/org/geotools/swt/icons/zoom_extent_co.gif";
    
    private static ImageCache imageCache;

    private HashMap<String, Image> imageMap = new HashMap<String, Image>();

    private ImageCache() {
    }

    public static ImageCache getInstance() {
        if (imageCache == null) {
            imageCache = new ImageCache();
        }
        return imageCache;
    }

    /**
     * Get an image for a certain key.
     * 
     * <p><b>The only keys to be used are the static strings in this class</b></p>
     * 
     * @param key a file key, as for example {@link ImageCache#IMAGE_PAN}.
     * 
     * @return the image.
     */
    public Image getImage( String key ) {
        Image image = imageMap.get(key);
        if (image == null) {
            image = createImage(key);
            imageMap.put(key, image);
        }
        return image;
    }

    private Image createImage( String key ) {
        Image image = null;
        try {
            URL resourceUrl = getClass().getResource(key);
            File resourceFile = new File(resourceUrl.toURI());
            image = new Image(Display.getCurrent(), resourceFile.getAbsolutePath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * Disposes the images and clears the internal map.
     */
    public void dispose() {
        Set<Entry<String, Image>> entrySet = imageMap.entrySet();
        for( Entry<String, Image> entry : entrySet ) {
            entry.getValue().dispose();
        }
        imageMap.clear();
    }

}
