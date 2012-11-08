/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.geotools.data.DataUtilities;

/**
 * Default locator for online resources. Searches by absolute URL, relative
 * path w.r.t. to SLD document or classpath.
 * 
 * @author Jan De Moerloose
 * 
 */
public class DefaultResourceLocator implements ResourceLocator {

    URL sourceUrl;

    private static final java.util.logging.Logger LOGGER = org.geotools.util.logging.Logging
        .getLogger("org.geotools.styling");

    public void setSourceUrl(URL sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
    
    public URL locateResource(String uri) {
        URL url = null;
        try {
            url = new URL(uri);

            //url is valid, check if it is relative
            File f = DataUtilities.urlToFile(url);
            if (f != null && !f.isAbsolute()) {
                //ok, relative url, if the file exists when we are ok
                if (!f.exists()) {
                    URL relativeUrl = makeRelativeURL(f.getPath());
                    if (relativeUrl != null) {
                        f = DataUtilities.urlToFile(relativeUrl);
                        if (f.exists()) {
                            //bingo!
                            url = relativeUrl;
                        }
                    }
                }
            }
        } catch (MalformedURLException mfe) {
            LOGGER.fine("Looks like " + uri + " is a relative path..");
            if (sourceUrl != null) {
                url = makeRelativeURL(uri);
            }
            if (url == null)
            {
                url = getClass().getResource(uri);
                if (url == null)
                        LOGGER.warning("can't parse " + uri + " as a java resource present in the classpath");
            }
        }
        return url;
    }

    URL makeRelativeURL(String uri) {
        try {
            return new URL(sourceUrl, uri);
        } catch (MalformedURLException e) {
            LOGGER.warning("can't parse " + uri + " as relative to"
                    + sourceUrl.toExternalForm());
        }
        return null;
    }
}
