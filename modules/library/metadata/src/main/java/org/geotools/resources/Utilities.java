/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.resources;

import java.util.Queue;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import org.geotools.util.logging.Logging;


/**
 * A set of miscellaneous methods.
 *
 * @since 2.0
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @deprecated Moved to {@link org.geotools.util} package.
 */
@Deprecated
public final class Utilities {
    /**
     * Forbid object creation.
     */
    private Utilities() {
    }

    /**
     * Convenience method for testing two objects for equality. One or both objects may be null.
     */
    public static boolean equals(final Object object1, final Object object2) {
        return (object1==object2) || (object1!=null && object1.equals(object2));
    }

    /**
     * Convenience method for testing two objects for equality. One or both objects may be null.
     * If both are non-null and are arrays, then every array elements will be compared.
     * <p>
     * This method may be useful when the objects may or may not be array. If they are known
     * to be arrays, consider using {@link Arrays#deepEquals(Object[],Object[])} or one of its
     * primitive counter-part instead.
     * <p>
     * <strong>Rules for choosing an {@code equals} or {@code deepEquals} method</strong>
     * <ul>
     *   <li>If <em>both</em> objects are declared as {@code Object[]} (not anything else like
     *   {@code String[]}), consider using {@link Arrays#deepEquals(Object[],Object[])} except
     *   if it is known that the array elements can never be other arrays.</li>
     *
     *   <li>Otherwise if both objects are arrays (e.g. {@code Expression[]}, {@code String[]},
     *   {@code int[]}, <cite>etc.</cite>), use {@link Arrays#equals(Object[],Object[])}. This
     *   rule is applicable to arrays of primitive type too, since {@code Arrays.equals} is
     *   overriden with primitive counter-parts.</li>
     *
     *   <li>Otherwise if at least one object is anything else than {@code Object} (e.g.
     *   {@code String}, {@code Expression}, <cite>etc.</cite>), use {@link #equals(Object,Object)}.
     *   Using this {@code deepEquals} method would be an overkill since there is no chance that
     *   {@code String} or {@code Expression} could be an array.</li>
     *
     *   <li>Otherwise if <em>both</em> objects are declared exactly as {@code Object} type and
     *   it is known that they could be arrays, only then invoke this {@code deepEquals} method.
     *   In such case, make sure that the hash code is computed using {@link #deepHashCode} for
     *   consistency.</li>
     * </ul>
     */
    public static boolean deepEquals(final Object object1, final Object object2) {
        return org.geotools.util.Utilities.deepEquals(object1, object2);
    }

    /**
     * Returns a hash code for the specified object, which may be an array.
     * This method returns one of the following values:
     * <p>
     * <ul>
     *   <li>If the supplied object is {@code null}, then this method returns 0.</li>
     *   <li>Otherwise if the object is an array of objects, then
     *       {@link Arrays#deepHashCode(Object[])} is invoked.</li>
     *   <li>Otherwise if the object is an array of primitive type, then the corresponding
     *       {@link Arrays#hashCode(double[]) Arrays.hashCode(...)} method is invoked.</li>
     *   <li>Otherwise {@link Object#hashCode()} is invoked.<li>
     * </ul>
     * <p>
     * This method should be invoked <strong>only</strong> if the object type is declared
     * exactly as {@code Object}, not as some subtype like {@code Object[]}, {@code String} or
     * {@code float[]}. In the later cases, use the appropriate {@link Arrays} method instead.
     */
    public static int deepHashCode(final Object object) {
        return org.geotools.util.Utilities.deepHashCode(object);
    }

    /**
     * Returns a string representation of the specified object, which may be an array.
     * This method returns one of the following values:
     * <p>
     * <ul>
     *   <li>If the object is an array of objects, then
     *       {@link Arrays#deepToString(Object[])} is invoked.</li>
     *   <li>Otherwise if the object is an array of primitive type, then the corresponding
     *       {@link Arrays#toString(double[]) Arrays.toString(...)} method is invoked.</li>
     *   <li>Otherwise {@link String#valueOf(String)} is invoked.<li>
     * </ul>
     * <p>
     * This method should be invoked <strong>only</strong> if the object type is declared
     * exactly as {@code Object}, not as some subtype like {@code Object[]}, {@code Number} or
     * {@code float[]}. In the later cases, use the appropriate {@link Arrays} method instead.
     */
    public static String deepToString(final Object object) {
        return org.geotools.util.Utilities.deepToString(object);
    }

    /**
     * Returns a {@linkplain Queue queue} which is always empty and accepts no element.
     *
     * @see Collections#emptyList
     * @see Collections#emptySet
     */
    @SuppressWarnings("unchecked")
    public static <E> Queue<E> emptyQueue() {
        return org.geotools.util.Utilities.emptyQueue();
    }

    /**
     * Returns {@code true} if the two specified objects implements exactly the same set of
     * interfaces. Only interfaces assignable to {@code base} are compared. Declaration order
     * doesn't matter. For example in ISO 19111, different interfaces exist for different coordinate
     * system geometries ({@code CartesianCS}, {@code PolarCS}, etc.). We can check if two
     * CS implementations has the same geometry with the following code:
     *
     * <blockquote><code>
     * if (sameInterfaces(cs1, cs2, {@linkplain org.opengis.referencing.cs.CoordinateSystem}.class))
     * </code></blockquote>
     *
     * @deprecated Moved to {@link Classes}.
     */
    public static <T> boolean sameInterfaces(final Class<? extends T> object1,
                                             final Class<? extends T> object2,
                                             final Class<T> base)
    {
        return Classes.sameInterfaces(object1, object2, base);
    }

    /**
     * Returns a string of the specified length filled with white spaces.
     * This method tries to return a pre-allocated string if possible.
     *
     * @param  length The string length. Negative values are clamped to 0.
     * @return A string of length {@code length} filled with white spaces.
     */
    public static String spaces(int length) {
        return org.geotools.util.Utilities.spaces(length);
    }

    /**
     * Returns a short class name for the specified class. This method will
     * omit the package name.  For example, it will return "String" instead
     * of "java.lang.String" for a {@link String} object. It will also name
     * array according Java language usage,  for example "double[]" instead
     * of "[D".
     *
     * @param  classe The object class (may be {@code null}).
     * @return A short class name for the specified object.
     *
     * @deprecated Moved to {@link Classes}.
     */
    public static String getShortName(Class<?> classe) {
        return Classes.getShortName(classe);
    }

    /**
     * Returns a short class name for the specified object. This method will
     * omit the package name. For example, it will return "String" instead
     * of "java.lang.String" for a {@link String} object.
     *
     * @param  object The object (may be {@code null}).
     * @return A short class name for the specified object.
     *
     * @deprecated Moved to {@link Classes}.
     */
    public static String getShortClassName(final Object object) {
        return Classes.getShortClassName(object);
    }

    /**
     * Invoked when a recoverable error occurs. This exception is similar to
     * {@link #unexpectedException unexpectedException} except that it doesn't
     * log the stack trace and uses a lower logging level.
     *
     * @param paquet  The package where the error occurred. This information
     *                may be used to fetch an appropriate {@link Logger} for
     *                logging the error.
     * @param classe  The class where the error occurred.
     * @param method  The method name where the error occurred.
     * @param error   The error.
     *
     * @deprecated Moved to {@link Logging#recoverableException}.
     */
    public static void recoverableException(final String   paquet,
                                            final Class<?> classe,
                                            final String   method,
                                            final Throwable error)
    {
        final LogRecord record = getLogRecord(error);
        record.setLevel(Level.FINER);
        record.setSourceClassName (classe.getName());
        record.setSourceMethodName(method);
        record.setLoggerName(paquet);
        Logging.getLogger(paquet).log(record);
    }

    /**
     * Returns a log record for the specified exception.
     *
     * @deprecated Will be deleted after we removed the {@link #recoverableException}
     *             deprecated method.
     */
    public static LogRecord getLogRecord(final Throwable error) {
        final StringBuilder buffer = new StringBuilder(Classes.getShortClassName(error));
        final String message = error.getLocalizedMessage();
        if (message != null) {
            buffer.append(": ");
            buffer.append(message);
        }
        return new LogRecord(Level.WARNING, buffer.toString());
    }
}
