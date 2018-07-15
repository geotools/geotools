package org.geotools.data.arcgisrest.schema.query;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Feature {

    /** (Required) */
    @SerializedName("attributes")
    @Expose
    private Attributes attributes;
    /** (Required) */
    @SerializedName("geometry")
    @Expose
    private Geometry geometry;

    /**
     * (Required)
     *
     * @return The attributes
     */
    public Attributes getAttributes() {
        return attributes;
    }

    /**
     * (Required)
     *
     * @param attributes The attributes
     */
    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    /**
     * (Required)
     *
     * @return The geometry
     */
    public Geometry getGeometry() {
        return geometry;
    }

    /**
     * (Required)
     *
     * @param geometry The geometry
     */
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(attributes).append(geometry).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Feature) == false) {
            return false;
        }
        Feature rhs = ((Feature) other);
        return new EqualsBuilder()
                .append(attributes, rhs.attributes)
                .append(geometry, rhs.geometry)
                .isEquals();
    }
}
