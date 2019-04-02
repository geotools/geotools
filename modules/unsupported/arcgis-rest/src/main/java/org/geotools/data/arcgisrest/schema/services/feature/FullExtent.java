
package org.geotools.data.arcgisrest.schema.services.feature;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FullExtent {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("xmin")
    @Expose
    private Double xmin;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("ymin")
    @Expose
    private Double ymin;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("xmax")
    @Expose
    private Double xmax;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("ymax")
    @Expose
    private Double ymax;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("spatialReference")
    @Expose
    private SpatialReference__2 spatialReference;

<<<<<<< HEAD
    /**
     * 
     * (Required)
     * 
     */
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public Double getXmin() {
        return xmin;
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
    public void setXmin(Double xmin) {
        this.xmin = xmin;
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
    public Double getYmin() {
        return ymin;
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
    public void setYmin(Double ymin) {
        this.ymin = ymin;
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
    public Double getXmax() {
        return xmax;
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
    public void setXmax(Double xmax) {
        this.xmax = xmax;
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
    public Double getYmax() {
        return ymax;
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
    public void setYmax(Double ymax) {
        this.ymax = ymax;
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
    public SpatialReference__2 getSpatialReference() {
        return spatialReference;
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
    public void setSpatialReference(SpatialReference__2 spatialReference) {
        this.spatialReference = spatialReference;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
<<<<<<< HEAD
        sb.append(FullExtent.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("xmin");
        sb.append('=');
        sb.append(((this.xmin == null)?"<null>":this.xmin));
        sb.append(',');
        sb.append("ymin");
        sb.append('=');
        sb.append(((this.ymin == null)?"<null>":this.ymin));
        sb.append(',');
        sb.append("xmax");
        sb.append('=');
        sb.append(((this.xmax == null)?"<null>":this.xmax));
        sb.append(',');
        sb.append("ymax");
        sb.append('=');
        sb.append(((this.ymax == null)?"<null>":this.ymax));
        sb.append(',');
        sb.append("spatialReference");
        sb.append('=');
        sb.append(((this.spatialReference == null)?"<null>":this.spatialReference));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
=======
        sb.append(FullExtent.class.getName())
                .append('@')
                .append(Integer.toHexString(System.identityHashCode(this)))
                .append('[');
        sb.append("xmin");
        sb.append('=');
        sb.append(((this.xmin == null) ? "<null>" : this.xmin));
        sb.append(',');
        sb.append("ymin");
        sb.append('=');
        sb.append(((this.ymin == null) ? "<null>" : this.ymin));
        sb.append(',');
        sb.append("xmax");
        sb.append('=');
        sb.append(((this.xmax == null) ? "<null>" : this.xmax));
        sb.append(',');
        sb.append("ymax");
        sb.append('=');
        sb.append(((this.ymax == null) ? "<null>" : this.ymax));
        sb.append(',');
        sb.append("spatialReference");
        sb.append('=');
        sb.append(((this.spatialReference == null) ? "<null>" : this.spatialReference));
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
        result = ((result* 31)+((this.ymin == null)? 0 :this.ymin.hashCode()));
        result = ((result* 31)+((this.xmin == null)? 0 :this.xmin.hashCode()));
        result = ((result* 31)+((this.ymax == null)? 0 :this.ymax.hashCode()));
        result = ((result* 31)+((this.xmax == null)? 0 :this.xmax.hashCode()));
        result = ((result* 31)+((this.spatialReference == null)? 0 :this.spatialReference.hashCode()));
=======
        result = ((result * 31) + ((this.ymin == null) ? 0 : this.ymin.hashCode()));
        result = ((result * 31) + ((this.xmin == null) ? 0 : this.xmin.hashCode()));
        result = ((result * 31) + ((this.ymax == null) ? 0 : this.ymax.hashCode()));
        result = ((result * 31) + ((this.xmax == null) ? 0 : this.xmax.hashCode()));
        result =
                ((result * 31)
                        + ((this.spatialReference == null) ? 0 : this.spatialReference.hashCode()));
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FullExtent) == false) {
            return false;
        }
        FullExtent rhs = ((FullExtent) other);
<<<<<<< HEAD
        return ((((((this.ymin == rhs.ymin)||((this.ymin!= null)&&this.ymin.equals(rhs.ymin)))&&((this.xmin == rhs.xmin)||((this.xmin!= null)&&this.xmin.equals(rhs.xmin))))&&((this.ymax == rhs.ymax)||((this.ymax!= null)&&this.ymax.equals(rhs.ymax))))&&((this.xmax == rhs.xmax)||((this.xmax!= null)&&this.xmax.equals(rhs.xmax))))&&((this.spatialReference == rhs.spatialReference)||((this.spatialReference!= null)&&this.spatialReference.equals(rhs.spatialReference))));
=======
        return ((((((this.ymin == rhs.ymin) || ((this.ymin != null) && this.ymin.equals(rhs.ymin)))
                                        && ((this.xmin == rhs.xmin)
                                                || ((this.xmin != null)
                                                        && this.xmin.equals(rhs.xmin))))
                                && ((this.ymax == rhs.ymax)
                                        || ((this.ymax != null) && this.ymax.equals(rhs.ymax))))
                        && ((this.xmax == rhs.xmax)
                                || ((this.xmax != null) && this.xmax.equals(rhs.xmax))))
                && ((this.spatialReference == rhs.spatialReference)
                        || ((this.spatialReference != null)
                                && this.spatialReference.equals(rhs.spatialReference))));
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    }

}
