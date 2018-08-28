package org.geotools.data.arcgisrest.schema.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class DrawingInfo {

    /** (Required) */
    @SerializedName("renderer")
    @Expose
    private Renderer renderer;
    /** (Required) */
    @SerializedName("transparency")
    @Expose
    private Integer transparency;

    /**
     * (Required)
     *
     * @return The renderer
     */
    public Renderer getRenderer() {
        return renderer;
    }

    /**
     * (Required)
     *
     * @param renderer The renderer
     */
    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    /**
     * (Required)
     *
     * @return The transparency
     */
    public Integer getTransparency() {
        return transparency;
    }

    /**
     * (Required)
     *
     * @param transparency The transparency
     */
    public void setTransparency(Integer transparency) {
        this.transparency = transparency;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(renderer).append(transparency).toHashCode();
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
        return new EqualsBuilder()
                .append(renderer, rhs.renderer)
                .append(transparency, rhs.transparency)
                .isEquals();
    }
}
