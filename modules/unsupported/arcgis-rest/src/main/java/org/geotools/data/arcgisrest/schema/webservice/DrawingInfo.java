
package org.geotools.data.arcgisrest.schema.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DrawingInfo {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("renderer")
    @Expose
    private Renderer renderer;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("transparency")
    @Expose
    private Integer transparency;

    /**
     * 
     * (Required)
     * 
     */
    public Renderer getRenderer() {
        return renderer;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    /**
     * 
     * (Required)
     * 
     */
    public Integer getTransparency() {
        return transparency;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setTransparency(Integer transparency) {
        this.transparency = transparency;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(DrawingInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("renderer");
        sb.append('=');
        sb.append(((this.renderer == null)?"<null>":this.renderer));
        sb.append(',');
        sb.append("transparency");
        sb.append('=');
        sb.append(((this.transparency == null)?"<null>":this.transparency));
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
        result = ((result* 31)+((this.renderer == null)? 0 :this.renderer.hashCode()));
        result = ((result* 31)+((this.transparency == null)? 0 :this.transparency.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DrawingInfo) == false) {
            return false;
        }
        DrawingInfo rhs = ((DrawingInfo) other);
        return (((this.renderer == rhs.renderer)||((this.renderer!= null)&&this.renderer.equals(rhs.renderer)))&&((this.transparency == rhs.transparency)||((this.transparency!= null)&&this.transparency.equals(rhs.transparency))));
    }

}
