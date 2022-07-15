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

import static org.geotools.stac.client.STACClient.GEOJSON_MIME;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Landing page for STAC APIs, basically a document with links, with the addition of conformance
 * classes.
 */
public class STACLandingPage extends AbstractDocument {

    private static final String SERCH_REL = "search";

    @JsonProperty("conformsTo")
    List<String> conformance;

    public List<String> getConformance() {
        return conformance;
    }

    public void setConformance(List<String> conformance) {
        this.conformance = conformance;
    }

    public String getSearchLink(HttpMethod method) {
        return getLinks().stream()
                .filter(l -> isSearchLink(l, method))
                .map(l -> l.getHref())
                .findFirst()
                .orElse(null);
    }

    private boolean isSearchLink(Link l, HttpMethod method) {
        return SERCH_REL.equals(l.getRel())
                && (GEOJSON_MIME.equals(l.getType()) || l.getType() == null)
                && (l.getMethod() == null || l.getMethod() == method);
    }
}
