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

package org.geotools.data.arcgisrest.schema.catalog;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Project Open Data ContactPoint vCard
 *
 * <p>A Dataset ContactPoint as a vCard object
 */
public class Vcard {

    /**
     * Metadata Context
     *
     * <p>IRI for the JSON-LD data type. This should be vcard:Contact for contactPoint
     */
    @SerializedName("@type")
    @Expose
    private Vcard.Type type;
    /**
     * Contact Name
     *
     * <p>A full formatted name, eg Firstname Lastname (Required)
     */
    @SerializedName("fn")
    @Expose
    private String fn;
    /**
     * Email
     *
     * <p>Email address for the contact (Required)
     */
    @SerializedName("hasEmail")
    @Expose
    private String hasEmail;

    /**
     * Metadata Context
     *
     * <p>IRI for the JSON-LD data type. This should be vcard:Contact for contactPoint
     */
    public Vcard.Type getType() {
        return type;
    }

    /**
     * Metadata Context
     *
     * <p>IRI for the JSON-LD data type. This should be vcard:Contact for contactPoint
     */
    public void setType(Vcard.Type type) {
        this.type = type;
    }

    /**
     * Contact Name
     *
     * <p>A full formatted name, eg Firstname Lastname (Required)
     */
    public String getFn() {
        return fn;
    }

    /**
     * Contact Name
     *
     * <p>A full formatted name, eg Firstname Lastname (Required)
     */
    public void setFn(String fn) {
        this.fn = fn;
    }

    /**
     * Email
     *
     * <p>Email address for the contact (Required)
     */
    public String getHasEmail() {
        return hasEmail;
    }

    /**
     * Email
     *
     * <p>Email address for the contact (Required)
     */
    public void setHasEmail(String hasEmail) {
        this.hasEmail = hasEmail;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Vcard.class.getName())
                .append('@')
                .append(Integer.toHexString(System.identityHashCode(this)))
                .append('[');
        sb.append("type");
        sb.append('=');
        sb.append(((this.type == null) ? "<null>" : this.type));
        sb.append(',');
        sb.append("fn");
        sb.append('=');
        sb.append(((this.fn == null) ? "<null>" : this.fn));
        sb.append(',');
        sb.append("hasEmail");
        sb.append('=');
        sb.append(((this.hasEmail == null) ? "<null>" : this.hasEmail));
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
        result = ((result * 31) + ((this.fn == null) ? 0 : this.fn.hashCode()));
        result = ((result * 31) + ((this.hasEmail == null) ? 0 : this.hasEmail.hashCode()));
        result = ((result * 31) + ((this.type == null) ? 0 : this.type.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Vcard) == false) {
            return false;
        }
        Vcard rhs = ((Vcard) other);
        return Objects.equals(this.fn, rhs.fn)
                && Objects.equals(this.hasEmail, rhs.hasEmail)
                && Objects.equals(this.type, rhs.type);
    }

    public enum Type {
        @SerializedName("vcard:Contact")
        VCARD_CONTACT("vcard:Contact");
        private final String value;
        private static final Map<String, Vcard.Type> CONSTANTS = new HashMap<>();

        static {
            for (Vcard.Type c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Vcard.Type fromValue(String value) {
            Vcard.Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }
    }
}
