/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.property;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.util.Converters;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Read a property file directly.
 * <p>
 * This implementation does not perform any filtering or processing; it leaves that up to wrappers to manipulate the content into the format or
 * projection requested by the user.
 * <p>
 * 
 * <p>
 * The content of this file should start with a the property "_" with the value being the typeSpec describing the featureType. Thereafter each line
 * will should have a FeatureID as the property and the attribtues as the value separated by | characters.
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
 * Many values may be represented by a special tag: <code><null></code>. An empty element: <code>||</code> is interpreted as the empty string:
 * </p>
 * 
 * <pre>
 *  <code>
 *  fid4=4||<null> -> Feature( id=2, name="", geom=null )
 *  </code>
 * </pre>
 * 
 * @author Jody Garnett (LISAsoft)
 * 
 * @source $URL$
 * @version $Id
 * @since 8.0
 */
public class PropertyFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {
    private static final Logger LOGGER = Logging.getLogger("org.geotools.data.property");

    BufferedReader reader;

    SimpleFeatureType type;

    String line;

    String next;

    String[] text;

    String fid;
    
    public PropertyFeatureReader(String namespace, File file) throws IOException {
        reader = new BufferedReader(new FileReader(file));

        // read until "_=";
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("_="))
                break;
        }

        if ((line == null) || !line.startsWith("_=")) {
            throw new IOException("Property file schema not available found");
        }
        String typeSpec = line.substring(2);
        String name = file.getName();
        String typeName = name.substring(0,name.lastIndexOf('.'));
        try {
            type = DataUtilities.createType(namespace, typeName, typeSpec);
        } catch (SchemaException e) {
            throw new DataSourceException(typeName + " schema not available", e);
        }
        line = null;
        next = null;
    }

    public SimpleFeatureType getFeatureType() {
        return type;
    }

    /**
     * Grab the next feature from the property file.
     * 
     * @return feature
     * 
     * @throws IOException
     * @throws NoSuchElementException Check hasNext() to avoid reading off the end of the file
     */
    public SimpleFeature next() throws IOException, NoSuchElementException {
        if (hasNext()) {
            line = next;
            next = null;

            int split = line.indexOf('=');
            fid = line.substring(0, split);
            text = line.substring(split + 1).split("\\|", -1);// use -1 as limit to include empty trailing spaces
            if (type.getAttributeCount() != text.length)
                throw new DataSourceException("Format error: expected " + type.getAttributeCount()
                        + " attributes, but found " + text.length + ". [" + line + "]");
        } else {
            throw new NoSuchElementException();
        }
        Object[] values = new Object[type.getAttributeCount()];

        for (int i = 0; i < type.getAttributeCount(); i++) {
            try {
                values[i] = read(i);
            } catch (RuntimeException e) {
                values[i] = null;
            } catch (IOException e) {
                throw e;
            }
        }
        return SimpleFeatureBuilder.build(type, values, fid);
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

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws IOException DOCUMENT ME!
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
        while (true) {
            String txt = reader.readLine();
            if (txt == null) {
                break;
            }
            if (txt.startsWith("#") || txt.startsWith("!")) {
                continue; // skip content
            }
            txt = trimLeft(txt);
            if (txt.endsWith("\\")) {
                buffer.append(txt.substring(0, txt.length() - 1));
                buffer.append("\n");
                continue;
            } else {
                buffer.append(txt);
                break;
            }
        }
        if (buffer.length() == 0) {
            return null; // there is no line
        }
        String raw = buffer.toString();
        raw = raw.replace("\\n", "\n");
        raw = raw.replace("\\r", "\r");
        raw = raw.replace("\\t", "\t");
        return raw;
    }

    /**
     * Trim leading white space as described Properties.
     * 
     * @see Properties#load(java.io.Reader)
     * @param txt
     * @return txt leading whitespace removed
     */
    String trimLeft(String txt) {
        // trim
        int start = 0;
        WHITESPACE: for (int i = 0; i < txt.length(); i++) {
            char ch = txt.charAt(i);
            if (Character.isWhitespace(ch)) {
                continue;
            } else {
                start = i;
                break WHITESPACE;
            }
        }
        return txt.substring(start);
    }

    /**
     * Be sure to call close when you are finished with this reader; as it must close the file it has open.
     * 
     * @throws IOException
     */
    public void close() throws IOException {
        if (reader == null) {
            LOGGER.warning("Stream seems to be already closed.");
        } else {
            reader.close();
        }
        reader = null;
    }
}
