
package org.geotools.data.arcgisrest.schema.webservice;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Outline {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("color")
    @Expose
    private List<Integer> color = new ArrayList<Integer>();
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("width")
    @Expose
    private Double width;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("type")
    @Expose
    private String type;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("style")
    @Expose
    private String style;

    /**
     * 
     * (Required)
     * 
     */
    public List<Integer> getColor() {
        return color;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setColor(List<Integer> color) {
        this.color = color;
    }

    /**
     * 
     * (Required)
     * 
     */
    public Double getWidth() {
        return width;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setWidth(Double width) {
        this.width = width;
    }

    /**
     * 
     * (Required)
     * 
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * (Required)
     * 
     */
    public String getStyle() {
        return style;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setStyle(String style) {
        this.style = style;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Outline.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("color");
        sb.append('=');
        sb.append(((this.color == null)?"<null>":this.color));
        sb.append(',');
        sb.append("width");
        sb.append('=');
        sb.append(((this.width == null)?"<null>":this.width));
        sb.append(',');
        sb.append("type");
        sb.append('=');
        sb.append(((this.type == null)?"<null>":this.type));
        sb.append(',');
        sb.append("style");
        sb.append('=');
        sb.append(((this.style == null)?"<null>":this.style));
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
        result = ((result* 31)+((this.width == null)? 0 :this.width.hashCode()));
        result = ((result* 31)+((this.style == null)? 0 :this.style.hashCode()));
        result = ((result* 31)+((this.color == null)? 0 :this.color.hashCode()));
        result = ((result* 31)+((this.type == null)? 0 :this.type.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Outline) == false) {
            return false;
        }
        Outline rhs = ((Outline) other);
        return (((((this.width == rhs.width)||((this.width!= null)&&this.width.equals(rhs.width)))&&((this.style == rhs.style)||((this.style!= null)&&this.style.equals(rhs.style))))&&((this.color == rhs.color)||((this.color!= null)&&this.color.equals(rhs.color))))&&((this.type == rhs.type)||((this.type!= null)&&this.type.equals(rhs.type))));
    }

}
