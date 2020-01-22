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

import java.util.Arrays;
import java.util.List;

/**
 * A Mock implementation to be used in unit tests ONLY
 *
 * <p>Follows simple string comparison for URL evaluation
 *
 * @author ImranR
 */
public class MockURLChecker implements URLChecker {

    List<String> allowedList = Arrays.asList("schemas.opengis.net"); // url calls

    private boolean enabled = true;

    public MockURLChecker() {
        allowedList = Arrays.asList("schemas.opengis.net", "schemas.xmlsoap.org");
    }

    @Override
    public String getName() {
        return "Mock";
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean evaluate(String url) {

        for (String allowed : allowedList) {
            // passed string literal is found
            if (url.indexOf(allowed) > -1) return true;
        }
        return false;
    }
}
