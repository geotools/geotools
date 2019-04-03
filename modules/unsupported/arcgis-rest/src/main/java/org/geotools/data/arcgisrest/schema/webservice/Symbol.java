
package org.geotools.data.arcgisrest.schema.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Symbol {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("color")
    @Expose
    private Object color;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("outline")
    @Expose
    private Outline outline;
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
    public Object getColor() {
        return color;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setColor(Object color) {
        this.color = color;
    }

    /**
     * 
     * (Required)
     * 
     */
    public Outline getOutline() {
        return outline;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setOutline(Outline outline) {
        this.outline = outline;
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
        sb.append(Symbol.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("color");
        sb.append('=');
        sb.append(((this.color == null)?"<null>":this.color));
        sb.append(',');
        sb.append("outline");
        sb.append('=');
        sb.append(((this.outline == null)?"<null>":this.outline));
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
        result = ((result* 31)+((this.outline == null)? 0 :this.outline.hashCode()));
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
        if ((other instanceof Symbol) == false) {
            return false;
        }
        Symbol rhs = ((Symbol) other);
        return (((((this.outline == rhs.outline)||((this.outline!= null)&&this.outline.equals(rhs.outline)))&&((this.style == rhs.style)||((this.style!= null)&&this.style.equals(rhs.style))))&&((this.color == rhs.color)||((this.color!= null)&&this.color.equals(rhs.color))))&&((this.type == rhs.type)||((this.type!= null)&&this.type.equals(rhs.type))));
    }

}
