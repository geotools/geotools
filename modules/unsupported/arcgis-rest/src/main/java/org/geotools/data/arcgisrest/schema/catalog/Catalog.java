
package org.geotools.data.arcgisrest.schema.catalog;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Project Open Data Catalog
 * <p>
 * Validates an entire collection of Project Open Data metadata JSON objects. Agencies produce said collections in the form of Data.json files.
 * 
 */
public class Catalog {

    /**
     * Metadata Context
     * <p>
     * URL or JSON object for the JSON-LD Context that defines the schema used
     * 
     */
    @SerializedName("@context")
    @Expose
    private URI context;
    /**
     * Metadata Catalog ID
     * <p>
     * IRI for the JSON-LD Node Identifier of the Catalog. This should be the URL of the data.json file itself.
     * 
     */
    @SerializedName("@id")
    @Expose
    private URI id;
    /**
     * Metadata Context
     * <p>
     * IRI for the JSON-LD data type. This should be dcat:Catalog for the Catalog
     * 
     */
    @SerializedName("@type")
    @Expose
    private Catalog.Type type;
    /**
     * Version of Schema
     * <p>
     * Version of Schema
     * (Required)
     * 
     */
    @SerializedName("conformsTo")
    @Expose
    private Catalog.ConformsTo conformsTo;
    /**
     * Data Dictionary
     * <p>
     * URL for the JSON Schema file that defines the schema used
     * 
     */
    @SerializedName("describedBy")
    @Expose
    private URI describedBy;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("dataset")
    @Expose
    private List<Dataset> dataset = new ArrayList<Dataset>();

    /**
     * Metadata Context
     * <p>
     * URL or JSON object for the JSON-LD Context that defines the schema used
     * 
     */
    public URI getContext() {
        return context;
    }

    /**
     * Metadata Context
     * <p>
     * URL or JSON object for the JSON-LD Context that defines the schema used
     * 
     */
    public void setContext(URI context) {
        this.context = context;
    }

    /**
     * Metadata Catalog ID
     * <p>
     * IRI for the JSON-LD Node Identifier of the Catalog. This should be the URL of the data.json file itself.
     * 
     */
    public URI getId() {
        return id;
    }

    /**
     * Metadata Catalog ID
     * <p>
     * IRI for the JSON-LD Node Identifier of the Catalog. This should be the URL of the data.json file itself.
     * 
     */
    public void setId(URI id) {
        this.id = id;
    }

    /**
     * Metadata Context
     * <p>
     * IRI for the JSON-LD data type. This should be dcat:Catalog for the Catalog
     * 
     */
    public Catalog.Type getType() {
        return type;
    }

    /**
     * Metadata Context
     * <p>
     * IRI for the JSON-LD data type. This should be dcat:Catalog for the Catalog
     * 
     */
    public void setType(Catalog.Type type) {
        this.type = type;
    }

    /**
     * Version of Schema
     * <p>
     * Version of Schema
     * (Required)
     * 
     */
    public Catalog.ConformsTo getConformsTo() {
        return conformsTo;
    }

    /**
     * Version of Schema
     * <p>
     * Version of Schema
     * (Required)
     * 
     */
    public void setConformsTo(Catalog.ConformsTo conformsTo) {
        this.conformsTo = conformsTo;
    }

    /**
     * Data Dictionary
     * <p>
     * URL for the JSON Schema file that defines the schema used
     * 
     */
    public URI getDescribedBy() {
        return describedBy;
    }

    /**
     * Data Dictionary
     * <p>
     * URL for the JSON Schema file that defines the schema used
     * 
     */
    public void setDescribedBy(URI describedBy) {
        this.describedBy = describedBy;
    }

    /**
     * 
     * (Required)
     * 
     */
    public List<Dataset> getDataset() {
        return dataset;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setDataset(List<Dataset> dataset) {
        this.dataset = dataset;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Catalog.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("context");
        sb.append('=');
        sb.append(((this.context == null)?"<null>":this.context));
        sb.append(',');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("type");
        sb.append('=');
        sb.append(((this.type == null)?"<null>":this.type));
        sb.append(',');
        sb.append("conformsTo");
        sb.append('=');
        sb.append(((this.conformsTo == null)?"<null>":this.conformsTo));
        sb.append(',');
        sb.append("describedBy");
        sb.append('=');
        sb.append(((this.describedBy == null)?"<null>":this.describedBy));
        sb.append(',');
        sb.append("dataset");
        sb.append('=');
        sb.append(((this.dataset == null)?"<null>":this.dataset));
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
        result = ((result* 31)+((this.context == null)? 0 :this.context.hashCode()));
        result = ((result* 31)+((this.describedBy == null)? 0 :this.describedBy.hashCode()));
        result = ((result* 31)+((this.id == null)? 0 :this.id.hashCode()));
        result = ((result* 31)+((this.conformsTo == null)? 0 :this.conformsTo.hashCode()));
        result = ((result* 31)+((this.type == null)? 0 :this.type.hashCode()));
        result = ((result* 31)+((this.dataset == null)? 0 :this.dataset.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Catalog) == false) {
            return false;
        }
        Catalog rhs = ((Catalog) other);
        return (((((((this.context == rhs.context)||((this.context!= null)&&this.context.equals(rhs.context)))&&((this.describedBy == rhs.describedBy)||((this.describedBy!= null)&&this.describedBy.equals(rhs.describedBy))))&&((this.id == rhs.id)||((this.id!= null)&&this.id.equals(rhs.id))))&&((this.conformsTo == rhs.conformsTo)||((this.conformsTo!= null)&&this.conformsTo.equals(rhs.conformsTo))))&&((this.type == rhs.type)||((this.type!= null)&&this.type.equals(rhs.type))))&&((this.dataset == rhs.dataset)||((this.dataset!= null)&&this.dataset.equals(rhs.dataset))));
    }

    public enum ConformsTo {

        @SerializedName("https://project-open-data.cio.gov/v1.1/schema")
        HTTPS_PROJECT_OPEN_DATA_CIO_GOV_V_1_1_SCHEMA("https://project-open-data.cio.gov/v1.1/schema");
        private final String value;
        private final static Map<String, Catalog.ConformsTo> CONSTANTS = new HashMap<String, Catalog.ConformsTo>();

        static {
            for (Catalog.ConformsTo c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private ConformsTo(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Catalog.ConformsTo fromValue(String value) {
            Catalog.ConformsTo constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum Type {

        @SerializedName("dcat:Catalog")
        DCAT_CATALOG("dcat:Catalog");
        private final String value;
        private final static Map<String, Catalog.Type> CONSTANTS = new HashMap<String, Catalog.Type>();

        static {
            for (Catalog.Type c: values()) {
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

        public static Catalog.Type fromValue(String value) {
            Catalog.Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
