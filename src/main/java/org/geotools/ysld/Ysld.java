package org.geotools.ysld;

import org.geotools.ysld.encode.YsldEncoder;
import org.geotools.ysld.parse.YsldParser;
import org.geotools.ysld.transform.sld.SldTransformer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.ysld.validate.YsldValidator;
import org.yaml.snakeyaml.error.MarkedYAMLException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import java.io.*;
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
     *     <li>{@link java.io.Reader}</li>
     *     <li>{@link java.io.InputStream}</li>
     *     <li>{@link java.io.File}</li>
     *     <li>{@link java.lang.String}</li>
     * </ul>
     * </p>
     * @param input The input object.
     *
     * @throws java.lang.IllegalArgumentException If the input object can not be converted.
     */
    static YsldInput reader(Object input) throws IOException {
        if (input instanceof Reader) {
            return new YsldInput((Reader)input);
        }
        else if (input instanceof InputStream) {
            return new YsldInput(new BufferedReader(new InputStreamReader((InputStream)input)));
        }
        else if (input instanceof File) {
            return new YsldInput(new BufferedReader(new FileReader((File)input))) {
                @Override
                public void close() throws IOException {
                    reader.close();
                }
            };
        }
        else if (input instanceof String) {
            return new YsldInput(new StringReader((String)input));
        }
        else {
            throw new IllegalArgumentException("Unable to turn " + input + " into reader");
        }
    }

    /**
     * Creates a {@link java.io.Writer} from an output object.
     * <p>
     * Handles objects of the following type.
     * <ul>
     *     <li>{@link java.io.Reader}</li>
     *     <li>{@link java.io.InputStream}</li>
     *     <li>{@link java.io.File}</li>
     * </ul>
     * </p>
     * @param output The output object.
     *
     * @throws java.lang.IllegalArgumentException If the output object can not be converted.
     */
    static Writer writer(Object output) throws IOException {
        if (output instanceof Writer) {
            return (Writer) output;
        }
        else if (output instanceof OutputStream) {
            return new BufferedWriter(new OutputStreamWriter((OutputStream)output));
        }
        else if (output instanceof File) {
            return new BufferedWriter(new FileWriter((File)output));
        }
        else {
            throw new IllegalArgumentException("Unable to turn " + output + " into writer");
        }
    }

    /**
     * Creates an XML reader from an input object.
     *
     * @param input THe input object, see {@link #reader(Object)} for details.
     */
    public static XMLStreamReader xmlReader(Object input) throws IOException{
        YsldInput in = reader(input);
        try {
            XMLInputFactory xmlFactory = XMLInputFactory.newFactory();
            try {
                return xmlFactory.createXMLStreamReader(in.reader);
            } catch (XMLStreamException e) {
                throw new IOException(e);
            }
        }
        finally {
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
        YsldInput in = reader(ysld);
        try {
            return new YsldParser(in.reader).parse();
        }
        finally {
            in.close();
        }
    }

    /**
     * Encodes a GeoTools style object as Ysld.
     *
     * @param sld The sld to encode.
     * @param output The output object, anything accepted by {@link #writer(Object)}
     */
    public static void encode(StyledLayerDescriptor sld, Object output) throws IOException {
        YsldEncoder e = new YsldEncoder(writer(output));
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
        SldTransformer tx = new SldTransformer(sld, ysld);
        try {
            tx.transform();
        }
        catch(XMLStreamException e) {
            throw new IOException(e);
        }
    }

    /**
     * Validates a Ysld stream.
     *
     * @param ysld The Ysld.
     *
     * @return List of marked exceptions corresponding to validation errors.
     */
    public static List<MarkedYAMLException> validate(Object ysld) throws IOException {
        YsldInput in = reader(ysld);
        try {
            return new YsldValidator().validate(in.reader);
        }
        finally {
            in.close();;
        }

    }

    static class YsldInput {
        Reader reader;

        YsldInput(Reader reader) {
            this.reader = reader;
        }

        public void close() throws IOException {
        }
    }
}
