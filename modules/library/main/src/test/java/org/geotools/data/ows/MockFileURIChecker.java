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
 * A Mock implementation intended to test Simple Dependency Injection
 *
 * @author ImranR
 */
public class MockFileURIChecker implements URLChecker {

    List<String> allowedList;

    boolean enabled = true;

    public MockFileURIChecker() {
        allowedList = Arrays.asList("ftp://user:pass@www.myserver.com", "file:///data_dir");
    }

    @Override
    public String getName() {
        return "MockFile";
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
