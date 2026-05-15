/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.text.cqljson;

import java.io.IOException;
import org.geotools.api.filter.Filter;
import org.geotools.filter.text.cql2.CQLException;
import tools.jackson.databind.JsonNode;

public class CQLJsonTest {
    protected Filter parse(String line) throws IOException, CQLException {
        return CQL2Json.toFilter(line);
    }

    protected JsonNode serialize(Filter filter) {
        return CQL2Json.toCQL2Json(filter);
    }
}
