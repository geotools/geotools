/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.arcgisrest.schema.services.feature;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;

public class Layer {

    /** (Required) */
    @SerializedName("id")
    @Expose
    private Integer id;
    /** (Required) */
    @SerializedName("name")
    @Expose
    private String name;
    /** (Required) */
    @SerializedName("parentLayerId")
    @Expose
    private Integer parentLayerId;
    /** (Required) */
    @SerializedName("defaultVisibility")
    @Expose
    private Boolean defaultVisibility;
    /** (Required) */
    @SerializedName("subLayerIds")
    @Expose
    private Object subLayerIds;
    /** (Required) */
    @SerializedName("minScale")
    @Expose
    private Integer minScale;
    /** (Required) */
    @SerializedName("maxScale")
    @Expose
    private Integer maxScale;

    /** (Required) */
    public Integer getId() {
        return id;
    }

    /** (Required) */
    public void setId(Integer id) {
        this.id = id;
    }

    /** (Required) */
    public String getName() {
        return name;
    }

    /** (Required) */
    public void setName(String name) {
        this.name = name;
    }

    /** (Required) */
    public Integer getParentLayerId() {
        return parentLayerId;
    }

    /** (Required) */
    public void setParentLayerId(Integer parentLayerId) {
        this.parentLayerId = parentLayerId;
    }

    /** (Required) */
    public Boolean getDefaultVisibility() {
        return defaultVisibility;
    }

    /** (Required) */
    public void setDefaultVisibility(Boolean defaultVisibility) {
        this.defaultVisibility = defaultVisibility;
    }

    /** (Required) */
    public Object getSubLayerIds() {
        return subLayerIds;
    }

    /** (Required) */
    public void setSubLayerIds(Object subLayerIds) {
        this.subLayerIds = subLayerIds;
    }

    /** (Required) */
    public Integer getMinScale() {
        return minScale;
    }

    /** (Required) */
    public void setMinScale(Integer minScale) {
        this.minScale = minScale;
    }

    /** (Required) */
    public Integer getMaxScale() {
        return maxScale;
    }

    /** (Required) */
    public void setMaxScale(Integer maxScale) {
        this.maxScale = maxScale;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Layer.class.getName())
                .append('@')
                .append(Integer.toHexString(System.identityHashCode(this)))
                .append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null) ? "<null>" : this.id));
        sb.append(',');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null) ? "<null>" : this.name));
        sb.append(',');
        sb.append("parentLayerId");
        sb.append('=');
        sb.append(((this.parentLayerId == null) ? "<null>" : this.parentLayerId));
        sb.append(',');
        sb.append("defaultVisibility");
        sb.append('=');
        sb.append(((this.defaultVisibility == null) ? "<null>" : this.defaultVisibility));
        sb.append(',');
        sb.append("subLayerIds");
        sb.append('=');
        sb.append(((this.subLayerIds == null) ? "<null>" : this.subLayerIds));
        sb.append(',');
        sb.append("minScale");
        sb.append('=');
        sb.append(((this.minScale == null) ? "<null>" : this.minScale));
        sb.append(',');
        sb.append("maxScale");
        sb.append('=');
        sb.append(((this.maxScale == null) ? "<null>" : this.maxScale));
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
        result = ((result * 31) + ((this.subLayerIds == null) ? 0 : this.subLayerIds.hashCode()));
        result = ((result * 31) + ((this.maxScale == null) ? 0 : this.maxScale.hashCode()));
        result = ((result * 31) + ((this.name == null) ? 0 : this.name.hashCode()));
        result = ((result * 31) + ((this.defaultVisibility == null) ? 0 : this.defaultVisibility.hashCode()));
        result = ((result * 31) + ((this.id == null) ? 0 : this.id.hashCode()));
        result = ((result * 31) + ((this.minScale == null) ? 0 : this.minScale.hashCode()));
        result = ((result * 31) + ((this.parentLayerId == null) ? 0 : this.parentLayerId.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Layer) == false) {
            return false;
        }
        Layer rhs = ((Layer) other);
        return Objects.equals(this.subLayerIds, rhs.subLayerIds)
                && Objects.equals(this.maxScale, rhs.maxScale)
                && Objects.equals(this.name, rhs.name)
                && Objects.equals(this.defaultVisibility, rhs.defaultVisibility)
                && Objects.equals(this.id, rhs.id)
                && Objects.equals(this.minScale, rhs.minScale)
                && Objects.equals(this.parentLayerId, rhs.parentLayerId);
    }
}
