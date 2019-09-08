
package org.geotools.data.arcgisrest.schema.query;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Feature {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("attributes")
    @Expose
    private Attributes attributes;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("geometry")
    @Expose
    private Geometry geometry;

    /**
     * 
     * (Required)
     * 
     */
    public Attributes getAttributes() {
        return attributes;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    /**
     * 
     * (Required)
     * 
     */
    public Geometry getGeometry() {
        return geometry;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Feature.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("attributes");
        sb.append('=');
        sb.append(((this.attributes == null)?"<null>":this.attributes));
        sb.append(',');
        sb.append("geometry");
        sb.append('=');
        sb.append(((this.geometry == null)?"<null>":this.geometry));
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
        result = ((result* 31)+((this.attributes == null)? 0 :this.attributes.hashCode()));
        result = ((result* 31)+((this.geometry == null)? 0 :this.geometry.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Feature) == false) {
            return false;
        }
        Feature rhs = ((Feature) other);
        return (((this.attributes == rhs.attributes)||((this.attributes!= null)&&this.attributes.equals(rhs.attributes)))&&((this.geometry == rhs.geometry)||((this.geometry!= null)&&this.geometry.equals(rhs.geometry))));
    }

}
