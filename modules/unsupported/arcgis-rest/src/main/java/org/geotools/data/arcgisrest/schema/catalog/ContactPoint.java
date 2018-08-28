package org.geotools.data.arcgisrest.schema.catalog;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Project Open Data ContactPoint vCard
 *
 * <p>A Dataset ContactPoint as a vCard object
 */
public class ContactPoint {

    /**
     * Metadata Context
     *
     * <p>IRI for the JSON-LD data type. This should be vcard:Contact for contactPoint
     */
    @SerializedName("@type")
    @Expose
    private ContactPoint.Type type;
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
     *
     * @return The type
     */
    public ContactPoint.Type getType() {
        return type;
    }

    /**
     * Metadata Context
     *
     * <p>IRI for the JSON-LD data type. This should be vcard:Contact for contactPoint
     *
     * @param type The @type
     */
    public void setType(ContactPoint.Type type) {
        this.type = type;
    }

    /**
     * Contact Name
     *
     * <p>A full formatted name, eg Firstname Lastname (Required)
     *
     * @return The fn
     */
    public String getFn() {
        return fn;
    }

    /**
     * Contact Name
     *
     * <p>A full formatted name, eg Firstname Lastname (Required)
     *
     * @param fn The fn
     */
    public void setFn(String fn) {
        this.fn = fn;
    }

    /**
     * Email
     *
     * <p>Email address for the contact (Required)
     *
     * @return The hasEmail
     */
    public String getHasEmail() {
        return hasEmail;
    }

    /**
     * Email
     *
     * <p>Email address for the contact (Required)
     *
     * @param hasEmail The hasEmail
     */
    public void setHasEmail(String hasEmail) {
        this.hasEmail = hasEmail;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(type).append(fn).append(hasEmail).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ContactPoint) == false) {
            return false;
        }
        ContactPoint rhs = ((ContactPoint) other);
        return new EqualsBuilder()
                .append(type, rhs.type)
                .append(fn, rhs.fn)
                .append(hasEmail, rhs.hasEmail)
                .isEquals();
    }

    public enum Type {
        @SerializedName("vcard:Contact")
        VCARD_CONTACT("vcard:Contact");
        private final String value;
        private static final Map<String, ContactPoint.Type> CONSTANTS =
                new HashMap<String, ContactPoint.Type>();

        static {
            for (ContactPoint.Type c : values()) {
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

        public static ContactPoint.Type fromValue(String value) {
            ContactPoint.Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }
    }
}
