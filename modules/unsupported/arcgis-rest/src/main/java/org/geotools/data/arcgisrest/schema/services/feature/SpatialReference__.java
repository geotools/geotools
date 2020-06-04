package org.geotools.data.arcgisrest.schema.services.feature;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SpatialReference__ {

    /** (Required) */
    @SerializedName("wkid")
    @Expose
    private Integer wkid;
    /** (Required) */
    @SerializedName("latestWkid")
    @Expose
    private Integer latestWkid;

    /**
     * (Required)
     *
     * @return The wkid
     */
    public Integer getWkid() {
        return wkid;
    }

    /**
     * (Required)
     *
     * @param wkid The wkid
     */
    public void setWkid(Integer wkid) {
        this.wkid = wkid;
    }

    /**
     * (Required)
     *
     * @return The latestWkid
     */
    public Integer getLatestWkid() {
        return latestWkid;
    }

    /**
     * (Required)
     *
     * @param latestWkid The latestWkid
     */
    public void setLatestWkid(Integer latestWkid) {
        this.latestWkid = latestWkid;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(wkid).append(latestWkid).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SpatialReference__) == false) {
            return false;
        }
        SpatialReference__ rhs = ((SpatialReference__) other);
        return new EqualsBuilder()
                .append(wkid, rhs.wkid)
                .append(latestWkid, rhs.latestWkid)
                .isEquals();
    }
}
