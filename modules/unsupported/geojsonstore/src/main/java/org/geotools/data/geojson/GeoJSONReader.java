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
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.Geometries;
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
    public static final String GEOMETRY_NAME = "geometry";

    private static final Logger LOGGER = Logging.getLogger(GeoJSONReader.class);

    private JsonParser parser;

    private static JsonFactory factory = new JsonFactory();;

    private SimpleFeatureType schema;

    private SimpleFeatureBuilder builder;

    private int nextID = 0;

    private String baseName = "features";

    private boolean schemaChanged = false;

    private static GeometryFactory gFac = new GeometryFactory();

    private URL url;

    private InputStream is;

    private boolean guessingDates = true;

    /** For reading be a bit more lenient regarding what we parse */
    private DateParser dateParser = new DateParser();

    public GeoJSONReader(URL url) throws IOException {
        this.url = url;
        this.is = url.openStream();
        parser = factory.createParser(url);
        baseName = FilenameUtils.getBaseName(url.getPath());
    }

    public GeoJSONReader(InputStream is) throws IOException {
        parser = factory.createParser(is);
    }

    /**
     * Returns true if the parser is trying to convert string formatted as dates into
     * java.util.Date, false otherwise. Defaults to true.
     */
    public boolean isGuessingDates() {
        return guessingDates;
    }

    /** Enables/Disables guessing strings formatted as dates into java.util.Date. */
    public void setGuessingDates(boolean guessingDates) {
        this.guessingDates = guessingDates;
    }

    public boolean isConnected() {
        if (url != null) {
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
        return true;
    }

    public static SimpleFeature parseFeature(String json) throws JsonParseException, IOException {
        try (JsonParser lParser = factory.createParser(new ByteArrayInputStream(json.getBytes()))) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JtsModule());
            ObjectNode node = mapper.readTree(lParser);
            try (GeoJSONReader reader = new GeoJSONReader((InputStream) null)) {
                SimpleFeature feature = reader.getNextFeature(node);
                return feature;
            }
        }
    }

    /**
     * @param jsonString
     * @return
     */
    public static SimpleFeatureCollection parseFeatureCollection(String jsonString) {
        try (GeoJSONReader reader =
                new GeoJSONReader(new ByteArrayInputStream(jsonString.getBytes()))) {
            SimpleFeatureCollection features = (SimpleFeatureCollection) reader.getFeatures();
            return features;
        } catch (IOException e) {
            throw new RuntimeException("problem parsing FeatureCollection", e);
        }
    }

    /**
     * @param input
     * @return
     */
    public static Geometry parseGeometry(String input) {
        try (JsonParser lParser =
                factory.createParser(new ByteArrayInputStream(input.getBytes()))) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JtsModule());
            ObjectNode node = mapper.readTree(lParser);
            GeometryParser<Geometry> gParser = new GenericGeometryParser(gFac);
            Geometry g = gParser.geometryFromJson(node);
            return g;
        } catch (IOException e) {
            throw new RuntimeException("problem parsing Geometry", e);
        }
    }

    public SimpleFeatureCollection getFeatures() throws IOException {
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
        if (type == null) {
            throw new RuntimeException(
                    "Missing object type in GeoJSON Parsing, expected type=Feature here");
        }
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
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JtsModule());
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
                } else if (binding == Boolean.class) {
                    builder.set(n.getKey(), n.getValue().booleanValue());
                } else if (binding == Object.class) {
                    builder.set(n.getKey(), n.getValue());
                } else if (binding == List.class) {
                    ArrayNode array = (ArrayNode) n.getValue();
                    List<Object> list = new ArrayList<>();
                    for (int i = 0; i < array.size(); i++) {
                        JsonNode item = array.get(i);
                        Object vc;
                        switch (item.getNodeType()) {
                            case BOOLEAN:
                                vc = item.asBoolean();
                                break;
                            case NUMBER:
                                vc = item.asDouble();
                                break;
                            case STRING:
                                vc = item.asText();
                                break;
                            case NULL:
                                vc = null;
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Cannot handle arrays with values of type "
                                                + item.getNodeType());
                        }
                        list.add(vc);
                    }
                    builder.set(n.getKey(), list);
                } else if (Geometry.class.isAssignableFrom(binding)) {
                    GeometryParser<Geometry> gParser = new GenericGeometryParser(gFac);
                    Geometry g = gParser.geometryFromJson(n.getValue());
                    builder.set(n.getKey(), g);
                } else if (Date.class.isAssignableFrom(binding)) {
                    String text = n.getValue().asText();
                    Date date = dateParser.parse(text);
                    if (date != null) {
                        builder.set(n.getKey(), date);
                    } else {
                        // will go through the Converter machinery which, depending on the
                        // classpath, might try out a larger set of conversions, or end up
                        // with a null value
                        builder.set(n.getKey(), n.getValue().asText());
                    }

                } else {
                    LOGGER.warning("Unable to parse object of type " + binding);
                    builder.set(n.getKey(), n.getValue().asText());
                }
            }
            JsonNode geom = node.get("geometry");
            GeometryParser<Geometry> gParser = new GenericGeometryParser(gFac);
            Geometry g = gParser.geometryFromJson(geom);
            builder.set(GEOMETRY_NAME, g);

            String newId = baseName + "." + nextID++;
            feature = builder.buildFeature(newId);
        }
        return feature;
    }

    /** Create a simpleFeatureBuilder for the current schema + these new properties. */
    private SimpleFeatureBuilder getBuilder(JsonNode props) {

        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        // GeoJSON is always WGS84
        typeBuilder.setCRS(DefaultGeographicCRS.WGS84);
        typeBuilder.setName(baseName);
        HashSet<String> existing = new HashSet<>();
        if (schema != null) {
            // copy the existing types to the new schema
            for (AttributeDescriptor att : schema.getAttributeDescriptors()) {
                typeBuilder.add(att);
                existing.add(att.getLocalName());
            }
        }

        if (typeBuilder.getDefaultGeometry() == null) {
            typeBuilder.setDefaultGeometry(GEOMETRY_NAME);
            if (!existing.contains(GEOMETRY_NAME)) {
                typeBuilder.add(GEOMETRY_NAME, Geometry.class, DefaultGeographicCRS.WGS84);
            }
        }
        Iterator<Entry<String, JsonNode>> fields = props.fields();
        while (fields.hasNext()) {
            Entry<String, JsonNode> n = fields.next();
            if (existing.contains(n.getKey())) {
                continue;
            } else {
                existing.add(n.getKey());
            }
            typeBuilder.nillable(true);
            JsonNode value = n.getValue();

            if (value instanceof IntNode) {
                typeBuilder.add(n.getKey(), Integer.class);
            } else if (value instanceof DoubleNode) {
                typeBuilder.add(n.getKey(), Double.class);
            } else if (value instanceof BooleanNode) {
                typeBuilder.add(n.getKey(), Boolean.class);
            } else if (value instanceof ObjectNode) {
                if (Optional.ofNullable(value.get("type"))
                        .map(t -> t.asText())
                        .map(t -> Geometries.getForName(t))
                        .isPresent()) {
                    typeBuilder.add(n.getKey(), Geometry.class, DefaultGeographicCRS.WGS84);
                } else {
                    // a complex object, we don't know what it is going to be
                    typeBuilder.add(n.getKey(), Object.class);
                }
            } else if (value instanceof ArrayNode) {
                typeBuilder.add(n.getKey(), List.class);
            } else if (value instanceof TextNode && guessingDates) {
                // it could be a date too
                Date date = dateParser.parse(value.asText());
                if (date != null) {
                    typeBuilder.add(n.getKey(), Date.class);
                } else {
                    typeBuilder.defaultValue("");
                    typeBuilder.add(n.getKey(), String.class);
                }
            } else {
                typeBuilder.defaultValue("");
                typeBuilder.add(n.getKey(), String.class);
            }
        }
        schema = typeBuilder.buildFeatureType();

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
        if (parser != null) {
            parser.close();
            parser = null;
        }
        if (is != null) {
            is.close();
        }
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
        @SuppressWarnings("PMD.UseTryWithResources")
        public void close() {
            try {
                try {
                    if (parser != null) parser.close();
                } finally {
                    if (is != null) is.close();
                }
            } catch (IOException e) {
                throw new RuntimeException("Unexpected failure closing iterator", e);
            }
            parser = null;
        }
    }
}
