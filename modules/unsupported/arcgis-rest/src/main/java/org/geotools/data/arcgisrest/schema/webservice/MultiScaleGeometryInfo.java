
package org.geotools.data.arcgisrest.schema.webservice;

import java.util.ArrayList;
import java.util.List;
<<<<<<< HEAD
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
=======
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS

public class MultiScaleGeometryInfo {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("levels")
    @Expose
    private List<Integer> levels = new ArrayList<Integer>();

<<<<<<< HEAD
    /**
     * 
     * (Required)
     * 
     */
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public List<Integer> getLevels() {
        return levels;
    }

<<<<<<< HEAD
    /**
     * 
     * (Required)
     * 
     */
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public void setLevels(List<Integer> levels) {
        this.levels = levels;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
<<<<<<< HEAD
        sb.append(MultiScaleGeometryInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("levels");
        sb.append('=');
        sb.append(((this.levels == null)?"<null>":this.levels));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
=======
        sb.append(MultiScaleGeometryInfo.class.getName())
                .append('@')
                .append(Integer.toHexString(System.identityHashCode(this)))
                .append('[');
        sb.append("levels");
        sb.append('=');
        sb.append(((this.levels == null) ? "<null>" : this.levels));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
<<<<<<< HEAD
        result = ((result* 31)+((this.levels == null)? 0 :this.levels.hashCode()));
=======
        result = ((result * 31) + ((this.levels == null) ? 0 : this.levels.hashCode()));
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
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
<<<<<<< HEAD
        return ((this.levels == rhs.levels)||((this.levels!= null)&&this.levels.equals(rhs.levels)));
=======
        return ((this.levels == rhs.levels)
                || ((this.levels != null) && this.levels.equals(rhs.levels)));
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    }

}
