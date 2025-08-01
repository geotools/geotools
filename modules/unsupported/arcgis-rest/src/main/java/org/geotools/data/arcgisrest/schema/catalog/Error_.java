/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.data.arcgisrest.schema.catalog;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Error_ {

    /** (Required) */
    @SerializedName("code")
    @Expose
    private Integer code;
    /** (Required) */
    @SerializedName("message")
    @Expose
    private String message;
    /** (Required) */
    @SerializedName("details")
    @Expose
    private List<String> details = new ArrayList<>();

    /**
     * (Required)
     *
     * @return The code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * (Required)
     *
     * @param code The code
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * (Required)
     *
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * (Required)
     *
     * @param message The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * (Required)
     *
     * @return The details
     */
    public List<String> getDetails() {
        return details;
    }

    /**
     * (Required)
     *
     * @param details The details
     */
    public void setDetails(List<String> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(code)
                .append(message)
                .append(details)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Error_) == false) {
            return false;
        }
        Error_ rhs = ((Error_) other);
        return new EqualsBuilder()
                .append(code, rhs.code)
                .append(message, rhs.message)
                .append(details, rhs.details)
                .isEquals();
    }
}
