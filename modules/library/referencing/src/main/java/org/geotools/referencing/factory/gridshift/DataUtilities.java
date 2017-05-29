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

    private static final boolean IS_WINDOWS_OS = System.getProperty("os.name").toUpperCase()
            .contains("WINDOWS");

    /**
     * Takes a URL and converts it to a File. The attempts to deal with Windows UNC format specific
     * problems, specifically files located on network shares and different drives.
     * 
     * If the URL.getAuthority() returns null or is empty, then only the url's path property is used
     * to construct the file. Otherwise, the authority is prefixed before the path.
     * 
     * It is assumed that url.getProtocol returns "file".
     * 
     * Authority is the drive or network share the file is located on. Such as "C:", "E:",
     * "\\fooServer"
     * 
     * @param url
     *            a URL object that uses protocol "file"
     * @return a File that corresponds to the URL's location
     */
    public static File urlToFile(URL url) {
        if (!"file".equals(url.getProtocol())) {
            return null; // not a File URL
        }
        String string = url.toExternalForm();
        if( url.getQuery() != null){
            string = string.substring(0, string.indexOf("?"));
        }
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

        if (IS_WINDOWS_OS && string.startsWith(standardPrefix)) {
            // win32: host/share reference. Keep the host slashes.
            path3 = string.substring(standardPrefix.length() - 2);
            File f = new File(path3);
            if (!f.exists()) {
                // Make path relative to be backwards compatible.
                path3 = path3.substring(2, path3.length());
            }
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
     * A replacement for {@link File#toURL()} and <code>File.toURI().toURL()</code>.
     * <p>
     * {@link File#toURL()} does not percent-escape characters and <code>File.toURI().toURL()</code> does not percent-escape non-ASCII characters.
     * This method ensures that URL characters are correctly percent-escaped, and works around the reported misbehaviour of some Java implementations
     * on Mac.
     * 
     * @param file
     * @return URL
     */
    public static URL fileToURL(File file) {
        try {
            String string = file.toURI().toASCIIString();
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
