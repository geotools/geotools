package org.geotools.data.arcgisrest.schema.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Index {

    /** (Required) */
    @SerializedName("name")
    @Expose
    private String name;
    /** (Required) */
    @SerializedName("fields")
    @Expose
    private String fields;
    /** (Required) */
    @SerializedName("isAscending")
    @Expose
    private Boolean isAscending;
    /** (Required) */
    @SerializedName("isUnique")
    @Expose
    private Boolean isUnique;
    /** (Required) */
    @SerializedName("description")
    @Expose
    private String description;

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
     * @return The fields
     */
    public String getFields() {
        return fields;
    }

    /**
     * (Required)
     *
     * @param fields The fields
     */
    public void setFields(String fields) {
        this.fields = fields;
    }

    /**
     * (Required)
     *
     * @return The isAscending
     */
    public Boolean getIsAscending() {
        return isAscending;
    }

    /**
     * (Required)
     *
     * @param isAscending The isAscending
     */
    public void setIsAscending(Boolean isAscending) {
        this.isAscending = isAscending;
    }

    /**
     * (Required)
     *
     * @return The isUnique
     */
    public Boolean getIsUnique() {
        return isUnique;
    }

    /**
     * (Required)
     *
     * @param isUnique The isUnique
     */
    public void setIsUnique(Boolean isUnique) {
        this.isUnique = isUnique;
    }

    /**
     * (Required)
     *
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * (Required)
     *
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(name)
                .append(fields)
                .append(isAscending)
                .append(isUnique)
                .append(description)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Index) == false) {
            return false;
        }
        Index rhs = ((Index) other);
        return new EqualsBuilder()
                .append(name, rhs.name)
                .append(fields, rhs.fields)
                .append(isAscending, rhs.isAscending)
                .append(isUnique, rhs.isUnique)
                .append(description, rhs.description)
                .isEquals();
    }
}
