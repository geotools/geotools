
package org.geotools.data.arcgisrest.schema.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpatialReference {

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

<<<<<<< HEAD
    /**
     * 
     * (Required)
     * 
     */
=======
    @SerializedName("wkt")
    @Expose
    private String wkt;

    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public Integer getWkid() {
        return wkid;
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
    public void setWkid(Integer wkid) {
        this.wkid = wkid;
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
    public Integer getLatestWkid() {
        return latestWkid;
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
    public void setLatestWkid(Integer latestWkid) {
        this.latestWkid = latestWkid;
    }

    public String getWkt() {
        return wkt;
    }

    public void setWkt(String wkt) {
        this.wkt = wkt;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
<<<<<<< HEAD
        sb.append(SpatialReference.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
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
=======
        sb.append(SpatialReference.class.getName())
                .append('@')
                .append(Integer.toHexString(System.identityHashCode(this)))
                .append('[');
        sb.append("wkid");
        sb.append('=');
        sb.append(((this.wkid == null) ? "<null>" : this.wkid));
        sb.append(',');
        sb.append("latestWkid");
        sb.append('=');
        sb.append(((this.latestWkid == null) ? "<null>" : this.latestWkid));
        sb.append(',');
        sb.append("wkt");
        sb.append('=');
        sb.append(((this.wkt == null) ? "<null>" : this.wkt));
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
        result = ((result* 31)+((this.wkid == null)? 0 :this.wkid.hashCode()));
        result = ((result* 31)+((this.latestWkid == null)? 0 :this.latestWkid.hashCode()));
=======
        result = ((result * 31) + ((this.wkid == null) ? 0 : this.wkid.hashCode()));
        result = ((result * 31) + ((this.wkt == null) ? 0 : this.wkt.hashCode()));
        result = ((result * 31) + ((this.latestWkid == null) ? 0 : this.latestWkid.hashCode()));
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SpatialReference) == false) {
            return false;
        }
        SpatialReference rhs = ((SpatialReference) other);
<<<<<<< HEAD
        return (((this.wkid == rhs.wkid)||((this.wkid!= null)&&this.wkid.equals(rhs.wkid)))&&((this.latestWkid == rhs.latestWkid)||((this.latestWkid!= null)&&this.latestWkid.equals(rhs.latestWkid))));
=======
        return ((((this.wkid == rhs.wkid) || ((this.wkid != null) && this.wkid.equals(rhs.wkid)))
                        && ((this.wkt == rhs.wkt)
                                || ((this.wkt != null) && this.wkt.equals(rhs.wkt))))
                && ((this.latestWkid == rhs.latestWkid)
                        || ((this.latestWkid != null) && this.latestWkid.equals(rhs.latestWkid))));
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    }

}
