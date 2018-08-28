package org.geotools.data.arcgisrest.schema.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Outline {

    /** (Required) */
    @SerializedName("color")
    @Expose
    private List<Integer> color = new ArrayList<Integer>();
    /** (Required) */
    @SerializedName("width")
    @Expose
    private Double width;
    /** (Required) */
    @SerializedName("type")
    @Expose
    private String type;
    /** (Required) */
    @SerializedName("style")
    @Expose
    private String style;

    /**
     * (Required)
     *
     * @return The color
     */
    public List<Integer> getColor() {
        return color;
    }

    /**
     * (Required)
     *
     * @param color The color
     */
    public void setColor(List<Integer> color) {
        this.color = color;
    }

    /**
     * (Required)
     *
     * @return The width
     */
    public Double getWidth() {
        return width;
    }

    /**
     * (Required)
     *
     * @param width The width
     */
    public void setWidth(Double width) {
        this.width = width;
    }

    /**
     * (Required)
     *
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * (Required)
     *
     * @param type The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * (Required)
     *
     * @return The style
     */
    public String getStyle() {
        return style;
    }

    /**
     * (Required)
     *
     * @param style The style
     */
    public void setStyle(String style) {
        this.style = style;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(color)
                .append(width)
                .append(type)
                .append(style)
                .toHashCode();
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
        return new EqualsBuilder()
                .append(color, rhs.color)
                .append(width, rhs.width)
                .append(type, rhs.type)
                .append(style, rhs.style)
                .isEquals();
    }
}
