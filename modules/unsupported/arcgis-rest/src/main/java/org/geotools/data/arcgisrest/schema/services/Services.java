
package org.geotools.data.arcgisrest.schema.services;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Services {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("currentVersion")
    @Expose
    private Double currentVersion;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("services")
    @Expose
    private List<Service> services = new ArrayList<Service>();

    /**
     * 
     * (Required)
     * 
     */
    public Double getCurrentVersion() {
        return currentVersion;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setCurrentVersion(Double currentVersion) {
        this.currentVersion = currentVersion;
    }

    /**
     * 
     * (Required)
     * 
     */
    public List<Service> getServices() {
        return services;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setServices(List<Service> services) {
        this.services = services;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Services.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("currentVersion");
        sb.append('=');
        sb.append(((this.currentVersion == null)?"<null>":this.currentVersion));
        sb.append(',');
        sb.append("services");
        sb.append('=');
        sb.append(((this.services == null)?"<null>":this.services));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.currentVersion == null)? 0 :this.currentVersion.hashCode()));
        result = ((result* 31)+((this.services == null)? 0 :this.services.hashCode()));
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
        return (((this.currentVersion == rhs.currentVersion)||((this.currentVersion!= null)&&this.currentVersion.equals(rhs.currentVersion)))&&((this.services == rhs.services)||((this.services!= null)&&this.services.equals(rhs.services))));
    }

}
