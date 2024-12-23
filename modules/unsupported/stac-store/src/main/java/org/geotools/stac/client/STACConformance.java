/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.stac.client;

import static java.util.regex.Pattern.quote;

import java.util.List;
import java.util.regex.Pattern;

public enum STACConformance {
    CORE("core"),
    COLLECTIONS("collections"),
    FEATURES("ogcapi-features"),
    ITEM_SEARCH("item-search"),
    CONTEXT("item-search#context"),
    FIELDS("item-search#fields"),
    SORT("item-search#sort"),
    QUERY("item-search#query"),
    FILTER("item-search#filter");

    private static final String STAC_PREFIX = "https://api.stacspec.org/v1.0.";
    private final Pattern pattern;

    STACConformance(String suffix) {
        this.pattern = Pattern.compile(quote(STAC_PREFIX) + ".*/" + quote(suffix) + ".*");
    }

    /**
     * Checks if this STAC conformance class is a match, ignoring details about the version such as beta/RC/minor
     * version changes (as long as it's a 1.0.x)
     *
     * @param conformance
     */
    public boolean matches(List<String> conformance) {
        return conformance.stream().anyMatch(c -> pattern.matcher(c).matches());
    }
}
