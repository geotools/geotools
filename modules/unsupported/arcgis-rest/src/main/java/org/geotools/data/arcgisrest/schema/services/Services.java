package org.geotools.data.arcgisrest.schema.services;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Services {

    /** (Required) */
    @SerializedName("currentVersion")
    @Expose
    private Double currentVersion;
    /** (Required) */
    @SerializedName("services")
    @Expose
    private List<Service> services = new ArrayList<Service>();

    /**
     * (Required)
     *
     * @return The currentVersion
     */
    public Double getCurrentVersion() {
        return currentVersion;
    }

    /**
     * (Required)
     *
     * @param currentVersion The currentVersion
     */
    public void setCurrentVersion(Double currentVersion) {
        this.currentVersion = currentVersion;
    }

    /**
     * (Required)
     *
     * @return The services
     */
    public List<Service> getServices() {
        return services;
    }

    /**
     * (Required)
     *
     * @param services The services
     */
    public void setServices(List<Service> services) {
        this.services = services;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(currentVersion).append(services).toHashCode();
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
        return new EqualsBuilder()
                .append(currentVersion, rhs.currentVersion)
                .append(services, rhs.services)
                .isEquals();
    }
}
