
package org.geotools.data.arcgisrest.schema.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Template {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("name")
    @Expose
    private String name;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("description")
    @Expose
    private String description;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("drawingTool")
    @Expose
    private String drawingTool;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("prototype")
    @Expose
    private Prototype prototype;

    /**
     * 
     * (Required)
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * (Required)
     * 
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * (Required)
     * 
     */
    public String getDrawingTool() {
        return drawingTool;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setDrawingTool(String drawingTool) {
        this.drawingTool = drawingTool;
    }

    /**
     * 
     * (Required)
     * 
     */
    public Prototype getPrototype() {
        return prototype;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setPrototype(Prototype prototype) {
        this.prototype = prototype;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Template.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("description");
        sb.append('=');
        sb.append(((this.description == null)?"<null>":this.description));
        sb.append(',');
        sb.append("drawingTool");
        sb.append('=');
        sb.append(((this.drawingTool == null)?"<null>":this.drawingTool));
        sb.append(',');
        sb.append("prototype");
        sb.append('=');
        sb.append(((this.prototype == null)?"<null>":this.prototype));
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
        result = ((result* 31)+((this.name == null)? 0 :this.name.hashCode()));
        result = ((result* 31)+((this.description == null)? 0 :this.description.hashCode()));
        result = ((result* 31)+((this.drawingTool == null)? 0 :this.drawingTool.hashCode()));
        result = ((result* 31)+((this.prototype == null)? 0 :this.prototype.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Template) == false) {
            return false;
        }
        Template rhs = ((Template) other);
        return (((((this.name == rhs.name)||((this.name!= null)&&this.name.equals(rhs.name)))&&((this.description == rhs.description)||((this.description!= null)&&this.description.equals(rhs.description))))&&((this.drawingTool == rhs.drawingTool)||((this.drawingTool!= null)&&this.drawingTool.equals(rhs.drawingTool))))&&((this.prototype == rhs.prototype)||((this.prototype!= null)&&this.prototype.equals(rhs.prototype))));
    }

}
