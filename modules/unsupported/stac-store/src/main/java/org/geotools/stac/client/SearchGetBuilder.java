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

import static org.geotools.stac.client.FilterLang.CQL2_JSON;
import static org.geotools.stac.client.FilterLang.CQL2_TEXT;
import static org.geotools.stac.client.STACClient.OBJECT_MAPPER;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import org.apache.http.client.utils.URIBuilder;
import org.geootols.filter.text.cql_2.CQL2;
import org.geotools.filter.text.cqljson.CQL2Json;

/** Builds a Search GET URL based on the landing page and {@link SearchQuery} specification */
class SearchGetBuilder {

    private static final Collector<CharSequence, ?, String> COMMA_JOINER = Collectors.joining(",");

    STACLandingPage landingPage;

    public SearchGetBuilder(STACLandingPage landingPage) {
        this.landingPage = landingPage;
    }

    URL toGetURL(SearchQuery search) throws MalformedURLException, URISyntaxException {
        String base = landingPage.getSearchLink(HttpMethod.GET);
        if (base == null) {
            throw new IllegalArgumentException("Cannot find GeoJSON search GET link");
        }

        URIBuilder builder = new URIBuilder(base);
        if (search.getBbox() != null) {
            String spec =
                    DoubleStream.of(search.getBbox())
                            .mapToObj(String::valueOf)
                            .collect(COMMA_JOINER);
            builder.addParameter("bbox", spec);
        }
        if (search.getCollections() != null) {
            String spec = search.getCollections().stream().collect(COMMA_JOINER);
            builder.addParameter("collections", spec);
        }
        if (search.getDatetime() != null) builder.addParameter("datetime", search.getDatetime());
        if (search.getIntersects() != null) {
            try {
                builder.addParameter(
                        "intersects", OBJECT_MAPPER.writeValueAsString(search.getIntersects()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        if (search.getFilter() != null) {
            FilterLang lang = Optional.ofNullable(search.getFilterLang()).orElse(CQL2_TEXT);
            String spec;
            if (CQL2_TEXT.equals(lang)) spec = CQL2.toCQL2(search.getFilter());
            else if (CQL2_JSON.equals(lang)) spec = CQL2Json.toCQL2(search.getFilter());
            else throw new IllegalArgumentException("Unrecognized filter language: " + lang);
            builder.addParameter("filter", spec);
            builder.addParameter("filter-lang", lang.toString());
        }
        if (search.getLimit() != null) {
            builder.addParameter("limit", String.valueOf(search.getLimit()));
        }
        return builder.build().toURL();
    }
}
