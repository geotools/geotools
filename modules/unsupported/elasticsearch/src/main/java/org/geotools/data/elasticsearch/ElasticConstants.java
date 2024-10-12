/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.elasticsearch;

import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.Map;

final class ElasticConstants {

    public static final Map<String, Object> MATCH_ALL = ImmutableMap.of("match_all", Collections.emptyMap());

    /** Key used in the feature type user data to store the format for date fields, if relevant. */
    public static final String DATE_FORMAT = "date_format";

    /** Key used in the feature type user data to store the full name for fields. */
    public static final String FULL_NAME = "full_name";

    /**
     * Key used in the feature type user data to store the Elasticsearch geometry type ({@link
     * ElasticAttribute.ElasticGeometryType}).
     */
    public static final String GEOMETRY_TYPE = "geometry_type";

    /** Key used in the feature type user data to indicate whether the field is analyzed. */
    public static final String ANALYZED = "analyzed";

    /** Key used in the feature type user data to indicate whether the field is nested. */
    public static final String NESTED = "nested";
}
