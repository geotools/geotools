/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
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
package org.geotools.ysld;

import org.geotools.ysld.encode.YsldEncoder;
import org.geotools.ysld.parse.YsldParser;
import org.geotools.ysld.parse.ZoomContextFinder;
import org.geotools.ysld.transform.sld.SldTransformer;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.ResourceLocator;
import org.geotools.styling.SLDParser;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.ysld.validate.YsldValidator;
import org.yaml.snakeyaml.error.MarkedYAMLException;

import javax.annotation.Nullable;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import java.io.*;
import java.util.Collections;
import java.util.List;

/**
 * Parses, encodes, and transforms Ysld.
 */
public class Ysld {

    public static String OPTION_PREFIX = "x-";

    /**
     * Creates a {@link java.io.Reader} from an input object.
     * <p>
     * Handles objects of the following type.
     * <ul>
     * <li>{@link java.io.Reader}</li>
     * <li>{@link java.io.InputStream}</li>
     * <li>{@link java.io.File}</li>
     * <li>{@link java.lang.String}</li>
     * </ul>
     * </p>
     * 
     * @param input The input object.
     *
     * @throws java.lang.IllegalArgumentException If the input object can not be converted.
     */
    static YsldInput reader(Object input) throws IOException {
        if (input instanceof Reader) {
            return new YsldInput((Reader) input);
        } else if (input instanceof InputStream) {
            return new YsldInput(new BufferedReader(new InputStreamReader((InputStream) input)));
        } else if (input instanceof File) {
            return new YsldInput(new BufferedReader(new FileReader((File) input)));
        } else if (input instanceof String) {
            return new YsldInput(new StringReader((String) input));
        } else {
            throw new IllegalArgumentException("Unable to turn " + input + " into reader");
        }
    }

    /**
     * Creates a {@link java.io.Writer} from an output object.
     * <p>
     * Handles objects of the following type.
     * <ul>
     * <li>{@link java.io.Reader}</li>
     * <li>{@link java.io.InputStream}</li>
     * <li>{@link java.io.File}</li>
     * </ul>
     * </p>
     * 
     * @param output The output object.
     *
     * @throws java.lang.IllegalArgumentException If the output object can not be converted.
     */
    static Writer writer(Object output) throws IOException {
        if (output instanceof Writer) {
            return (Writer) output;
        } else if (output instanceof OutputStream) {
            return new BufferedWriter(new OutputStreamWriter((OutputStream) output));
        } else if (output instanceof File) {
            return new BufferedWriter(new FileWriter((File) output));
        } else {
            throw new IllegalArgumentException("Unable to turn " + output + " into writer");
        }
    }

    /**
     * Creates an XML reader from an input object.
     *
     * @param input THe input object, see {@link #reader(Object)} for details.
     */
    public static XMLStreamReader xmlReader(Object input) throws IOException {
        YsldInput in = reader(input);
        try {
            XMLInputFactory xmlFactory = XMLInputFactory.newFactory();
            try {
                return xmlFactory.createXMLStreamReader(in.reader);
            } catch (XMLStreamException e) {
                throw new IOException(e);
            }
        } finally {
            in.close();
        }
    }

    /**
     * Parses a Ysld stream into GeoTools style objects.
     *
     * @param ysld The Ysld content, anything accepted by {@link #reader(Object)}.
     * @param locator Resource locator for resolving relative URIs.
     *
     * @return The GeoTools SLD object.
     */
    public static StyledLayerDescriptor parse(Object ysld,
            @Nullable List<ZoomContextFinder> zCtxtFinders, @Nullable ResourceLocator locator)
            throws IOException {
        return parse(ysld, zCtxtFinders, locator, new UomMapper());
    }

    /**
     * Parses a Ysld stream into GeoTools style objects.
     *
     * @param ysld The Ysld content, anything accepted by {@link #reader(Object)}.
     * @param locator Resource locator for resolving relative URIs.
     *
     * @return The GeoTools SLD object.
     */
    public static StyledLayerDescriptor parse(Object ysld,
            @Nullable List<ZoomContextFinder> zCtxtFinders, @Nullable ResourceLocator locator,
            @Nullable UomMapper uomMapper) throws IOException {
        YsldInput in = reader(ysld);
        try {
            YsldParser parser = new YsldParser(in.reader);
            if (zCtxtFinders != null) {
                parser.setZoomContextFinders(zCtxtFinders);
            }
            if (locator != null) {
                parser.setResourceLocator(locator);
            }
            if (uomMapper != null) {
                parser.setUomMapper(uomMapper);
            }
            return parser.parse();
        } finally {
            in.close();
        }
    }

    /**
     * Parses a Ysld stream into GeoTools style objects.
     *
     * @param ysld The Ysld content, anything accepted by {@link #reader(Object)}.
     *
     * @return The GeoTools SLD object.
     */
    public static StyledLayerDescriptor parse(Object ysld) throws IOException {
        return parse(ysld, (List<ZoomContextFinder>) null, (ResourceLocator) null, new UomMapper());
    }

    /**
     * Encodes a GeoTools style object as Ysld.
     *
     * @param sld The sld to encode.
     * @param output The output object, anything accepted by {@link #writer(Object)}
     */
    public static void encode(StyledLayerDescriptor sld, Object output) throws IOException {
        encode(sld, output, new UomMapper());
    }

    /**
     * Encodes a GeoTools style object as Ysld.
     *
     * @param sld The sld to encode.
     * @param output The output object, anything accepted by {@link #writer(Object)}
     */
    public static void encode(StyledLayerDescriptor sld, Object output, UomMapper uomMapper)
            throws IOException {
        YsldEncoder e = new YsldEncoder(writer(output), uomMapper);
        e.encode(sld);
    }

    /**
     * Transforms an SLD stream to Ysld.
     *
     * @param sld SLD xml reader.
     * @param ysld Ysld writer.
     *
     */
    public static void transform(XMLStreamReader sld, Writer ysld) throws IOException {
        throw new UnsupportedOperationException();/*
                                                   * SldTransformer tx = new SldTransformer(sld, ysld); try { tx.transform(); }
                                                   * catch(XMLStreamException e) { throw new IOException(e); }
                                                   */
    }

    /**
     * Transforms an SLD stream to Ysld.
     *
     * @param sld SLD xml reader.
     * @param ysld Ysld writer.
     *
     */
    public static void transform(InputStream sld, Writer ysld) throws IOException {
        SLDParser parser = new SLDParser(CommonFactoryFinder.getStyleFactory(), sld);
        StyledLayerDescriptor style = parser.parseSLD();
        Ysld.encode(style, ysld);
    }

    /**
     * Validates a Ysld stream.
     *
     * @param ysld The Ysld.
     *
     * @return List of marked exceptions corresponding to validation errors.
     */
    public static List<MarkedYAMLException> validate(Object ysld) throws IOException {

        return validate(ysld, Collections.<ZoomContextFinder> emptyList(), new UomMapper());

    }

    /**
     * Validates a Ysld stream.
     *
     * @param ysld The Ysld.
     * @param zContextFinders additional zoom context finders in order of priority.
     * 
     * @return List of marked exceptions corresponding to validation errors.
     */
    public static List<MarkedYAMLException> validate(Object ysld,
            List<ZoomContextFinder> zContextFinders, UomMapper uomMapper) throws IOException {
        YsldInput in = reader(ysld);
        try {
            YsldValidator validator = new YsldValidator();
            validator.setZCtxtFinders(zContextFinders);
            return validator.validate(in.reader);
        } finally {
            in.close();
            ;
        }

    }

    static class YsldInput {
        Reader reader;

        YsldInput(Reader reader) {
            this.reader = reader;
        }

        public void close() throws IOException {
            reader.close();
        }
    }
}
