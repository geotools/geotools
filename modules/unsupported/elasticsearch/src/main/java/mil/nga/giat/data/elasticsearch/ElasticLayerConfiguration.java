/*
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Describes an Elasticsearch layer configuration as set of {@link ElasticAttribute}
 */
public class ElasticLayerConfiguration implements Serializable {

    private static final long serialVersionUID = 1838874365349725912L;

    /**
     * Key to identify the Elasticsearch layer configuration.
     */
    public static final String KEY = "ElasticLayerConfiguration";

    private final String docType;

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
