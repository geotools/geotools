package org.geotools.data.arcgisrest.schema.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Prototype {

    /** (Required) */
    @SerializedName("attributes")
    @Expose
    private Attributes attributes;

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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(attributes).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Prototype) == false) {
            return false;
        }
        Prototype rhs = ((Prototype) other);
        return new EqualsBuilder().append(attributes, rhs.attributes).isEquals();
    }
}
