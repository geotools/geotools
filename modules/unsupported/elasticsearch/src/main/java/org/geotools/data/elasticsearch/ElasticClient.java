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

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

interface ElasticClient extends Closeable {

    String RUN_AS = "es-security-runas-user";

    double getVersion();

    List<String> getTypes(String indexName) throws IOException;

    Map<String, Object> getMapping(String indexName, String type) throws IOException;

    ElasticResponse search(String searchIndices, String type, ElasticRequest request) throws IOException;

    ElasticResponse scroll(String scrollId, Integer scrollTime) throws IOException;

    @Override
    void close() throws IOException;

    void clearScroll(Set<String> scrollIds) throws IOException;
}
