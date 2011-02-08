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
import java.lang.reflect.Proxy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.geotools.util.Converters;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.vividsolutions.jts.geom.Coordinate;

public class GeoJSONUtil {

    /**
     * Date format (ISO 8601)
     */
    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    static {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    //
    // io
    //
    
    /**
     * Converts an object to a {@link Reader} instance.
     * <p>
     * The <tt>input</tt> parameter may be one of the following types:
     * <ul>
     *   <li>{@link Reader}
     *   <li>{@link InputStream}
     *   <li>{@link File}
     *   <li>{@link String} *
     * </ul>
     * * A string parameter is considered a file path.
     * </p>
     * 
     * @param input The input object.
     * 
     * @return A reader.
     * @throws IOException
     */
    public static Reader toReader(Object input) throws IOException {
        if (input instanceof BufferedReader) {
            return (BufferedReader) input;
        }
        
        if (input instanceof Reader) {
            return new BufferedReader((Reader)input);
        }
        
        if (input instanceof InputStream) {
            return new BufferedReader(new InputStreamReader((InputStream)input));
        }
        
        if (input instanceof File) {
            return new BufferedReader(new FileReader((File)input));
        }
        
        if (input instanceof String) {
            return new StringReader((String)input);
        }
        
        throw new IllegalArgumentException("Unable to turn " + input + " into a reader");
    }
    
    /**
     * Converts an object to {@link Writer} instance.
     * <p>
     * The <tt>output</tt> parameter may be one of the following types:
     * <ul>
     *   <li>{@link Writer}
     *   <li>{@link OutputStream}
     *   <li>{@link File}
     *   <li>{@link String} *
     * </ul>
     * * A string parameter is considered a file path.
     * </p>
     * @param output The output object.
     * 
     * @return A writer.
     * @throws IOException
     */
    public static Writer toWriter(Object output) throws IOException {
        if (output instanceof BufferedWriter) {
            return (BufferedWriter) output;
        }
        
        if (output instanceof Writer) {
            return new BufferedWriter((Writer)output);
        }
        
        if (output instanceof OutputStream) {
            return new BufferedWriter(new OutputStreamWriter((OutputStream)output));
        }
        
        if (output instanceof File) {
            return new BufferedWriter(new FileWriter((File)output));
        }
        
        if (output instanceof String) {
            return new BufferedWriter(new FileWriter((String)output));
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
        
        if (value instanceof Number || value instanceof Boolean || value instanceof Date) {
            literal(value, sb);
        } else {
            String str = Converters.convert(value, String.class);
            if(str == null) {
                str = value.toString();
            }
            string(str, sb);
        }
        return sb;
        
    }
    
    static StringBuilder literal(Object value, StringBuilder sb) {
        //handle date as special case special case
        if (value instanceof Date) {
            return string(DATE_FORMAT.format((Date)value), sb);
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
    public static <T> T trace(T handler, Class<T> clazz) {
        return (T) Proxy.newProxyInstance(handler.getClass().getClassLoader(), new Class[]{clazz}, 
                new TracingHandler(handler));
    }
    
    public static boolean addOrdinate(List ordinates, Object value) {
        if (ordinates != null) {
            ordinates.add(value);
        }
        
        return true;
    }
    
    public static Coordinate createCoordinate(List ordinates) {
        Coordinate c = new Coordinate();
        if (ordinates.size() > 0) {
            c.x = ((Number)ordinates.get(0)).doubleValue();
        }
        if (ordinates.size() > 1) {
            c.y = ((Number)ordinates.get(1)).doubleValue();
        }
        if (ordinates.size() > 2) {
            c.z = ((Number)ordinates.get(2)).doubleValue();
        }
        return c;
    }
    
    public static Coordinate[] createCoordinates(List coordinates) {
        return (Coordinate[]) coordinates.toArray(new Coordinate[coordinates.size()]);
    }
    
    public static <T> T parse(IContentHandler<T> handler, Object input, boolean trace) throws IOException {
        Reader reader = toReader(input);
        if (trace) {
            handler = (IContentHandler<T>) Proxy.newProxyInstance( handler.getClass().getClassLoader(), 
                new Class[]{ IContentHandler.class}, new TracingHandler(handler));
        }
        
        JSONParser parser = new JSONParser();
        try {
            parser.parse(reader, handler);
            return handler.getValue();
        } 
        catch (ParseException e) {
            throw (IOException) new IOException().initCause(e);
        }
    }
    
    public static void encode(String json, Object output) throws IOException {
        Writer w = toWriter(output);
        w.write(json);
        w.flush();
    }
    
    public static void encode(Map<String,Object> obj, Object output) throws IOException {
        Writer w = toWriter(output);
        JSONObject.writeJSONString(obj, w);
        w.flush();
    }
}
