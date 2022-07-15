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

import java.util.Map;

/** A Link with common (and less common) properties found in OGC APIs */
public class Link extends AnyJSONObject {

    String href;
    String rel;
    String type;
    String title;
    Boolean templated;
    Boolean merge;
    Map<String, Object> body;

    HttpMethod method;

    public Link() {}

    public Link(String href, String rel, String type, String title, String classification) {
        this.href = href;
        this.rel = rel;
        this.type = type;
        this.title = title;
    }

    public Link(String href, String rel, String type, String title) {
        this.href = href;
        this.rel = rel;
        this.type = type;
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isTemplated() {
        return templated;
    }

    public void setTemplated(Boolean templated) {
        this.templated = templated;
    }

    public Boolean getMerge() {
        return merge;
    }

    public void setMerge(Boolean merge) {
        this.merge = merge;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public void setBody(Map<String, Object> body) {
        this.body = body;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "Link{"
                + "href='"
                + href
                + '\''
                + ", rel='"
                + rel
                + '\''
                + ", type='"
                + type
                + '\''
                + ", title='"
                + title
                + '\''
                + ", templated="
                + templated
                + ", merge="
                + merge
                + ", body="
                + body
                + ", method="
                + method
                + '}';
    }
}
