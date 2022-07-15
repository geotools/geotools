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

import java.util.List;

public enum FeaturesConformance {
    CORE("core"),
    GEOJSON("geojson");

    private static final String PREFIX = "http://www.opengis.net/spec/ogcapi-features-1/1.0/conf/";
    private final String conformance;

    FeaturesConformance(String suffix) {
        this.conformance = PREFIX + suffix;
    }

    /**
     * Checks if this Features conformance class has an exact match
     *
     * @param conformance
     */
    public boolean matches(List<String> conformance) {
        return conformance.stream().anyMatch(c -> this.conformance.equals(c));
    }
}
