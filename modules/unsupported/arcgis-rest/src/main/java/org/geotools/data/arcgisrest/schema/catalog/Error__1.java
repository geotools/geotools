
package org.geotools.data.arcgisrest.schema.catalog;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Error__1 {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("code")
    @Expose
    private Integer code;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("message")
    @Expose
    private String message;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("details")
    @Expose
    private List<String> details = new ArrayList<String>();

    /**
     * 
     * (Required)
     * 
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * 
     * (Required)
     * 
     */
    public String getMessage() {
        return message;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 
     * (Required)
     * 
     */
    public List<String> getDetails() {
        return details;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setDetails(List<String> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Error__1 .class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("code");
        sb.append('=');
        sb.append(((this.code == null)?"<null>":this.code));
        sb.append(',');
        sb.append("message");
        sb.append('=');
        sb.append(((this.message == null)?"<null>":this.message));
        sb.append(',');
        sb.append("details");
        sb.append('=');
        sb.append(((this.details == null)?"<null>":this.details));
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
        result = ((result* 31)+((this.details == null)? 0 :this.details.hashCode()));
        result = ((result* 31)+((this.code == null)? 0 :this.code.hashCode()));
        result = ((result* 31)+((this.message == null)? 0 :this.message.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Error__1) == false) {
            return false;
        }
        Error__1 rhs = ((Error__1) other);
        return ((((this.details == rhs.details)||((this.details!= null)&&this.details.equals(rhs.details)))&&((this.code == rhs.code)||((this.code!= null)&&this.code.equals(rhs.code))))&&((this.message == rhs.message)||((this.message!= null)&&this.message.equals(rhs.message))));
    }

}
