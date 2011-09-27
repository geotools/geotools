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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * A singleton cache for images.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 *
 *
 *
 * @source $URL$
 */
@SuppressWarnings("nls")
public class ImageCache {

    public static final String CHECKED = "/icons/checked.gif";
    public static final String UNCHECKED = "/icons/unchecked.gif";
    public static final String STYLE = "/icons/style.gif";
    public static final String GRID = "/icons/grid.gif";
    public static final String FEATURE = "/icons/feature.gif";
    public static final String UP = "/icons/up.gif";
    public static final String DOWN = "/icons/down.gif";
    public static final String OPEN = "/icons/open.gif";
    public static final String ALPHA = "/icons/alpha.gif";
    public static final String REMOVE_LAYER = "/icons/remove_layer.gif";
    public static final String IMAGE_INFO = "/icons/info_mode.gif";
    public static final String IMAGE_INFO_ICON = "/icons/info_source.gif";
    public static final String IMAGE_PAN = "/icons/pan_mode.gif";
    public static final String IMAGE_ZOOMIN = "/icons/zoom_in_co.gif";
    public static final String IMAGE_ZOOMOUT = "/icons/zoom_out_co.gif";
    public static final String IMAGE_FULLEXTENT = "/icons/zoom_extent_co.gif";

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

            File tempDir = new File(System.getProperty("java.io.tmpdir"));
            File temporaryFile = new File(tempDir, key);
            temporaryFile.getParentFile().mkdirs();
            InputStream in = getClass().getResourceAsStream(key);
            FileOutputStream out = new FileOutputStream(temporaryFile);
            byte[] buffer = new byte[1024];
            int len;
            while( (len = in.read(buffer)) != -1 ) {
                out.write(buffer, 0, len);
            }

            // File resourceFile = new File(resourceUrl.toURI());
            image = new Image(Display.getCurrent(), temporaryFile.getAbsolutePath());
            // } catch (URISyntaxException e) {
            // e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
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

    /**
     * Method to add images with key.
     * 
     * <p>This is handy for example in cases in which 
     * it is not possible to retrieve the images from
     * within the bundle. Ex. eclipse plugin environment.
     *
     * @param key the key for the image.
     * @param image the image to add.
     */
    public void addImage( String key, Image image ) {
        imageMap.put(key, image);
    }

    /**
     * Getter for the list of keys of the images, which are also the relative path.
     * 
     * @return the list of keys.
     */
    public List<String> getRelativePaths() {
        return Arrays.asList(CHECKED, UNCHECKED, STYLE, GRID, FEATURE, UP, DOWN, OPEN, REMOVE_LAYER, IMAGE_INFO, IMAGE_INFO_ICON,
                IMAGE_PAN, IMAGE_ZOOMIN, IMAGE_ZOOMOUT, IMAGE_FULLEXTENT);
    }
}
