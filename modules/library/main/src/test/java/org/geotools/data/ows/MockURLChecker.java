/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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

import java.util.function.Function;

/** A mock URLChecker implementation for testing purposes. */
public class MockURLChecker implements URLChecker {

    private String name = "MockURLChecker";
    private boolean enabled = true;
    private Function<String, Boolean> checker;

    public MockURLChecker(String name, Function<String, Boolean> checker) {
        this.name = name;
        this.checker = checker;
    }

    public MockURLChecker(String name, boolean enabled, Function<String, Boolean> checker) {
        this.name = name;
        this.enabled = enabled;
        this.checker = checker;
    }

    public MockURLChecker(Function<String, Boolean> checker) {
        this.checker = checker;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean confirm(String location) {
        return checker.apply(location);
    }
}
