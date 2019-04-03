
package org.geotools.data.arcgisrest.schema.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditingInfo {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("lastEditDate")
    @Expose
    private Object lastEditDate;

    /**
     * 
     * (Required)
     * 
     */
    public Object getLastEditDate() {
        return lastEditDate;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setLastEditDate(Object lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(EditingInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("lastEditDate");
        sb.append('=');
        sb.append(((this.lastEditDate == null)?"<null>":this.lastEditDate));
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
        result = ((result* 31)+((this.lastEditDate == null)? 0 :this.lastEditDate.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof EditingInfo) == false) {
            return false;
        }
        EditingInfo rhs = ((EditingInfo) other);
        return ((this.lastEditDate == rhs.lastEditDate)||((this.lastEditDate!= null)&&this.lastEditDate.equals(rhs.lastEditDate)));
    }

}
