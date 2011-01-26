/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.metadata.sql;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

import org.opengis.metadata.MetaData;
import org.opengis.util.CodeList;
import org.opengis.util.InternationalString;

import org.geotools.util.SimpleInternationalString;


/**
 * A connection to a metadata database. The metadata database can be created
 * using one of the scripts suggested in GeoAPI, for example
 * <code><A HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/metadata/doc-files/postgre/create.sql">create.sql</A></CODE>.
 * Then, in order to get for example a telephone number, the following code
 * may be used.
 *
 * <BLOCKQUOTE><PRE>
 * import org.opengis.metadata.citation.{@linkplain org.opengis.metadata.citation.Telephone Telephone};
 * ...
 * Connection     connection = ...
 * MetadataSource source     = new MetadataSource(connection);
 * Telephone      telephone  = (Telephone) source.getEntry(Telephone.class, id);
 * </PRE></BLOCKQUOTE>
 *
 * where {@code id} is the primary key value for the desired record in the
 * {@code CI_Telephone} table.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Touraïvane
 * @author Olivier Kartotaroeno (Institut de Recherche pour le Développement)
 * @author Martin Desruisseaux (IRD)
 */
public class MetadataSource {
    /**
     * The package for metadata <strong>interfaces</strong> (not the implementation).
     */
    final String metadataPackage = "org.opengis.metadata.";

    /**
     * The connection to the database.
     */
    private final Connection connection;

    /**
     * The SQL query to use for fetching the attribute in a specific row.
     * The first question mark is the table name to search into; the second
     * one is the primary key of the record to search.
     */
    private final String query = "SELECT * FROM metadata.\"?\" WHERE id=?";

    /**
     * The SQL query to use for fetching a code list element.
     * The first question mark is the table name to search into;
     * the second one is the primary key of the element to search.
     */
    private final String codeQuery = "SELECT name FROM metadata.\"?\" WHERE code=?";

    /**
     * The prepared statements created is previous call to {@link #getValue}.
     * Those statements are encapsulated into {@link MetadataResult} objects.
     */
    private final Map<Class<?>,MetadataResult> statements = new HashMap<Class<?>,MetadataResult>();

    /**
     * The map from GeoAPI names to ISO names. For example the GeoAPI
     * {@link org.opengis.metadata.citation.Citation} interface maps
     * to the ISO 19115 {@code CI_Citation} name.
     */
    private final Properties geoApiToIso = new Properties();

    /**
     * Type of collections.
     */
    private final Properties collectionTypes = new Properties();

    /**
     * The class loader to use for proxy creation.
     */
    private final ClassLoader loader;

    /**
     * Creates a new metadata source.
     *
     * @param connection The connection to the database.
     */
    public MetadataSource(final Connection connection) {
        this.connection = connection;
        try {
            InputStream in = MetaData.class.getResourceAsStream("GeoAPI_to_ISO.properties");
            geoApiToIso.load(in);
            in.close();
            in = MetaData.class.getResourceAsStream("CollectionTypes.properties");
            // TODO: remove the (!= null) check after the next geoapi update.
            if (in != null) {
                collectionTypes.load(in);
                in.close();
            }
        } catch (IOException exception) {
            /*
             * Note: we do not expose the checked IOException because in a future
             *       version (when we will be allowed to use J2SE 1.5), it should
             *       disaspear. This is because a J2SE 1.5 enabled version should
             *       use method's annotations instead.
             */
            throw new MetadataException("Can't read resources.", exception); // TODO: localize
        }
        loader = getClass().getClassLoader();
    }

    /**
     * Returns an implementation of the specified metadata interface filled
     * with the data referenced by the specified identifier. Alternatively,
     * this method can also returns a {@link CodeList} element.
     *
     * @param  type The interface to implement (e.g.
     *         {@link org.opengis.metadata.citation.Citation}), or
     *         the {@link CodeList}.
     * @param  identifier The identifier used in order to locate the record for
     *         the metadata entity to be created. This is usually the primary key
     *         of the record to search for.
     * @return An implementation of the required interface, or the code list element.
     * @throws SQLException if a SQL query failed.
     */
    public synchronized Object getEntry(final Class type, final String identifier)
            throws SQLException
    {
        if (CodeList.class.isAssignableFrom(type)) {
            return getCodeList(type, identifier);
        }
        return Proxy.newProxyInstance(loader, new Class[] {type},
                                      new MetadataEntity(identifier, this));
    }

    /**
     * Returns an attribute from a table.
     *
     * @param  type       The interface class. This is mapped to the table name in the database.
     * @param  method     The method invoked. This is mapped to the column name in the database.
     * @param  identifier The primary key of the record to search for.
     * @return The value of the requested attribute.
     * @throws SQLException if the SQL query failed.
     */
    final synchronized Object getValue(final Class<?> type, final Method method, final String identifier)
            throws SQLException
    {
        final String className = getClassName(type);
        MetadataResult  result = statements.get(type);
        if (result == null) {
            result = new MetadataResult(connection, query, getTableName(className));
            statements.put(type, result);
        }
        final String  columnName = getColumnName(className, method);
        final Class<?> valueType = method.getReturnType();
        /*
         * Process the ResultSet value according the expected return type. If a collection
         * is expected, then assumes that the ResultSet contains an array and invokes the
         * 'getValue' method for each element.
         */
        if (Collection.class.isAssignableFrom(valueType)) {
            final Collection<Object> collection;
            if (List.class.isAssignableFrom(valueType)) {
                collection = new ArrayList<Object>();
            } else if (SortedSet.class.isAssignableFrom(valueType)) {
                collection = new TreeSet<Object>();
            } else {
                collection = new LinkedHashSet<Object>();
            }
            assert valueType.isAssignableFrom(collection.getClass());
            final Object elements = result.getArray(identifier, columnName);
            if (elements != null) {
                final Class  elementType = getElementType(className, method);
                final boolean isMetadata = isMetadata(elementType);
                final int         length = Array.getLength(elements);
                for (int i=0; i<length; i++) {
                    collection.add(isMetadata ? getEntry(elementType, Array.get(elements, i).toString())
                                              : convert (elementType, Array.get(elements, i)));
                }
            }
            return collection;
        }
        /*
         * If a GeoAPI interface or a code list is expected, then assumes that the ResultSet
         * value is a foreigner key. Queries again the database in the foreigner table.
         */
        if (valueType.isInterface() && isMetadata(valueType)) {
            final String foreigner = result.getString(identifier, columnName);
            return result.wasNull() ? null : getEntry(valueType, foreigner);
        }
        if (CodeList.class.isAssignableFrom(valueType)) {
            final String foreigner = result.getString(identifier, columnName);
            return result.wasNull() ? null : getCodeList(valueType, foreigner);
        }
        /*
         * Not a foreigner key. Get the value and transform it to the
         * espected type, if needed.
         */
        return convert(valueType, result.getObject(identifier, columnName));
    }

    /**
     * Returns {@code true} if the specified type belong to the metadata package.
     */
    private boolean isMetadata(final Class valueType) {
        return valueType.getName().startsWith(metadataPackage);
    }

    /**
     * Converts the specified non-metadata value into an object of the expected type.
     * The expected value is an instance of a class outside the metadata package, for
     * example {@link String}, {@link InternationalString}, {@link URI}, etc.
     */
    private static Object convert(final Class<?> valueType, final Object value) {
        if (value!=null && !valueType.isAssignableFrom(value.getClass())) {
            if (InternationalString.class.isAssignableFrom(valueType)) {
               return new SimpleInternationalString(value.toString());
            }
            if (URL.class.isAssignableFrom(valueType)) try {
                return new URL(value.toString());
            } catch (MalformedURLException exception) {
                // TODO: localize and provides more details.
                throw new MetadataException("Illegal value.", exception);
            }
            if (URI.class.isAssignableFrom(valueType)) try {
                return new URI(value.toString());
            } catch (URISyntaxException exception) {
                // TODO: localize and provides more details.
                throw new MetadataException("Illegal value.", exception);
            }
        }
        return value;
    }

    /**
     * Returns a code list of the given type.
     *
     * @param  type The type, as a subclass of {@link CodeList}.
     * @param  identifier The identifier in the code list. This method accepts either The numerical
     *         value of the code to search for (usually the primary key), or the code name.
     * @return The code list element.
     * @throws SQLException if a SQL query failed.
     */
    private CodeList getCodeList(final Class<?> type, String identifier) throws SQLException {
        assert Thread.holdsLock(this);
        final String className = getClassName(type);
        int     code;          // The identifier as an integer.
        boolean isNumerical;   // 'true' if 'code' is valid.
        try {
            code = Integer.parseInt(identifier);
            isNumerical = true;
        } catch (NumberFormatException exception) {
            code = 0;
            isNumerical = false;
        }
        /*
         * Converts the numerical value into the code list name.
         */
        if (isNumerical) {
            MetadataResult result = statements.get(type);
            if (result == null) {
                result = new MetadataResult(connection, codeQuery, getTableName(className));
                statements.put(type, result);
            }
            identifier = result.getString(identifier);
        }
        /*
         * Search a code list with the same name than the one declared
         * in the database. We will use name instead of code numerical
         * value, since the later is more bug prone.
         */
        final CodeList<?>[] values;
        try {
            values = (CodeList[]) type.getMethod("values", (Class []) null)
                                      .invoke   (null,     (Object[]) null);
        } catch (NoSuchMethodException exception) {
            throw new MetadataException("Can't read code list.", exception); // TODO: localize
        } catch (IllegalAccessException exception) {
            throw new MetadataException("Can't read code list.", exception); // TODO: localize
        } catch (InvocationTargetException exception) {
            throw new MetadataException("Can't read code list.", exception); // TODO: localize
        }
        CodeList<?> candidate;
        final StringBuilder candidateName = new StringBuilder(className);
        candidateName.append('.');
        final int base = candidateName.length();
        if (code>=1 && code<values.length) {
            candidate = values[code-1];
            candidateName.append(candidate.name());
            if (identifier.equals(geoApiToIso.getProperty(candidateName.toString()))) {
                return candidate;
            }
        }
        /*
         * The previous code was an optimization which checked directly the code list
         * for the same code than the one used in the database. Most of the time, the
         * name matches and this loop is never executed. If we reach this point, then
         * maybe the numerical code are not the same in the database than in the Java
         * CodeList implementation. Check each code list element by name.
         */
        for (int i=0; i<values.length; i++) {
            candidate = values[i];
            candidateName.setLength(base);
            candidateName.append(candidate.name());
            if (identifier.equals(geoApiToIso.getProperty(candidateName.toString()))) {
                return candidate;
            }
        }
        // TODO: localize
        throw new SQLException("Unknow code list: \""+identifier+"\" in table \"" +
                               getTableName(className)+'"');
    }

    /**
     * Returns the unqualified Java interface name for the specified type.
     * This is usually the GeoAPI name.
     */
    private static String getClassName(final Class<?> type) {
        final String className = type.getName();
        return className.substring(className.lastIndexOf('.') + 1);
    }

    /**
     * Returns the table name for the specified class.
     * This is usually the ISO 19115 name.
     */
    private String getTableName(final String className) {
        final String tableName = geoApiToIso.getProperty(className);
        return (tableName != null) ? tableName : className;
    }

    /**
     * Returns the column name for the specified method.
     */
    private String getColumnName(final String className, final Method method) {
        final String methodName = method.getName();
        final String columnName = geoApiToIso.getProperty(className+'.'+methodName);
        return (columnName != null) ? columnName : methodName;
    }

    /**
     * Returns the element type in collection for the specified method.
     */
    private Class getElementType(final String className, final Method method) {
        final String key = className+'.'+method.getName();
        final String typeName = collectionTypes.getProperty(key);
        Exception cause = null;
        if (typeName != null) try {
            return Class.forName(typeName);
        } catch (ClassNotFoundException exception) {
            cause = exception;
        }
        // TODO: localize.
        final MetadataException e = new MetadataException("Unknow element type for "+key);
        if (cause != null) {
            e.initCause(cause);
        }
        throw e;
    }

    /**
     * Close all connections used in this object.
     */
    public synchronized void close() throws SQLException {
        for (final Iterator it=statements.values().iterator(); it.hasNext();) {
            ((MetadataResult)it.next()).close();
            it.remove();
        }
        connection.close();
    }
}
