package org.geotools.data.arcgisrest.schema.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class EditingInfo {

    /** (Required) */
    @SerializedName("lastEditDate")
    @Expose
    private Object lastEditDate;

    /**
     * (Required)
     *
     * @return The lastEditDate
     */
    public Object getLastEditDate() {
        return lastEditDate;
    }

    /**
     * (Required)
     *
     * @param lastEditDate The lastEditDate
     */
    public void setLastEditDate(Object lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(lastEditDate).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof EditingInfo) == false) {
            return false;
        }
        EditingInfo rhs = ((EditingInfo) other);
        return new EqualsBuilder().append(lastEditDate, rhs.lastEditDate).isEquals();
    }
}
