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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import org.locationtech.jts.geom.Geometry;
import org.opengis.filter.Filter;

/** Represents a STAC query used in search requests */
public class SearchQuery {

    private List<String> collections;
    private double[] bbox;
    private Integer limit;
    private String datetime;
    Geometry intersects;

    @JsonProperty("filter-lang")
    FilterLang filterLang;

    @JsonSerialize(using = STACFilterSerializer.class)
    private Filter filter;

    public List<String> getCollections() {
        return collections;
    }

    public void setCollections(List<String> collections) {
        this.collections = collections;
    }

    public double[] getBbox() {
        return bbox;
    }

    public void setBbox(double[] bbox) {
        this.bbox = bbox;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Filter getFilter() {
        return filter;
    }

    // Using JsonNode to prevent Jackson from trying to parse into object if is CQL2-JSON
    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public FilterLang getFilterLang() {
        return filterLang;
    }

    public void setFilterLang(FilterLang filterLang) {
        this.filterLang = filterLang;
    }

    public Geometry getIntersects() {
        return intersects;
    }

    public void setIntersects(Geometry intersects) {
        this.intersects = intersects;
    }
}
