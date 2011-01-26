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
package org.geotools.metadata;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.URI;
import java.net.URL;
import java.net.URISyntaxException;
import java.net.MalformedURLException;
import java.util.LinkedHashSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.opengis.annotation.UML;
import org.opengis.util.InternationalString;

import org.geotools.util.Utilities;
import org.geotools.resources.XArray;
import org.geotools.resources.Classes;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.util.CheckedCollection;
import org.geotools.util.SimpleInternationalString;


/**
 * The getters declared in a GeoAPI interface, together with setters (if any)
 * declared in the Geotools implementation.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
final class PropertyAccessor {
    /**
     * The locale to use for changing character case.
     */
    private static final Locale LOCALE = Locale.US;

    /**
     * The prefix for getters on boolean values.
     */
    private static final String IS = "is";

    /**
     * The prefix for getters (general case).
     */
    private static final String GET = "get";

    /**
     * The prefix for setters.
     */
    private static final String SET = "set";

    /**
     * Methods to exclude from {@link #getGetters}. They are method inherited from
     * {@link java.lang.Object}. Some of them, especially {@link Object#hashCode()}
     * {@link Object#toString()} and {@link Object#clone()}, may be declared explicitly
     * in some interface with a formal contract. Note: only no-argument methods need to
     * be declared in this list.
     */
    private static final String[] EXCLUDES = {
        "clone", "finalize", "getClass", "hashCode", "notify", "notifyAll", "toString", "wait"
    };

    /**
     * Getters shared between many instances of this class. Two different implementations
     * may share the same getters but different setters.
     */
    private static final Map<Class<?>, Method[]> SHARED_GETTERS = new HashMap<Class<?>, Method[]>();

    /**
     * The implemented metadata interface.
     */
    final Class<?> type;

    /**
     * The implementation class. The following condition must hold:
     *
     * <blockquote><pre>
     * type.{@linkplain Class#isAssignableFrom isAssignableFrom}(implementation);
     * </pre></blockquote>
     */
    final Class<?> implementation;

    /**
     * The getter methods. This array should not contain any null element.
     */
    private final Method[] getters;

    /**
     * The corresponding setter methods, or {@code null} if none. This array must have
     * the same length than {@link #getters}. For every {@code getters[i]} element,
     * {@code setters[i]} is the corresponding setter or {@code null} if there is none.
     */
    private final Method[] setters;

    /**
     * Index of getter or setter for a given name. The name must be all lower cases with
     * conversion done using {@link #LOCALE}. This map must be considered as immutable
     * after construction.
     */
    private final Map<String,Integer> mapping;

    /**
     * Creates a new property reader for the specified metadata implementation.
     *
     * @param  metadata The metadata implementation to wrap.
     * @param  type The interface implemented by the metadata.
     *         Should be the value returned by {@link #getType}.
     */
    PropertyAccessor(final Class<?> implementation, final Class<?> type) {
        this.implementation = implementation;
        this.type = type;
        assert type.isAssignableFrom(implementation) : implementation;
        getters = getGetters(type);
        mapping = new HashMap<String,Integer>(getters.length + (getters.length + 3) / 4);
        Method[] setters = null;
        final Class<?>[] arguments = new Class[1];
        for (int i=0; i<getters.length; i++) {
            /*
             * Fetch the getter and remind its name. We do the same for
             * the UML tag attached to the getter, if any.
             */
            final Integer index = i;
            Method getter  = getters[i];
            String name    = getter.getName();
            final int base = prefix(name).length();
            addMapping(name.substring(base), index);
            final UML annotation = getter.getAnnotation(UML.class);
            if (annotation != null) {
                addMapping(annotation.identifier(), index);
            }
            /*
             * Now try to infer the setter from the getter. We replace the "get" prefix by
             * "set" and look for a parameter of the same type than the getter return type.
             */
            Class<?> returnType = getter.getReturnType();
            arguments[0] = returnType;
            if (name.length() > base) {
                final char lo = name.charAt(base);
                final char up = Character.toUpperCase(lo);
                if (lo != up) {
                    name = SET + up + name.substring(base + 1);
                } else {
                    name = SET + name.substring(base);
                }
            }
            Method setter;
            try {
                setter = implementation.getMethod(name, arguments);
            } catch (NoSuchMethodException e) {
                /*
                 * If we found no setter method expecting an argument of the same type than the
                 * argument returned by the GeoAPI method,  try again with the type returned by
                 * the implementation class. It is typically the same type, but sometime it may
                 * be a subtype.
                 */
                try {
                    getter = implementation.getMethod(getter.getName(), (Class[]) null);
                } catch (NoSuchMethodException error) {
                    // Should never happen, since the implementation class
                    // implements the the interface where the getter come from.
                    throw new AssertionError(error);
                }
                if (returnType.equals(returnType = getter.getReturnType())) {
                    continue;
                }
                arguments[0] = returnType;
                try {
                    setter = implementation.getMethod(name, arguments);
                } catch (NoSuchMethodException ignore) {
                    continue;
                }
            }
            if (setters == null) {
                setters = new Method[getters.length];
            }
            setters[i] = setter;
        }
        this.setters = setters;
    }

    /**
     * Adds the given (name, index) pair to {@link #mapping}, making sure we don't
     * overwrite an existing entry with different value.
     */
    private void addMapping(String name, final Integer index) throws IllegalArgumentException {
        name = name.trim();
        if (name.length() != 0) {
            final String lower = name.toLowerCase(LOCALE);
            final Integer old = mapping.put(lower, index);
            if (old != null && !old.equals(index)) {
                throw new IllegalArgumentException(Errors.format(
                        ErrorKeys.PARAMETER_NAME_CLASH_$4, name, index, lower, old));
            }
        }
    }

    /**
     * Returns the metadata interface implemented by the specified implementation.
     * Only one metadata interface can be implemented.
     *
     * @param  metadata The metadata implementation to wraps.
     * @param  interfacePackage The root package for metadata interfaces.
     * @return The single interface, or {@code null} if none where found.
     */
    static Class<?> getType(Class<?> implementation, final String interfacePackage) {
        if (implementation != null && !implementation.isInterface()) {
            /*
             * Gets every interfaces from the supplied package in declaration order,
             * including the ones declared in the super-class.
             */
            final Set<Class<?>> interfaces = new LinkedHashSet<Class<?>>();
            do {
                getInterfaces(implementation, interfacePackage, interfaces);
                implementation = implementation.getSuperclass();
            } while (implementation != null);
            /*
             * If we found more than one interface, removes the
             * ones that are sub-interfaces of the other.
             */
            for (final Iterator<Class<?>> it=interfaces.iterator(); it.hasNext();) {
                final Class<?> candidate = it.next();
                for (final Class<?> child : interfaces) {
                    if (candidate != child && candidate.isAssignableFrom(child)) {
                        it.remove();
                        break;
                    }
                }
            }
            final Iterator<Class<?>> it=interfaces.iterator();
            if (it.hasNext()) {
                final Class<?> candidate = it.next();
                if (!it.hasNext()) {
                    return candidate;
                }
                // Found more than one interface; we don't know which one to pick.
                // Returns 'null' for now; the caller will thrown an exception.
            }
        }
        return null;
    }

    /**
     * Puts every interfaces for the given type in the specified collection.
     * This method invokes itself recursively for scanning parent interfaces.
     */
    private static void getInterfaces(final Class<?> type, final String interfacePackage,
            final Collection<Class<?>> interfaces)
    {
        for (final Class<?> candidate : type.getInterfaces()) {
            if (candidate.getName().startsWith(interfacePackage)) {
                interfaces.add(candidate);
            }
            getInterfaces(candidate, interfacePackage, interfaces);
        }
    }

    /**
     * Returns the getters. The returned array should never be modified,
     * since it may be shared among many instances of {@code PropertyAccessor}.
     *
     * @param  type The metadata interface.
     * @return The getters declared in the given interface (never {@code null}).
     */
    private static Method[] getGetters(final Class<?> type) {
        synchronized (SHARED_GETTERS) {
            Method[] getters = SHARED_GETTERS.get(type);
            if (getters == null) {
                getters = type.getMethods();
                int count = 0;
                for (int i=0; i<getters.length; i++) {
                    final Method candidate = getters[i];
                    if (candidate.getAnnotation(Deprecated.class) != null) {
                        // Ignores deprecated methods.
                        continue;
                    }
                    if (!candidate.getReturnType().equals(Void.TYPE) &&
                         candidate.getParameterTypes().length == 0)
                    {
                        /*
                         * We do not require a name starting with "get" or "is" prefix because some
                         * methods do not begin with such prefix, as in "ConformanceResult.pass()".
                         * Consequently we must provide special cases for no-arg methods inherited
                         * from java.lang.Object because some interfaces declare explicitly the
                         * contract for those methods.
                         *
                         * Note that testing candidate.getDeclaringClass().equals(Object.class)
                         * is not suffisient because the method may be overriden in a subclass.
                         */
                        final String name = candidate.getName();
                        if (!name.startsWith(SET) && !isExcluded(name)) {
                            getters[count++] = candidate;
                        }
                    }
                }
                getters = XArray.resize(getters, count);
                SHARED_GETTERS.put(type, getters);
            }
            return getters;
        }
    }

    /**
     * Returns {@code true} if the specified method is on the exclusion list.
     */
    private static boolean isExcluded(final String name) {
        for (int i=0; i<EXCLUDES.length; i++) {
            if (name.equals(EXCLUDES[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the prefix of the specified method name. If the method name don't starts with
     * a prefix (for example {@link org.opengis.metadata.quality.ConformanceResult#pass()}),
     * then this method returns an empty string.
     */
    private static String prefix(final String name) {
        if (name.startsWith(GET)) {
            return GET;
        }
        if (name.startsWith(IS)) {
            return IS;
        }
        if (name.startsWith(SET)) {
            return SET;
        }
        return "";
    }

    /**
     * Returns the number of properties that can be read.
     */
    final int count() {
        return getters.length;
    }

    /**
     * Returns the index of the specified property, or -1 if none.
     * The search is case-insensitive.
     *
     * @param  key The property to search.
     * @return The index of the given key, or -1 if none.
     */
    final int indexOf(String key) {
        key = key.trim().toLowerCase(LOCALE);
        final Integer index = mapping.get(key);
        return (index != null) ? index.intValue() : -1;
    }

    /**
     * Always returns the index of the specified property (never -1).
     * The search is case-insensitive.
     *
     * @param  key The property to search.
     * @return The index of the given key.
     * @throws IllegalArgumentException if the given key is not found.
     */
    final int requiredIndexOf(String key) throws IllegalArgumentException {
        key = key.trim();
        final Integer index = mapping.get(key.toLowerCase(LOCALE));
        if (index != null) {
            return index;
        }
        throw new IllegalArgumentException(Errors.format(ErrorKeys.UNKNOW_PARAMETER_NAME_$1, key));
    }

    /**
     * Returns {@code true} if the specified string starting at the specified index contains
     * no lower case characters. The characters don't have to be in upper case however (e.g.
     * non-alphabetic characters)
     */
    private static boolean isAcronym(final String name, int offset) {
        final int length = name.length();
        while (offset < length) {
            if (Character.isLowerCase(name.charAt(offset++))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the name of the property at the given index, or {@code null} if none.
     */
    final String name(final int index) {
        if (index >= 0 && index < getters.length) {
            String name = getters[index].getName();
            final int base = prefix(name).length();
            /*
             * Remove the "get" or "is" prefix and turn the first character after the
             * prefix into lower case. For example the method name "getTitle" will be
             * replaced by the property name "title". We will performs this operation
             * only if there is at least 1 character after the prefix.
             */
            if (name.length() > base) {
                if (isAcronym(name, base)) {
                    name = name.substring(base);
                } else {
                    final char up = name.charAt(base);
                    final char lo = Character.toLowerCase(up);
                    if (up != lo) {
                        name = lo + name.substring(base + 1);
                    } else {
                        name = name.substring(base);
                    }
                }
            }
            return name;
        }
        return null;
    }

    /**
     * Returns the type of the property at the given index.
     */
    final Class<?> type(final int index) {
        if (index >= 0 && index < getters.length) {
            return getters[index].getReturnType();
        }
        return null;
    }

    /**
     * Returns {@code true} if the property at the given index is writable.
     */
    final boolean isWritable(final int index) {
        return (index >= 0) && (index < getters.length) && (setters != null) && (setters[index] != null);
    }

    /**
     * Returns the value for the specified metadata, or {@code null} if none.
     */
    final Object get(final int index, final Object metadata) {
        return (index >= 0 && index < getters.length) ? get(getters[index], metadata) : null;
    }

    /**
     * Gets a value from the specified metadata. We do not expect any checked exception to
     * be thrown, since {@code org.opengis.metadata} do not declare any.
     *
     * @param method The method to use for the query.
     * @param metadata The metadata object to query.
     */
    private static Object get(final Method method, final Object metadata) {
        assert !method.getReturnType().equals(Void.TYPE) : method;
        try {
            return method.invoke(metadata, (Object[]) null);
        } catch (IllegalAccessException e) {
            // Should never happen since 'getters' should contains only public methods.
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            final Throwable cause = e.getTargetException();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw new UndeclaredThrowableException(cause);
        }
    }

    /**
     * Sets a value for the specified metadata.
     *
     * @param  index The index of the property to set.
     * @param  metadata The metadata object on which to set the value.
     * @param  value The new value.
     * @return The old value.
     * @throws IllegalArgumentException if the specified property can't be set.
     * @throws ClassCastException if the given value is not of the expected type.
     */
    final Object set(final int index, final Object metadata, final Object value)
            throws IllegalArgumentException, ClassCastException
    {
        String key;
        if (index >= 0 && index < getters.length && setters != null) {
            final Method getter = getters[index];
            final Method setter = setters[index];
            if (setter != null) {
                final Object old = get(getter, metadata);
                set(getter, setter, metadata, new Object[] {value});
                return old;
            } else {
                key = getter.getName();
                key = key.substring(prefix(key).length());
            }
        } else {
            key = String.valueOf(index);
        }
        throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$1, key));
    }

    /**
     * Sets a value for the specified metadata. We do not expect any checked exception to
     * be thrown.
     *
     * @param getter The method to use for fetching the previous value.
     * @param setter The method to use for setting the new value.
     * @param metadata The metadata object to query.
     * @param arguments The argument to give to the method to be invoked.
     * @throws ClassCastException if at least one element of the {@code arguments} array
     *         is not of the expected type.
     */
    private static void set(final Method getter, final Method setter,
                            final Object metadata, final Object[] arguments)
            throws ClassCastException
    {
        final Class<?>[] paramTypes = setter.getParameterTypes();
        for (int i=0; i<paramTypes.length; i++) {
            final Object argument = arguments[i];
            if (argument == null) {
                continue; // Null argument (which is valid): nothing to do.
            }
            final Class<?> paramType = paramTypes[i];
            if (Classes.primitiveToWrapper(paramType).isInstance(argument)) {
                continue; // Argument is of the expected type: nothing to do.
            }
            /*
             * If an argument is not of the expected type, tries to convert it.
             * We handle two cases:
             *
             *   - Strings to be converted to Number, File, URL, etc.
             *   - Singleton to be added into an existing collection.
             *
             * We check for the collection case first in order to extract the element
             * type, which will be used for String conversions (if applicable) later.
             * The collections are handled in one of the two ways below:
             *
             *   - If the user gives a collection, the user's collection replaces any
             *     previous one. The content of the previous collection is discarted.
             *
             *   - If the user gives a singleton, the single value is added to existing
             *     collection (if any). The previous values are not discarted. This
             *     allow for incremental filling of an attribute.
             */
            final Collection<?> addTo;
            final Class<?> elementType;
            if (Collection.class.isAssignableFrom(paramType) && !(argument instanceof Collection)) {
                // Expected a collection but got a singleton.
                addTo = (Collection) get(getter, metadata);
                if (addTo instanceof CheckedCollection) {
                    elementType = ((CheckedCollection) addTo).getElementType();
                } else {
                    Class<?> c = Classes.boundOfParameterizedAttribute(setter);
                    if (c == null) {
                        c = Classes.boundOfParameterizedAttribute(getter);
                        if (c == null) {
                            c = Object.class;
                        }
                    }
                    elementType = c;
                }
            } else {
                addTo = null;
                elementType = paramType;
            }
            /*
             * Handles the strings in a special way (converting to URI, URL, File,
             * Number, etc.). If there is no known way to parse the string, or if
             * the parsing failed, an exception is thrown.
             */
            Object parsed = null;
            Exception failure = null;
            if (elementType.isInstance(argument)) {
                parsed = argument;
            } else if (argument instanceof CharSequence) {
                final String text = argument.toString();
                if (InternationalString.class.isAssignableFrom(elementType)) {
                    parsed = new SimpleInternationalString(text);
                } else if (File.class.isAssignableFrom(elementType)) {
                    parsed = new File(text);
                } else if (URL.class.isAssignableFrom(elementType)) try {
                    parsed = new URL(text);
                } catch (MalformedURLException e) {
                    failure = e;
                } else if (URI.class.isAssignableFrom(elementType)) try {
                    parsed = new URI(text);
                } catch (URISyntaxException e) {
                    failure = e;
                } else try {
                    parsed = Classes.valueOf(elementType, text);
                } catch (RuntimeException e) {
                    // Include IllegalArgumentException and NumberFormatException
                    failure = e;
                }
            }
            /*
             * Checks if there is no known conversion, or if the conversion failed. In the later
             * case the parse failure is saved as the cause. We still throw a ClassCastException
             * since we get here because the argument was not of the expected type.
             */
            if (parsed == null) {
                final ClassCastException e = new ClassCastException(Errors.format(
                        ErrorKeys.ILLEGAL_CLASS_$2, argument.getClass(), elementType));
                e.initCause(failure);
                throw e;
            }
            /*
             * We now have an object of the appropriate type. If this is a singleton to be added in
             * an existing collection, add it now and set the new value to the whole collection. In
             * the later case, we rely on ModifiableMetadata.copyCollection(...) optimization for
             * detecting that the new collection is the same instance than the old one so there is
             * nothing to do. We could exit from the method, but let it goes in case the user define
             * (or override) the 'setFoo(...)' method in an other way.
             */
            if (addTo != null) {
                addUnsafe(addTo, parsed);
                parsed = addTo;
            }
            arguments[i] = parsed;
        }
        try {
            setter.invoke(metadata, arguments);
        } catch (IllegalAccessException e) {
            // Should never happen since 'setters' should contains only public methods.
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            final Throwable cause = e.getTargetException();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw new UndeclaredThrowableException(cause);
        }
    }

    /**
     * Unsafe addition into a collection. In GeoTools implementation, the collection is actually
     * an instance of {@link CheckedCollection}, so the check will be performed at runtime.
     * However other implementations could use unchecked collection. There is not much we can do.
     */
    @SuppressWarnings("unchecked")
    private static void addUnsafe(final Collection<?> addTo, final Object element) {
        ((Collection) addTo).add(element);
    }

    /**
     * Compares the two specified metadata objects. The comparaison is <cite>shallow</cite>,
     * i.e. all metadata attributes are compared using the {@link Object#equals} method without
     * recursive call to this {@code shallowEquals} method for other metadata.
     * <p>
     * This method can optionaly excludes null values from the comparaison. In metadata,
     * null value often means "don't know", so in some occasion we want to consider two
     * metadata as different only if an attribute value is know for sure to be different.
     *
     * @param metadata1 The first metadata object to compare.
     * @param metadata2 The second metadata object to compare.
     * @param skipNulls If {@code true}, only non-null values will be compared.
     */
    public boolean shallowEquals(final Object metadata1, final Object metadata2, final boolean skipNulls) {
        assert type.isInstance(metadata1) : metadata1;
        assert type.isInstance(metadata2) : metadata2;
        for (int i=0; i<getters.length; i++) {
            final Method  method = getters[i];
            final Object  value1 = get(method, metadata1);
            final Object  value2 = get(method, metadata2);
            final boolean empty1 = isEmpty(value1);
            final boolean empty2 = isEmpty(value2);
            if (empty1 && empty2) {
                continue;
            }
            if (!Utilities.equals(value1, value2)) {
                if (!skipNulls || (!empty1 && !empty2)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Copies all metadata from source to target. The source can be any implementation of
     * the metadata interface, but the target must be the implementation expected by this
     * class.
     *
     * @param  source The metadata to copy.
     * @param  target The target metadata.
     * @param  skipNulls If {@code true}, only non-null values will be copied.
     * @return {@code true} in case of success, or {@code false} if at least
     *         one setter method was not found.
     * @throws UnmodifiableMetadataException if the target metadata is unmodifiable.
     */
    public boolean shallowCopy(final Object source, final Object target, final boolean skipNulls)
            throws UnmodifiableMetadataException
    {
        boolean success = true;
        assert type          .isInstance(source) : source;
        assert implementation.isInstance(target) : target;
        final Object[] arguments = new Object[1];
        for (int i=0; i<getters.length; i++) {
            final Method getter = getters[i];
            arguments[0] = get(getter, source);
            if (!skipNulls || !isEmpty(arguments[0])) {
                if (setters == null) {
                    return false;
                }
                final Method setter = setters[i];
                if (setter != null) {
                    set(getter, setter, target, arguments);
                } else {
                    success = false;
                }
            }
        }
        return success;
    }

    /**
     * Replaces every properties in the specified metadata by their
     * {@linkplain ModifiableMetadata#unmodifiable unmodifiable variant.
     */
    final void freeze(final Object metadata) {
        assert implementation.isInstance(metadata) : metadata;
        if (setters != null) {
            final Object[] arguments = new Object[1];
            for (int i=0; i<getters.length; i++) {
                final Method setter = setters[i];
                if (setter != null) {
                    final Method getter = getters[i];
                    final Object source = get(getter, metadata);
                    final Object target = ModifiableMetadata.unmodifiable(source);
                    if (source != target) {
                        arguments[0] = target;
                        set(getter, setter, metadata, arguments);
                    }
                }
            }
        }
    }

    /**
     * Returns {@code true} if the metadata is modifiable. This method is not public because it
     * uses heuristic rules. In case of doubt, this method conservatively returns {@code true}.
     */
    final boolean isModifiable() {
        if (setters != null) {
            return true;
        }
        for (int i=0; i<getters.length; i++) {
            // Immutable objects usually don't need to be cloned. So if
            // an object is cloneable, it is probably not immutable.
            if (Cloneable.class.isAssignableFrom(getters[i].getReturnType())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a hash code for the specified metadata. The hash code is defined as the
     * sum of hash code values of all non-null properties. This is the same contract than
     * {@link java.util.Set#hashCode} and ensure that the hash code value is insensitive
     * to the ordering of properties.
     */
    public int hashCode(final Object metadata) {
        assert type.isInstance(metadata) : metadata;
        int code = 0;
        for (int i=0; i<getters.length; i++) {
            final Object value = get(getters[i], metadata);
            if (!isEmpty(value)) {
                code += value.hashCode();
            }
        }
        return code;
    }

    /**
     * Counts the number of non-null properties.
     */
    public int count(final Object metadata, final int max) {
        assert type.isInstance(metadata) : metadata;
        int count = 0;
        for (int i=0; i<getters.length; i++) {
            if (!isEmpty(get(getters[i], metadata))) {
                if (++count >= max) {
                    break;
                }
            }
        }
        return count;
    }

    /**
     * Returns {@code true} if the specified object is null or an empty collection,
     * array or string.
     */
    static boolean isEmpty(final Object value) {
        return value == null ||
                ((value instanceof Collection) && ((Collection) value).isEmpty()) ||
                ((value instanceof CharSequence) && value.toString().trim().length() == 0) ||
                (value.getClass().isArray() && Array.getLength(value) == 0);
    }
}
