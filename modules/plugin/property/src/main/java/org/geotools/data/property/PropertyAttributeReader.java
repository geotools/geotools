package org.geotools.data.property;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 *  fid4=4||<null>
 * </pre>
 * <p>
 * You can use \ to escape a | character, you can also use it to protect newlines::
 * <pre>
 * fid4=4|I have a \\|splitting\\| headache|POINT(0,0)
 * fid5=5|Example of \nmulti-lin text|POINT(1,1)
 * fid6=6|Second \\
 * example of multi-line text|POINT(2,2)
 * </pre>
 * 
 * @author Jody Garnett
 *
 * @source $URL$
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
    // multiline start
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
            // skip comments
            if( txt.startsWith("#") || txt.startsWith("!")){
                continue;
            }
            // ignore leading white space
            txt = trimLeft( txt );
            
            // handle escaped line feeds used to span multiple lines
            if( txt.endsWith("\\")){
                buffer.append(txt.substring(0,txt.length()-1) );
                buffer.append("\n");
                continue;
            }
            else {
                buffer.append(txt);
                break;
            }
        }
        if( buffer.length() == 0 ){
            return null; // there is no line
        }
        String raw = buffer.toString();
//        String line = decodeString(raw);
//        return line;
        return raw;
    }
    /**
     * Used to decode common whitespace chracters and escaped | characters.
     * 
     * @param txt Origional raw text as stored
     * @return decoded text as provided for storage
     * @see PropertyAttributeWriter#encodeString(String)
     */
    String decodeString( String txt ){
        // unpack whitespace constants
        txt = txt.replace( "\\n", "\n");
        txt = txt.replaceAll("\\r", "\r" );

        // unpack our our escaped characters
        txt = txt.replace("\\|", "|" );
        // txt = txt.replace("\\\\", "\\" );
        
        return txt;
    }
    /**
     * Trim leading white space as described Properties file standard.
     * @see Properties#load(java.io.Reader)
     * @param txt
     * @return txt leading whitespace removed
     */
    String trimLeft( String txt ){
        // trim
        int start = 0;
        WHITESPACE: for( int i=0; i < txt.length(); i++ ){
            char ch = txt.charAt(i);
            if( Character.isWhitespace(ch)){
                continue;
            }
            else {
                start = i;
                break WHITESPACE;
            }
        }
        return txt.substring(start);        
    }
    // multiline end
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
            String data = line.substring(split+1);
            
            text = splitIntoText(data);
        } else {
            throw new NoSuchElementException();
        }
    }
    /**
     * Split the provided text using | charater as a seperator.
     * <p>
     * This method respects the used of \ to "escape" a | character allowing
     * representations such as the following:<pre>
     * String example="text|example of escaped \\| character|text";
     * 
     * // represents: "text|example of escaped \| character|text"
     * String split=splitIntoText( example );</pre>
     * 
     * @param data Origional raw text as stored
     * @return data split using | as seperator
     * @throws DataSourceException if the information stored is inconsistent with the headered provided
     */
    private String[] splitIntoText(String data) throws DataSourceException {
        // return data.split("|", -1); // use -1 as a limit to include empty trailing spaces
        // return data.split("[.-^\\\\]\\|",-1); //use -1 as limit to include empty trailing spaces

        String split[] = new String[type.getAttributeCount()];
        int i = 0;
        StringBuilder item = new StringBuilder();
        for (String str : data.split("\\|",-1)) {
            if (i == type.getAttributeCount()) {
                // limit reached
                throw new DataSourceException("format error: expected " + text.length
                        + " attributes, stopped after finding " + i + ". [" + line
                        + "] split into " + Arrays.asList(text));
            }
            if (str.endsWith("\\")) {
//                String shorter = str.substring(0, str.length() - 1);
//                item.append(shorter);
                item.append(str);
                item.append("|");
            } else {
                item.append(str);
                split[i] = item.toString();

                i++;
                item = new StringBuilder();
            }
        }
        if (i < type.getAttributeCount()) {
            throw new DataSourceException("format error: expected " + type.getAttributeCount()
                    + " attributes, but only found " + i + ". [" + line + "] split into "
                    + Arrays.asList(text));
        }
        return split;
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
        //boolean isEmpty = "".equals(stringValue);
        try {
            // read the value and decode any interesting characters
            stringValue = decodeString( text[index] );
        } catch (RuntimeException huh) {
            huh.printStackTrace();
            stringValue = null;
        }
        // check for special <null> flag
        if ("<null>".equals(stringValue)) {
            stringValue = null;
        //    isEmpty = true;
        }
        if (stringValue == null) {
            if (attType.isNillable()) {
                return null; // it was an explicit "<null>"
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
