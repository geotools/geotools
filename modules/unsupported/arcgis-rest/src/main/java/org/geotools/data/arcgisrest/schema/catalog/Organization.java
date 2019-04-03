<<<<<<< HEAD
<<<<<<< HEAD

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
=======
=======

>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
package org.geotools.data.arcgisrest.schema.catalog;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Project Open Data Organization
<<<<<<< HEAD
 *
 * <p>A Dataset Publisher Organization as a foaf:Agent object
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
 * <p>
 * A Dataset Publisher Organization as a foaf:Agent object
 * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
 */
public class Organization {

    /**
     * Metadata Context
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * IRI for the JSON-LD data type. This should be org:Organization for each publisher
     * 
=======
     *
     * <p>IRI for the JSON-LD data type. This should be org:Organization for each publisher
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * IRI for the JSON-LD data type. This should be org:Organization for each publisher
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    @SerializedName("@type")
    @Expose
    private Organization.Type type;
    /**
     * Publisher Name
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     * <p>
     * A full formatted name, eg Firstname Lastname
     * (Required)
     * 
<<<<<<< HEAD
=======
     *
     * <p>A full formatted name, eg Firstname Lastname (Required)
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    @SerializedName("name")
    @Expose
    private String name;
    /**
     * Project Open Data Organization
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * A Dataset Publisher Organization as a foaf:Agent object
     * 
=======
     *
     * <p>A Dataset Publisher Organization as a foaf:Agent object
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * A Dataset Publisher Organization as a foaf:Agent object
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    @SerializedName("subOrganizationOf")
    @Expose
    private Organization subOrganizationOf;

    /**
     * Metadata Context
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * IRI for the JSON-LD data type. This should be org:Organization for each publisher
     * 
=======
     *
     * <p>IRI for the JSON-LD data type. This should be org:Organization for each publisher
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * IRI for the JSON-LD data type. This should be org:Organization for each publisher
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public Organization.Type getType() {
        return type;
    }

    /**
     * Metadata Context
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * IRI for the JSON-LD data type. This should be org:Organization for each publisher
     * 
=======
     *
     * <p>IRI for the JSON-LD data type. This should be org:Organization for each publisher
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * IRI for the JSON-LD data type. This should be org:Organization for each publisher
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setType(Organization.Type type) {
        this.type = type;
    }

    /**
     * Publisher Name
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     * <p>
     * A full formatted name, eg Firstname Lastname
     * (Required)
     * 
<<<<<<< HEAD
=======
     *
     * <p>A full formatted name, eg Firstname Lastname (Required)
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public String getName() {
        return name;
    }

    /**
     * Publisher Name
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     * <p>
     * A full formatted name, eg Firstname Lastname
     * (Required)
     * 
<<<<<<< HEAD
=======
     *
     * <p>A full formatted name, eg Firstname Lastname (Required)
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Project Open Data Organization
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * A Dataset Publisher Organization as a foaf:Agent object
     * 
=======
     *
     * <p>A Dataset Publisher Organization as a foaf:Agent object
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * A Dataset Publisher Organization as a foaf:Agent object
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public Organization getSubOrganizationOf() {
        return subOrganizationOf;
    }

    /**
     * Project Open Data Organization
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * A Dataset Publisher Organization as a foaf:Agent object
     * 
=======
     *
     * <p>A Dataset Publisher Organization as a foaf:Agent object
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * A Dataset Publisher Organization as a foaf:Agent object
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setSubOrganizationOf(Organization subOrganizationOf) {
        this.subOrganizationOf = subOrganizationOf;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
<<<<<<< HEAD
<<<<<<< HEAD
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
=======
        sb.append(Organization.class.getName())
                .append('@')
                .append(Integer.toHexString(System.identityHashCode(this)))
                .append('[');
=======
        sb.append(Organization.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
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
        result = ((result* 31)+((this.name == null)? 0 :this.name.hashCode()));
        result = ((result* 31)+((this.type == null)? 0 :this.type.hashCode()));
        result = ((result* 31)+((this.subOrganizationOf == null)? 0 :this.subOrganizationOf.hashCode()));
=======
        result = ((result * 31) + ((this.name == null) ? 0 : this.name.hashCode()));
        result = ((result * 31) + ((this.type == null) ? 0 : this.type.hashCode()));
        result =
                ((result * 31)
                        + ((this.subOrganizationOf == null)
                                ? 0
                                : this.subOrganizationOf.hashCode()));
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
        result = ((result* 31)+((this.name == null)? 0 :this.name.hashCode()));
        result = ((result* 31)+((this.type == null)? 0 :this.type.hashCode()));
        result = ((result* 31)+((this.subOrganizationOf == null)? 0 :this.subOrganizationOf.hashCode()));
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
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
<<<<<<< HEAD
<<<<<<< HEAD
        return ((((this.name == rhs.name)||((this.name!= null)&&this.name.equals(rhs.name)))&&((this.type == rhs.type)||((this.type!= null)&&this.type.equals(rhs.type))))&&((this.subOrganizationOf == rhs.subOrganizationOf)||((this.subOrganizationOf!= null)&&this.subOrganizationOf.equals(rhs.subOrganizationOf))));
    }

    public enum Type {

        @SerializedName("org:Organization")
        ORG_ORGANIZATION("org:Organization");
        private final String value;
        private final static Map<String, Organization.Type> CONSTANTS = new HashMap<String, Organization.Type>();

        static {
            for (Organization.Type c: values()) {
=======
        return ((((this.name == rhs.name) || ((this.name != null) && this.name.equals(rhs.name)))
                        && ((this.type == rhs.type)
                                || ((this.type != null) && this.type.equals(rhs.type))))
                && ((this.subOrganizationOf == rhs.subOrganizationOf)
                        || ((this.subOrganizationOf != null)
                                && this.subOrganizationOf.equals(rhs.subOrganizationOf))));
=======
        return ((((this.name == rhs.name)||((this.name!= null)&&this.name.equals(rhs.name)))&&((this.type == rhs.type)||((this.type!= null)&&this.type.equals(rhs.type))))&&((this.subOrganizationOf == rhs.subOrganizationOf)||((this.subOrganizationOf!= null)&&this.subOrganizationOf.equals(rhs.subOrganizationOf))));
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    }

    public enum Type {

        @SerializedName("org:Organization")
        ORG_ORGANIZATION("org:Organization");
        private final String value;
        private final static Map<String, Organization.Type> CONSTANTS = new HashMap<String, Organization.Type>();

        static {
<<<<<<< HEAD
            for (Organization.Type c : values()) {
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
            for (Organization.Type c: values()) {
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
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
<<<<<<< HEAD
<<<<<<< HEAD

    }

=======
    }
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======

    }

>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
}
