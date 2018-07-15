package org.geotools.data.arcgisrest.schema.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Extent {

    /** (Required) */
    @SerializedName("xmin")
    @Expose
    private Double xmin;
    /** (Required) */
    @SerializedName("ymin")
    @Expose
    private Double ymin;
    /** (Required) */
    @SerializedName("xmax")
    @Expose
    private Double xmax;
    /** (Required) */
    @SerializedName("ymax")
    @Expose
    private Double ymax;
    /** (Required) */
    @SerializedName("spatialReference")
    @Expose
    private SpatialReference spatialReference;

    /**
     * (Required)
     *
     * @return The xmin
     */
    public Double getXmin() {
        return xmin;
    }

    /**
     * (Required)
     *
     * @param xmin The xmin
     */
    public void setXmin(Double xmin) {
        this.xmin = xmin;
    }

    /**
     * (Required)
     *
     * @return The ymin
     */
    public Double getYmin() {
        return ymin;
    }

    /**
     * (Required)
     *
     * @param ymin The ymin
     */
    public void setYmin(Double ymin) {
        this.ymin = ymin;
    }

    /**
     * (Required)
     *
     * @return The xmax
     */
    public Double getXmax() {
        return xmax;
    }

    /**
     * (Required)
     *
     * @param xmax The xmax
     */
    public void setXmax(Double xmax) {
        this.xmax = xmax;
    }

    /**
     * (Required)
     *
     * @return The ymax
     */
    public Double getYmax() {
        return ymax;
    }

    /**
     * (Required)
     *
     * @param ymax The ymax
     */
    public void setYmax(Double ymax) {
        this.ymax = ymax;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(xmin)
                .append(ymin)
                .append(xmax)
                .append(ymax)
                .append(spatialReference)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Extent) == false) {
            return false;
        }
        Extent rhs = ((Extent) other);
        return new EqualsBuilder()
                .append(xmin, rhs.xmin)
                .append(ymin, rhs.ymin)
                .append(xmax, rhs.xmax)
                .append(ymax, rhs.ymax)
                .append(spatialReference, rhs.spatialReference)
                .isEquals();
    }
}
