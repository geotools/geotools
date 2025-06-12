/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.util;

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.geotools.api.annotation.UML;

/**
 * Base class for all code lists. Subclasses shall provides a {@code values()} method which returns all {@code CodeList}
 * element in an array of the appropriate class. Code list are extensible, i.e. invoking the public constructor in any
 * subclass will automatically add the newly created {@code CodeList} element in the array to be returned by
 * {@code values()}.
 *
 * @param <E> The type of this code list.
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
public abstract class CodeList<E extends CodeList<E>> implements Comparable<E>, Serializable {
    /** Serial number for compatibility with different versions. */
    private static final long serialVersionUID = 5655809691319522885L;

    /** The values for each code list. */
    private static final Map<Class<? extends CodeList>, Collection<? extends CodeList>> VALUES = new HashMap<>();

    /** The types expected in constructors. */
    @SuppressWarnings({"unchecked", "PMD.UseShortArrayInitializer"})
    private static final Class<String>[] CONSTRUCTOR_PARAMETERS = new Class[] {String.class};

    /** The code value. */
    private final transient int ordinal;

    /** The code name. */
    private final String name;

    /**
     * The identifier declared in the {@link UML} annotation, or an empty string if there is no such annotation or if
     * the annotation contains an empty string. This field will be computed only when first needed.
     */
    private transient String identifier;

    /**
     * Creates a new code list element and add it to the given collection. Subclasses will typically give a static
     * reference to an {@link java.util.ArrayList} for the {@code values} argument. This list is used for
     * {@code values()} method implementations.
     *
     * @param name The code name.
     * @param values The collection to add the element to.
     */
    @SuppressWarnings("unchecked")
    protected CodeList(String name, final Collection<E> values) {
        this.name = name;
        synchronized (values) {
            this.ordinal = values.size();
            if (!values.add((E) this)) {
                throw new IllegalArgumentException("Duplicated value: " + name);
            }
        }
        final Class<? extends CodeList> codeType = getClass();
        synchronized (VALUES) {
            final Collection<? extends CodeList> previous = VALUES.put(codeType, values);
            if (previous != null && previous != values) {
                VALUES.put(codeType, previous); // Roll back
                throw new IllegalArgumentException("List already exists: " + values);
            }
        }
    }

    /**
     * Returns the code of the given type that matches the given name, or returns a new one if none match it. More
     * specifically, this methods returns the first element of the given class where <code>
     * {@linkplain #matches matches}(name)</code> returned {@code true}. If no such element is found, then a new
     * instance is created using the constructor expecting a single {@link String} argument.
     *
     * @param <T> The compile-time type given as the {@code codeType} parameter.
     * @param codeType The type of code list.
     * @param name The name of the code to obtain.
     * @return A code matching the given name.
     */
    public static <T extends CodeList> T valueOf(final Class<T> codeType, String name) {
        if (name == null) {
            return null;
        }
        name = name.trim();
        final Collection<? extends CodeList> values;
        synchronized (VALUES) {
            values = VALUES.get(codeType);
            if (values == null) {
                if (codeType == null) {
                    throw new IllegalArgumentException("Code type is null");
                } else {
                    throw new IllegalStateException("No collection of " + codeType.getSimpleName());
                }
            }
        }
        synchronized (values) {
            for (final CodeList code : values) {
                if (code.matches(name)) {
                    return codeType.cast(code);
                }
            }
            try {
                final Constructor<T> constructor = codeType.getDeclaredConstructor(CONSTRUCTOR_PARAMETERS);
                constructor.setAccessible(true);
                return constructor.newInstance(name);
            } catch (Exception exception) {
                throw new IllegalArgumentException("Can't create code of type " + codeType.getSimpleName(), exception);
            }
        }
    }

    /**
     * Returns the ordinal of this code constant. This is its position in its elements declaration, where the initial
     * constant is assigned an ordinal of zero.
     *
     * @return The position of this code constants in elements declaration.
     */
    public final int ordinal() {
        return ordinal;
    }

    /**
     * Returns the identifier declared in the {@link UML} annotation, or {@code null} if none. The UML identifier shall
     * be the ISO or OGC name for this code constant.
     *
     * @return The ISO/OGC identifier for this code constant, or {@code null} if none.
     * @since GeoAPI 2.2
     */
    public String identifier() {
        // Save the field in a local variable for protection against concurrent change (this
        // operation is garanteed atomic according Java specification). We don't synchronize
        // since it is not a problem if this method is executed twice in concurrent threads.
        String identifier = this.identifier;
        if (identifier == null) {
            final Class<? extends CodeList> codeType = getClass();
            Field field;
            try {
                field = codeType.getField(name);
            } catch (NoSuchFieldException e) {
                // There is no field for a code of this name. It may be normal, since the user
                // may have created a custom CodeList without declaring it as a constant.
                field = null;
            }
            if (field != null && Modifier.isStatic(field.getModifiers())) {
                final Object value;
                try {
                    value = field.get(null);
                } catch (IllegalAccessException e) {
                    // Should never happen since getField(String) returns only public fields.
                    throw new AssertionError(e);
                }
                if (equals(value)) {
                    final UML annotation = field.getAnnotation(UML.class);
                    if (annotation != null) {
                        identifier = annotation.identifier();
                    }
                }
            }
            if (identifier == null) {
                identifier = "";
            }
            this.identifier = identifier;
        }
        return identifier.length() != 0 ? identifier : null;
    }

    /**
     * Returns the name of this code list constant.
     *
     * @return The name of this code constant.
     */
    public final String name() {
        return name;
    }

    /**
     * Returns {@code true} if the given name matches the {@linkplain #name name}, {@linkplain #identifier identifier}
     * or any other identification string of this code list element. The comparison is case-insensitive.
     *
     * @param name The name to check.
     * @return {@code true} if the given name matches the code name or identifier.
     * @since GeoAPI 2.2
     */
    public boolean matches(String name) {
        if (name == null) {
            return false;
        }
        if (name.equalsIgnoreCase(this.name)) {
            return true;
        }
        final String identifier = identifier();
        return (identifier != null) && name.equalsIgnoreCase(identifier);
    }

    /**
     * Returns the list of codes of the same kind than this code.
     *
     * @return The codes of the same kind than this code.
     */
    public abstract E[] family();

    /**
     * Compares this code with the specified object for order. Returns a negative integer, zero, or a positive integer
     * as this object is less than, equal to, or greater than the specified object.
     *
     * <p>Code list constants are only comparable to other code list constants of the same type. The natural order
     * implemented by this method is the order in which the constants are declared.
     *
     * @param other The code constant to compare with this code.
     * @return -1 if the given code is less than this code, +1 if greater or 0 if equal.
     */
    @Override
    public final int compareTo(final E other) {
        final Class<? extends CodeList> ct = this.getClass();
        final Class<? extends CodeList> co = other.getClass();
        if (!ct.equals(co)) {
            throw new ClassCastException("Can't compare " + ct.getSimpleName() + " to " + co.getSimpleName());
        }
        return ordinal - ((CodeList) other).ordinal;
    }

    /**
     * Compares the specified object with this code list for equality. This method compares only {@linkplain #ordinal
     * ordinal} values for consistency with the {@link #compareTo} method. Ordinal values are unique for each code list
     * element of the same class.
     *
     * @param object The object to compare with this code.
     * @return {@code true} if the given object is equals to this code.
     * @since GeoAPI 2.2
     */
    @Override
    public final boolean equals(final Object object) {
        if (object != null && object.getClass().equals(getClass())) {
            return ordinal == ((CodeList) object).ordinal;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ordinal);
    }

    /** Returns a string representation of this code list. */
    @Override
    public String toString() {
        return getClass().getSimpleName() + '[' + name + ']';
    }

    /**
     * Resolves the code list to an unique instance after deserialization. The instance is resolved using its
     * {@linkplain #name() name} only (not its {@linkplain #ordinal() ordinal}).
     *
     * @return This code list as an unique instance.
     * @throws ObjectStreamException if the deserialization failed.
     */
    protected Object readResolve() throws ObjectStreamException {
        final Class<? extends CodeList> codeType = getClass();
        final Collection<? extends CodeList> values;
        synchronized (VALUES) {
            values = VALUES.get(codeType);
        }
        if (values != null) {
            synchronized (values) {
                for (final CodeList code : values) {
                    if (!codeType.isInstance(code)) {
                        // Paranoiac check - should never happen unless the subclass
                        // modifies itself the collection given to the constructor,
                        // in which case we will not touch it.
                        return this;
                    }
                    if (code.matches(name)) {
                        return code;
                    }
                }
                // We have verified with codeType.isInstance(code) that every elements are
                // of the appropriate class. This is the best we can do for type safety.
                @SuppressWarnings("unchecked")
                final Collection<CodeList> unsafe = (Collection) values;
                if (!unsafe.add(this)) {
                    // Paranoiac check - should never happen.
                    throw new InvalidObjectException(name);
                }
            }
        }
        return this;
    }
}
