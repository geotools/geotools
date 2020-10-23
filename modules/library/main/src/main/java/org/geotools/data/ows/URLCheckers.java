/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ows;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.factory.FactoryRegistry;

/**
 * @author ImranR
 *     <p>URL Checkers
 *     <p>Scans for implementations of
 *     <p>org.geotools.data.ows.URLChecker
 */
public class URLCheckers {

    // list contains all known org.geotools.data.ows.URLChecker implementations

    private static List<URLChecker> urlCheckerList;

    private static FactoryRegistry registry;

    private static void initEmptyUrlCheckList() {
        urlCheckerList = new ArrayList<URLChecker>(CommonFactoryFinder.getURLCheckers(null));
    }

    /** @return unmodifiable Collection of URLChecker implementations */
    public static List<URLChecker> getURLCheckerList() {
        if (urlCheckerList == null) initEmptyUrlCheckList();
        return (Collections.unmodifiableList(urlCheckerList));
    }

    public static List<URLChecker> getEnabledURLCheckerList() {
        return getURLCheckerList().stream().filter(u -> u.isEnabled()).collect(Collectors.toList());
    }

    public static synchronized void addURLChecker(URLChecker urlChecker) {
        if (urlCheckerList == null) initEmptyUrlCheckList();
        urlCheckerList.add(urlChecker);
    }

    public static synchronized boolean removeURLChecker(URLChecker urlChecker) {
        if (urlCheckerList == null) initEmptyUrlCheckList();
        return urlCheckerList.remove(urlChecker);
    }

    /**
     * This methods evaluates the passed URL against all enabled URLChecker
     *
     * @param URL to evaluate using all available URLCheckers
     * @return boolean
     * @throws IOException
     */
    public static boolean evaluate(URL url) throws IOException {
        return evaluate(url.toExternalForm());
    }

    /**
     * This methods evaluates the passed URI against all enabled URLChecker
     *
     * @param URI to evaluate using all available URLCheckers
     * @return boolean
     * @throws IOException
     */
    public static boolean evaluate(URI uri) throws IOException {
        return evaluate(uri.toURL());
    }

    /**
     * This methods evaluates the passed String (expected to be a URL or URI) against all enabled
     * URLChecker
     *
     * @param String(URI/URI) to evaluate using all available URLCheckers
     * @return boolean
     * @throws IOException
     */
    public static synchronized boolean evaluate(String url) throws IOException {
        // get enabled URLChecker only
        List<URLChecker> enabledURLCheckrList = getEnabledURLCheckerList();
        // no evaluators.. dont do anything
        if (enabledURLCheckrList.isEmpty()) return true;
        // evaluate using all available implementations
        for (URLChecker urlChecker : enabledURLCheckrList) {
            if (urlChecker.evaluate(url)) return true;
        }
        // no URLChecker evaluated the passed URL/URI
        throw new IOException("Evaluation Failure: " + url + ": did not pass security evaluation");
    }
}
