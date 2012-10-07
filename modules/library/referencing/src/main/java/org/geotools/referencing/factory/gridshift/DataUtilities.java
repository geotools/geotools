/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.referencing.factory.gridshift;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * Part of DataUtilities from gt-main, which cannot be used here because of cyclic Maven
 * dependencies.
 * 
 * <p>
 * 
 * FIXME: module dependencies should be refactored until this class does not need to exist.
 * 
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 */
public class DataUtilities {

    /**
     * Copy of DataUtilities.urlToFile(URL url) in gt-main, which cannot be used here because of
     * cyclic Maven dependencies.
     */
    public static File urlToFile(URL url) {
        if (!"file".equals(url.getProtocol())) {
            return null; // not a File URL
        }
        String string = url.toExternalForm();
        if (string.contains("+")) {
            // this represents an invalid URL created using either
            // file.toURL(); or
            // file.toURI().toURL() on a specific version of Java 5 on Mac
            string = string.replace("+", "%2B");
        }
        try {
            string = URLDecoder.decode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Could not decode the URL to UTF-8 format", e);
        }
        String path3;
        String simplePrefix = "file:/";
        String standardPrefix = "file://";
        String os = System.getProperty("os.name");
        if (os.toUpperCase().contains("WINDOWS") && string.startsWith(standardPrefix)) {
            // win32: host/share reference
            path3 = string.substring(standardPrefix.length() - 2);
        } else if (string.startsWith(standardPrefix)) {
            path3 = string.substring(standardPrefix.length());
        } else if (string.startsWith(simplePrefix)) {
            path3 = string.substring(simplePrefix.length() - 1);
        } else {
            String auth = url.getAuthority();
            String path2 = url.getPath().replace("%20", " ");
            if (auth != null && !auth.equals("")) {
                path3 = "//" + auth + path2;
            } else {
                path3 = path2;
            }
        }
        return new File(path3);
    }
    
    /**
     * A replacement for File.toURI().toURL().
     * <p>
     * The handling of file.toURL() is broken; the handling of file.toURI().toURL() is known to be
     * broken on a few platforms like mac. We have the urlToFile( URL ) method that is able to
     * untangle both these problems and we use it in the geotools library.
     * <p>
     * However occasionally we need to pick up a file and hand it to a third party library like EMF;
     * this method performs a couple of sanity checks which we can use to prepare a good URL
     * reference to a file in these situtations.
     * 
     * @param file
     * @return URL
     */
    public static URL fileToURL(File file) {
        try {
            URL url = file.toURI().toURL();
            String string = url.toExternalForm();
            if (string.contains("+")) {
                // this represents an invalid URL created using either
                // file.toURL(); or
                // file.toURI().toURL() on a specific version of Java 5 on Mac
                string = string.replace("+", "%2B");
            }
            if (string.contains(" ")) {
                // this represents an invalid URL created using either
                // file.toURL(); or
                // file.toURI().toURL() on a specific version of Java 5 on Mac
                string = string.replace(" ", "%20");
            }
            return new URL(string);
        } catch (MalformedURLException e) {
            return null;
        }
    }

}
