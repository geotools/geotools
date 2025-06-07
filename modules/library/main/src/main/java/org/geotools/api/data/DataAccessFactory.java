/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.api.data;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.util.InternationalString;
import org.geotools.util.KVP;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.factory.Factory;

/**
 * Constructs a live DataAccess from a set of connection parameters.
 *
 * <p>The following example shows how a user might connect to a PostGIS database, and maintain the resulting datastore
 * in a Registry:
 *
 * <p>
 *
 * <pre><code>
 * HashMap params = new HashMap();
 * params.put("namespace", "leeds");
 * params.put("dbtype", "postgis");
 * params.put("host","feathers.leeds.ac.uk");
 * params.put("port", "5432");
 * params.put("database","postgis_test");
 * params.put("user","postgis_ro");
 * params.put("passwd","postgis_ro");
 *
 * DefaultRegistry registry = new DefaultRegistry();
 * registry.addDataStore("leeds", params);
 *
 * DataStore postgis = registry.getDataStore( "leeds" );
 * SimpleFeatureSource = postgis.getFeatureSource( "table" );
 * </code></pre>
 *
 * The required parameters are described by the getParameterInfo() method. Client
 *
 * <h2>Implementation Notes</h2>
 *
 * <p>An instance of this interface should exist for all DataAccess implementations that want to advantage of the
 * dynamic plug-in system. In addition to implementing this interface a DataAccess implementation should provide a
 * services file:
 *
 * <p><code>META-INF/services/org.geotools.api.data.DataAccessFactory</code>
 *
 * <p>The file should contain a single line which gives the full name of the implementing class.
 *
 * <p>Example:<br>
 * <code>e.g.
 * org.geotools.data.mytype.MyTypeDataSourceFacotry</code>
 *
 * <p>The factories are never called directly by client code, instead the DataStoreFinder class is used.
 *
 * @author Jody Garnett (Refractions Research)
 */
public interface DataAccessFactory extends Factory {
    /**
     * Construct a live DataAccess using the connection parameters provided.
     *
     * <p>You can think of this class as setting up a connection to the back end data source. The required parameters
     * are described by the getParameterInfo() method.
     *
     * <p>Magic Params: the following params are magic and are honoured by convention by the GeoServer and uDig
     * application.
     *
     * <ul>
     *   <li>"user": is taken to be the user name
     *   <li>"passwd": is taken to be the password
     *   <li>"namespace": is taken to be the namespace prefix (and will be kept in sync with GeoServer namespace
     *       management.
     * </ul>
     *
     * When we eventually move over to the use of OpperationalParam we will have to find someway to codify this
     * convention.
     *
     * @param params The full set of information needed to construct a live data store. Typical key values for the map
     *     include: url - location of a resource, used by file reading datasources. dbtype - the type of the database to
     *     connect to, e.g. postgis, mysql
     * @return The created DataStore, this may be null if the required resource was not found or if insufficent
     *     parameters were given. Note that canProcess() should have returned false if the problem is to do with
     *     insuficent parameters.
     * @throws IOException if there were any problems setting up (creating or connecting) the datasource.
     */
    DataAccess<? extends FeatureType, ? extends Feature> createDataStore(Map<String, ?> params) throws IOException;

    /**
     * Name suitable for display to end user.
     *
     * <p>A non localized display name for this data store type.
     *
     * @return A short name suitable for display in a user interface.
     */
    String getDisplayName();

    /**
     * Describe the nature of the datasource constructed by this factory.
     *
     * <p>A non localized description of this data store type.
     *
     * @return A human readable description that is suitable for inclusion in a list of available datasources.
     */
    String getDescription();

    /**
     * MetaData about the required Parameters (for createDataStore).
     *
     * <p>
     * Interpretation of FeatureDescriptor values:
     * </p>
     *
     * <ul>
     * <li>
     * getDisplayName(): Gets the localized display name of this feature.
     * </li>
     * <li>
     * getName(): Gets the programmatic name of this feature (used as the key
     * in params)
     * </li>
     * <li>
     * getShortDescription(): Gets the short description of this feature.
     * </li>
     * </ul>
     *
     * <p>
     * This should be the same as:
     * </p>
     * <pre><code>
     * Object params = factory.getParameters();
     * BeanInfo info = getBeanInfo( params );
     *
     * return info.getPropertyDescriptors();
     * <code></pre>
     *
     * @return Param array describing the Map for createDataStore
     */
    Param[] getParametersInfo();

    /**
     * Test to see if this factory is suitable for processing the data pointed to by the params map.
     *
     * <p>If this datasource requires a number of parameters then this mehtod should check that they are all present and
     * that they are all valid. If the datasource is a file reading data source then the extentions or mime types of any
     * files specified should be checked. For example, a Shapefile datasource should check that the url param ends with
     * shp, such tests should be case insensative.
     *
     * @param params The full set of information needed to construct a live data source.
     * @return booean true if and only if this factory can process the resource indicated by the param set and all the
     *     required params are pressent.
     */
    default boolean canProcess(java.util.Map<String, ?> params) {
        if (params == null) {
            return false;
        }
        Param[] arrayParameters = getParametersInfo();
        for (Param param : arrayParameters) {
            Object value;
            if (!params.containsKey(param.key)) {
                if (param.required) {
                    return false; // missing required key!
                } else {
                    continue;
                }
            }
            try {
                value = param.lookUp(params);
            } catch (IOException e) {
                // could not upconvert/parse to expected type!
                // even if this parameter is not required
                // we are going to refuse to process
                // these params
                return false;
            }
            if (value == null) {
                if (param.required) {
                    return false;
                }
            } else {
                if (!param.type.isInstance(value)) {
                    return false; // value was not of the required type
                }
                if (param.metadata != null) {
                    // check metadata
                    if (param.metadata.containsKey(Param.OPTIONS)) {
                        @SuppressWarnings("unchecked")
                        List<Object> options = (List<Object>) param.metadata.get(Param.OPTIONS);
                        if (options != null && !options.contains(value)) {
                            return false; // invalid option
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Test to see if the implementation is available for use. This method ensures all the appropriate libraries to
     * construct the DataAccess are available.
     *
     * <p>Most factories will simply return <code>true</code> as GeoTools will distribute the appropriate libraries.
     * Though it's not a bad idea for DataStoreFactories to check to make sure that the libraries are there.
     *
     * <p>OracleDataStoreFactory is an example of one that may generally return <code>false</code>, since GeoTools can
     * not distribute the oracle jars. (they must be added by the client.)
     *
     * <p>One may ask how this is different than canProcess, and basically available is used by the DataStoreFinder
     * getAvailableDataStore method, so that DataStores that can not even be used do not show up as options in gui
     * applications.
     *
     * @return <tt>true</tt> if and only if this factory has all the appropriate jars on the classpath to create
     *     DataStores.
     */
    boolean isAvailable();

    /**
     * Data class used to capture Parameter requirements.
     *
     * <p>Subclasses may provide specific setAsText()/getAsText() requirements
     */
    @SuppressWarnings("unchecked")
    public static class Param extends Parameter {

        /**
         * Provides support for text representations
         *
         * <p>The parameter type of String is assumed.
         *
         * @param key Key used to file this Param in the Parameter Map for createDataStore
         */
        public Param(String key) {
            this(key, String.class, null);
        }

        /**
         * Provides support for text representations.
         *
         * <p>You may specify a <code>type</code> for this Param.
         *
         * @param key Key used to file this Param in the Parameter Map for createDataStore
         * @param type Class type intended for this Param
         */
        public Param(String key, Class<?> type) {
            this(key, type, null);
        }

        /**
         * Provides support for text representations
         *
         * @param key Key used to file this Param in the Parameter Map for createDataStore
         * @param type Class type intended for this Param
         * @param description User description of Param (40 chars or less)
         */
        public Param(String key, Class<?> type, String description) {
            this(key, type, description, true);
        }

        /**
         * Provides support for text representations
         *
         * @param key Key used to file this Param in the Parameter Map for createDataStore
         * @param type Class type intended for this Param
         * @param description User description of Param (40 chars or less)
         * @param required <code>true</code> is param is required
         */
        public Param(String key, Class<?> type, String description, boolean required) {
            this(key, type, description, required, null);
        }

        /**
         * Provides support for text representations
         *
         * @param key Key used to file this Param in the Parameter Map for createDataStore
         * @param type Class type intended for this Param
         * @param description User description of Param (40 chars or less)
         * @param required <code>true</code> is param is required
         * @param sample Sample value as an example for user input
         */
        public Param(String key, Class<?> type, String description, boolean required, Object sample) {
            this(
                    key,
                    type,
                    description == null ? null : new SimpleInternationalString(description),
                    required,
                    sample,
                    null);
        }

        /**
         * Provides support for text representations
         *
         * @param key Key used to file this Param in the Parameter Map for createDataStore
         * @param type Class type intended for this Param
         * @param description User description of Param (40 chars or less)
         * @param required <code>true</code> is param is required
         * @param sample Sample value as an example for user input
         */
        public Param(String key, Class<?> type, InternationalString description, boolean required, Object sample) {
            super(key, type, new SimpleInternationalString(key), description, required, 1, 1, sample, null);
        }

        /**
         * Provides support for text representations
         *
         * @param key Key used to file this Param in the Parameter Map for createDataStore
         * @param type Class type intended for this Param
         * @param description User description of Param (40 chars or less)
         * @param required <code>true</code> is param is required
         * @param sample Sample value as an example for user input
         * @param metadata metadata information, preferably keyed by known identifiers like
         *     {@link Parameter#IS_PASSWORD}
         */
        public Param(
                String key,
                Class<?> type,
                String description,
                boolean required,
                Object sample,
                Map<String, ?> metadata) {
            this(key, type, new SimpleInternationalString(description), required, sample, metadata);
        }

        public Param(
                String key, Class<?> type, String description, boolean required, Object sample, Object... metadata) {
            this(key, type, description, required, sample, new KVP(metadata));
        }

        /**
         * Provides support for text representations
         *
         * @param key Key used to file this Param in the Parameter Map for createDataStore
         * @param type Class type intended for this Param
         * @param description User description of Param (40 chars or less)
         * @param required <code>true</code> is param is required
         * @param sample Sample value as an example for user input
         * @param metadata metadata information, preferably keyed by known identifiers like
         *     {@link Parameter#IS_PASSWORD}
         */
        public Param(
                String key,
                Class<?> type,
                InternationalString description,
                boolean required,
                Object sample,
                Map<String, ?> metadata) {
            super(key, type, new SimpleInternationalString(key), description, required, 1, 1, sample, metadata);
        }

        /**
         * Supports all Parameter values.
         *
         * @param key machine readable key for use in a java.util.Map
         * @param type Java class for the expected value
         * @param title Human readable title used for use in a user interface
         * @param description Human readable description
         * @param required true if the value is required
         * @param min Minimum value; or -1 if not needed
         * @param max Maximum value; or -1 for unbound
         * @param sample Sample value; may be used as a default in a user interface
         * @param metadata Hints to the user interface (read the javadocs for each metadata key)
         */
        public Param(
                String key,
                Class<?> type,
                InternationalString title,
                InternationalString description,
                boolean required,
                int min,
                int max,
                Object sample,
                Map<String, ?> metadata) {
            super(key, type, title, description, required, min, max, sample, metadata);
        }

        /**
         * Lookup Param in a user supplied map.
         *
         * <p>Type conversion will occur if required, this may result in an IOException. An IOException will be throw in
         * the Param is required and the Map does not contain the Map.
         *
         * <p>The handle method is used to process the user's value.
         *
         * @param map Map of user input
         * @return Parameter as specified in map
         * @throws IOException if parse could not handle value
         */
        public Object lookUp(Map<String, ?> map) throws IOException {
            if (!map.containsKey(key)) {
                if (required) {
                    throw new IOException("Parameter " + key + " is required:" + description);
                } else {
                    return null;
                }
            }

            Object value = map.get(key);

            if (value == null) {
                return null;
            }

            if (value instanceof String && (type != String.class)) {
                value = handle((String) value);
            }

            if (value == null) {
                return null;
            }

            if (!type.isInstance(value)) {
                throw new IOException(type.getName()
                        + " required for parameter "
                        + key
                        + ": not "
                        + value.getClass().getName());
            }

            return value;
        }

        /** Convert value to text representation for this Parameter */
        public String text(Object value) {
            return value.toString();
        }

        /**
         * Handle text in a sensible manner.
         *
         * <p>Performs the most common way of handling text value:
         *
         * <ul>
         *   <li>null: If text is null
         *   <li>original text: if type == String.class
         *   <li>first character of original text: if type == Character.class
         *   <li>null: if type != String.class and text.getLength == 0
         *   <li>parse( text ): if type != String.class
         * </ul>
         *
         * @return Value as processed by text
         * @throws IOException If text could not be parsed
         */
        public Object handle(String text) throws IOException {
            if (text == null) {
                return null;
            }

            if (type == String.class) {
                return text;
            }
            if (type == Character.class) {
                return text.charAt(0);
            }
            if (text.length() == 0) {
                return null;
            }

            // if type is an array, tokenize the string and have the reflection
            // parsing be tried on each element, then build the array as a result
            if (type.isArray()) {
                StringTokenizer tokenizer = new StringTokenizer(text, " ");
                List<Object> result = new ArrayList<>();

                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    Object element;

                    try {
                        if (type.getComponentType() == String.class) {
                            element = token;
                        } else {
                            element = parse(token);
                        }
                    } catch (IOException ioException) {
                        throw ioException;
                    } catch (Throwable throwable) {
                        throw new DataSourceException(
                                "Problem creating " + type.getName() + " from '" + text + "'", throwable);
                    }

                    result.add(element);
                }

                Object array = Array.newInstance(type.getComponentType(), result.size());

                for (int i = 0; i < result.size(); i++) {
                    Array.set(array, i, result.get(i));
                }

                return array;
            }

            try {
                return parse(text);
            } catch (IOException ioException) {
                throw ioException;
            } catch (Throwable throwable) {
                throw new DataSourceException("Problem creating " + type.getName() + " from '" + text + "'", throwable);
            }
        }

        /**
         * Provides support for text representations
         *
         * <p>Provides basic support for common types using reflection.
         *
         * <p>If needed you may extend this class to handle your own custome types.
         *
         * @param text Text representation of type should not be null or empty
         * @return Object converted from text representation
         * @throws IOException If text could not be parsed
         */
        public Object parse(String text) throws Throwable {
            Constructor<?> constructor;

            if (type.isEnum()) {
                if (text == null || text.isEmpty()) {
                    return null;
                }
                for (Object constant : type.getEnumConstants()) {
                    if (constant.toString().equalsIgnoreCase(text)) {
                        return constant;
                    }
                }
            }

            try {
                constructor = type.getConstructor(new Class[] {String.class});
            } catch (SecurityException | NoSuchMethodException e) {
                //  type( String ) constructor is not public
                throw new IOException("Could not create " + type.getName() + " from text");
            } // No type( String ) constructor

            try {
                return constructor.newInstance(new Object[] {
                    text,
                });
            } catch (IllegalArgumentException
                    | IllegalAccessException
                    | InstantiationException illegalArgumentException) {
                throw new DataSourceException(
                        "Could not create " + type.getName() + ": from '" + text + "'", illegalArgumentException);
            } catch (InvocationTargetException targetException) {
                throw targetException.getCause();
            }
        }

        /** key=Type description */
        @Override
        public String toString() {
            StringBuffer buf = new StringBuffer();
            buf.append(key);
            buf.append('=');
            buf.append(type.getName());
            buf.append(' ');

            if (required) {
                buf.append("REQUIRED ");
            }

            buf.append(description);

            return buf.toString();
        }
    }
}
