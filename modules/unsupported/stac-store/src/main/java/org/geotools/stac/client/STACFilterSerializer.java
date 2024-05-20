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
package org.geotools.stac.client;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.geootols.filter.text.cql_2.CQL2;
import org.geotools.api.filter.Filter;
import org.geotools.filter.text.cqljson.CQL2Json;

public class STACFilterSerializer extends JsonSerializer<Filter> {
    @Override
    public void serialize(
            Filter filter, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        FilterLang lang = null;
        Object container = jsonGenerator.currentValue();
        if (container instanceof SearchQuery) {
            lang = ((SearchQuery) container).getFilterLang();
        }
        if (lang == null) lang = FilterLang.CQL2_JSON;

        Filter defaulted = GeometryDefaulter.defaultGeometry(filter);
        if (lang == FilterLang.CQL2_TEXT) {
            jsonGenerator.writeString(CQL2.toCQL2(defaulted));
        } else if (lang == FilterLang.CQL2_JSON) {
            JsonNode node = CQL2Json.toCQL2Json(defaulted);
            jsonGenerator.writeObject(node);
        }
    }
}
