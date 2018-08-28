package org.geotools.data.arcgisrest.schema.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Count {

    /** */
    @SerializedName("count")
    @Expose
    private Integer count;

    /** @return The count */
    public Integer getCount() {
        return count;
    }

    /** @param count The count */
    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(count).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Count) == false) {
            return false;
        }
        Count rhs = ((Count) other);
        return new EqualsBuilder().append(count, rhs.count).isEquals();
    }
}
