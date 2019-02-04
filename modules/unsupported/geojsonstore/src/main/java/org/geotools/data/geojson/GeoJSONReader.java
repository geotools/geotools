package org.geotools.data.geojson;

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
import org.locationtech.jts.io.ParseException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Utility class to provide a reader for GeoJSON streams
 *
 * @author ian
 */
public class GeoJSONReader {
  /** GEOMETRY_NAME */
  public static final String GEOMETRY_NAME = "the_geom";

  private static final Logger LOGGER = Logging.getLogger(GeoJSONReader.class);

  private InputStream inputStream;

  private URL url;

  private JsonParser parser;

  private JsonFactory factory;

  private SimpleFeatureType schema;

  static org.locationtech.jts.io.geojson.GeoJsonReader jReader =
      new org.locationtech.jts.io.geojson.GeoJsonReader();
  SimpleFeatureTypeBuilder typeBuilder = null;

  private SimpleFeatureBuilder builder;
  private int nextID = 0;
  private String baseName = "features";
  public GeoJSONReader(URL url) throws IOException {
    this.url = url;
    factory = new JsonFactory();
    parser = factory.createParser(url);
    baseName = FilenameUtils.getBaseName(url.getPath());
  }

  public boolean isConnected() {
    try {
      inputStream = url.openStream();
      if (inputStream == null) {
        url = new URL(url.toExternalForm());
        inputStream = url.openStream();
      }
    } catch (IOException e) {
      // whoops
      return false;
    }
    try {
      if (inputStream.available() == 0) {
        url = new URL(url.toExternalForm());
        inputStream = url.openStream();
      }

      LOGGER.finest("inputstream is " + inputStream);
      return (inputStream != null) && (inputStream.available() > 0);
    } catch (IOException e) {
      // something went wrong
      LOGGER.throwing(this.getClass().getName(), "isConnected", e);
      return false;
    }
  }

  public FeatureCollection getFeatures() throws IOException {
    if (!isConnected()) {
      throw new IOException("not connected to " + url.toExternalForm());
    }
    ObjectMapper mapper = new ObjectMapper();
    List<SimpleFeature> features = new ArrayList<>();
    builder = null;
    while (!parser.isClosed()) {
      JsonToken token = parser.nextToken();
      if (token == null) {
        break;
      }
      if (JsonToken.FIELD_NAME.equals(token) && "features".equalsIgnoreCase(parser.currentName())) {
        token = parser.nextToken();
        if (!JsonToken.START_ARRAY.equals(token) || token == null) {
          break;
        }
        while (parser.nextToken() == JsonToken.START_OBJECT) {
          ObjectNode node = mapper.readTree(parser);
          if (node == null) {
            System.out.println(parser.nextTextValue());
          }
          SimpleFeature feature = getNextFeature(node);
          features.add(feature);
        }
      }
    }

    return DataUtilities.collection(features);
  }

  /**
   * @param node
   * @return
   * @throws IOException
   */
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
    Iterator<Entry<String, JsonNode>> fields = props.fields();
    while (fields.hasNext()) {
      Entry<String, JsonNode> n = fields.next();
      Class<?> binding = schema.getDescriptor(n.getKey()).getType().getBinding();
      if(binding == Integer.class) {
        builder.set(n.getKey(), n.getValue().asInt());     
      } else if(binding == Double.class) {
        builder.set(n.getKey(), n.getValue().asDouble());     
      } else if(binding == String.class){
        builder.set(n.getKey(), n.getValue().textValue());     
      } else {
        builder.set(n.getKey(), n.getValue().toString());
      }
    }
    JsonNode geom = node.get("geometry");
    Geometry g=null;
    try {
      String gString = geom.toString();
      if(!geom.isNull()&&!gString.isEmpty()) {
        g = jReader.read(gString);
      }
      builder.set(GEOMETRY_NAME, g);
    } catch (ParseException e) {
      LOGGER.log(Level.FINER, e.getMessage(), e);
      throw new IOException(e);
    }
    JsonNode id = node.get("id");
    if (id != null && id.isValueNode()) {}

    SimpleFeature feature = builder.buildFeature(baseName+"."+nextID++);
    // System.out.println(feature);
    return feature;
  }

  /**
   * @param props
   * @return
   */
  private SimpleFeatureBuilder getBuilder(JsonNode props) {

    if (typeBuilder == null) {
      if (schema == null) {
        typeBuilder = new SimpleFeatureTypeBuilder();

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
    }
    return new SimpleFeatureBuilder(schema);
  }

  public FeatureIterator<SimpleFeature> getIterator() throws IOException {
    if (!isConnected()) {
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

  private class GeoJsonIterator implements FeatureIterator<SimpleFeature> {
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

      if (feature != null) return feature;
      else throw new NoSuchElementException();
    }

    @Override
    public void close() {
      parser = null;
    }
  }
}
