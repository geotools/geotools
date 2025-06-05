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

package org.geotools.data.arcgisrest.schema.query;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Layer {

    /** (Required) */
    @SerializedName("objectIdFieldName")
    @Expose
    private String objectIdFieldName;
    /** (Required) */
    @SerializedName("globalIdFieldName")
    @Expose
    private String globalIdFieldName;
    /** (Required) */
    @SerializedName("geometryType")
    @Expose
    private String geometryType;
    /** (Required) */
    @SerializedName("spatialReference")
    @Expose
    private SpatialReference spatialReference;
    /** (Required) */
    @SerializedName("fields")
    @Expose
    private List<Object> fields = new ArrayList<>();
    /** (Required) */
    @SerializedName("features")
    @Expose
    private List<Object> features = new ArrayList<>();

    /** (Required) */
    public String getObjectIdFieldName() {
        return objectIdFieldName;
    }

    /** (Required) */
    public void setObjectIdFieldName(String objectIdFieldName) {
        this.objectIdFieldName = objectIdFieldName;
    }

    /** (Required) */
    public String getGlobalIdFieldName() {
        return globalIdFieldName;
    }

    /** (Required) */
    public void setGlobalIdFieldName(String globalIdFieldName) {
        this.globalIdFieldName = globalIdFieldName;
    }

    /** (Required) */
    public String getGeometryType() {
        return geometryType;
    }

    /** (Required) */
    public void setGeometryType(String geometryType) {
        this.geometryType = geometryType;
    }

    /** (Required) */
    public SpatialReference getSpatialReference() {
        return spatialReference;
    }

    /** (Required) */
    public void setSpatialReference(SpatialReference spatialReference) {
        this.spatialReference = spatialReference;
    }

    /** (Required) */
    public List<Object> getFields() {
        return fields;
    }

    /** (Required) */
    public void setFields(List<Object> fields) {
        this.fields = fields;
    }

    /** (Required) */
    public List<Object> getFeatures() {
        return features;
    }

    /** (Required) */
    public void setFeatures(List<Object> features) {
        this.features = features;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Layer.class.getName())
                .append('@')
                .append(Integer.toHexString(System.identityHashCode(this)))
                .append('[');
        sb.append("objectIdFieldName");
        sb.append('=');
        sb.append(((this.objectIdFieldName == null) ? "<null>" : this.objectIdFieldName));
        sb.append(',');
        sb.append("globalIdFieldName");
        sb.append('=');
        sb.append(((this.globalIdFieldName == null) ? "<null>" : this.globalIdFieldName));
        sb.append(',');
        sb.append("geometryType");
        sb.append('=');
        sb.append(((this.geometryType == null) ? "<null>" : this.geometryType));
        sb.append(',');
        sb.append("spatialReference");
        sb.append('=');
        sb.append(((this.spatialReference == null) ? "<null>" : this.spatialReference));
        sb.append(',');
        sb.append("fields");
        sb.append('=');
        sb.append(((this.fields == null) ? "<null>" : this.fields));
        sb.append(',');
        sb.append("features");
        sb.append('=');
        sb.append(((this.features == null) ? "<null>" : this.features));
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
        result = ((result * 31) + ((this.features == null) ? 0 : this.features.hashCode()));
        result = ((result * 31) + ((this.globalIdFieldName == null) ? 0 : this.globalIdFieldName.hashCode()));
        result = ((result * 31) + ((this.objectIdFieldName == null) ? 0 : this.objectIdFieldName.hashCode()));
        result = ((result * 31) + ((this.spatialReference == null) ? 0 : this.spatialReference.hashCode()));
        result = ((result * 31) + ((this.fields == null) ? 0 : this.fields.hashCode()));
        result = ((result * 31) + ((this.geometryType == null) ? 0 : this.geometryType.hashCode()));
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
        return Objects.equals(this.features, rhs.features)
                && Objects.equals(this.globalIdFieldName, rhs.globalIdFieldName)
                && Objects.equals(this.objectIdFieldName, rhs.objectIdFieldName)
                && Objects.equals(this.spatialReference, rhs.spatialReference)
                && Objects.equals(this.fields, rhs.fields)
                && Objects.equals(this.geometryType, rhs.geometryType);
    }
}
