package org.geotools.data.arcgisrest.schema.catalog;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Project Open Data Distribution
 *
 * <p>Validates an entire collection of common core metadata JSON objects. Agencies produce said
 * collections in the form of Data.json files.
 */
public class Distribution {

    /**
     * Metadata Context
     *
     * <p>IRI for the JSON-LD data type. This should be dcat:Distribution for each Distribution
     */
    @SerializedName("@type")
    @Expose
    private Distribution.Type type;
    /**
     * Download URL
     *
     * <p>URL providing direct access to a downloadable file of a dataset
     */
    @SerializedName("downloadURL")
    @Expose
    private URI downloadURL;
    /**
     * Media Type
     *
     * <p>The machine-readable file format (IANA Media Type or MIME Type) of the distribution’s
     * downloadURL
     */
    @SerializedName("mediaType")
    @Expose
    private Object mediaType;
    /**
     * Format
     *
     * <p>A human-readable description of the file format of a distribution
     */
    @SerializedName("format")
    @Expose
    private Object format;
    /**
     * Access URL
     *
     * <p>URL providing indirect access to a dataset
     */
    @SerializedName("accessURL")
    @Expose
    private Object accessURL;
    /**
     * Description
     *
     * <p>Human-readable description of the distribution
     */
    @SerializedName("description")
    @Expose
    private Object description;
    /**
     * Title
     *
     * <p>Human-readable name of the distribution
     */
    @SerializedName("title")
    @Expose
    private Object title;
    /**
     * Data Standard
     *
     * <p>URL providing indirect access to a dataset
     */
    @SerializedName("conformsTo")
    @Expose
    private Object conformsTo;
    /**
     * Data Dictionary
     *
     * <p>URL to the data dictionary for the distribution found at the downloadURL
     */
    @SerializedName("describedBy")
    @Expose
    private Object describedBy;
    /**
     * Data Dictionary Type
     *
     * <p>The machine-readable file format (IANA Media Type or MIME Type) of the distribution’s
     * describedBy URL
     */
    @SerializedName("describedByType")
    @Expose
    private Object describedByType;

    /**
     * Metadata Context
     *
     * <p>IRI for the JSON-LD data type. This should be dcat:Distribution for each Distribution
     *
     * @return The type
     */
    public Distribution.Type getType() {
        return type;
    }

    /**
     * Metadata Context
     *
     * <p>IRI for the JSON-LD data type. This should be dcat:Distribution for each Distribution
     *
     * @param type The @type
     */
    public void setType(Distribution.Type type) {
        this.type = type;
    }

    /**
     * Download URL
     *
     * <p>URL providing direct access to a downloadable file of a dataset
     *
     * @return The downloadURL
     */
    public URI getDownloadURL() {
        return downloadURL;
    }

    /**
     * Download URL
     *
     * <p>URL providing direct access to a downloadable file of a dataset
     *
     * @param downloadURL The downloadURL
     */
    public void setDownloadURL(URI downloadURL) {
        this.downloadURL = downloadURL;
    }

    /**
     * Media Type
     *
     * <p>The machine-readable file format (IANA Media Type or MIME Type) of the distribution’s
     * downloadURL
     *
     * @return The mediaType
     */
    public Object getMediaType() {
        return mediaType;
    }

    /**
     * Media Type
     *
     * <p>The machine-readable file format (IANA Media Type or MIME Type) of the distribution’s
     * downloadURL
     *
     * @param mediaType The mediaType
     */
    public void setMediaType(Object mediaType) {
        this.mediaType = mediaType;
    }

    /**
     * Format
     *
     * <p>A human-readable description of the file format of a distribution
     *
     * @return The format
     */
    public Object getFormat() {
        return format;
    }

    /**
     * Format
     *
     * <p>A human-readable description of the file format of a distribution
     *
     * @param format The format
     */
    public void setFormat(Object format) {
        this.format = format;
    }

    /**
     * Access URL
     *
     * <p>URL providing indirect access to a dataset
     *
     * @return The accessURL
     */
    public Object getAccessURL() {
        return accessURL;
    }

    /**
     * Access URL
     *
     * <p>URL providing indirect access to a dataset
     *
     * @param accessURL The accessURL
     */
    public void setAccessURL(Object accessURL) {
        this.accessURL = accessURL;
    }

    /**
     * Description
     *
     * <p>Human-readable description of the distribution
     *
     * @return The description
     */
    public Object getDescription() {
        return description;
    }

    /**
     * Description
     *
     * <p>Human-readable description of the distribution
     *
     * @param description The description
     */
    public void setDescription(Object description) {
        this.description = description;
    }

    /**
     * Title
     *
     * <p>Human-readable name of the distribution
     *
     * @return The title
     */
    public Object getTitle() {
        return title;
    }

    /**
     * Title
     *
     * <p>Human-readable name of the distribution
     *
     * @param title The title
     */
    public void setTitle(Object title) {
        this.title = title;
    }

    /**
     * Data Standard
     *
     * <p>URL providing indirect access to a dataset
     *
     * @return The conformsTo
     */
    public Object getConformsTo() {
        return conformsTo;
    }

    /**
     * Data Standard
     *
     * <p>URL providing indirect access to a dataset
     *
     * @param conformsTo The conformsTo
     */
    public void setConformsTo(Object conformsTo) {
        this.conformsTo = conformsTo;
    }

    /**
     * Data Dictionary
     *
     * <p>URL to the data dictionary for the distribution found at the downloadURL
     *
     * @return The describedBy
     */
    public Object getDescribedBy() {
        return describedBy;
    }

    /**
     * Data Dictionary
     *
     * <p>URL to the data dictionary for the distribution found at the downloadURL
     *
     * @param describedBy The describedBy
     */
    public void setDescribedBy(Object describedBy) {
        this.describedBy = describedBy;
    }

    /**
     * Data Dictionary Type
     *
     * <p>The machine-readable file format (IANA Media Type or MIME Type) of the distribution’s
     * describedBy URL
     *
     * @return The describedByType
     */
    public Object getDescribedByType() {
        return describedByType;
    }

    /**
     * Data Dictionary Type
     *
     * <p>The machine-readable file format (IANA Media Type or MIME Type) of the distribution’s
     * describedBy URL
     *
     * @param describedByType The describedByType
     */
    public void setDescribedByType(Object describedByType) {
        this.describedByType = describedByType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(type)
                .append(downloadURL)
                .append(mediaType)
                .append(format)
                .append(accessURL)
                .append(description)
                .append(title)
                .append(conformsTo)
                .append(describedBy)
                .append(describedByType)
                .toHashCode();
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
        return new EqualsBuilder()
                .append(type, rhs.type)
                .append(downloadURL, rhs.downloadURL)
                .append(mediaType, rhs.mediaType)
                .append(format, rhs.format)
                .append(accessURL, rhs.accessURL)
                .append(description, rhs.description)
                .append(title, rhs.title)
                .append(conformsTo, rhs.conformsTo)
                .append(describedBy, rhs.describedBy)
                .append(describedByType, rhs.describedByType)
                .isEquals();
    }

    public enum Type {
        @SerializedName("dcat:Distribution")
        DCAT_DISTRIBUTION("dcat:Distribution");
        private final String value;
        private static final Map<String, Distribution.Type> CONSTANTS =
                new HashMap<String, Distribution.Type>();

        static {
            for (Distribution.Type c : values()) {
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
