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
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @author ImranR
 *     <p>A Wrapper designed to evaluate HTTPClient implementations`s GET/POST requests through
 *     evaluations provided through implementations of
 */
public class ControlledHttpClient extends DelegateHTTPClient {

    private List<URLChecker> urlCheckerList = null;

    /** @param delegate */
    public ControlledHttpClient(HTTPClient delegate) {
        super(delegate);
    }

    public ControlledHttpClient(HTTPClient delegate, List<URLChecker> urlCheckerList) {
        super(delegate);
        this.urlCheckerList = urlCheckerList;
    }

    @Override
    public HTTPResponse post(URL url, InputStream postContent, String postContentType)
            throws IOException {
        if (evaluate(url)) return super.post(url, postContent, postContentType);
        else {
            throw new IOException(
                    "Evaluation Failure: "
                            + url.toExternalForm()
                            + ": did not pass security evaluation");
        }
    }

    @Override
    public HTTPResponse get(URL url) throws IOException {
        if (evaluate(url)) return super.get(url);
        else {
            throw new IOException(
                    "Evaluation Failure: "
                            + url.toExternalForm()
                            + ": did not pass security evaluation");
        }
    }

    @Override
    public HTTPResponse get(URL url, Map<String, String> headers) throws IOException {
        if (evaluate(url)) return super.get(url, headers);
        else {
            throw new IOException(
                    "Evaluation Failure: "
                            + url.toExternalForm()
                            + ": did not pass security evaluation");
        }
    }

    private boolean evaluate(URL url) {
        List<URLChecker> effectiveList = getEffectiveList();
        if (effectiveList.isEmpty()) return true;
        boolean passed = false;
        for (URLChecker urlChecker : effectiveList) {
            passed |= (!urlChecker.isEnabled() || urlChecker.evaluate(url.toExternalForm()));
        }
        // should pass atleast one validation check
        return passed;
    }

    private List<URLChecker> getEffectiveList() {
        // give precedence to list set through constructor
        if (urlCheckerList != null) return urlCheckerList;

        // else resolve to defaults on each request, since there state might change
        return URLCheckers.getEnabledURLCheckerList();
    }
}
