package org.geotools.data.arcgisrest.schema.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Symbol {

    /** (Required) */
    @SerializedName("color")
    @Expose
    private Object color;
    /** (Required) */
    @SerializedName("outline")
    @Expose
    private Outline outline;
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
    public Object getColor() {
        return color;
    }

    /**
     * (Required)
     *
     * @param color The color
     */
    public void setColor(Object color) {
        this.color = color;
    }

    /**
     * (Required)
     *
     * @return The outline
     */
    public Outline getOutline() {
        return outline;
    }

    /**
     * (Required)
     *
     * @param outline The outline
     */
    public void setOutline(Outline outline) {
        this.outline = outline;
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
                .append(outline)
                .append(type)
                .append(style)
                .toHashCode();
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
        return new EqualsBuilder()
                .append(color, rhs.color)
                .append(outline, rhs.outline)
                .append(type, rhs.type)
                .append(style, rhs.style)
                .isEquals();
    }
}
