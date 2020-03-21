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
import org.geotools.util.URLs;

/**
 * Default locator for online resources. Searches by absolute URL, relative path w.r.t. to SLD
 * document or classpath.
 *
 * @author Jan De Moerloose
 */
public class DefaultResourceLocator implements ResourceLocator {

    URL sourceUrl;

    private static final java.util.logging.Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(DefaultResourceLocator.class);

    public void setSourceUrl(URL sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public URL locateResource(String uri) {
        URL url = null;
        try {
            url = new URL(uri);

            File f = URLs.urlToFile(url);
            if (f != null && !f.isAbsolute()) {
                // ok, relative url, if the file exists when we are ok
                if (!f.exists() && sourceUrl != null) {
                    String query = getQueryPart(url);
                    URL relativeUrl = makeRelativeURL(f.getPath(), query);
                    if (relativeUrl != null) {
                        URL validated = validateRelativeURL(relativeUrl);
                        if (validated != null) {
                            url = validated;
                        }
                    }
                }
            }
        } catch (MalformedURLException mfe) {
            LOGGER.fine("Looks like " + uri + " is a relative path..");
            if (sourceUrl != null) {
                url = makeRelativeURL(uri, null);
            }
            if (url == null) {
                url = getClass().getResource(uri);
                if (url == null)
                    LOGGER.warning(
                            "can't parse " + uri + " as a java resource present in the classpath");
            }
        }
        return url;
    }

    /**
     * Variant to URL.getQuery() that won't balk at having # in the query (e.g., a color in a
     * parametric SVG reference), e.g. <code>firestation.svg?fill=#FF0000</code>
     */
    private String getQueryPart(URL url) {
        String external = url.toExternalForm();
        int idx = external.indexOf('?');
        if (idx == -1 || idx == external.length() - 1) {
            return null;
        } else {
            return external.substring(idx + 1);
        }
    }

    protected URL validateRelativeURL(URL relativeUrl) {
        File f;
        f = URLs.urlToFile(relativeUrl);
        if (f != null && f.exists()) {
            // bingo!
            return relativeUrl;
        }
        return null;
    }

    protected URL makeRelativeURL(String uri, String query) {
        try {
            if (query != null) {
                return new URL(sourceUrl, uri + "?" + query);
            } else {
                return new URL(sourceUrl, uri);
            }
        } catch (MalformedURLException e) {
            LOGGER.warning("can't parse " + uri + " as relative to" + sourceUrl.toExternalForm());
        }
        return null;
    }
}
