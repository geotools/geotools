/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */

package org.geotools.data.arcgisrest.schema.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;

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

    /** (Required) */
    public String getName() {
        return name;
    }

    /** (Required) */
    public void setName(String name) {
        this.name = name;
    }

    /** (Required) */
    public String getType() {
        return type;
    }

    /** (Required) */
    public void setType(String type) {
        this.type = type;
    }

    /** (Required) */
    public String getAlias() {
        return alias;
    }

    /** (Required) */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /** (Required) */
    public String getSqlType() {
        return sqlType;
    }

    /** (Required) */
    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    /** (Required) */
    public Boolean getNullable() {
        return nullable;
    }

    /** (Required) */
    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }

    /** (Required) */
    public Boolean getEditable() {
        return editable;
    }

    /** (Required) */
    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    /** (Required) */
    public Object getDomain() {
        return domain;
    }

    /** (Required) */
    public void setDomain(Object domain) {
        this.domain = domain;
    }

    /** (Required) */
    public Object getDefaultValue() {
        return defaultValue;
    }

    /** (Required) */
    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Field.class.getName())
                .append('@')
                .append(Integer.toHexString(System.identityHashCode(this)))
                .append('[');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null) ? "<null>" : this.name));
        sb.append(',');
        sb.append("type");
        sb.append('=');
        sb.append(((this.type == null) ? "<null>" : this.type));
        sb.append(',');
        sb.append("alias");
        sb.append('=');
        sb.append(((this.alias == null) ? "<null>" : this.alias));
        sb.append(',');
        sb.append("sqlType");
        sb.append('=');
        sb.append(((this.sqlType == null) ? "<null>" : this.sqlType));
        sb.append(',');
        sb.append("nullable");
        sb.append('=');
        sb.append(((this.nullable == null) ? "<null>" : this.nullable));
        sb.append(',');
        sb.append("editable");
        sb.append('=');
        sb.append(((this.editable == null) ? "<null>" : this.editable));
        sb.append(',');
        sb.append("domain");
        sb.append('=');
        sb.append(((this.domain == null) ? "<null>" : this.domain));
        sb.append(',');
        sb.append("defaultValue");
        sb.append('=');
        sb.append(((this.defaultValue == null) ? "<null>" : this.defaultValue));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result * 31) + ((this.sqlType == null) ? 0 : this.sqlType.hashCode()));
        result = ((result * 31) + ((this.nullable == null) ? 0 : this.nullable.hashCode()));
        result = ((result * 31) + ((this.editable == null) ? 0 : this.editable.hashCode()));
        result = ((result * 31) + ((this.defaultValue == null) ? 0 : this.defaultValue.hashCode()));
        result = ((result * 31) + ((this.domain == null) ? 0 : this.domain.hashCode()));
        result = ((result * 31) + ((this.name == null) ? 0 : this.name.hashCode()));
        result = ((result * 31) + ((this.alias == null) ? 0 : this.alias.hashCode()));
        result = ((result * 31) + ((this.type == null) ? 0 : this.type.hashCode()));
        return result;
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
        return Objects.equals(this.sqlType, rhs.sqlType)
                && Objects.equals(this.nullable, rhs.nullable)
                && Objects.equals(this.editable, rhs.editable)
                && Objects.equals(this.defaultValue, rhs.defaultValue)
                && Objects.equals(this.domain, rhs.domain)
                && Objects.equals(this.name, rhs.name)
                && Objects.equals(this.alias, rhs.alias)
                && Objects.equals(this.type, rhs.type);
    }
}
