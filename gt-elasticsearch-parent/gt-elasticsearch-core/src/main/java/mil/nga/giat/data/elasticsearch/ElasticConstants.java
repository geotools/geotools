/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import mil.nga.giat.data.elasticsearch.ElasticAttribute.ElasticGeometryType;

public final class ElasticConstants {

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

}
