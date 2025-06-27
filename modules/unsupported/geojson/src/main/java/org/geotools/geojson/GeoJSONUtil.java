/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geojson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.apache.commons.lang3.time.FastDateFormat;
import org.geotools.geojson.geom.GeometryHandler;
import org.geotools.util.Converters;
import org.json.simple.JSONObject;
import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

public class GeoJSONUtil {

    /** Date format (ISO 8601) */
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public static final TimeZone TIME_ZONE = TimeZone.getTimeZone("GMT");

    public static final FastDateFormat dateFormatter = FastDateFormat.getInstance(DATE_FORMAT, TIME_ZONE);

    //
    // io
    //

    /**
     * Converts an object to a {@link Reader} instance.
     *
     * <p>The <tt>input</tt> parameter may be one of the following types:
     *
     * <ul>
     *   <li>{@link Reader}
     *   <li>{@link InputStream}
     *   <li>{@link File}
     *   <li>{@link String} *
     * </ul>
     *
     * * A string parameter is considered a file path.
     *
     * @param input The input object.
     * @return A reader.
     */
    public static Reader toReader(Object input) throws IOException {
        if (input instanceof BufferedReader) {
            return (BufferedReader) input;
        }

        if (input instanceof Reader) {
            return new BufferedReader((Reader) input);
        }

        if (input instanceof InputStream) {
            return new BufferedReader(new InputStreamReader((InputStream) input, StandardCharsets.UTF_8));
        }

        if (input instanceof File) {
            return new BufferedReader(new FileReader((File) input, StandardCharsets.UTF_8));
        }

        if (input instanceof String) {
            return new StringReader((String) input);
        }

        throw new IllegalArgumentException("Unable to turn " + input + " into a reader");
    }

    /**
     * Converts an object to {@link Writer} instance.
     *
     * <p>The <tt>output</tt> parameter may be one of the following types:
     *
     * <ul>
     *   <li>{@link Writer}
     *   <li>{@link OutputStream}
     *   <li>{@link File}
     *   <li>{@link String} *
     * </ul>
     *
     * * A string parameter is considered a file path.
     *
     * @param output The output object.
     * @return A writer.
     */
    public static Writer toWriter(Object output) throws IOException {
        // If the user passed in an OutputStreamWriter, we'll trust them to close it themselves.
        if (output instanceof OutputStreamWriter) {
            return new Writer() {
                Writer writer = new BufferedWriter((Writer) output);

                @Override
                public void write(char[] cbuf, int off, int len) throws IOException {
                    writer.write(cbuf, off, len);
                }

                @Override
                public void flush() throws IOException {
                    writer.flush();
                }

                @Override
                public void close() throws IOException {}
            };
        }

        if (output instanceof BufferedWriter) {
            return (BufferedWriter) output;
        }

        if (output instanceof Writer) {
            return new BufferedWriter((Writer) output);
        }

        if (output instanceof OutputStream) {
            return new BufferedWriter(new OutputStreamWriter((OutputStream) output, StandardCharsets.UTF_8));
        }

        if (output instanceof File) {
            return new BufferedWriter(new FileWriter((File) output, StandardCharsets.UTF_8));
        }

        if (output instanceof String) {
            return new BufferedWriter(new FileWriter((String) output, StandardCharsets.UTF_8));
        }

        throw new IllegalArgumentException("Unable to turn " + output + " into a writer");
    }

    //
    // encoding
    //
    public static StringBuilder string(String string, StringBuilder sb) {
        sb.append("\"").append(JSONObject.escape(string)).append("\"");
        return sb;
    }

    public static StringBuilder entry(String key, Object value, StringBuilder sb) {

        string(key, sb).append(":");

        value(value, sb);
        return sb;
    }

    private static void value(Object value, StringBuilder sb) {
        if (value == null) {
            nul(sb);
        } else if (value instanceof List) {
            sb.append('[');
            boolean firstValue = true;
            for (Object member : (List) value) {
                if (firstValue) {
                    firstValue = false;
                } else {
                    sb.append(',');
                }

                value(member, sb);
            }
            sb.append(']');
        } else if (value instanceof Map) {
            sb.append('{');
            boolean firstEntry = true;
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
                if (firstEntry) {
                    firstEntry = false;
                } else {
                    sb.append(',');
                }
                entry(entry.getKey().toString(), entry.getValue(), sb);
            }
            sb.append('}');
        } else if (value.getClass().isArray()) {
            array(value, sb);
        } else if (value instanceof Number || value instanceof Boolean || value instanceof Date) {
            literal(value, sb);
        } else {
            String str = Converters.convert(value, String.class);
            if (str == null) {
                str = value.toString();
            }
            string(str, sb);
        }
    }

    private static void array(Object array, StringBuilder sb) {
        sb.append("[");
        int length = Array.getLength(array);
        for (int i = 0; i < length; i++) {
            Object value = Array.get(array, i);
            value(value, sb);
            if (i < length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
    }

    static StringBuilder literal(Object value, StringBuilder sb) {
        // handle date as special case special case
        if (value instanceof Date) {
            return string(dateFormatter.format((Date) value), sb);
        }

        return sb.append(value);
    }

    public static StringBuilder array(String key, Object value, StringBuilder sb) {
        return string(key, sb).append(":").append(value);
    }

    public static StringBuilder nul(StringBuilder sb) {
        sb.append("null");
        return sb;
    }

    //
    // parsing
    //
    @SuppressWarnings("unchecked")
    public static <T> T trace(T handler, Class<T> clazz) {
        return (T) Proxy.newProxyInstance(
                handler.getClass().getClassLoader(), new Class[] {clazz}, new TracingHandler(handler));
    }

    public static boolean addOrdinate(List<Object> ordinates, Object value) {
        if (ordinates != null) {
            ordinates.add(value);
        }

        return true;
    }

    public static Coordinate createCoordinate(List ordinates) throws ParseException {
        Coordinate c = new Coordinate();
        if (ordinates.size() <= 1) {
            throw new ParseException(
                    ParseException.ERROR_UNEXPECTED_EXCEPTION, "Too few ordinates to create coordinate");
        }
        if (ordinates.size() > 1) {
            c.x = ((Number) ordinates.get(0)).doubleValue();
            c.y = ((Number) ordinates.get(1)).doubleValue();
        }
        if (ordinates.size() > 2) {
            c.setZ(((Number) ordinates.get(2)).doubleValue());
        }
        return c;
    }

    public static Coordinate[] createCoordinates(List<Coordinate> coordinates) {
        return coordinates.toArray(new Coordinate[coordinates.size()]);
    }

    @SuppressWarnings("unchecked")
    public static <T> T parse(IContentHandler<T> handler, Object input, boolean trace) throws IOException {
        try (Reader reader = toReader(input)) {
            if (trace) {
                handler = (IContentHandler<T>) Proxy.newProxyInstance(
                        handler.getClass().getClassLoader(),
                        new Class[] {IContentHandler.class},
                        new TracingHandler(handler));
            }

            JSONParser parser = new JSONParser();
            try {
                parser.parse(reader, handler);
                return handler.getValue();
            } catch (ParseException e) {
                throw (IOException) new IOException().initCause(e);
            }
        }
    }

    public static void encode(String json, Object output) throws IOException {
        try (Writer w = toWriter(output)) {
            w.write(json);
            w.flush();
        }
    }

    public static void encode(Map<String, Object> obj, Object output) throws IOException {
        try (Writer w = toWriter(output)) {
            JSONObject.writeJSONString(obj, w);
            w.flush();
        }
    }

    public static Object replaceGeometry(Object justAdded) throws ParseException, IOException {
        if (justAdded instanceof Map) {
            Map map = (Map) justAdded;
            if (map.size() == 2
                    && map.containsKey("type")
                    && (map.containsKey("coordinates") || map.containsKey("geometries"))) {
                return reparseMapToGeography(map);
            }
        }

        return justAdded;
    }

    private static Geometry reparseMapToGeography(Map map) throws ParseException, IOException {
        GeometryHandler geomHandler = new GeometryHandler(new GeometryFactory());

        replayMap(map, geomHandler);
        return geomHandler.getValue();
    }

    static void replayMap(Map map, ContentHandler handler) throws ParseException, IOException {
        handler.startObject();
        for (Object key : map.keySet()) {
            handler.startObjectEntry((String) key);
            replayObject(map.get(key), handler);
            handler.endObjectEntry();
        }
        handler.endObject();
    }

    private static void replayObject(Object object, ContentHandler handler) throws ParseException, IOException {
        if (object instanceof Map) {
            replayMap((Map) object, handler);
        } else if (object instanceof List) {
            replayList((List) object, handler);
        } else {
            handler.primitive(object);
        }
    }

    private static void replayList(List list, ContentHandler handler) throws ParseException, IOException {
        handler.startArray();
        for (Object o : list) {
            replayObject(o, handler);
        }
        handler.endArray();
    }
}
