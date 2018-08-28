package org.geotools.data.arcgisrest.schema.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Field {

    /** (Required) */
    @SerializedName("name")
    @Expose
    private String name;
    /** (Required) */
    @SerializedName("type")
    @Expose
    private String type;
    /** (Required) */
    @SerializedName("alias")
    @Expose
    private String alias;
    /** (Required) */
    @SerializedName("sqlType")
    @Expose
    private String sqlType;
    /** (Required) */
    @SerializedName("nullable")
    @Expose
    private Boolean nullable;
    /** (Required) */
    @SerializedName("editable")
    @Expose
    private Boolean editable;
    /** (Required) */
    @SerializedName("domain")
    @Expose
    private Object domain;
    /** (Required) */
    @SerializedName("defaultValue")
    @Expose
    private Object defaultValue;

    /**
     * (Required)
     *
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * (Required)
     *
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
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
     * @return The alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * (Required)
     *
     * @param alias The alias
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * (Required)
     *
     * @return The sqlType
     */
    public String getSqlType() {
        return sqlType;
    }

    /**
     * (Required)
     *
     * @param sqlType The sqlType
     */
    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    /**
     * (Required)
     *
     * @return The nullable
     */
    public Boolean getNullable() {
        return nullable;
    }

    /**
     * (Required)
     *
     * @param nullable The nullable
     */
    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }

    /**
     * (Required)
     *
     * @return The editable
     */
    public Boolean getEditable() {
        return editable;
    }

    /**
     * (Required)
     *
     * @param editable The editable
     */
    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    /**
     * (Required)
     *
     * @return The domain
     */
    public Object getDomain() {
        return domain;
    }

    /**
     * (Required)
     *
     * @param domain The domain
     */
    public void setDomain(Object domain) {
        this.domain = domain;
    }

    /**
     * (Required)
     *
     * @return The defaultValue
     */
    public Object getDefaultValue() {
        return defaultValue;
    }

    /**
     * (Required)
     *
     * @param defaultValue The defaultValue
     */
    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(name)
                .append(type)
                .append(alias)
                .append(sqlType)
                .append(nullable)
                .append(editable)
                .append(domain)
                .append(defaultValue)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Field) == false) {
            return false;
        }
        Field rhs = ((Field) other);
        return new EqualsBuilder()
                .append(name, rhs.name)
                .append(type, rhs.type)
                .append(alias, rhs.alias)
                .append(sqlType, rhs.sqlType)
                .append(nullable, rhs.nullable)
                .append(editable, rhs.editable)
                .append(domain, rhs.domain)
                .append(defaultValue, rhs.defaultValue)
                .isEquals();
    }
}
