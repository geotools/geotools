package org.geotools.data.arcgisrest.schema.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class MultiScaleGeometryInfo {

    /** (Required) */
    @SerializedName("levels")
    @Expose
    private List<Integer> levels = new ArrayList<Integer>();

    /**
     * (Required)
     *
     * @return The levels
     */
    public List<Integer> getLevels() {
        return levels;
    }

    /**
     * (Required)
     *
     * @param levels The levels
     */
    public void setLevels(List<Integer> levels) {
        this.levels = levels;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(levels).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof MultiScaleGeometryInfo) == false) {
            return false;
        }
        MultiScaleGeometryInfo rhs = ((MultiScaleGeometryInfo) other);
        return new EqualsBuilder().append(levels, rhs.levels).isEquals();
    }
}
