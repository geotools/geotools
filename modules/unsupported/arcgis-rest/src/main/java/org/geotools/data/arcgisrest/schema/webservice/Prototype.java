
package org.geotools.data.arcgisrest.schema.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Prototype {

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Prototype.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("attributes");
        sb.append('=');
        sb.append(((this.attributes == null)?"<null>":this.attributes));
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
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Prototype) == false) {
            return false;
        }
        Prototype rhs = ((Prototype) other);
        return ((this.attributes == rhs.attributes)||((this.attributes!= null)&&this.attributes.equals(rhs.attributes)));
    }

}
