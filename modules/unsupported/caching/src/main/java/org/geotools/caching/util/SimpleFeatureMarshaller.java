/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.caching.util;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;


/** Simple marshaller that can write features to an ObjectOutputStream.
 * Feature is not Serializable, but this is based on the idea that most attributes object are Serializable
 * (JTS geometries are Serializable), and that attributes which are not simple, are either a collection we can iterate through, or another Feature.
 * Serialization is then achieved recursively.
 * Unmarshalling implies to know the FeatureType of the marshalled feature.
 *
 * Storage format : Header,
 *                  Attributes
 *
 * Header := int     : FeatureType hashCode,
 *           String  : FeatureType name,
 *           String  : Feature ID,
 *           int     : number of attributes
 * Attributes := [Attribute]
 * Attribute  := int : multiplicity, or O if simple, or -1 if FeatureAttribute,
 *               Object|Feature|[Attribute] : attribute value
 *
 * This implementation does not have the ambition of being robust.
 *
 * @task test with other FeatureType than DefaultFeatureType
 * @task add method marshall(Feature, ByteArrayOutputStream) and unmarshall(ByteArrayOutputStream), or create sub class.
 *
 * @author Christophe Rousson, SoC 2007, CRG-ULAVAL
 *
 *
 * @source $URL$
 */
public class SimpleFeatureMarshaller {
    /**
     * marker to indicate an attribute is a feature in the serialized form
     */
    public static final int FEATURE = -1;
    public static final int SIMPLEATTRIBUTE = 0;
    
    private HashMap<String, SimpleFeatureType> types;
    private HashMap<String, SimpleFeatureBuilder> builders;

    /** Default constructor.
     */
    public SimpleFeatureMarshaller() {
        types = new HashMap<String, SimpleFeatureType>();
        builders = new HashMap<String, SimpleFeatureBuilder>();
    }

    /**
     * Registers a type with the feature marshaller
     * 
     * @param type
     */
    public void registerType(SimpleFeatureType type) {
        if (!types.containsKey(type.getName().getURI())){
            types.put(type.getName().getURI(), type);
        }
    }

    /**
     * Looks in the type cache for a particular feature  type
     * @param typeName
     * @return
     */
    private SimpleFeatureType typeLookUp(String typeName) {
        return types.get(typeName);
    }

    /** Marshall a feature into a stream.
     * The type of that feature is not marshalled,
     * type name is marshalled.
     *
     * @param f the Feature to marshall
     * @param s the stream to write to
     * @throws IOException
     */
    public void marshall(SimpleFeature f, ObjectOutput s)
        throws IOException {
        SimpleFeatureType type = (SimpleFeatureType) f.getType();
        registerType(type);
        s.writeObject(type.getName().getURI());
        s.writeObject(f.getID());
        
        int natt = f.getAttributes().size();
        s.writeInt(natt);
        
        for (Iterator<Object> it = f.getAttributes().iterator(); it.hasNext();) {
        	Object att = it.next();
        	marshallSimpleAttribute(att, s);
        }
    }

    /** Marshall an attribute into a stream.
     *
     * @task test object is instance of Serializable
     *
     * @param o an attribute value which is Serializable, or a feature, or a collection
     * @param s the stream to write to
     * @throws IOException
     */
    protected void marshallSimpleAttribute(Object o, ObjectOutput s)
        throws IOException {
        if (o instanceof Collection) {
            throw new IllegalArgumentException(
                "Got instance of SimpleFeature with complex attributes.");
        } else if (o instanceof SimpleFeature) {
            s.writeInt(FEATURE);
            marshall((SimpleFeature) o, s);
        } else {
            s.writeInt(SIMPLEATTRIBUTE);
            s.writeObject(o);
        }
    }

    /** Inverse operation of marshall : read a feature from a stream.
     *
     * @param s the stream to read from
     * @return the unmarshalled feature
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws IllegalAttributeException
     */
    public SimpleFeature unmarshall(ObjectInput s)
        throws IOException, ClassNotFoundException, IllegalAttributeException {
        String typeName = (String) s.readObject();
        SimpleFeatureType type = typeLookUp(typeName);

        if (type != null) {
            SimpleFeature f = unmarshall(s, type);

            if (f == null) {
                System.err.println("Returning null feature");
            }

            return f;
        } else {
            throw new IllegalStateException(typeName + " is not a registered type.");
        }
    }

    /** Inverse operation of marshall : read a feature from a stream.
     *
     * @param s the stream to read from
     * @param the type of the feature to unmarshall
     * @return the unmarshalled feature
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws IllegalAttributeException
     */
    protected SimpleFeature unmarshall(ObjectInput s, SimpleFeatureType type)
        throws IOException, ClassNotFoundException, IllegalAttributeException {
        String fid = (String) s.readObject();
        int natt = s.readInt();

        SimpleFeatureBuilder builder = lookupBuilder(type);

        if (!(natt == type.getAttributeCount())) {
            throw new IOException("Schema error");
        }

        for (int i = 0; i < natt; i++) {
            builder.add(unmarshallSimpleAttribute(s));
        }
        //return builder.feature(fid);
        return builder.buildFeature(fid);
    }
    
    /*
     * Looks up a feature builder from the builder cache; if not found then
     * it will create a new one and add it to the cache.
     */
    private SimpleFeatureBuilder lookupBuilder(SimpleFeatureType type){
        SimpleFeatureBuilder builder = builders.get(type.getName().getURI());
        if (builder == null){
            builder = new SimpleFeatureBuilder(type);
            builders.put(type.getName().getURI(), builder);
        }
        return builder;
    }

    /** Read attribute values from a stream.
     *
     * @param s the stream to read from
     * @return a list of attribute values, possibly a singleton, if attribute's multiplicity is 1
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws IllegalAttributeException
     */
    protected Object unmarshallSimpleAttribute(ObjectInput s)
        throws IOException, ClassNotFoundException, IllegalAttributeException {
        int m = s.readInt();
        Object att = null;

        if (m == SIMPLEATTRIBUTE) {
            att = s.readObject();
        } else if (m == FEATURE) {
            SimpleFeature f = unmarshall(s);
            att = f;
        } else { // this should never happen
            throw new IllegalAttributeException(null, null, "Found complex attribute which is not legal for SimpleFeature.");
        }

        return att;
    }
}
