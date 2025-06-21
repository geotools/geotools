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
package org.geotools.util;

import java.text.MessageFormat;
import java.util.Date;
import org.geotools.metadata.i18n.ErrorKeys;

/**
 * A central place to register transformations between an arbitrary class and a {@link Number}. For example, it is
 * sometime convenient to consider {@link Date} objects as if they were {@link Long} objects for computation purpose in
 * generic algorithms. Client can call the following method to convert an arbitrary object to a {@link Number}:
 *
 * <blockquote>
 *
 * <pre>
 * Object someArbitraryObject = new Date();
 * Number myObjectAsANumber = {@link ClassChanger#toNumber ClassChanger.toNumber}(someArbitraryObject);
 * </pre>
 *
 * </blockquote>
 *
 * @since 2.0
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
@SuppressWarnings("PMD.UseShortArrayInitializer")
public abstract class ClassChanger<S extends Comparable<S>, T extends Number> {
    /** Wrapper classes sorted by their wide. */
    @SuppressWarnings("unchecked")
    private static final Class<? extends Number>[] TYPES_BY_SIZE =
            new Class[] {Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class};

    /**
     * A list of class objects that can be converted to numbers. This list is initialized to a few commons
     * {@link ClassChanger} instances for some standard Java classes like {@link Date}. More objects can be added
     * dynamically. This list must be <u>ordered</u>: subclasses must be listed before parent classes.
     */
    private static ClassChanger<?, ?>[] changers = new ClassChanger[] {
        new ClassChanger<>(Date.class, Long.class) {
            @Override
            protected Long convert(final Date object) {
                return object.getTime();
            }

            @Override
            protected Date inverseConvert(final Long value) {
                return new Date(value.longValue());
            }
        }
    };

    /** Parent class for {@link #convert}'s input objects. */
    private final Class<S> source;

    /** Parent class for {@link #convert}'s output objects. */
    private final Class<T> target;

    /**
     * Constructs a new class changer.
     *
     * @param source Parent class for {@link #convert}'s input objects.
     * @param target Parent class for {@link #convert}'s output objects.
     */
    protected ClassChanger(final Class<S> source, final Class<T> target) {
        this.source = source;
        this.target = target;
        if (!Comparable.class.isAssignableFrom(source)) {
            throw new IllegalArgumentException(String.valueOf(source));
        }
        if (!Number.class.isAssignableFrom(target)) {
            throw new IllegalArgumentException(String.valueOf(target));
        }
    }

    /**
     * Returns the numerical value for an object.
     *
     * @param object Object to convert (may be null).
     * @return The object's numerical value.
     * @throws ClassCastException if {@code object} is not of the expected class.
     */
    protected abstract T convert(final S object) throws ClassCastException;

    /**
     * Returns an instance of the converted classe from a numerical value.
     *
     * @param value The value to wrap.
     * @return An instance of the source classe.
     */
    protected abstract S inverseConvert(final T value);

    /** Returns a string representation for this class changer. */
    @Override
    public String toString() {
        return "ClassChanger[" + source.getName() + "\u00A0\u21E8\u00A0" + target.getName() + ']';
    }

    /**
     * Registers a new converter. All registered {@link ClassChanger} will be taken in account by the {@link #toNumber}
     * method. The example below register a conversion for the {@link Date} class:
     *
     * <blockquote>
     *
     * <pre>
     * &nbsp;ClassChanger.register(new ClassChanger(Date.class, Long.class) {
     * &nbsp;    protected Long convert(final Comparable o) {
     * &nbsp;        return ((Date) o).getTime();
     * &nbsp;    }
     * &nbsp;
     * &nbsp;    protected Comparable inverseConvert(final Number number) {
     * &nbsp;        return new Date(number.longValue());
     * &nbsp;    }
     * &nbsp;});
     * </pre>
     *
     * </blockquote>
     *
     * @param converter The {@link ClassChanger} to add.
     * @throws IllegalStateException if an other {@link ClassChanger} was already registered for the same {@code source}
     *     class. This is usually not a concern since the registration usually take place during the class
     *     initialization ("static" constructor).
     */
    public static synchronized void register(final ClassChanger<?, ?> converter) throws IllegalStateException {
        int i;
        for (i = 0; i < changers.length; i++) {
            if (changers[i].source.isAssignableFrom(converter.source)) {
                /*
                 * We found a converter for a parent class. The new converter should be
                 * inserted before its parent.  But before the insertion, we will check
                 * if this converter was not already registered later in the array.
                 */
                for (int j = i; j < changers.length; j++) {
                    if (changers[j].source.equals(converter.source)) {
                        throw new IllegalStateException(changers[j].toString());
                    }
                }
                break;
            }
        }
        changers = XArray.insert(changers, i, 1);
        changers[i] = converter;
    }

    /**
     * Returns the class changer for the specified classe.
     *
     * @param source The class.
     * @return The class changer for the specified class.
     * @throws ClassNotFoundException if {@code source} is not a registered class.
     */
    private static synchronized <S extends Comparable<S>> ClassChanger<S, ?> getClassChanger(final Class<S> source)
            throws ClassNotFoundException {
        for (final ClassChanger<?, ?> candidate : changers) {
            if (candidate.source.isAssignableFrom(source)) {
                @SuppressWarnings("unchecked")
                final ClassChanger<S, ?> c = (ClassChanger<S, ?>) candidate;
                return c;
            }
        }
        throw new ClassNotFoundException(source.getName());
    }

    /**
     * Returns the target class for the specified source class, if a suitable transformation is known. The source class
     * is a {@link Comparable} subclass that will be specified as input to {@link #convert}. The target class is a
     * {@link Number} subclass that will be returned as output by {@link #convert}. If no suitable mapping is found,
     * then {@code source} is returned.
     */
    public static synchronized Class<?> getTransformedClass(final Class<?> source) {
        if (source != null) {
            for (ClassChanger<?, ?> changer : changers) {
                if (changer.source.isAssignableFrom(source)) {
                    return changer.target;
                }
            }
        }
        return source;
    }

    /**
     * Returns the numeric value for the specified object. For example the code <code>
     * toNumber(new&nbsp;Date())</code> returns the {@link Date#getTime()} value of the specified date object as a
     * {@link Long}.
     *
     * @param object Object to convert (may be null).
     * @return {@code null} if {@code object} was null; otherwise {@code object} if the supplied object is already an
     *     instance of {@link Number}; otherwise a new number with the numerical value.
     * @throws ClassNotFoundException if {@code object} is not an instance of a registered class.
     */
    @SuppressWarnings("unchecked")
    public static <K> K toNumber(final Comparable<K> object) throws ClassNotFoundException {
        if (object != null) {
            if (object instanceof Number) {
                return (K) object;
            }
            ClassChanger changer = getClassChanger(object.getClass());
            return (K) changer.convert(object);
        }
        return null;
    }

    /**
     * Wraps the specified number as an instance of the specified classe. For example <code>
     * toComparable(new&nbsp;Long(time),&nbsp;Date.class)</code> is equivalent to <code>
     * new&nbsp;Date(time)</code>. There is of course no point to use this method if the destination class is know at
     * compile time. This method is useful for creating instance of classes choosen dynamically at run time.
     *
     * @param value The numerical value (may be null).
     * @param classe The desired classe for return value.
     * @throws ClassNotFoundException if {@code classe} is not a registered class.
     */
    @SuppressWarnings("unchecked")
    public static <C extends Comparable> C toComparable(final Number value, final Class<C> classe)
            throws ClassNotFoundException {
        if (value != null) {
            if (Number.class.isAssignableFrom(classe)) {
                return classe.cast(value);
            }
            ClassChanger changer = getClassChanger(classe);
            final Comparable<?> c = changer.inverseConvert(value);
            return classe.cast(c);
        }
        return null;
    }

    /**
     * Converts a wrapper class to a primitive class. For example this method converts <code>
     * {@linkplain Double}.class</code> to <code>Double.{@linkplain Double#TYPE TYPE}</code>.
     *
     * @param c The wrapper class.
     * @return The primitive class.
     * @throws IllegalArgumentException if the specified class is not a wrapper for a primitive.
     */
    public static Class<?> toPrimitive(final Class<?> c) throws IllegalArgumentException {
        if (Double.class.equals(c)) return Double.TYPE;
        if (Float.class.equals(c)) return Float.TYPE;
        if (Long.class.equals(c)) return Long.TYPE;
        if (Integer.class.equals(c)) return Integer.TYPE;
        if (Short.class.equals(c)) return Short.TYPE;
        if (Byte.class.equals(c)) return Byte.TYPE;
        if (Boolean.class.equals(c)) return Boolean.TYPE;
        if (Character.class.equals(c)) return Character.TYPE;
        throw unknownType(c);
    }

    /**
     * Converts a primitive class to a wrapper class. For example this method converts <code>
     * Double.{@linkplain Double#TYPE TYPE}</code> to <code>{@linkplain Double}.class</code>.
     *
     * @param c The primitive class.
     * @return The wrapper class.
     * @throws IllegalArgumentException if the specified class is not a primitive.
     */
    public static Class<?> toWrapper(final Class<?> c) throws IllegalArgumentException {
        if (Double.TYPE.equals(c)) return Double.class;
        if (Float.TYPE.equals(c)) return Float.class;
        if (Long.TYPE.equals(c)) return Long.class;
        if (Integer.TYPE.equals(c)) return Integer.class;
        if (Short.TYPE.equals(c)) return Short.class;
        if (Byte.TYPE.equals(c)) return Byte.class;
        if (Boolean.TYPE.equals(c)) return Boolean.class;
        if (Character.TYPE.equals(c)) return Character.class;
        throw unknownType(c);
    }

    /**
     * Casts the number to the specified class. The class must by one of {@link Byte}, {@link Short}, {@link Integer},
     * {@link Long}, {@link Float} or {@link Double}.
     */
    @SuppressWarnings("unchecked")
    public static <N extends Number> N cast(final Number n, final Class<N> c) throws IllegalArgumentException {
        if (n == null || n.getClass().equals(c)) {
            return (N) n;
        }
        if (Byte.class.equals(c)) return (N) Byte.valueOf(n.byteValue());
        if (Short.class.equals(c)) return (N) Short.valueOf(n.shortValue());
        if (Integer.class.equals(c)) return (N) Integer.valueOf(n.intValue());
        if (Long.class.equals(c)) return (N) Long.valueOf(n.longValue());
        if (Float.class.equals(c)) return (N) Float.valueOf(n.floatValue());
        if (Double.class.equals(c)) return (N) Double.valueOf(n.doubleValue());
        throw unknownType(c);
    }

    /**
     * Returns the class of the widest type. Numbers {@code n1} and {@code n2} must be instance of any of {@link Byte},
     * {@link Short}, {@link Integer}, {@link Long}, {@link Float} or {@link Double} types. At most one of the argument
     * can be null.
     */
    public static Class<? extends Number> getWidestClass(final Number n1, final Number n2)
            throws IllegalArgumentException {
        return getWidestClass(n1 != null ? n1.getClass() : null, n2 != null ? n2.getClass() : null);
    }

    /**
     * Returns the class of the widest type. Classes {@code c1} and {@code c2} must be of any of {@link Byte},
     * {@link Short}, {@link Integer}, {@link Long}, {@link Float} or {@link Double} types. At most one of the argument
     * can be null.
     */
    public static Class<? extends Number> getWidestClass(
            final Class<? extends Number> c1, final Class<? extends Number> c2) throws IllegalArgumentException {
        if (c1 == null) return c2;
        if (c2 == null) return c1;
        return TYPES_BY_SIZE[Math.max(getRank(c1), getRank(c2))];
    }

    /**
     * Returns the class of the finest type. Classes {@code c1} and {@code c2} must be of any of {@link Byte},
     * {@link Short}, {@link Integer}, {@link Long}, {@link Float} or {@link Double} types. At most one of the argument
     * can be null.
     */
    public static Class<? extends Number> getFinestClass(
            final Class<? extends Number> c1, final Class<? extends Number> c2) throws IllegalArgumentException {
        if (c1 == null) return c2;
        if (c2 == null) return c1;
        return TYPES_BY_SIZE[Math.min(getRank(c1), getRank(c2))];
    }

    /** Returns the smallest class capable to hold the specified value. */
    public static Class<? extends Number> getFinestClass(final double value) {
        final long lg = (long) value;
        if (value == lg) {
            if (lg >= Byte.MIN_VALUE && lg <= Byte.MAX_VALUE) return Byte.class;
            if (lg >= Short.MIN_VALUE && lg <= Short.MAX_VALUE) return Short.class;
            if (lg >= Integer.MIN_VALUE && lg <= Integer.MAX_VALUE) return Integer.class;
            return Long.class;
        }
        final float fv = (float) value;
        if (value == fv) {
            return Float.class;
        }
        return Double.class;
    }

    /** Returns the rank (in the {@link #TYPES_BY_SIZE} array) of the specified class. */
    private static int getRank(final Class<? extends Number> c) throws IllegalArgumentException {
        for (int i = 0; i < TYPES_BY_SIZE.length; i++) {
            if (TYPES_BY_SIZE[i].isAssignableFrom(c)) {
                return i;
            }
        }
        throw unknownType(c);
    }

    /** Returns an exception for an unnkown type. */
    private static IllegalArgumentException unknownType(final Class<?> type) {
        return new IllegalArgumentException(MessageFormat.format(ErrorKeys.UNKNOW_TYPE_$1, type));
    }
}
