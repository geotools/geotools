/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import mil.nga.giat.data.elasticsearch.ElasticAttribute.ElasticGeometryType;

/**
 * Describes an Elasticsearch layer configuration as set of {@link ElasticAttribute}
 */
public class ElasticLayerConfiguration implements Serializable {

    private static final long serialVersionUID = 1838874365349725912L;

    /**
     * Key used in the feature type user data to store the format for date
     * fields, if relevant.
     */
    public static final String DATE_FORMAT = "date_format";

    /**
     * Key used in the feature type user data to store the full name for fields.
     */
    public static final String FULL_NAME = "full_name";
    
    /**
     * Key used in the feature type user data to store the Elasticsearch geometry
     * type ({@link ElasticGeometryType}).
     */
    public static final String GEOMETRY_TYPE = "geometry_type";

    /**
     * Key used in the feature type user data to indicate whether the field is analyzed.
     */
    public static final String ANALYZED = "analyzed";
    
    /**
     * Key used in the feature type user data to indicate whether the field is nested.
     */
    public static final String NESTED = "nested";

    /**
     * Key to identify the Elasticsearch layer configuration.
     */
    public static final String KEY = "ElasticLayerConfiguration";
    
    private String docType;
    
    private String layerName;
    
    private final List<ElasticAttribute> attributes;
    
    public ElasticLayerConfiguration(String docType) {
        this.docType = docType;
        this.layerName = docType;
        this.attributes = new ArrayList<>();
    }
    
    public ElasticLayerConfiguration(ElasticLayerConfiguration other) {
        this(other.docType);
        setLayerName(other.layerName);
        for (final ElasticAttribute attribute : other.attributes) {
            attributes.add(new ElasticAttribute(attribute));
        }
    }

    public String getDocType() {
        return docType;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public List<ElasticAttribute> getAttributes() {
        return attributes;
    }

}
