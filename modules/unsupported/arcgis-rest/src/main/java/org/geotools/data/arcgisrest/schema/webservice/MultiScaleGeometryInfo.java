
package org.geotools.data.arcgisrest.schema.webservice;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MultiScaleGeometryInfo {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("levels")
    @Expose
    private List<Integer> levels = new ArrayList<Integer>();

    /**
     * 
     * (Required)
     * 
     */
    public List<Integer> getLevels() {
        return levels;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setLevels(List<Integer> levels) {
        this.levels = levels;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MultiScaleGeometryInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("levels");
        sb.append('=');
        sb.append(((this.levels == null)?"<null>":this.levels));
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
        result = ((result* 31)+((this.levels == null)? 0 :this.levels.hashCode()));
        return result;
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
        return ((this.levels == rhs.levels)||((this.levels!= null)&&this.levels.equals(rhs.levels)));
    }

}
