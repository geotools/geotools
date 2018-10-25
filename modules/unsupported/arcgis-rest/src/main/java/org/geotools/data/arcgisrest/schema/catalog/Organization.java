
package org.geotools.data.arcgisrest.schema.catalog;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Project Open Data Organization
 * <p>
 * A Dataset Publisher Organization as a foaf:Agent object
 * 
 */
public class Organization {

    /**
     * Metadata Context
     * <p>
     * IRI for the JSON-LD data type. This should be org:Organization for each publisher
     * 
     */
    @SerializedName("@type")
    @Expose
    private Organization.Type type;
    /**
     * Publisher Name
     * <p>
     * A full formatted name, eg Firstname Lastname
     * (Required)
     * 
     */
    @SerializedName("name")
    @Expose
    private String name;
    /**
     * Project Open Data Organization
     * <p>
     * A Dataset Publisher Organization as a foaf:Agent object
     * 
     */
    @SerializedName("subOrganizationOf")
    @Expose
    private Organization subOrganizationOf;

    /**
     * Metadata Context
     * <p>
     * IRI for the JSON-LD data type. This should be org:Organization for each publisher
     * 
     */
    public Organization.Type getType() {
        return type;
    }

    /**
     * Metadata Context
     * <p>
     * IRI for the JSON-LD data type. This should be org:Organization for each publisher
     * 
     */
    public void setType(Organization.Type type) {
        this.type = type;
    }

    /**
     * Publisher Name
     * <p>
     * A full formatted name, eg Firstname Lastname
     * (Required)
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * Publisher Name
     * <p>
     * A full formatted name, eg Firstname Lastname
     * (Required)
     * 
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Project Open Data Organization
     * <p>
     * A Dataset Publisher Organization as a foaf:Agent object
     * 
     */
    public Organization getSubOrganizationOf() {
        return subOrganizationOf;
    }

    /**
     * Project Open Data Organization
     * <p>
     * A Dataset Publisher Organization as a foaf:Agent object
     * 
     */
    public void setSubOrganizationOf(Organization subOrganizationOf) {
        this.subOrganizationOf = subOrganizationOf;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Organization.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("type");
        sb.append('=');
        sb.append(((this.type == null)?"<null>":this.type));
        sb.append(',');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("subOrganizationOf");
        sb.append('=');
        sb.append(((this.subOrganizationOf == null)?"<null>":this.subOrganizationOf));
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
        result = ((result* 31)+((this.name == null)? 0 :this.name.hashCode()));
        result = ((result* 31)+((this.type == null)? 0 :this.type.hashCode()));
        result = ((result* 31)+((this.subOrganizationOf == null)? 0 :this.subOrganizationOf.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Organization) == false) {
            return false;
        }
        Organization rhs = ((Organization) other);
        return ((((this.name == rhs.name)||((this.name!= null)&&this.name.equals(rhs.name)))&&((this.type == rhs.type)||((this.type!= null)&&this.type.equals(rhs.type))))&&((this.subOrganizationOf == rhs.subOrganizationOf)||((this.subOrganizationOf!= null)&&this.subOrganizationOf.equals(rhs.subOrganizationOf))));
    }

    public enum Type {

        @SerializedName("org:Organization")
        ORG_ORGANIZATION("org:Organization");
        private final String value;
        private final static Map<String, Organization.Type> CONSTANTS = new HashMap<String, Organization.Type>();

        static {
            for (Organization.Type c: values()) {
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

        public static Organization.Type fromValue(String value) {
            Organization.Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
