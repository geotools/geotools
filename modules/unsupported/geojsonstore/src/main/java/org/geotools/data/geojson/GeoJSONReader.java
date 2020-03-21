/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.geojson;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.bedatadriven.jackson.datatype.jts.parsers.GenericGeometryParser;
import com.bedatadriven.jackson.datatype.jts.parsers.GeometryParser;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.geotools.data.DataUtilities;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;

/**
 * Utility class to provide a reader for GeoJSON streams
 *
 * @author ian
 */
public class GeoJSONReader implements AutoCloseable {
    /** GEOMETRY_NAME */
    public static final String GEOMETRY_NAME = "the_geom";

    private static final Logger LOGGER = Logging.getLogger(GeoJSONReader.class);

    private JsonParser parser;

    private JsonFactory factory;

    private SimpleFeatureType schema;

    SimpleFeatureTypeBuilder typeBuilder = null;

    private SimpleFeatureBuilder builder;
    private int nextID = 0;
    private String baseName = "features";

    private boolean schemaChanged = false;
    private GeometryFactory gFac = new GeometryFactory();
    private URL url;

    public GeoJSONReader(URL url) throws IOException {
        this.url = url;
        factory = new JsonFactory();
        parser = factory.createParser(url);
        baseName = FilenameUtils.getBaseName(url.getPath());
    }

    public boolean isConnected() {

        try (InputStream inputStream = url.openStream()) {
            if (inputStream != null && inputStream.available() > 0) {
                return true;
            }
            url = new URL(url.toExternalForm());
            try (InputStream inputStream2 = url.openStream()) {
                return inputStream2 != null && inputStream2.available() > 0;
            }
        } catch (IOException e) {
            LOGGER.log(Level.FINE, "Failure trying to determine if connected", e);
            return false;
        }
    }

    public FeatureCollection getFeatures() throws IOException {
        if (!isConnected()) {
            throw new IOException("not connected to " + url.toExternalForm());
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JtsModule());
        List<SimpleFeature> features = new ArrayList<>();
        builder = null;
        while (!parser.isClosed()) {
            JsonToken token = parser.nextToken();
            if (token == null) {
                break;
            }
            if (JsonToken.FIELD_NAME.equals(token)
                    && "features".equalsIgnoreCase(parser.currentName())) {
                token = parser.nextToken();
                if (!JsonToken.START_ARRAY.equals(token) || token == null) {
                    break;
                }
                while (parser.nextToken() == JsonToken.START_OBJECT) {
                    ObjectNode node = mapper.readTree(parser);

                    SimpleFeature feature = getNextFeature(node);
                    features.add(feature);
                }
            }
        }
        if (isSchemaChanged()) {
            // retype the features if the schema changes
            List<SimpleFeature> nFeatures = new ArrayList<>(features.size());
            for (SimpleFeature feature : features) {
                if (feature.getFeatureType() != schema) {
                    SimpleFeature nFeature = DataUtilities.reType(schema, feature);
                    nFeatures.add(nFeature);
                } else {
                    nFeatures.add(feature);
                }
            }
            return DataUtilities.collection(nFeatures);
        }
        return DataUtilities.collection(features);
    }

    /** */
    private SimpleFeature getNextFeature(ObjectNode node) throws IOException {
        JsonNode type = node.get("type");
        if (!"Feature".equalsIgnoreCase(type.asText())) {
            throw new RuntimeException(
                    "Unexpected object type in GeoJSON Parsing, expected Feature got '"
                            + type.asText()
                            + "'");
        }
        JsonNode props = node.get("properties");
        if (builder == null) {
            builder = getBuilder(props);
        }
        boolean restart = true;
        SimpleFeature feature = null;
        while (restart) {
            restart = false;

            Iterator<Entry<String, JsonNode>> fields = props.fields();
            while (fields.hasNext()) {
                Entry<String, JsonNode> n = fields.next();
                AttributeDescriptor descriptor = schema.getDescriptor(n.getKey());
                if (descriptor == null) {
                    // we haven't seen this attribute before
                    restart = true;
                    builder = null;
                    schema = null;
                    // rebuild the schema
                    builder = getBuilder(props);
                    setSchemaChanged(true);
                    descriptor = schema.getDescriptor(n.getKey());
                }
                Class<?> binding = descriptor.getType().getBinding();
                if (binding == Integer.class) {
                    builder.set(n.getKey(), n.getValue().asInt());
                } else if (binding == Double.class) {
                    builder.set(n.getKey(), n.getValue().asDouble());
                } else if (binding == String.class) {
                    builder.set(n.getKey(), n.getValue().textValue());
                } else {
                    builder.set(n.getKey(), n.getValue().toString());
                }
            }
            JsonNode geom = node.get("geometry");
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JtsModule());
            GeometryParser<Geometry> gParser = new GenericGeometryParser(gFac);
            Geometry g = gParser.geometryFromJson(geom);
            builder.set(GEOMETRY_NAME, g);

            String newId = baseName + "." + nextID++;
            feature = builder.buildFeature(newId);
        }
        return feature;
    }

    /** */
    private SimpleFeatureBuilder getBuilder(JsonNode props) {

        if (schema == null) {
            typeBuilder = new SimpleFeatureTypeBuilder();
            // GeoJSON is always WGS84
            typeBuilder.setCRS(DefaultGeographicCRS.WGS84);
            typeBuilder.setName(baseName);
            if (typeBuilder.getDefaultGeometry() == null) {
                typeBuilder.setDefaultGeometry(GEOMETRY_NAME);
                typeBuilder.add(GEOMETRY_NAME, Geometry.class, DefaultGeographicCRS.WGS84);
            }
            Iterator<Entry<String, JsonNode>> fields = props.fields();
            while (fields.hasNext()) {
                Entry<String, JsonNode> n = fields.next();
                typeBuilder.nillable(true);
                JsonNode value = n.getValue();

                if (value instanceof IntNode) {
                    typeBuilder.add(n.getKey(), Integer.class);
                } else if (value instanceof DoubleNode) {
                    typeBuilder.add(n.getKey(), Double.class);
                } else {
                    typeBuilder.defaultValue("");
                    typeBuilder.add(n.getKey(), String.class);
                }
            }
            schema = typeBuilder.buildFeatureType();
        }
        return new SimpleFeatureBuilder(schema);
    }

    public FeatureIterator<SimpleFeature> getIterator() throws IOException {
        if (!isConnected()) {
            LOGGER.fine("trying to read an unconnected data stream");
            return new DefaultFeatureCollection(null, null).features();
        }
        return new GeoJsonIterator(parser);
    }

    public FeatureType getSchema() throws IOException {
        if (!isConnected()) {
            throw new IOException("not connected to " + url.toExternalForm());
        }
        return schema;
    }

    /** @param schema the schema to set */
    public void setSchema(SimpleFeatureType schema) {
        this.schema = schema;
    }

    /** @return the schemaChanged */
    public boolean isSchemaChanged() {
        return schemaChanged;
    }

    /** @param schemaChanged the schemaChanged to set */
    public void setSchemaChanged(boolean schemaChanged) {
        this.schemaChanged = schemaChanged;
    }

    @Override
    public void close() throws IOException {
        parser.close();
        parser = null;
    }

    private class GeoJsonIterator implements FeatureIterator<SimpleFeature>, AutoCloseable {
        /** @throws IOException */
        ObjectMapper mapper = new ObjectMapper();

        JsonParser parser;

        private SimpleFeature feature;

        public GeoJsonIterator(JsonParser parser) throws IOException {
            if (!isConnected()) {
                throw new IOException("not connected to " + url.toExternalForm());
            }
            this.parser = parser;
            builder = null;
            while (!parser.isClosed()) {
                JsonToken token = parser.nextToken();
                if (token == null) {
                    break;
                }
                if (JsonToken.FIELD_NAME.equals(token)
                        && "features".equalsIgnoreCase(parser.currentName())) {
                    token = parser.nextToken();

                    if (!JsonToken.START_ARRAY.equals(token) || token == null) {
                        throw new IOException("No Features found");
                    }
                    break;
                }
            }
        }

        @Override
        public boolean hasNext() {
            // make sure not to read too far if they call hasNext() multiple times
            if (feature != null) {
                return true;
            }
            try {

                if (parser.nextToken() == JsonToken.START_OBJECT) {
                    ObjectNode node = mapper.readTree(parser);
                    feature = getNextFeature(node);
                    if (feature != null) return true;
                }
            } catch (IOException e) {
                LOGGER.log(Level.FINER, e.getMessage(), e);
            }
            return false;
        }

        @Override
        public SimpleFeature next() throws NoSuchElementException {

            if (feature != null) {
                SimpleFeature ret = feature;
                feature = null;
                return ret;
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void close() {
            parser = null;
        }
    }
}
