
package org.geotools.data.arcgisrest.schema.services.feature;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class XssPreventionInfo {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("xssPreventionEnabled")
    @Expose
    private Boolean xssPreventionEnabled;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("xssPreventionRule")
    @Expose
    private String xssPreventionRule;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("xssInputRule")
    @Expose
    private String xssInputRule;

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    /**
     * 
     * (Required)
     * 
     */
<<<<<<< HEAD
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public Boolean getXssPreventionEnabled() {
        return xssPreventionEnabled;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    /**
     * 
     * (Required)
     * 
     */
<<<<<<< HEAD
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public void setXssPreventionEnabled(Boolean xssPreventionEnabled) {
        this.xssPreventionEnabled = xssPreventionEnabled;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    /**
     * 
     * (Required)
     * 
     */
<<<<<<< HEAD
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public String getXssPreventionRule() {
        return xssPreventionRule;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    /**
     * 
     * (Required)
     * 
     */
<<<<<<< HEAD
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public void setXssPreventionRule(String xssPreventionRule) {
        this.xssPreventionRule = xssPreventionRule;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    /**
     * 
     * (Required)
     * 
     */
<<<<<<< HEAD
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public String getXssInputRule() {
        return xssInputRule;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    /**
     * 
     * (Required)
     * 
     */
<<<<<<< HEAD
=======
    /** (Required) */
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    public void setXssInputRule(String xssInputRule) {
        this.xssInputRule = xssInputRule;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
<<<<<<< HEAD
<<<<<<< HEAD
        sb.append(XssPreventionInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("xssPreventionEnabled");
        sb.append('=');
        sb.append(((this.xssPreventionEnabled == null)?"<null>":this.xssPreventionEnabled));
        sb.append(',');
        sb.append("xssPreventionRule");
        sb.append('=');
        sb.append(((this.xssPreventionRule == null)?"<null>":this.xssPreventionRule));
        sb.append(',');
        sb.append("xssInputRule");
        sb.append('=');
        sb.append(((this.xssInputRule == null)?"<null>":this.xssInputRule));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
=======
        sb.append(XssPreventionInfo.class.getName())
                .append('@')
                .append(Integer.toHexString(System.identityHashCode(this)))
                .append('[');
=======
        sb.append(XssPreventionInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
        sb.append("xssPreventionEnabled");
        sb.append('=');
        sb.append(((this.xssPreventionEnabled == null)?"<null>":this.xssPreventionEnabled));
        sb.append(',');
        sb.append("xssPreventionRule");
        sb.append('=');
        sb.append(((this.xssPreventionRule == null)?"<null>":this.xssPreventionRule));
        sb.append(',');
        sb.append("xssInputRule");
        sb.append('=');
        sb.append(((this.xssInputRule == null)?"<null>":this.xssInputRule));
        sb.append(',');
<<<<<<< HEAD
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
<<<<<<< HEAD
<<<<<<< HEAD
        result = ((result* 31)+((this.xssPreventionEnabled == null)? 0 :this.xssPreventionEnabled.hashCode()));
        result = ((result* 31)+((this.xssInputRule == null)? 0 :this.xssInputRule.hashCode()));
        result = ((result* 31)+((this.xssPreventionRule == null)? 0 :this.xssPreventionRule.hashCode()));
=======
        result =
                ((result * 31)
                        + ((this.xssPreventionEnabled == null)
                                ? 0
                                : this.xssPreventionEnabled.hashCode()));
        result = ((result * 31) + ((this.xssInputRule == null) ? 0 : this.xssInputRule.hashCode()));
        result =
                ((result * 31)
                        + ((this.xssPreventionRule == null)
                                ? 0
                                : this.xssPreventionRule.hashCode()));
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
        result = ((result* 31)+((this.xssPreventionEnabled == null)? 0 :this.xssPreventionEnabled.hashCode()));
        result = ((result* 31)+((this.xssInputRule == null)? 0 :this.xssInputRule.hashCode()));
        result = ((result* 31)+((this.xssPreventionRule == null)? 0 :this.xssPreventionRule.hashCode()));
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof XssPreventionInfo) == false) {
            return false;
        }
        XssPreventionInfo rhs = ((XssPreventionInfo) other);
<<<<<<< HEAD
<<<<<<< HEAD
        return ((((this.xssPreventionEnabled == rhs.xssPreventionEnabled)||((this.xssPreventionEnabled!= null)&&this.xssPreventionEnabled.equals(rhs.xssPreventionEnabled)))&&((this.xssInputRule == rhs.xssInputRule)||((this.xssInputRule!= null)&&this.xssInputRule.equals(rhs.xssInputRule))))&&((this.xssPreventionRule == rhs.xssPreventionRule)||((this.xssPreventionRule!= null)&&this.xssPreventionRule.equals(rhs.xssPreventionRule))));
=======
        return ((((this.xssPreventionEnabled == rhs.xssPreventionEnabled)
                                || ((this.xssPreventionEnabled != null)
                                        && this.xssPreventionEnabled.equals(
                                                rhs.xssPreventionEnabled)))
                        && ((this.xssInputRule == rhs.xssInputRule)
                                || ((this.xssInputRule != null)
                                        && this.xssInputRule.equals(rhs.xssInputRule))))
                && ((this.xssPreventionRule == rhs.xssPreventionRule)
                        || ((this.xssPreventionRule != null)
                                && this.xssPreventionRule.equals(rhs.xssPreventionRule))));
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
        return ((((this.xssPreventionEnabled == rhs.xssPreventionEnabled)||((this.xssPreventionEnabled!= null)&&this.xssPreventionEnabled.equals(rhs.xssPreventionEnabled)))&&((this.xssInputRule == rhs.xssInputRule)||((this.xssInputRule!= null)&&this.xssInputRule.equals(rhs.xssInputRule))))&&((this.xssPreventionRule == rhs.xssPreventionRule)||((this.xssPreventionRule!= null)&&this.xssPreventionRule.equals(rhs.xssPreventionRule))));
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    }

}
