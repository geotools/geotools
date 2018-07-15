package org.geotools.data.arcgisrest.schema.services.feature;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class XssPreventionInfo {

    /** (Required) */
    @SerializedName("xssPreventionEnabled")
    @Expose
    private Boolean xssPreventionEnabled;
    /** (Required) */
    @SerializedName("xssPreventionRule")
    @Expose
    private String xssPreventionRule;
    /** (Required) */
    @SerializedName("xssInputRule")
    @Expose
    private String xssInputRule;

    /**
     * (Required)
     *
     * @return The xssPreventionEnabled
     */
    public Boolean getXssPreventionEnabled() {
        return xssPreventionEnabled;
    }

    /**
     * (Required)
     *
     * @param xssPreventionEnabled The xssPreventionEnabled
     */
    public void setXssPreventionEnabled(Boolean xssPreventionEnabled) {
        this.xssPreventionEnabled = xssPreventionEnabled;
    }

    /**
     * (Required)
     *
     * @return The xssPreventionRule
     */
    public String getXssPreventionRule() {
        return xssPreventionRule;
    }

    /**
     * (Required)
     *
     * @param xssPreventionRule The xssPreventionRule
     */
    public void setXssPreventionRule(String xssPreventionRule) {
        this.xssPreventionRule = xssPreventionRule;
    }

    /**
     * (Required)
     *
     * @return The xssInputRule
     */
    public String getXssInputRule() {
        return xssInputRule;
    }

    /**
     * (Required)
     *
     * @param xssInputRule The xssInputRule
     */
    public void setXssInputRule(String xssInputRule) {
        this.xssInputRule = xssInputRule;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(xssPreventionEnabled)
                .append(xssPreventionRule)
                .append(xssInputRule)
                .toHashCode();
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
        return new EqualsBuilder()
                .append(xssPreventionEnabled, rhs.xssPreventionEnabled)
                .append(xssPreventionRule, rhs.xssPreventionRule)
                .append(xssInputRule, rhs.xssInputRule)
                .isEquals();
    }
}
