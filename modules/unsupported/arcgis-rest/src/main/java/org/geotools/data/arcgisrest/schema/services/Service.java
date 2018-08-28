package org.geotools.data.arcgisrest.schema.services;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Service {

    /** (Required) */
    @SerializedName("name")
    @Expose
    private String name;
    /** (Required) */
    @SerializedName("type")
    @Expose
    private String type;
    /** (Required) */
    @SerializedName("url")
    @Expose
    private String url;

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
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * (Required)
     *
     * @param type The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * (Required)
     *
     * @return The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * (Required)
     *
     * @param url The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(type).append(url).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Service) == false) {
            return false;
        }
        Service rhs = ((Service) other);
        return new EqualsBuilder()
                .append(name, rhs.name)
                .append(type, rhs.type)
                .append(url, rhs.url)
                .isEquals();
    }
}
