
package org.geotools.data.arcgisrest.schema.catalog;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Project Open Data Distribution
 * <p>
 * Validates an entire collection of common core metadata JSON objects. Agencies produce said collections in the form of Data.json files.
 * 
 */
public class Distribution {

    /**
     * Metadata Context
     * <p>
     * IRI for the JSON-LD data type. This should be dcat:Distribution for each Distribution
     * 
     */
    @SerializedName("@type")
    @Expose
    private Distribution.Type type;
    /**
     * Download URL
     * <p>
     * URL providing direct access to a downloadable file of a dataset
     * 
     */
    @SerializedName("downloadURL")
    @Expose
    private URI downloadURL;
    /**
     * Media Type
     * <p>
     * The machine-readable file format (IANA Media Type or MIME Type) of the distribution’s downloadURL
     * 
     */
    @SerializedName("mediaType")
    @Expose
    private Object mediaType;
    /**
     * Format
     * <p>
     * A human-readable description of the file format of a distribution
     * 
     */
    @SerializedName("format")
    @Expose
    private Object format;
    /**
     * Access URL
     * <p>
     * URL providing indirect access to a dataset
     * 
     */
    @SerializedName("accessURL")
    @Expose
    private Object accessURL;
    /**
     * Description
     * <p>
     * Human-readable description of the distribution
     * 
     */
    @SerializedName("description")
    @Expose
    private Object description;
    /**
     * Title
     * <p>
     * Human-readable name of the distribution
     * 
     */
    @SerializedName("title")
    @Expose
    private Object title;
    /**
     * Data Standard
     * <p>
     * URL providing indirect access to a dataset
     * 
     */
    @SerializedName("conformsTo")
    @Expose
    private Object conformsTo;
    /**
     * Data Dictionary
     * <p>
     * URL to the data dictionary for the distribution found at the downloadURL
     * 
     */
    @SerializedName("describedBy")
    @Expose
    private Object describedBy;
    /**
     * Data Dictionary Type
     * <p>
     * The machine-readable file format (IANA Media Type or MIME Type) of the distribution’s describedBy URL
     * 
     */
    @SerializedName("describedByType")
    @Expose
    private Object describedByType;

    /**
     * Metadata Context
     * <p>
     * IRI for the JSON-LD data type. This should be dcat:Distribution for each Distribution
     * 
     */
    public Distribution.Type getType() {
        return type;
    }

    /**
     * Metadata Context
     * <p>
     * IRI for the JSON-LD data type. This should be dcat:Distribution for each Distribution
     * 
     */
    public void setType(Distribution.Type type) {
        this.type = type;
    }

    /**
     * Download URL
     * <p>
     * URL providing direct access to a downloadable file of a dataset
     * 
     */
    public URI getDownloadURL() {
        return downloadURL;
    }

    /**
     * Download URL
     * <p>
     * URL providing direct access to a downloadable file of a dataset
     * 
     */
    public void setDownloadURL(URI downloadURL) {
        this.downloadURL = downloadURL;
    }

    /**
     * Media Type
     * <p>
     * The machine-readable file format (IANA Media Type or MIME Type) of the distribution’s downloadURL
     * 
     */
    public Object getMediaType() {
        return mediaType;
    }

    /**
     * Media Type
     * <p>
     * The machine-readable file format (IANA Media Type or MIME Type) of the distribution’s downloadURL
     * 
     */
    public void setMediaType(Object mediaType) {
        this.mediaType = mediaType;
    }

    /**
     * Format
     * <p>
     * A human-readable description of the file format of a distribution
     * 
     */
    public Object getFormat() {
        return format;
    }

    /**
     * Format
     * <p>
     * A human-readable description of the file format of a distribution
     * 
     */
    public void setFormat(Object format) {
        this.format = format;
    }

    /**
     * Access URL
     * <p>
     * URL providing indirect access to a dataset
     * 
     */
    public Object getAccessURL() {
        return accessURL;
    }

    /**
     * Access URL
     * <p>
     * URL providing indirect access to a dataset
     * 
     */
    public void setAccessURL(Object accessURL) {
        this.accessURL = accessURL;
    }

    /**
     * Description
     * <p>
     * Human-readable description of the distribution
     * 
     */
    public Object getDescription() {
        return description;
    }

    /**
     * Description
     * <p>
     * Human-readable description of the distribution
     * 
     */
    public void setDescription(Object description) {
        this.description = description;
    }

    /**
     * Title
     * <p>
     * Human-readable name of the distribution
     * 
     */
    public Object getTitle() {
        return title;
    }

    /**
     * Title
     * <p>
     * Human-readable name of the distribution
     * 
     */
    public void setTitle(Object title) {
        this.title = title;
    }

    /**
     * Data Standard
     * <p>
     * URL providing indirect access to a dataset
     * 
     */
    public Object getConformsTo() {
        return conformsTo;
    }

    /**
     * Data Standard
     * <p>
     * URL providing indirect access to a dataset
     * 
     */
    public void setConformsTo(Object conformsTo) {
        this.conformsTo = conformsTo;
    }

    /**
     * Data Dictionary
     * <p>
     * URL to the data dictionary for the distribution found at the downloadURL
     * 
     */
    public Object getDescribedBy() {
        return describedBy;
    }

    /**
     * Data Dictionary
     * <p>
     * URL to the data dictionary for the distribution found at the downloadURL
     * 
     */
    public void setDescribedBy(Object describedBy) {
        this.describedBy = describedBy;
    }

    /**
     * Data Dictionary Type
     * <p>
     * The machine-readable file format (IANA Media Type or MIME Type) of the distribution’s describedBy URL
     * 
     */
    public Object getDescribedByType() {
        return describedByType;
    }

    /**
     * Data Dictionary Type
     * <p>
     * The machine-readable file format (IANA Media Type or MIME Type) of the distribution’s describedBy URL
     * 
     */
    public void setDescribedByType(Object describedByType) {
        this.describedByType = describedByType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Distribution.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("type");
        sb.append('=');
        sb.append(((this.type == null)?"<null>":this.type));
        sb.append(',');
        sb.append("downloadURL");
        sb.append('=');
        sb.append(((this.downloadURL == null)?"<null>":this.downloadURL));
        sb.append(',');
        sb.append("mediaType");
        sb.append('=');
        sb.append(((this.mediaType == null)?"<null>":this.mediaType));
        sb.append(',');
        sb.append("format");
        sb.append('=');
        sb.append(((this.format == null)?"<null>":this.format));
        sb.append(',');
        sb.append("accessURL");
        sb.append('=');
        sb.append(((this.accessURL == null)?"<null>":this.accessURL));
        sb.append(',');
        sb.append("description");
        sb.append('=');
        sb.append(((this.description == null)?"<null>":this.description));
        sb.append(',');
        sb.append("title");
        sb.append('=');
        sb.append(((this.title == null)?"<null>":this.title));
        sb.append(',');
        sb.append("conformsTo");
        sb.append('=');
        sb.append(((this.conformsTo == null)?"<null>":this.conformsTo));
        sb.append(',');
        sb.append("describedBy");
        sb.append('=');
        sb.append(((this.describedBy == null)?"<null>":this.describedBy));
        sb.append(',');
        sb.append("describedByType");
        sb.append('=');
        sb.append(((this.describedByType == null)?"<null>":this.describedByType));
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
        result = ((result* 31)+((this.accessURL == null)? 0 :this.accessURL.hashCode()));
        result = ((result* 31)+((this.downloadURL == null)? 0 :this.downloadURL.hashCode()));
        result = ((result* 31)+((this.format == null)? 0 :this.format.hashCode()));
        result = ((result* 31)+((this.description == null)? 0 :this.description.hashCode()));
        result = ((result* 31)+((this.describedBy == null)? 0 :this.describedBy.hashCode()));
        result = ((result* 31)+((this.mediaType == null)? 0 :this.mediaType.hashCode()));
        result = ((result* 31)+((this.conformsTo == null)? 0 :this.conformsTo.hashCode()));
        result = ((result* 31)+((this.type == null)? 0 :this.type.hashCode()));
        result = ((result* 31)+((this.title == null)? 0 :this.title.hashCode()));
        result = ((result* 31)+((this.describedByType == null)? 0 :this.describedByType.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Distribution) == false) {
            return false;
        }
        Distribution rhs = ((Distribution) other);
        return (((((((((((this.accessURL == rhs.accessURL)||((this.accessURL!= null)&&this.accessURL.equals(rhs.accessURL)))&&((this.downloadURL == rhs.downloadURL)||((this.downloadURL!= null)&&this.downloadURL.equals(rhs.downloadURL))))&&((this.format == rhs.format)||((this.format!= null)&&this.format.equals(rhs.format))))&&((this.description == rhs.description)||((this.description!= null)&&this.description.equals(rhs.description))))&&((this.describedBy == rhs.describedBy)||((this.describedBy!= null)&&this.describedBy.equals(rhs.describedBy))))&&((this.mediaType == rhs.mediaType)||((this.mediaType!= null)&&this.mediaType.equals(rhs.mediaType))))&&((this.conformsTo == rhs.conformsTo)||((this.conformsTo!= null)&&this.conformsTo.equals(rhs.conformsTo))))&&((this.type == rhs.type)||((this.type!= null)&&this.type.equals(rhs.type))))&&((this.title == rhs.title)||((this.title!= null)&&this.title.equals(rhs.title))))&&((this.describedByType == rhs.describedByType)||((this.describedByType!= null)&&this.describedByType.equals(rhs.describedByType))));
    }

    public enum Type {

        @SerializedName("dcat:Distribution")
        DCAT_DISTRIBUTION("dcat:Distribution");
        private final String value;
        private final static Map<String, Distribution.Type> CONSTANTS = new HashMap<String, Distribution.Type>();

        static {
            for (Distribution.Type c: values()) {
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

        public static Distribution.Type fromValue(String value) {
            Distribution.Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
