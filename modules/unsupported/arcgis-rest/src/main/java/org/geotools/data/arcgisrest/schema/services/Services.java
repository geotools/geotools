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

package org.geotools.data.arcgisrest.schema.services;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Services {

    /** (Required) */
    @SerializedName("currentVersion")
    @Expose
    private Double currentVersion;
    /** (Required) */
    @SerializedName("services")
    @Expose
    private List<Service> services = new ArrayList<>();

    /** (Required) */
    public Double getCurrentVersion() {
        return currentVersion;
    }

    /** (Required) */
    public void setCurrentVersion(Double currentVersion) {
        this.currentVersion = currentVersion;
    }

    /** (Required) */
    public List<Service> getServices() {
        return services;
    }

    /** (Required) */
    public void setServices(List<Service> services) {
        this.services = services;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Services.class.getName())
                .append('@')
                .append(Integer.toHexString(System.identityHashCode(this)))
                .append('[');
        sb.append("currentVersion");
        sb.append('=');
        sb.append(((this.currentVersion == null) ? "<null>" : this.currentVersion));
        sb.append(',');
        sb.append("services");
        sb.append('=');
        sb.append(((this.services == null) ? "<null>" : this.services));
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
        result = ((result * 31) + ((this.currentVersion == null) ? 0 : this.currentVersion.hashCode()));
        result = ((result * 31) + ((this.services == null) ? 0 : this.services.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Services) == false) {
            return false;
        }
        Services rhs = ((Services) other);
        return Objects.equals(this.currentVersion, rhs.currentVersion) && Objects.equals(this.services, rhs.services);
    }
}
