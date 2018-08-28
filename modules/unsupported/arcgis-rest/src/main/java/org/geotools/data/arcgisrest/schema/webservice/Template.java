package org.geotools.data.arcgisrest.schema.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Template {

    /** (Required) */
    @SerializedName("name")
    @Expose
    private String name;
    /** (Required) */
    @SerializedName("description")
    @Expose
    private String description;
    /** (Required) */
    @SerializedName("drawingTool")
    @Expose
    private String drawingTool;
    /** (Required) */
    @SerializedName("prototype")
    @Expose
    private Prototype prototype;

    /**
     * (Required)
     *
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * (Required)
     *
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * (Required)
     *
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * (Required)
     *
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * (Required)
     *
     * @return The drawingTool
     */
    public String getDrawingTool() {
        return drawingTool;
    }

    /**
     * (Required)
     *
     * @param drawingTool The drawingTool
     */
    public void setDrawingTool(String drawingTool) {
        this.drawingTool = drawingTool;
    }

    /**
     * (Required)
     *
     * @return The prototype
     */
    public Prototype getPrototype() {
        return prototype;
    }

    /**
     * (Required)
     *
     * @param prototype The prototype
     */
    public void setPrototype(Prototype prototype) {
        this.prototype = prototype;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(name)
                .append(description)
                .append(drawingTool)
                .append(prototype)
                .toHashCode();
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
        return new EqualsBuilder()
                .append(name, rhs.name)
                .append(description, rhs.description)
                .append(drawingTool, rhs.drawingTool)
                .append(prototype, rhs.prototype)
                .isEquals();
    }
}
