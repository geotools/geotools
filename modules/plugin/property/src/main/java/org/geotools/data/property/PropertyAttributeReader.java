package org.geotools.data.property;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;

import org.geotools.data.AttributeReader;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.util.Converters;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Simple AttributeReader that works against Java properties files.
 * 
 * <p>
 * This AttributeReader is part of the GeoTools AbstractDataStore tutorial, and should be considered
 * a Toy.
 * </p>
 * 
 * <p>
 * The content of this file should start with a the property "_" with the value being the typeSpec
 * describing the featureType. Thereafter each line will should have a FeatureID as the property and
 * the attribtues as the value separated by | characters.
 * </p>
 * 
 * <pre>
 * <code>
 * _=id:Integer|name:String|geom:Geometry
 * fid1=1|Jody|<i>well known text</i>
 * fid2=2|Brent|<i>well known text</i>
 * fid3=3|Dave|<i>well known text</i>
 * </code>
 * </pre>
 * 
 * <p>
 * May values may be represented by a special tag: <code><null></code>. An empty element:
 * <code>||</code> is interpreted as the empty string:
 * </p>
 * 
 * <pre>
 *  <code>
 *  fid4=4||<null> -> Feature( id=2, name="", geom=null )
 *  </code>
 * </pre>
 * 
 * @author Jody Garnett
 */
public class PropertyAttributeReader implements AttributeReader {
    BufferedReader reader;

    SimpleFeatureType type;

    String line;

    String next;

    String[] text;

    String fid;

    /**
     * Creates a new PropertyAttributeReader object.
     * 
     * @param file File being read
     * 
     * @throws IOException
     * @throws DataSourceException
     */
    public PropertyAttributeReader(File file) throws IOException {
        String typeName = typeName(file);
        String namespace = namespace(file);
        reader = new BufferedReader(new FileReader(file));

        // read until "_=";
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("_="))
                break;
        }

        if ((line == null) || !line.startsWith("_=")) {
            throw new IOException(typeName + " schema not available");
        }

        String typeSpec = line.substring(2);

        try {
            type = DataUtilities.createType(namespace, typeName, typeSpec);
        } catch (SchemaException e) {
            throw new DataSourceException(typeName + " schema not available", e);
        }

        line = null;
        next = null;
    }

    /**
     * TypeName for the provided file.
     * 
     * @param file File being read
     * @return suitable typeName
     */
    private static String typeName(File file) {
        String name = file.getName();
        int split = name.lastIndexOf('.');

        return (split == -1) ? name : name.substring(0, split);
    }

    /**
     * Namespace for the provided file
     * 
     * @param file File being read
     * 
     * @return suitable namespace
     */
    private static String namespace(File file) {
        File parent = file.getParentFile();

        return (parent == null) ? "" : (parent.getName() + ".");
    }

    /**
     * Number of attributes to expect based on header information.
     * 
     * @return number of attribtues
     */
    public int getAttributeCount() {
        return type.getAttributeCount();
    }

    /**
     * AttribtueDescriptor (name and type) for position marked by index.
     * 
     * @param index
     * @return AttributeDescriptor describing attribute name and type
     * 
     * @throws ArrayIndexOutOfBoundsException
     */
    public AttributeDescriptor getAttributeType(int index) throws ArrayIndexOutOfBoundsException {
        return type.getDescriptor(index);
    }

    /**
     * Close the internal reader accessing the file.
     * 
     * @throws IOException
     */
    public void close() throws IOException {
        reader.close();
        reader = null;
    }

    /**
     * Check if the file has another line.
     * 
     * @return <code>true</code> if the file has another line
     * @throws IOException
     */
    public boolean hasNext() throws IOException {
        if (next != null) {
            return true;
        }
        next = readLine();
        return next != null;
    }
    String readLine() throws IOException {
        StringBuilder buffer = new StringBuilder();
        while( true ){
            String txt = reader.readLine();
            if( txt == null ){
                break;
            }
            if( txt.startsWith("#")){
                continue; // skip content
            }
            if( txt.endsWith("\\")){
                buffer.append(txt.substring(0,txt.length()-1) );
                buffer.append("\n");
                continue;
            }
            else {
                txt = txt.trim();
                buffer.append(txt);
                break;
            }
        }
        if( buffer.length() == 0 ){
            return null; // there is no line
        }
        String raw = buffer.toString();
        raw = raw.replace("\\n", "\n" );
        raw = raw.replace("\\r", "\r" );
        raw = raw.replace("\\t", "\t" );
        return raw;
    }
    /**
     * Retrieve the next line.
     * 
     * @throws IOException
     * @throws NoSuchElementException
     */
    public void next() throws IOException {
        if (hasNext()) {
            line = next;
            next = null;

            int split = line.indexOf('=');
            fid = line.substring(0, split);
            text = line.substring(split + 1).split("\\|");
            if (type.getAttributeCount() != text.length)
                throw new DataSourceException("format error: expected " + type.getAttributeCount()
                        + " attributes, but found " + text.length + ". [" + line + "]");
        } else {
            throw new NoSuchElementException();
        }
    }

    /**
     * Retrieve the FeatureId identifying the current line.
     * 
     * @return FeatureID for the current line.
     */
    public String getFeatureID() {
        if (line == null) {
            return null;
        }

        return fid;
    }

    /**
     * Read attribute in position marked by <code>index</code>.
     * 
     * @param index Attribute position to read
     * 
     * @return Value for the attribtue in position <code>index</code>
     * 
     * @throws IOException
     * @throws ArrayIndexOutOfBoundsException
     */
    public Object read(int index) throws IOException, ArrayIndexOutOfBoundsException {
        if (line == null) {
            throw new IOException("No content available - did you remeber to call next?");
        }

        AttributeDescriptor attType = type.getDescriptor(index);

        String stringValue = null;
        try {
            // read the value
            stringValue = text[index];

            // trim off any whitespace
            if (stringValue != null) {
                stringValue = stringValue.trim();
            }
            if ("".equals(stringValue)) {
                stringValue = null;
            }
        } catch (RuntimeException e1) {
            e1.printStackTrace();
            stringValue = null;
        }

        // check for special <null> flag
        if ("<null>".equals(stringValue)) {
            stringValue = null;
        }
        if (stringValue == null) {
            if (attType.isNillable()) {
                return null;
            }
        }
        // Use of Converters to convert from String to requested java binding
        Object value = Converters.convert(stringValue, attType.getType().getBinding());

        if (attType.getType() instanceof GeometryType) {
            // this is to be passed on in the geometry objects so the srs name gets encoded
            CoordinateReferenceSystem crs = ((GeometryType) attType.getType())
                    .getCoordinateReferenceSystem();
            if (crs != null) {
                // must be geometry, but check anyway
                if (value != null && value instanceof Geometry) {
                    ((Geometry) value).setUserData(crs);
                }
            }
        }
        return value;
    }
}