package org.geotools.data.arcgisrest.schema.services.feature;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

    /**
     * (Required)
     *
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * (Required)
     *
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * (Required)
     *
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * (Required)
     *
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * (Required)
     *
     * @return The parentLayerId
     */
    public Integer getParentLayerId() {
        return parentLayerId;
    }

    /**
     * (Required)
     *
     * @param parentLayerId The parentLayerId
     */
    public void setParentLayerId(Integer parentLayerId) {
        this.parentLayerId = parentLayerId;
    }

    /**
     * (Required)
     *
     * @return The defaultVisibility
     */
    public Boolean getDefaultVisibility() {
        return defaultVisibility;
    }

    /**
     * (Required)
     *
     * @param defaultVisibility The defaultVisibility
     */
    public void setDefaultVisibility(Boolean defaultVisibility) {
        this.defaultVisibility = defaultVisibility;
    }

    /**
     * (Required)
     *
     * @return The subLayerIds
     */
    public Object getSubLayerIds() {
        return subLayerIds;
    }

    /**
     * (Required)
     *
     * @param subLayerIds The subLayerIds
     */
    public void setSubLayerIds(Object subLayerIds) {
        this.subLayerIds = subLayerIds;
    }

    /**
     * (Required)
     *
     * @return The minScale
     */
    public Integer getMinScale() {
        return minScale;
    }

    /**
     * (Required)
     *
     * @param minScale The minScale
     */
    public void setMinScale(Integer minScale) {
        this.minScale = minScale;
    }

    /**
     * (Required)
     *
     * @return The maxScale
     */
    public Integer getMaxScale() {
        return maxScale;
    }

    /**
     * (Required)
     *
     * @param maxScale The maxScale
     */
    public void setMaxScale(Integer maxScale) {
        this.maxScale = maxScale;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(name)
                .append(parentLayerId)
                .append(defaultVisibility)
                .append(subLayerIds)
                .append(minScale)
                .append(maxScale)
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
                .append(id, rhs.id)
                .append(name, rhs.name)
                .append(parentLayerId, rhs.parentLayerId)
                .append(defaultVisibility, rhs.defaultVisibility)
                .append(subLayerIds, rhs.subLayerIds)
                .append(minScale, rhs.minScale)
                .append(maxScale, rhs.maxScale)
                .isEquals();
    }
}
