/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jackson.datatype.projjson.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an identifier in PROJJSON.
 *
 * <p>An identifier consists of an authority (organization or namespace) and a code that is unique within that
 * authority. For example, "EPSG:4326" would be represented with the authority "EPSG" and the code "4326".
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Identifier {

    private String authority;
    private Object code;
    private String version;
    private String uri;

    /** Creates a new Identifier with default values. */
    public Identifier() {
        // Default constructor for Jackson
    }

    /**
     * Gets the authority of the identifier.
     *
     * @return The authority name, e.g., "EPSG"
     */
    @JsonProperty("authority")
    public String getAuthority() {
        return authority;
    }

    /**
     * Sets the authority of the identifier.
     *
     * @param authority The authority name
     */
    public void setAuthority(String authority) {
        this.authority = authority;
    }

    /**
     * Gets the code of the identifier.
     *
     * <p>The code can be either a string or a number in PROJJSON.
     *
     * @return The code, e.g., "4326" or 4326
     */
    @JsonProperty("code")
    public Object getCode() {
        return code;
    }

    /**
     * Sets the code of the identifier.
     *
     * @param code The code
     */
    public void setCode(Object code) {
        this.code = code;
    }

    /**
     * Gets the version of the authority.
     *
     * @return The version, e.g., "9.0"
     */
    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    /**
     * Sets the version of the authority.
     *
     * @param version The version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Gets the URI associated with this identifier.
     *
     * @return The URI
     */
    @JsonProperty("uri")
    public String getUri() {
        return uri;
    }

    /**
     * Sets the URI associated with this identifier.
     *
     * @param uri The URI
     */
    public void setUri(String uri) {
        this.uri = uri;
    }
}
