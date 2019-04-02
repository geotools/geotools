
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

<<<<<<< HEAD
    /**
     * 
     * (Required)
     * 
     */
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public String getName() {
        return name;
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
    public void setName(String name) {
        this.name = name;
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
    public String getDescription() {
        return description;
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
    public void setDescription(String description) {
        this.description = description;
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
    public String getDrawingTool() {
        return drawingTool;
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
    public void setDrawingTool(String drawingTool) {
        this.drawingTool = drawingTool;
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
    public Prototype getPrototype() {
        return prototype;
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
    public void setPrototype(Prototype prototype) {
        this.prototype = prototype;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
<<<<<<< HEAD
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
=======
        sb.append(Template.class.getName())
                .append('@')
                .append(Integer.toHexString(System.identityHashCode(this)))
                .append('[');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null) ? "<null>" : this.name));
        sb.append(',');
        sb.append("description");
        sb.append('=');
        sb.append(((this.description == null) ? "<null>" : this.description));
        sb.append(',');
        sb.append("drawingTool");
        sb.append('=');
        sb.append(((this.drawingTool == null) ? "<null>" : this.drawingTool));
        sb.append(',');
        sb.append("prototype");
        sb.append('=');
        sb.append(((this.prototype == null) ? "<null>" : this.prototype));
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
        result = ((result* 31)+((this.name == null)? 0 :this.name.hashCode()));
        result = ((result* 31)+((this.description == null)? 0 :this.description.hashCode()));
        result = ((result* 31)+((this.drawingTool == null)? 0 :this.drawingTool.hashCode()));
        result = ((result* 31)+((this.prototype == null)? 0 :this.prototype.hashCode()));
=======
        result = ((result * 31) + ((this.name == null) ? 0 : this.name.hashCode()));
        result = ((result * 31) + ((this.description == null) ? 0 : this.description.hashCode()));
        result = ((result * 31) + ((this.drawingTool == null) ? 0 : this.drawingTool.hashCode()));
        result = ((result * 31) + ((this.prototype == null) ? 0 : this.prototype.hashCode()));
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
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
<<<<<<< HEAD
        return (((((this.name == rhs.name)||((this.name!= null)&&this.name.equals(rhs.name)))&&((this.description == rhs.description)||((this.description!= null)&&this.description.equals(rhs.description))))&&((this.drawingTool == rhs.drawingTool)||((this.drawingTool!= null)&&this.drawingTool.equals(rhs.drawingTool))))&&((this.prototype == rhs.prototype)||((this.prototype!= null)&&this.prototype.equals(rhs.prototype))));
=======
        return (((((this.name == rhs.name) || ((this.name != null) && this.name.equals(rhs.name)))
                                && ((this.description == rhs.description)
                                        || ((this.description != null)
                                                && this.description.equals(rhs.description))))
                        && ((this.drawingTool == rhs.drawingTool)
                                || ((this.drawingTool != null)
                                        && this.drawingTool.equals(rhs.drawingTool))))
                && ((this.prototype == rhs.prototype)
                        || ((this.prototype != null) && this.prototype.equals(rhs.prototype))));
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    }

}
