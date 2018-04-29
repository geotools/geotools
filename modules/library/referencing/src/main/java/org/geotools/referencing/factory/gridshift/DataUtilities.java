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
import java.net.URL;
import org.geotools.util.URLs;

/** @deprecated Use {@link URLs} */
public class DataUtilities {

    /** @deprecated Use {@link URLs#fileToUrl(File)} */
    public static URL fileToURL(File file) {
        return URLs.fileToUrl(file);
    }

    /** @deprecated Use {@link URLs#urlToFile(URL)} */
    public static File urlToFile(URL url) {
        return URLs.urlToFile(url);
    }
}
