
package org.geotools.data.arcgisrest.schema.services.feature;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpatialReference__2 {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("wkid")
    @Expose
    private Integer wkid;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("latestWkid")
    @Expose
    private Integer latestWkid;

    /**
     * 
     * (Required)
     * 
     */
    public Integer getWkid() {
        return wkid;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setWkid(Integer wkid) {
        this.wkid = wkid;
    }

    /**
     * 
     * (Required)
     * 
     */
    public Integer getLatestWkid() {
        return latestWkid;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setLatestWkid(Integer latestWkid) {
        this.latestWkid = latestWkid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(SpatialReference__2 .class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("wkid");
        sb.append('=');
        sb.append(((this.wkid == null)?"<null>":this.wkid));
        sb.append(',');
        sb.append("latestWkid");
        sb.append('=');
        sb.append(((this.latestWkid == null)?"<null>":this.latestWkid));
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
        result = ((result* 31)+((this.wkid == null)? 0 :this.wkid.hashCode()));
        result = ((result* 31)+((this.latestWkid == null)? 0 :this.latestWkid.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SpatialReference__2) == false) {
            return false;
        }
        SpatialReference__2 rhs = ((SpatialReference__2) other);
        return (((this.wkid == rhs.wkid)||((this.wkid!= null)&&this.wkid.equals(rhs.wkid)))&&((this.latestWkid == rhs.latestWkid)||((this.latestWkid!= null)&&this.latestWkid.equals(rhs.latestWkid))));
    }

}
