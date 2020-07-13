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
package org.geotools.data.geojson;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.PropertyType;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * Wrapper to handle writing GeoJSON FeatureCollections
 *
 * @author ian
 */
public class GeoJSONWriter implements AutoCloseable {
    static Logger LOGGER = Logging.getLogger("org.geotools.data.geojson");

    private OutputStream out;

    JsonGenerator generator;

    private ObjectMapper mapper;

    private CoordinateReferenceSystem outCRS;

    private MathTransform transform;

    private CoordinateReferenceSystem lastCRS;

    private boolean initalised = false;

    private ReferencedEnvelope bounds = null;

    private boolean encodeFeatureCollectionBounds = false;

    private boolean inArray = false;

    public GeoJSONWriter(OutputStream outputStream) throws IOException {
        // force the output CRS to be long, lat as required by spec
        CRSAuthorityFactory cFactory = CRS.getAuthorityFactory(true);
        try {
            outCRS = cFactory.createCoordinateReferenceSystem("EPSG:4326");
        } catch (FactoryException e) {
            throw new RuntimeException("CRS factory not found in GeoJSONDatastore writer", e);
        }
        mapper = new ObjectMapper();
        mapper.registerModule(new JtsModule());

        if (outputStream instanceof BufferedOutputStream) {
            this.out = outputStream;
        } else {
            this.out = new BufferedOutputStream(outputStream);
        }
        JsonFactory factory = new JsonFactory();
        generator = factory.createGenerator(out);
    }

    /** @throws IOException */
    private void initialise() throws IOException {
        generator.writeStartObject();
        generator.writeStringField("type", "FeatureCollection");
        if (bounds != null) {
            if (encodeFeatureCollectionBounds) {
                generator.writeArrayFieldStart("bbox");
                generator.writeArray(
                        new double[] {
                            bounds.getMinX(), bounds.getMinY(), bounds.getMaxX(), bounds.getMaxY()
                        },
                        0,
                        4);
            }
        }

        generator.writeFieldName("features");
        generator.writeStartArray();
        inArray = true;
        initalised = true;
    }

    public void write(SimpleFeature currentFeature) throws IOException {
        if (!initalised) {
            initialise();
        }
        // System.out.println("JSONWriter writing " + currentFeature.getID());
        writeFeature(currentFeature, generator);
    }

    /**
     * @param currentFeature
     * @throws IOException
     * @throws JsonProcessingException
     */
    private void writeFeature(SimpleFeature currentFeature, JsonGenerator g)
            throws IOException, JsonProcessingException {
        g.writeStartObject();
        g.writeStringField("type", "Feature");
        g.writeFieldName("properties");
        g.writeStartObject();
        for (Property p : currentFeature.getProperties()) {
            PropertyType type = p.getType();
            if (type instanceof GeometryType) {
                continue;
            }
            Object value = p.getValue();
            String name = p.getName().getLocalPart();
            if (value == null) {
                generator.writeNullField(name);
                continue;
            }
            Class<?> binding = p.getType().getBinding();
            if (binding == Integer.class) {
                g.writeNumberField(name, (int) value);
            } else if (binding == Double.class) {
                g.writeNumberField(name, (double) value);
            } else {
                g.writeFieldName(name);
                g.writeString(value.toString());
            }
        }
        g.writeEndObject();

        Geometry defaultGeometry = (Geometry) currentFeature.getDefaultGeometry();
        // Check CRS and Axis order before writing out to comply with
        // https://tools.ietf.org/html/rfc7946

        CoordinateReferenceSystem inCRS =
                currentFeature
                        .getDefaultGeometryProperty()
                        .getDescriptor()
                        .getCoordinateReferenceSystem();
        if (transform == null || inCRS != lastCRS) {
            lastCRS = inCRS;
            try {
                transform = CRS.findMathTransform(inCRS, outCRS, true);
            } catch (FactoryException e) {
                throw new RuntimeException(e);
            }
        }
        if (!CRS.equalsIgnoreMetadata(inCRS, outCRS)) {
            // reproject
            try {
                defaultGeometry = JTS.transform(defaultGeometry, transform);
            } catch (MismatchedDimensionException | TransformException e) {
                throw new RuntimeException(e);
            }
        }
        if (defaultGeometry != null) {
            g.writeFieldName("geometry");
            String gString = mapper.writeValueAsString(defaultGeometry);

            g.writeRawValue(gString);
            g.writeStringField("id", currentFeature.getID());
        } else {
            g.writeNull();
        }
        g.writeEndObject();
        g.flush();
    }

    @Override
    public void close() throws IOException {
        try {
            if (inArray) {
                generator.writeEndArray();
                generator.writeEndObject();
            }

            generator.close();
        } finally {
            out.close();
        }
    }

    /** @return the bounds */
    public ReferencedEnvelope getBounds() {
        return bounds;
    }

    /** @param bounds the bounds to set */
    public void setBounds(ReferencedEnvelope bounds) {
        this.bounds = bounds;
    }

    /** @return the encodeFeatureCollectionBounds */
    public boolean isEncodeFeatureCollectionBounds() {
        return encodeFeatureCollectionBounds;
    }

    /** @param encodeFeatureCollectionBounds the encodeFeatureCollectionBounds to set */
    public void setEncodeFeatureCollectionBounds(boolean encodeFeatureCollectionBounds) {
        this.encodeFeatureCollectionBounds = encodeFeatureCollectionBounds;
    }

    public static String toGeoJSON(Geometry geometry) {
        ObjectMapper lMapper = new ObjectMapper();
        lMapper.registerModule(new JtsModule());
        try {
            return lMapper.writeValueAsString(geometry);
        } catch (JsonProcessingException e) {
            LOGGER.warning(e.getLocalizedMessage());
        }
        return "";
    }

    /**
     * @param f
     * @return
     */
    public static String toGeoJSON(SimpleFeature f) {
        JsonFactory factory = new JsonFactory();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (JsonGenerator lGenerator = factory.createGenerator(out);
                GeoJSONWriter writer = new GeoJSONWriter(out)) {
            writer.writeFeature(f, lGenerator);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new String(out.toByteArray());
    }

    /**
     * @param fc
     * @return
     */
    public static String toGeoJSON(SimpleFeatureCollection fc) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (GeoJSONWriter writer = new GeoJSONWriter(out);
                SimpleFeatureIterator itr = fc.features()) {
            while (itr.hasNext()) {
                SimpleFeature f = itr.next();
                writer.write(f);
            }
        } catch (IOException e) {
            // very hard to actually generate this
            LOGGER.warning("Unexpected IOException converting featureCollection to GeoJSON");
            LOGGER.log(
                    Level.FINE,
                    "Unexpected IOException converting featureCollection to GeoJSON",
                    e);
        }
        return new String(out.toByteArray());
    }
}
