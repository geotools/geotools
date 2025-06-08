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
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.cfg.CacheProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.time.FastDateFormat;
import org.geotools.api.feature.Property;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.type.GeometryType;
import org.geotools.api.feature.type.PropertyType;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CRSAuthorityFactory;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.transform.IdentityTransform;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;

/**
 * Wrapper to handle writing GeoJSON FeatureCollections
 *
 * @author ian
 */
public class GeoJSONWriter implements AutoCloseable {
    static Logger LOGGER = Logging.getLogger("org.geotools.data.geojson");

    /** Date format (ISO 8601) */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSX";

    /** Default time zone, GMT */
    public static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone("GMT");

    public static final FastDateFormat DEFAULT_DATE_FORMATTER =
            FastDateFormat.getInstance(DEFAULT_DATE_FORMAT, DEFAULT_TIME_ZONE);

    /** Maximum number of decimal places (see https://xkcd.com/2170/ before changing it) */
    private int maxDecimals = JtsModule.DEFAULT_MAX_DECIMALS;

    private OutputStream out;

    JsonGenerator generator;

    private ObjectMapper mapper;

    private CoordinateReferenceSystem outCRS;

    private MathTransform transform;

    private CoordinateReferenceSystem lastCRS;

    private boolean initalised = false;

    private ReferencedEnvelope bounds = null;

    private boolean encodeFeatureBounds = false;

    private boolean inArray = false;

    private boolean encodeFeatureCollectionCRS = false;
    private final JtsModule module;
    private NumberFormat formatter = NumberFormat.getNumberInstance(Locale.ENGLISH);
    private boolean notWritenBbox = true;
    private boolean singleFeature = false;
    private FastDateFormat dateFormatter = DEFAULT_DATE_FORMATTER;

    /**
     * Prepares a writer over the target output stream.
     *
     * @param outputStream
     * @throws IOException
     */
    public GeoJSONWriter(OutputStream outputStream) throws IOException {
        // force the output CRS to be long, lat as required by spec
        CRSAuthorityFactory cFactory = CRS.getAuthorityFactory(true);
        try {
            outCRS = cFactory.createCoordinateReferenceSystem("EPSG:4326");
        } catch (FactoryException e) {
            throw new RuntimeException("CRS factory not found in GeoJSONDatastore writer", e);
        }
        mapper = new ObjectMapper();
        module = new JtsModule(maxDecimals);
        mapper.registerModule(module);

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
        if (!singleFeature) {
            generator.writeStartObject();
            generator.writeStringField("type", "FeatureCollection");
            if (bounds != null && isEncodeFeatureBounds()) {
                /* If they have provided a bbox we can write it out at the top of the file,
                 * else we'll need to do it at the bottom.
                 */
                writeBoundingEnvelope();
                notWritenBbox = false;
            }
            generator.writeFieldName("features");
            generator.writeStartArray();
            inArray = true;
        }
        initalised = true;
    }

    private void writeBoundingEnvelope() throws IOException {
        if (!CRS.equalsIgnoreMetadata(bounds.getCoordinateReferenceSystem(), outCRS)) {
            // reproject
            try {
                bounds = bounds.transform(outCRS, true);
            } catch (MismatchedDimensionException | TransformException | FactoryException e) {
                throw new RuntimeException(e);
            }
        }
        generator.writeArrayFieldStart("bbox");
        double[] coords = {bounds.getMinX(), bounds.getMinY(), bounds.getMaxX(), bounds.getMaxY()};
        formatter.setMaximumFractionDigits(maxDecimals);

        for (double c : coords) {
            generator.writeNumber(formatter.format(c));
        }
        generator.writeEndArray();
    }

    /**
     * Writes a single feature onto the output.
     *
     * @param currentFeature
     * @throws IOException
     */
    public void write(SimpleFeature currentFeature) throws IOException {
        if (!initalised) {
            initialise();
        }

        writeFeature(currentFeature, generator);
    }

    /**
     * @param currentFeature
     * @throws IOException
     * @throws JsonProcessingException
     */
    private void writeFeature(SimpleFeature currentFeature, JsonGenerator g)
            throws IOException, JsonProcessingException {
        Geometry defaultGeometry = (Geometry) currentFeature.getDefaultGeometry();
        if (isEncodeFeatureBounds() && notWritenBbox) {
            final BoundingBox bbox = currentFeature.getDefaultGeometryProperty().getBounds();
            if (bounds == null) {
                this.bounds = new ReferencedEnvelope(bbox);
            } else {
                bounds.expandToInclude((Envelope) bbox);
            }
        }
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
                g.writeNullField(name);
                continue;
            }
            Class<?> binding = p.getType().getBinding();
            g.writeFieldName(name);
            writeValue(g, value, binding);
        }
        g.writeEndObject();

        // Check CRS and Axis order before writing out to comply with
        // https://tools.ietf.org/html/rfc7946 unless they asked nicely
        if (defaultGeometry != null) {
            if (!encodeFeatureCollectionCRS) {
                defaultGeometry = reprojectGeometry(currentFeature);
            }
            if (isEncodeFeatureBounds()) {
                writeBbox(g, defaultGeometry);
            }

            g.writeFieldName("geometry");
            String gString = mapper.writeValueAsString(defaultGeometry);
            g.writeRawValue(gString);

        } else {
            g.writeFieldName("geometry");
            g.writeNull();
        }
        g.writeStringField("id", currentFeature.getID());
        g.writeEndObject();
        g.flush();
    }

    private void writeValue(JsonGenerator g, Object value, Class<?> binding) throws IOException {
        if (value == null) {
            g.writeNull();
            return;
        }

        if (binding == Integer.class) {
            g.writeNumber((int) value);
        } else if (binding == Double.class) {
            g.writeNumber((double) value);
        } else if (binding == Boolean.class) {
            g.writeBoolean((boolean) value);
        } else if (Date.class.isAssignableFrom(binding)) {
            g.writeString(this.dateFormatter.format(value));
        } else if (Object.class.isAssignableFrom(binding) && value instanceof JsonNode) {
            ((JsonNode) value)
                    .serialize(
                            g,
                            new DefaultSerializerProvider(
                                    mapper.getSerializerProvider(),
                                    mapper.getSerializationConfig(),
                                    mapper.getSerializerFactory()) {
                                @Override
                                public DefaultSerializerProvider createInstance(
                                        SerializationConfig config, SerializerFactory jsf) {
                                    throw new UnsupportedOperationException();
                                }

                                @Override
                                public DefaultSerializerProvider withCaches(CacheProvider cacheProvider) {
                                    throw new UnsupportedOperationException();
                                }
                            });
        } else if (binding.isArray()) {
            g.writeStartArray();
            int length = Array.getLength(value);
            for (int i = 0; i < length; i++) {
                writeValue(g, Array.get(value, i), binding.getComponentType());
            }
            g.writeEndArray();
        } else if (Collection.class.isAssignableFrom(binding)) {
            g.writeStartArray();
            for (Object v : (Collection) value) {
                writeValue(g, v, v == null ? null : v.getClass());
            }
            g.writeEndArray();
        } else {
            g.writeString(value.toString());
        }
    }

    private static GeometryFactory gf = new GeometryFactory();

    private Geometry reprojectGeometry(SimpleFeature currentFeature) {
        Geometry defaultGeometry = (Geometry) currentFeature.getDefaultGeometry();
        if (defaultGeometry == null) {
            LOGGER.fine("No geometry found in " + currentFeature.getID() + " skipping");
            GeometryCollection collection = gf.createGeometryCollection(null);
            return collection;
        }
        CoordinateReferenceSystem inCRS =
                currentFeature.getDefaultGeometryProperty().getDescriptor().getCoordinateReferenceSystem();
        if (transform == null || inCRS != lastCRS) {
            lastCRS = inCRS;
            try {
                if (inCRS == null) {
                    transform = IdentityTransform.create(2);
                } else {
                    transform = CRS.findMathTransform(inCRS, outCRS, true);
                }
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
        return defaultGeometry;
    }

    private void writeBbox(JsonGenerator g, Geometry defaultGeometry) throws IOException {
        g.writeFieldName("bbox");
        final Envelope envelope = defaultGeometry.getEnvelopeInternal();
        double[] coords = {envelope.getMinX(), envelope.getMinY(), envelope.getMaxX(), envelope.getMaxY()};
        formatter.setMaximumFractionDigits(maxDecimals);
        g.writeStartArray();
        for (double c : coords) {
            g.writeNumber(formatter.format(c));
        }
        g.writeEndArray();
    }

    @Override
    @SuppressWarnings("PMD.UseTryWithResources") // closing field
    public void close() throws IOException {
        try {
            if (inArray) {
                generator.writeEndArray();
                if (isEncodeFeatureBounds() && notWritenBbox) {
                    writeBoundingEnvelope();
                }
                generator.writeEndObject();
            }

            generator.close();
        } finally {
            out.close();
        }
    }

    /** @return the encodeFeatureCollectionBounds */
    public boolean isEncodeFeatureBounds() {
        return encodeFeatureBounds;
    }

    /** @param encodeFeatureCollectionBounds the encodeFeatureCollectionBounds to set */
    public void setEncodeFeatureBounds(boolean encodeFeatureCollectionBounds) {
        this.encodeFeatureBounds = encodeFeatureCollectionBounds;
    }

    /** Utility encoding a single JTS geometry in GeoJSON, and returning it as a string */
    public static String toGeoJSON(Geometry geometry) {
        return toGeoJSON(geometry, JtsModule.DEFAULT_MAX_DECIMALS);
    }

    /**
     * Utility encoding a single JTS geometry in GeoJSON with configurable max decimals, and returning it as a string
     */
    public static String toGeoJSON(Geometry geometry, int maxDecimals) {
        ObjectMapper lMapper = new ObjectMapper();
        lMapper.registerModule(new JtsModule(maxDecimals));
        try {
            return lMapper.writeValueAsString(geometry);
        } catch (JsonProcessingException e) {
            LOGGER.warning(e.getLocalizedMessage());
        }
        return "";
    }

    /** Utility encoding a single {@link SimpleFeature}, and returning it as a string */
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

    /** Utility encoding a @link {@link SimpleFeatureCollection}}, and returning it as a string */
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
            LOGGER.log(Level.FINE, "Unexpected IOException converting featureCollection to GeoJSON", e);
        }
        return new String(out.toByteArray());
    }

    /** Set to true to encode the feature collection CRS field. Defaults to false. */
    public void setEncodeFeatureCollectionCRS(boolean b) {
        encodeFeatureCollectionCRS = b;
    }

    /** @return true if the feature collections CRS will be encoded in the output */
    public boolean isEncodeFeatureCollectionCRS() {
        return encodeFeatureCollectionCRS;
    }

    /**
     * Returns the max number of decimals used for encoding
     *
     * @return
     */
    public int getMaxDecimals() {
        return maxDecimals;
    }

    /**
     * Set how many decimals should be used in writing out the coordinates. Users should consult https://xkcd.com/2170/
     * before changing this value, unless they are using a CRS different from 4326.
     *
     * @param number - the number of digits after the decimal place marker
     */
    public void setMaxDecimals(int number) {
        maxDecimals = number;
        module.setMaxDecimals(number);
    }

    /** Encodes the whole feature collection onto the output */
    public void writeFeatureCollection(SimpleFeatureCollection features) throws IOException {
        // the collection might be empty, but we still need the wrapper JSON
        // for a collection to be generated
        if (!initalised) {
            initialise();
        }
        try (SimpleFeatureIterator itr = features.features()) {
            while (itr.hasNext()) {
                SimpleFeature feature = itr.next();
                write(feature);
            }
        }
    }

    public void setBounds(ReferencedEnvelope bbox) {
        bounds = bbox;
    }

    /** Returns true if the generated JSON is for a single feature, without a feature collection wrapper around it. */
    public boolean isSingleFeature() {
        return singleFeature;
    }

    /**
     * Turns on and off the single feature mode. In single feature mode the feature collection wrapper elements won't be
     * emitted. Defaults to false.
     *
     * @param singleFeature
     */
    public void setSingleFeature(boolean singleFeature) {
        this.singleFeature = singleFeature;
    }

    /** Enables/disables pretty printing. */
    public void setPrettyPrinting(boolean prettyPrint) {
        if (prettyPrint) generator.setPrettyPrinter(new DefaultPrettyPrinter());
        else generator.setPrettyPrinter(null);
    }

    /** Returns true if pretty printing is enabled, false otherwise. */
    public boolean isPrettyPrinting() {
        return generator.getPrettyPrinter() != null;
    }

    /**
     * Sets the Timezone used to format the date fields. <code>null</code> is a valid value, the JVM local timezone will
     * be used in that case.
     */
    public void setTimeZone(TimeZone tz) {
        this.dateFormatter = FastDateFormat.getInstance(dateFormatter.getPattern(), tz);
    }

    /** Returns the timezone used to format dates. Defaults to GMT. */
    public TimeZone getTimeZone(TimeZone tz) {
        return dateFormatter.getTimeZone();
    }

    /**
     * Sets the date format for time encoding
     *
     * @param pattern {@link java.text.SimpleDateFormat} compatible * pattern
     */
    public void setDatePattern(String pattern) {
        this.dateFormatter = FastDateFormat.getInstance(pattern, dateFormatter.getTimeZone());
    }

    /** Returns the date formatter pattern. Defaults to DEFAULT_DATE_FORMAT */
    public String getDatePattern() {
        return this.dateFormatter.getPattern();
    }
}
