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
import java.util.ServiceLoader;
import java.util.stream.Collectors;

/**
 * @author ImranR
 *     <p>URL Checker Factory
 *     <p>Scans for implementations of
 *     <p>org.geotools.data.ows.URLChecker
 */
public class URLCheckerFactory {

    // list contains all known org.geotools.data.ows.URLChecker implementations

    private static List<URLChecker> urlCheckerList;

    static {
        urlCheckerList = URLCheckerFactory.initEvaluators(URLChecker.class);
    }

    private static <T> List<T> initEvaluators(Class<T> type) {
        // allowing scanning through SDI
        ServiceLoader<T> loader = ServiceLoader.load(type);
        loader.reload();
        List<T> urlCheckerList = new ArrayList<>();
        for (T aLoader : loader) {
            urlCheckerList.add(aLoader);
        }
        return urlCheckerList;
    }

    /** @return unmodifiable Collection of URLChecker implementations */
    public static List<URLChecker> getUrlCheckerList() {
        return (Collections.unmodifiableList(urlCheckerList));
    }

    public static List<URLChecker> getEnabledUrlCheckerList() {
        return getUrlCheckerList().stream().filter(u -> u.isEnabled()).collect(Collectors.toList());
    }

    public static synchronized void addURLChecker(URLChecker urlChecker) {
        urlCheckerList.add(urlChecker);
    }

    public static synchronized boolean removeURLChecker(URLChecker urlChecker) {
        return urlCheckerList.remove(urlChecker);
    }

    /**
     * A Utility method
     *
     * @param URL to evaluate using all available URLCheckers
     * @return boolean
     * @throws IOException
     */
    public static boolean evaluate(URL url) throws IOException {
        return evaluate(url.toExternalForm());
    }

    /**
     * A Utility method
     *
     * @param URI to evaluate using all available URLCheckers
     * @return boolean
     * @throws IOException
     */
    public static boolean evaluate(URI uri) throws IOException {
        return evaluate(uri.toURL());
    }

    /**
     * A Utility method
     *
     * @param String(URI/URI) to evaluate using all available URLCheckers
     * @return boolean
     * @throws IOException
     */
    public static synchronized boolean evaluate(String url) throws IOException {
        //        throw new IOException(
        //                ">>>Evaluation Failure: " + url + ": did not pass security evaluation");
        // get enabled URLChecker only
        List<URLChecker> enabledURLCheckrList = getEnabledUrlCheckerList();
        // no evaluators.. dont do anything
        if (enabledURLCheckrList.isEmpty()) return true;
        boolean passed = false;
        // evaluate using all available implementations
        for (URLChecker urlChecker : enabledURLCheckrList) {
            passed |= urlChecker.evaluate(url);
        }
        // passed all evaluation
        if (passed) return passed;
        else
            throw new IOException(
                    ">>>Evaluation Failure: " + url + ": did not pass security evaluation");
    }
}
