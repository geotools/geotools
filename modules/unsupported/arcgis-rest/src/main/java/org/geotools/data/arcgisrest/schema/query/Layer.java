package org.geotools.data.arcgisrest.schema.query;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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
    private List<Object> fields = new ArrayList<Object>();
    /** (Required) */
    @SerializedName("features")
    @Expose
    private List<Object> features = new ArrayList<Object>();

    /**
     * (Required)
     *
     * @return The objectIdFieldName
     */
    public String getObjectIdFieldName() {
        return objectIdFieldName;
    }

    /**
     * (Required)
     *
     * @param objectIdFieldName The objectIdFieldName
     */
    public void setObjectIdFieldName(String objectIdFieldName) {
        this.objectIdFieldName = objectIdFieldName;
    }

    /**
     * (Required)
     *
     * @return The globalIdFieldName
     */
    public String getGlobalIdFieldName() {
        return globalIdFieldName;
    }

    /**
     * (Required)
     *
     * @param globalIdFieldName The globalIdFieldName
     */
    public void setGlobalIdFieldName(String globalIdFieldName) {
        this.globalIdFieldName = globalIdFieldName;
    }

    /**
     * (Required)
     *
     * @return The geometryType
     */
    public String getGeometryType() {
        return geometryType;
    }

    /**
     * (Required)
     *
     * @param geometryType The geometryType
     */
    public void setGeometryType(String geometryType) {
        this.geometryType = geometryType;
    }

    /**
     * (Required)
     *
     * @return The spatialReference
     */
    public SpatialReference getSpatialReference() {
        return spatialReference;
    }

    /**
     * (Required)
     *
     * @param spatialReference The spatialReference
     */
    public void setSpatialReference(SpatialReference spatialReference) {
        this.spatialReference = spatialReference;
    }

    /**
     * (Required)
     *
     * @return The fields
     */
    public List<Object> getFields() {
        return fields;
    }

    /**
     * (Required)
     *
     * @param fields The fields
     */
    public void setFields(List<Object> fields) {
        this.fields = fields;
    }

    /**
     * (Required)
     *
     * @return The features
     */
    public List<Object> getFeatures() {
        return features;
    }

    /**
     * (Required)
     *
     * @param features The features
     */
    public void setFeatures(List<Object> features) {
        this.features = features;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(objectIdFieldName)
                .append(globalIdFieldName)
                .append(geometryType)
                .append(spatialReference)
                .append(fields)
                .append(features)
                .toHashCode();
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
        return new EqualsBuilder()
                .append(objectIdFieldName, rhs.objectIdFieldName)
                .append(globalIdFieldName, rhs.globalIdFieldName)
                .append(geometryType, rhs.geometryType)
                .append(spatialReference, rhs.spatialReference)
                .append(fields, rhs.fields)
                .append(features, rhs.features)
                .isEquals();
    }
}
