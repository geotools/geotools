
package org.geotools.data.arcgisrest.schema.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Extent {

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
    private SpatialReference spatialReference;

    /**
     * 
     * (Required)
     * 
     */
    public Double getXmin() {
        return xmin;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setXmin(Double xmin) {
        this.xmin = xmin;
    }

    /**
     * 
     * (Required)
     * 
     */
    public Double getYmin() {
        return ymin;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setYmin(Double ymin) {
        this.ymin = ymin;
    }

    /**
     * 
     * (Required)
     * 
     */
    public Double getXmax() {
        return xmax;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setXmax(Double xmax) {
        this.xmax = xmax;
    }

    /**
     * 
     * (Required)
     * 
     */
    public Double getYmax() {
        return ymax;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setYmax(Double ymax) {
        this.ymax = ymax;
    }

    /**
     * 
     * (Required)
     * 
     */
    public SpatialReference getSpatialReference() {
        return spatialReference;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setSpatialReference(SpatialReference spatialReference) {
        this.spatialReference = spatialReference;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Extent.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
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
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.ymin == null)? 0 :this.ymin.hashCode()));
        result = ((result* 31)+((this.xmin == null)? 0 :this.xmin.hashCode()));
        result = ((result* 31)+((this.ymax == null)? 0 :this.ymax.hashCode()));
        result = ((result* 31)+((this.xmax == null)? 0 :this.xmax.hashCode()));
        result = ((result* 31)+((this.spatialReference == null)? 0 :this.spatialReference.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Extent) == false) {
            return false;
        }
        Extent rhs = ((Extent) other);
        return ((((((this.ymin == rhs.ymin)||((this.ymin!= null)&&this.ymin.equals(rhs.ymin)))&&((this.xmin == rhs.xmin)||((this.xmin!= null)&&this.xmin.equals(rhs.xmin))))&&((this.ymax == rhs.ymax)||((this.ymax!= null)&&this.ymax.equals(rhs.ymax))))&&((this.xmax == rhs.xmax)||((this.xmax!= null)&&this.xmax.equals(rhs.xmax))))&&((this.spatialReference == rhs.spatialReference)||((this.spatialReference!= null)&&this.spatialReference.equals(rhs.spatialReference))));
    }

}
