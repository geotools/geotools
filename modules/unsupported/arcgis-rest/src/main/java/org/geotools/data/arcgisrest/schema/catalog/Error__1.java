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
import java.util.Objects;

public class Error__1 {

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

    /** (Required) */
    public Integer getCode() {
        return code;
    }

    /** (Required) */
    public void setCode(Integer code) {
        this.code = code;
    }

    /** (Required) */
    public String getMessage() {
        return message;
    }

    /** (Required) */
    public void setMessage(String message) {
        this.message = message;
    }

    /** (Required) */
    public List<String> getDetails() {
        return details;
    }

    /** (Required) */
    public void setDetails(List<String> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Error__1.class.getName())
                .append('@')
                .append(Integer.toHexString(System.identityHashCode(this)))
                .append('[');
        sb.append("code");
        sb.append('=');
        sb.append(((this.code == null) ? "<null>" : this.code));
        sb.append(',');
        sb.append("message");
        sb.append('=');
        sb.append(((this.message == null) ? "<null>" : this.message));
        sb.append(',');
        sb.append("details");
        sb.append('=');
        sb.append(((this.details == null) ? "<null>" : this.details));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result * 31) + ((this.details == null) ? 0 : this.details.hashCode()));
        result = ((result * 31) + ((this.code == null) ? 0 : this.code.hashCode()));
        result = ((result * 31) + ((this.message == null) ? 0 : this.message.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Error__1) == false) {
            return false;
        }
        Error__1 rhs = ((Error__1) other);
        return Objects.equals(this.details, rhs.details)
                && Objects.equals(this.code, rhs.code)
                && Objects.equals(this.message, rhs.message);
    }
}
