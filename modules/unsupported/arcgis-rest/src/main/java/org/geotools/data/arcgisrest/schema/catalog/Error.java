
package org.geotools.data.arcgisrest.schema.catalog;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Error {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("error")
    @Expose
    private Error__1 error;

    /**
     * 
     * (Required)
     * 
     */
    public Error__1 getError() {
        return error;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setError(Error__1 error) {
        this.error = error;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Error.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("error");
        sb.append('=');
        sb.append(((this.error == null)?"<null>":this.error));
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
        result = ((result* 31)+((this.error == null)? 0 :this.error.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Error) == false) {
            return false;
        }
        Error rhs = ((Error) other);
        return ((this.error == rhs.error)||((this.error!= null)&&this.error.equals(rhs.error)));
    }

}
