package org.geotools.data.arcgisrest.schema.catalog;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Project Open Data Organization
 *
 * <p>A Dataset Publisher Organization as a foaf:Agent object
 */
public class Publisher {

    /**
     * Metadata Context
     *
     * <p>IRI for the JSON-LD data type. This should be org:Organization for each publisher
     */
    @SerializedName("@type")
    @Expose
    private Publisher.Type type;
    /**
     * Publisher Name
     *
     * <p>A full formatted name, eg Firstname Lastname (Required)
     */
    @SerializedName("name")
    @Expose
    private String name;
    /**
     * Project Open Data Organization
     *
     * <p>A Dataset Publisher Organization as a foaf:Agent object
     */
    @SerializedName("subOrganizationOf")
    @Expose
    private Publisher subOrganizationOf;

    /**
     * Metadata Context
     *
     * <p>IRI for the JSON-LD data type. This should be org:Organization for each publisher
     *
     * @return The type
     */
    public Publisher.Type getType() {
        return type;
    }

    /**
     * Metadata Context
     *
     * <p>IRI for the JSON-LD data type. This should be org:Organization for each publisher
     *
     * @param type The @type
     */
    public void setType(Publisher.Type type) {
        this.type = type;
    }

    /**
     * Publisher Name
     *
     * <p>A full formatted name, eg Firstname Lastname (Required)
     *
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * Publisher Name
     *
     * <p>A full formatted name, eg Firstname Lastname (Required)
     *
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Project Open Data Organization
     *
     * <p>A Dataset Publisher Organization as a foaf:Agent object
     *
     * @return The subOrganizationOf
     */
    public Publisher getSubOrganizationOf() {
        return subOrganizationOf;
    }

    /**
     * Project Open Data Organization
     *
     * <p>A Dataset Publisher Organization as a foaf:Agent object
     *
     * @param subOrganizationOf The subOrganizationOf
     */
    public void setSubOrganizationOf(Publisher subOrganizationOf) {
        this.subOrganizationOf = subOrganizationOf;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(type)
                .append(name)
                .append(subOrganizationOf)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Publisher) == false) {
            return false;
        }
        Publisher rhs = ((Publisher) other);
        return new EqualsBuilder()
                .append(type, rhs.type)
                .append(name, rhs.name)
                .append(subOrganizationOf, rhs.subOrganizationOf)
                .isEquals();
    }

    public enum Type {
        @SerializedName("org:Organization")
        ORG_ORGANIZATION("org:Organization");
        private final String value;
        private static final Map<String, Publisher.Type> CONSTANTS =
                new HashMap<String, Publisher.Type>();

        static {
            for (Publisher.Type c : values()) {
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

        public static Publisher.Type fromValue(String value) {
            Publisher.Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }
    }
}
