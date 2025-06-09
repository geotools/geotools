/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */

package org.geotools.data.arcgisrest.schema.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;

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

    /** (Required) */
    public String getName() {
        return name;
    }

    /** (Required) */
    public void setName(String name) {
        this.name = name;
    }

    /** (Required) */
    public String getDescription() {
        return description;
    }

    /** (Required) */
    public void setDescription(String description) {
        this.description = description;
    }

    /** (Required) */
    public String getDrawingTool() {
        return drawingTool;
    }

    /** (Required) */
    public void setDrawingTool(String drawingTool) {
        this.drawingTool = drawingTool;
    }

    /** (Required) */
    public Prototype getPrototype() {
        return prototype;
    }

    /** (Required) */
    public void setPrototype(Prototype prototype) {
        this.prototype = prototype;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
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
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result * 31) + ((this.name == null) ? 0 : this.name.hashCode()));
        result = ((result * 31) + ((this.description == null) ? 0 : this.description.hashCode()));
        result = ((result * 31) + ((this.drawingTool == null) ? 0 : this.drawingTool.hashCode()));
        result = ((result * 31) + ((this.prototype == null) ? 0 : this.prototype.hashCode()));
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
        return Objects.equals(this.name, rhs.name)
                && Objects.equals(this.description, rhs.description)
                && Objects.equals(this.drawingTool, rhs.drawingTool)
                && Objects.equals(this.prototype, rhs.prototype);
    }
}
