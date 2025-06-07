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

import static org.geotools.util.Classes.BYTE;
import static org.geotools.util.Classes.CHARACTER;
import static org.geotools.util.Classes.DOUBLE;
import static org.geotools.util.Classes.FLOAT;
import static org.geotools.util.Classes.INTEGER;
import static org.geotools.util.Classes.LONG;
import static org.geotools.util.Classes.SHORT;
import static org.geotools.util.Classes.getEnumConstant;
import static org.geotools.util.Classes.wrapperToPrimitive;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.MessageFormat;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import org.geotools.api.util.Cloneable;
import org.geotools.metadata.i18n.ErrorKeys;

/**
 * An ordered set of ranges. {@code RangeSet} objects store an arbitrary number of {@linkplain Range ranges} in any
 * Java's primitives ({@code int}, {@code float}, etc.) or any {@linkplain Comparable comparable} objects. Ranges may be
 * added in any order. When a range is added, {@code RangeSet} first looks for an existing range overlapping the
 * specified range. If an overlapping range is found, ranges are merged as of {@link Range#union}. Consequently, ranges
 * returned by {@link #iterator} may not be the same than added ranges.
 *
 * <p>All entries in this set can be seen as {@link Range} objects. This class is <strong>not</strong> thread-safe.
 *
 * @param <T> The type of range elements.
 * @since 2.0
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Andrea Aime
 */
@SuppressWarnings("JdkObsolete") // SortedSet interface preserved for API compatibility
public class RangeSet<T extends Comparable<? super T>> extends AbstractSet<Range<T>>
        implements SortedSet<Range<T>>, Cloneable, Serializable {
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = 2439002271813328080L;

    /**
     * The comparator for ranges. Defined only in order to comply to {@link #comparator} contract, but not used for
     * internal working in this class.
     */
    @SuppressWarnings("unchecked")
    private static final Comparator<Range> COMPARATOR = (r1, r2) -> {
        int cmin = r1.getMinValue().compareTo(r2.getMinValue());
        int cmax = r1.getMaxValue().compareTo(r2.getMaxValue());
        if (cmin == 0) cmin = (r1.isMinIncluded() ? -1 : 0) - (r2.isMinIncluded() ? -1 : 0);
        if (cmax == 0) cmax = (r1.isMaxIncluded() ? +1 : 0) - (r2.isMaxIncluded() ? +1 : 0);
        if (cmin == cmax) return cmax; // Easy case: min and max are both greater, smaller or eq.
        if (cmin == 0) return cmax; // Easy case: only max value differ.
        if (cmax == 0) return cmin; // Easy case: only min value differ.
        // One range is included in the other.
        throw new IllegalArgumentException("Unordered ranges");
    };

    /** The {@linkplain #getElementClass element class} of ranges. */
    private final Class<T> elementClass;

    /**
     * Identical to {@code elementClass} except if the later is a {@link Number} subclass. In the later case, this field
     * is set to <code>{@link Number}.class</code>.
     */
    private final Class<?> relaxedClass;

    /**
     * Identical to {@code elementClass} except if the later is the wrapper of some primitive type. In the later case
     * this field is set to that primitive type. This is the type to be used in arrays.
     */
    private final Class<?> arrayElementClass;

    /**
     * The primitive type, as one of {@code DOUBLE}, {@code FLOAT}, {@code LONG}, {@code INTEGER}, {@code SHORT},
     * {@code BYTE}, {@code CHARACTER} or {@code OTHER} enumeration.
     */
    private final byte arrayElementCode;

    /**
     * Tableau d'intervalles. Il peut s'agir d'un tableau d'un des types primitifs du Java (par exemple {@code int[]} ou
     * {@code float[]}), ou d'un tableau de type {@code Comparable[]}. Les éléments de ce tableau doivent
     * obligatoirement être en ordre strictement croissant et sans doublon.
     *
     * <p>La longueur de ce tableau est le double du nombre d'intervalles. Il aurait été plus efficace d'utiliser une
     * variable séparée (pour ne pas être obligé d'agrandir ce tableau à chaque ajout d'un intervalle), mais
     * malheureusement le J2SE 1.4 ne nous fournit pas de méthode {@code Arrays.binarySearch} qui nous permettent de
     * spécifier les limites du tableau (voir RFE #4306897 à
     * http://developer.java.sun.com/developer/bugParade/bugs/4306897.html).
     *
     * @todo Revisit when we will be allowed to compile for Java 6.
     */
    private Object array;

    /**
     * Compte le nombre de modifications apportées au tableau des intervalles. Ce comptage sert à vérifier si une
     * modification survient pendant qu'un itérateur balayait les intervalles.
     */
    private int modCount;

    /**
     * {@code true} if and only if the element class represents a primitive type. This is equivalents to
     * {@code primitiveType.isPrimitive()} and is computed once for ever for performance reason.
     */
    private final boolean isPrimitive;

    /**
     * {@code true} if we should invoke {@link ClassChanger#toNumber} before to store a value into the array. It will be
     * the case if the array {@code array} contains primitive elements and the type {@code type} is not the
     * corresponding wrapper.
     */
    private final boolean useClassChanger;

    /** {@code true} if instances of {@link NumberRange} should be created instead of {@link Range}. */
    private final boolean isNumeric;

    /**
     * Constructs an empty set of range.
     *
     * @param type The class of the range elements. It must be a primitive type or a class implementing
     *     {@link Comparable}.
     * @throws IllegalArgumentException if {@code type} is not a primitive type or a class implementing
     *     {@link Comparable}.
     */
    public RangeSet(final Class<T> type) throws IllegalArgumentException {
        if (!Comparable.class.isAssignableFrom(type)) {
            throw new IllegalArgumentException(MessageFormat.format(ErrorKeys.NOT_COMPARABLE_CLASS_$1, type));
        }
        Class<?> elementType = ClassChanger.getTransformedClass(type); // e.g. change Date --> Long
        useClassChanger = (elementType != type);
        elementClass = type;
        arrayElementClass = wrapperToPrimitive(elementType);
        arrayElementCode = getEnumConstant(arrayElementClass);
        isPrimitive = arrayElementClass.isPrimitive();
        isNumeric = Number.class.isAssignableFrom(type);
        relaxedClass = isNumeric ? Number.class : type;
    }

    /** Converts a value from an arbitrary type to the wrapper of {@link #arrayElementClass}. */
    @SuppressWarnings("unchecked")
    private <R> Comparable<R> toArrayElement(Comparable<R> value) {
        if (!relaxedClass.isInstance(value)) {
            throw new IllegalArgumentException(
                    value == null
                            ? MessageFormat.format(ErrorKeys.NULL_ARGUMENT_$1, "value")
                            : MessageFormat.format(ErrorKeys.ILLEGAL_CLASS_$2, value.getClass(), elementClass));
        }
        if (useClassChanger)
            try {
                value = (Comparable) ClassChanger.toNumber(value);
            } catch (ClassNotFoundException cause) {
                /*
                 * Should not happen since the constructor should have make sure
                 * that this operation is legal for value of class 'type'.
                 */
                final ClassCastException exception = new ClassCastException(
                        MessageFormat.format(ErrorKeys.ILLEGAL_CLASS_$2, value.getClass(), elementClass));
                exception.initCause(cause);
                throw exception;
            }
        return value;
    }

    /** Returns the comparator associated with this sorted set. */
    @Override
    @SuppressWarnings("unchecked") // Because we share the same static COMPARATOR instance.
    public Comparator<Range<T>> comparator() {
        return (Comparator) COMPARATOR;
    }

    /** Remove all elements from this set of ranges. */
    @Override
    public void clear() {
        array = null;
        modCount++;
    }

    /** Returns the number of ranges in this set. */
    @Override
    public int size() {
        return (array != null) ? Array.getLength(array) / 2 : 0;
    }

    /**
     * Add a range to this set. Range may be added in any order. If the specified range overlap an existing range, the
     * two range will be merged as of {@link Range#union}.
     *
     * <p>Note: current version do not support open interval (i.e. {@code Range.is[Min/Max]Included()} must return
     * {@code true}).
     *
     * @param range The range to add.
     * @return {@code true} if this set changed as a result of the call.
     * @todo support open intervals.
     */
    @Override
    public boolean add(final Range<T> range) {
        if (!range.isMinIncluded() || !range.isMaxIncluded()) {
            throw new UnsupportedOperationException("Open interval not yet supported");
        }
        return add(range.getMinValue(), range.getMaxValue());
    }

    /**
     * Adds a range of values to this set. Range may be added in any order. If the specified range overlap an existing
     * range, the two ranges will be merged.
     *
     * @param min The lower value, inclusive.
     * @param max The upper value, inclusive.
     * @return {@code true} if this set changed as a result of the call.
     * @throws IllegalArgumentException if {@code lower} is greater than {@code upper}.
     */
    @SuppressWarnings("unchecked")
    public <N> boolean add(final Comparable<? super N> min, final Comparable<? super N> max)
            throws IllegalArgumentException {
        Comparable lower = toArrayElement(min);
        Comparable upper = toArrayElement(max);
        if (lower.compareTo(upper) > 0) {
            throw new IllegalArgumentException(MessageFormat.format(ErrorKeys.BAD_RANGE_$2, min, max));
        }
        if (array == null) {
            modCount++;
            array = Array.newInstance(arrayElementClass, 2);
            Array.set(array, 0, lower);
            Array.set(array, 1, upper);
            return true;
        }
        final int modCountChk = modCount;
        int i0 = binarySearch(lower);
        int i1;
        if (i0 < 0) {
            /*
             * Si le début de la plage ne correspond pas à une des dates en
             * mémoire, il faudra l'insérer à quelque part dans le tableau.
             * Si la date tombe dans une des plages déjà existantes (si son
             * index est impair), on étend la date de début pour prendre le
             * début de la plage. Visuellement, on fait:
             *
             *   0   1     2      3     4   5    6     7
             *   #####     ########     #####    #######
             *             <---^           ^
             *             lower(i=3)   upper(i=5)
             */
            if (((i0 = ~i0) & 1) != 0) { // Attention: c'est ~ et non -
                lower = (Comparable) Array.get(array, --i0);
                i1 = binarySearch(upper);
            } else {
                /*
                 * Si la date de début ne tombe pas dans une plage déjà
                 * existante, il faut étendre la valeur de début qui se
                 * trouve dans le tableau. Visuellement, on fait:
                 *
                 *   0   1     2      3     4   5    6     7
                 *   #####  ***########     #####    #######
                 *          ^                 ^
                 *       lower(i=2)        upper(i=5)
                 */
                if (i0 != Array.getLength(array) && (i1 = binarySearch(upper)) != ~i0) {
                    modCount++;
                    Array.set(array, i0, lower);
                } else {
                    /*
                     * Un cas particulier se produit si la nouvelle plage
                     * est à insérer à la fin du tableau. Dans ce cas, on
                     * n'a qu'à agrandir le tableau et écrire les valeurs
                     * directement à la fin. Ce traitement est nécessaire
                     * pour eviter les 'ArrayIndexOutOfBoundsException'.
                     * Un autre cas particulier se produit si la nouvelle
                     * plage est  entièrement  comprise entre deux plages
                     * déjà existantes.  Le même code ci-dessous insèrera
                     * la nouvelle plage à l'index 'i0'.
                     */
                    modCount++;
                    final Object old = array;
                    final int length = Array.getLength(array);
                    array = Array.newInstance(arrayElementClass, length + 2);
                    System.arraycopy(old, 0, array, 0, i0);
                    System.arraycopy(old, i0, array, i0 + 2, length - i0);
                    Array.set(array, i0 + 0, lower);
                    Array.set(array, i0 + 1, upper);
                    return true;
                }
            }
        } else {
            i0 &= ~1;
            i1 = binarySearch(upper);
        }
        /*
         * A ce stade, on est certain que 'i0' est pair et pointe vers le début
         * de la plage dans le tableau. Fait maintenant le traitement pour 'i1'.
         */
        if (i1 < 0) {
            /*
             * Si la date de fin tombe dans une des plages déjà existantes
             * (si son index est impair), on l'étend pour pendre la fin de
             * la plage trouvée dans le tableau. Visuellement, on fait:
             *
             *   0   1     2      3     4   5    6     7
             *   #####     ########     #####    #######
             *             ^             ^-->
             *          lower(i=2)     upper(i=5)
             */
            if (((i1 = ~i1) & 1) != 0) { // Attention: c'est ~ et non -
                upper = (Comparable) Array.get(array, i1);
            } else {
                /*
                 * Si la date de fin ne tombe pas dans une plage déjà
                 * existante, il faut étendre la valeur de fin qui se
                 * trouve dans le tableau. Visuellement, on fait:
                 *
                 *   0   1     2      3     4   5    6     7
                 *   #####     ########     #####**  #######
                 *             ^                  ^
                 *          lower(i=2)         upper(i=6)
                 */
                modCount++;
                Array.set(array, --i1, upper);
            }
        } else {
            i1 |= 1;
        }
        /*
         * A ce stade, on est certain que 'i1' est impair et pointe vers la fin
         * de la plage dans le tableau. On va maintenant supprimer tout ce qui
         * se trouve entre 'i0' et 'i1', à l'exclusion de 'i0' et 'i1'.
         */
        assert (i0 & 1) == 0 : i0;
        assert (i1 & 1) != 0 : i1;
        final int n = i1 - ++i0;
        if (n > 0) {
            modCount++;
            final Object old = array;
            final int length = Array.getLength(array);
            array = Array.newInstance(arrayElementClass, length - n);
            System.arraycopy(old, 0, array, 0, i0);
            System.arraycopy(old, i1, array, i0, length - i1);
        }
        assert (Array.getLength(array) & 1) == 0;
        return modCountChk != modCount;
    }

    /**
     * Add a range of values to this set. Range may be added in any order. If the specified range overlap an existing
     * range, the two ranges will be merged.
     *
     * @param lower The lower value, inclusive.
     * @param upper The upper value, inclusive.
     * @return {@code true} if this set changed as a result of the call.
     * @throws IllegalArgumentException if {@code lower} is greater than {@code upper}.
     */
    public boolean add(byte lower, byte upper) throws IllegalArgumentException {
        return add(Byte.valueOf(lower), Byte.valueOf(upper));
    }

    /**
     * Add a range of values to this set. Range may be added in any order. If the specified range overlap an existing
     * range, the two ranges will be merged.
     *
     * @param lower The lower value, inclusive.
     * @param upper The upper value, inclusive.
     * @return {@code true} if this set changed as a result of the call.
     * @throws IllegalArgumentException if {@code lower} is greater than {@code upper}.
     */
    public boolean add(short lower, short upper) throws IllegalArgumentException {
        return add(Short.valueOf(lower), Short.valueOf(upper));
    }

    /**
     * Add a range of values to this set. Range may be added in any order. If the specified range overlap an existing
     * range, the two ranges will be merged.
     *
     * @param lower The lower value, inclusive.
     * @param upper The upper value, inclusive.
     * @return {@code true} if this set changed as a result of the call.
     * @throws IllegalArgumentException if {@code lower} is greater than {@code upper}.
     */
    public boolean add(int lower, int upper) throws IllegalArgumentException {
        return add(Integer.valueOf(lower), Integer.valueOf(upper));
    }

    /**
     * Add a range of values to this set. Range may be added in any order. If the specified range overlap an existing
     * range, the two ranges will be merged.
     *
     * @param lower The lower value, inclusive.
     * @param upper The upper value, inclusive.
     * @return {@code true} if this set changed as a result of the call.
     * @throws IllegalArgumentException if {@code lower} is greater than {@code upper}.
     */
    public boolean add(long lower, long upper) throws IllegalArgumentException {
        return add(Long.valueOf(lower), Long.valueOf(upper));
    }

    /**
     * Add a range of values to this set. Range may be added in any order. If the specified range overlap an existing
     * range, the two ranges will be merged.
     *
     * @param lower The lower value, inclusive.
     * @param upper The upper value, inclusive.
     * @return {@code true} if this set changed as a result of the call.
     * @throws IllegalArgumentException if {@code lower} is greater than {@code upper}.
     */
    public boolean add(float lower, float upper) throws IllegalArgumentException {
        return add(Float.valueOf(lower), Float.valueOf(upper));
    }

    /**
     * Add a range of values to this set. Range may be added in any order. If the specified range overlap an existing
     * range, the two ranges will be merged.
     *
     * @param lower The lower value, inclusive.
     * @param upper The upper value, inclusive.
     * @return {@code true} if this set changed as a result of the call.
     * @throws IllegalArgumentException if {@code lower} is greater than {@code upper}.
     */
    public boolean add(double lower, double upper) throws IllegalArgumentException {
        return add(Double.valueOf(lower), Double.valueOf(upper));
    }

    /**
     * Remove a range of values from this set. Range may be removed in any order.
     *
     * @param min The lower value to remove, exclusive.
     * @param max The upper value to remove, exclusive.
     * @return {@code true} if this set changed as a result of the call.
     * @throws IllegalArgumentException if {@code lower} is greater than {@code upper}.
     */
    // this class uses converters to adapt  types, cannot be type safe
    @SuppressWarnings("unchecked")
    public <N> boolean remove(final Comparable<? super N> min, final Comparable<? super N> max)
            throws IllegalArgumentException {
        Comparable lower = toArrayElement(min);
        Comparable upper = toArrayElement(max);
        if (lower.compareTo(upper) >= 0) {
            throw new IllegalArgumentException(MessageFormat.format(ErrorKeys.BAD_RANGE_$2, min, max));
        }
        // if already empty, or range outside the current set, nothing to change
        if (array == null) {
            return false;
        }
        final int modCountChk = modCount;
        int i0 = binarySearch(lower);
        int i1 = binarySearch(upper);
        if (i0 < 0) {
            if (((i0 = ~i0) & 1) != 0) { // Attention: c'est ~ et non -
                /*
                 * Si le début de la plage ne correspond pas à une des dates en mémoire,
                 * il faudra faire un trou à quelque part dans le tableau. Si la date tombe
                 * dans une des plages déjà existantes (si son index est impair), on change
                 * la date de fin de la plage existante. Visuellement, on fait:
                 *
                 *   0   1     2      3     4   5    6     7
                 *   #####     #####---     --###    #######
                 *                 ^          ^
                 *             lower(i=3)   upper(i=5)
                 */
                modCount++;
                if (i1 != ~i0) {
                    Array.set(array, i0, lower);
                } else {
                    /*
                     * Special case if the upper index is inside the same range than the lower one:
                     *
                     *   0   1     2                3     4   5
                     *   #####     ####---------#####     #####
                     *                ^         ^
                     *           lower(i=3)   upper(i=3)
                     */
                    final Object old = array;
                    final int length = Array.getLength(array);
                    array = Array.newInstance(arrayElementClass, length + 2);
                    System.arraycopy(old, 0, array, 0, i0);
                    System.arraycopy(old, i0, array, i0 + 2, length - i0);
                    Array.set(array, i0 + 0, lower);
                    Array.set(array, i0 + 1, upper);
                    return true;
                }
            } else {
                /*
                 * Si la date de début ne tombe pas dans une plage déjà
                 * existante, il faut prendre la date de fin de la plage
                 * précédente. Visuellement, on fait:
                 *
                 *   0   1     2      3     4   5    6     7
                 *   #####     ########     #####    #######
                 *       <---^                  ^
                 *       lower(i=2)        upper(i=5)
                 */
                i0--;
            }
        } else {
            if ((i0 & 1) == 0) {
                i0--;
            }
        }
        /*
         * A ce stade, on est certain que 'i0' est impair et pointe vers la fin
         * d'une plage dans le tableau. Fait maintenant le traitement pour 'i1'.
         */
        if (i1 < 0) {
            /*
             * Si la date de fin tombe dans une des plages déjà existantes
             * (si son index est impair), on change la date de début de la
             * plage existante. Visuellement, on fait:
             *
             *   0   1     2      3     4   5    6     7
             *   #####     ########     --###    #######
             *                    ^       ^
             *            lower(i=3)    upper(i=5)
             */
            if (((i1 = ~i1) & 1) != 0) { // Attention: c'est ~ et non -
                modCount++;
                Array.set(array, --i1, upper);
            }
            /*
             * Si la date de fin ne tombe pas dans une plage déjà existante, il
             * faudra (plus tard) supprimer les éventuelles plages qui le précède.
             *
             *   0   1     2      3        4     5        6         7
             *   #####     ########        #######        ###########
             *                    ^                  ^
             *            lower(i=3)         upper(i=6)
             */
        } else {
            i1 &= ~1;
        }
        /*
         * A ce stade, on est certain que 'i1' est pair et pointe vers la début
         * de la plage dans le tableau. On va maintenant supprimer tout ce qui
         * se trouve entre 'i0' et 'i1', à l'exclusion de 'i0' et 'i1'.
         */
        assert (i0 & 1) != 0 : i0;
        assert (i1 & 1) == 0 : i1;
        final int n = i1 - ++i0;
        if (n > 0) {
            modCount++;
            final Object old = array;
            final int length = Array.getLength(array);
            array = Array.newInstance(arrayElementClass, length - n);
            System.arraycopy(old, 0, array, 0, i0);
            System.arraycopy(old, i1, array, i0, length - i1);
        }
        assert (Array.getLength(array) & 1) == 0;
        return modCountChk != modCount;
    }

    /**
     * Remove a range of values from this set. Range may be removed in any order.
     *
     * @param lower The lower value to remove, exclusive.
     * @param upper The upper value to remove, exclusive.
     * @return {@code true} if this set changed as a result of the call.
     * @throws IllegalArgumentException if {@code lower} is greater than {@code upper}.
     */
    public boolean remove(byte lower, byte upper) throws IllegalArgumentException {
        return remove(Byte.valueOf(lower), Byte.valueOf(upper));
    }

    /**
     * Remove a range of values from this set. Range may be removed in any order.
     *
     * @param lower The lower value to remove, exclusive.
     * @param upper The upper value to remove, exclusive.
     * @return {@code true} if this set changed as a result of the call.
     * @throws IllegalArgumentException if {@code lower} is greater than {@code upper}.
     */
    public boolean remove(short lower, short upper) throws IllegalArgumentException {
        return remove(Short.valueOf(lower), Short.valueOf(upper));
    }

    /**
     * Remove a range of values from this set. Range may be removed in any order.
     *
     * @param lower The lower value to remove, exclusive.
     * @param upper The upper value to remove, exclusive.
     * @return {@code true} if this set changed as a result of the call.
     * @throws IllegalArgumentException if {@code lower} is greater than {@code upper}.
     */
    public boolean remove(int lower, int upper) throws IllegalArgumentException {
        return remove(Integer.valueOf(lower), Integer.valueOf(upper));
    }

    /**
     * Remove a range of values from this set. Range may be removed in any order.
     *
     * @param lower The lower value to remove, exclusive.
     * @param upper The upper value to remove, exclusive.
     * @return {@code true} if this set changed as a result of the call.
     * @throws IllegalArgumentException if {@code lower} is greater than {@code upper}.
     */
    public boolean remove(long lower, long upper) throws IllegalArgumentException {
        return remove(Long.valueOf(lower), Long.valueOf(upper));
    }

    /**
     * Remove a range of values from this set. Range may be removed in any order.
     *
     * @param lower The lower value to remove, exclusive.
     * @param upper The upper value to remove, exclusive.
     * @return {@code true} if this set changed as a result of the call.
     * @throws IllegalArgumentException if {@code lower} is greater than {@code upper}.
     */
    public boolean remove(float lower, float upper) throws IllegalArgumentException {
        return remove(Float.valueOf(lower), Float.valueOf(upper));
    }

    /**
     * Remove a range of values from this set. Range may be removed in any order.
     *
     * @param lower The lower value to remove, exclusive.
     * @param upper The upper value to remove, exclusive.
     * @return {@code true} if this set changed as a result of the call.
     * @throws IllegalArgumentException if {@code lower} is greater than {@code upper}.
     */
    public boolean remove(double lower, double upper) throws IllegalArgumentException {
        return remove(Double.valueOf(lower), Double.valueOf(upper));
    }

    /**
     * Retourne l'index de l'élément {@code value} dans le tableau {@code array}. Cette méthode interprète le tableau
     * {@code array} comme un tableau d'un des types intrinsèques du Java, et appelle la méthode
     * {@code Arrays.binarySearch} appropriée.
     *
     * @param value The value to search. This value must have been converted with {@link #toNumber} prior to call this
     *     method.
     */
    private int binarySearch(final Comparable value) {
        switch (arrayElementCode) {
            case DOUBLE:
                return Arrays.binarySearch((double[]) array, ((Number) value).doubleValue());
            case FLOAT:
                return Arrays.binarySearch((float[]) array, ((Number) value).floatValue());
            case LONG:
                return Arrays.binarySearch((long[]) array, ((Number) value).longValue());
            case INTEGER:
                return Arrays.binarySearch((int[]) array, ((Number) value).intValue());
            case SHORT:
                return Arrays.binarySearch((short[]) array, ((Number) value).shortValue());
            case BYTE:
                return Arrays.binarySearch((byte[]) array, ((Number) value).byteValue());
            case CHARACTER:
                return Arrays.binarySearch((char[]) array, ((Character) value).charValue());
            default:
                return Arrays.binarySearch((Object[]) array, value);
        }
    }

    /**
     * Returns a new {@link Range} object initialized with the given values.
     *
     * @param lower The lower value, inclusive.
     * @param upper The upper value, inclusive.
     */
    private Range<T> newRange(final T lower, final T upper) {
        if (isNumeric) {
            // verified it's a number in the if, but cannot make it type safe
            @SuppressWarnings("unchecked")
            Range<T> result = new NumberRange(elementClass, lower, upper);
            return result;
        } else {
            return new Range<>(elementClass, lower, upper);
        }
    }

    /** Returns the value at the specified index. Even index are lower bounds, while odd index are upper bounds. */
    private T get(final int index) {
        Comparable value = (Comparable) Array.get(array, index);
        if (useClassChanger)
            try {
                value = ClassChanger.toComparable((Number) value, elementClass);
            } catch (ClassNotFoundException exception) {
                // Should not happen, since class type should
                // have been checked by all 'add(...)' methods
                throw new IllegalStateException(exception);
            }
        return elementClass.cast(value);
    }

    /**
     * Returns a {@linkplain Range#getMinValue range's minimum value} as a {@code double}. The {@code index} can be any
     * value from 0 inclusive to the set's {@link #size size} exclusive. The returned values always increase with
     * {@code index}.
     *
     * @param index The range index, from 0 inclusive to {@link #size size} exclusive.
     * @return The minimum value for the range at the specified index.
     * @throws IndexOutOfBoundsException if {@code index} is out of bounds.
     * @throws ClassCastException if range elements are not convertible to numbers.
     */
    public final double getMinValueAsDouble(int index) throws IndexOutOfBoundsException, ClassCastException {
        index *= 2;
        return isPrimitive ? Array.getDouble(array, index) : ((Number) Array.get(array, index)).doubleValue();
    }

    /**
     * Returns a {@linkplain Range#getMaxValue range's maximum value} as a {@code double}. The {@code index} can be any
     * value from 0 inclusive to the set's {@link #size size} exclusive. The returned values always increase with
     * {@code index}.
     *
     * @param index The range index, from 0 inclusive to {@link #size size} exclusive.
     * @return The maximum value for the range at the specified index.
     * @throws IndexOutOfBoundsException if {@code index} is out of bounds.
     * @throws ClassCastException if range elements are not convertible to numbers.
     */
    public final double getMaxValueAsDouble(int index) throws IndexOutOfBoundsException, ClassCastException {
        index = 2 * index + 1;
        return isPrimitive ? Array.getDouble(array, index) : ((Number) Array.get(array, index)).doubleValue();
    }

    /**
     * If the specified value is inside a range, returns the index of this range. Otherwise, returns {@code -1}.
     *
     * @param value The value to search.
     * @return The index of the range which contains this value, or -1 if there is no such range.
     */
    public <R> int indexOfRange(final Comparable<R> value) {
        int index = binarySearch(toArrayElement(value));
        if (index < 0) {
            // Found an insertion point. Make sure that the insertion
            // point is inside a range (i.e. before the maximum value).
            index = ~index; // Tild sign, not minus.
            if ((index & 1) == 0) {
                return -1;
            }
        }
        index /= 2; // Round toward 0 (odd index are maximum values).
        assert newRange(get(2 * index), get(2 * index + 1)).contains(value) : value;
        return index;
    }

    /**
     * Returns {@code true} if this set contains the specified element.
     *
     * @param object The object to compare to this set.
     * @return {@code true} if the given object is equals to this set.
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(final Object object) {
        final Range<T> range = (Range<T>) object;
        if (elementClass.equals(range.elementClass)) {
            if (range.isMinIncluded() && range.isMaxIncluded()) {
                final int index = binarySearch(toArrayElement((Comparable<? super T>) range.getMinValue()));
                if (index >= 0 && (index & 1) == 0) {
                    final int c = get(index + 1).compareTo(range.getMaxValue());
                    return c == 0;
                }
            }
        }
        return false;
    }

    /**
     * Returns the first (lowest) range currently in this sorted set.
     *
     * @throws NoSuchElementException if the set is empty.
     */
    @Override
    public Range<T> first() throws NoSuchElementException {
        if (array != null && Array.getLength(array) != 0) {
            return newRange(get(0), get(1));
        }
        throw new NoSuchElementException();
    }

    /**
     * Returns the last (highest) range currently in this sorted set.
     *
     * @throws NoSuchElementException if the set is empty.
     */
    @Override
    public Range<T> last() throws NoSuchElementException {
        if (array != null) {
            final int length = Array.getLength(array);
            if (length != 0) {
                return newRange(get(length - 2), get(length - 1));
            }
        }
        throw new NoSuchElementException();
    }

    /**
     * Returns a view of the portion of this sorted set whose elements range from {@code lower}, inclusive, to
     * {@code upper}, exclusive.
     *
     * @param lower Low endpoint (inclusive) of the sub set.
     * @param upper High endpoint (exclusive) of the sub set.
     * @return A view of the specified range within this sorted set.
     */
    @Override
    public SortedSet<Range<T>> subSet(final Range<T> lower, final Range<T> upper) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Returns a view of the portion of this sorted set whose elements are strictly less than {@code upper}.
     *
     * @param upper High endpoint (exclusive) of the headSet.
     * @return A view of the specified initial range of this sorted set.
     */
    @Override
    public SortedSet<Range<T>> headSet(final Range<T> upper) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Returns a view of the portion of this sorted set whose elements are greater than or equal to {@code lower}.
     *
     * @param lower Low endpoint (inclusive) of the tailSet.
     * @return A view of the specified final range of this sorted set.
     */
    @Override
    public SortedSet<Range<T>> tailSet(final Range<T> lower) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /** Returns an iterator over the elements in this set of ranges. All elements are {@link Range} objects. */
    @Override
    public java.util.Iterator<Range<T>> iterator() {
        return new Iterator();
    }

    /**
     * An iterator for iterating through ranges in a {@link RangeSet}. All elements are {@link Range} objects.
     *
     * @version $Id$
     * @author Martin Desruisseaux (IRD)
     */
    private final class Iterator implements java.util.Iterator<Range<T>> {
        /** Modification count at construction time. */
        private int modCount = RangeSet.this.modCount;

        /** The array length. */
        private int length = (array != null) ? Array.getLength(array) : 0;

        /** Current position in {@link RangeSet#array}. */
        private int position;

        /** Returns {@code true} if the iteration has more elements. */
        @Override
        public boolean hasNext() {
            return position < length;
        }

        /** Returns the next element in the iteration. */
        @Override
        public Range<T> next() {
            if (hasNext()) {
                final T lower = get(position++);
                final T upper = get(position++);
                if (RangeSet.this.modCount != modCount) {
                    // Check it last, in case a change occured
                    // while we was constructing the element.
                    throw new ConcurrentModificationException();
                }
                return newRange(lower, upper);
            }
            throw new NoSuchElementException();
        }

        /** Removes from the underlying collection the last element returned by the iterator. */
        @Override
        public void remove() {
            if (position != 0) {
                if (RangeSet.this.modCount == modCount) {
                    final Object newArray = Array.newInstance(arrayElementClass, length -= 2);
                    System.arraycopy(array, position, newArray, position -= 2, length - position);
                    System.arraycopy(array, 0, newArray, 0, position);
                    array = newArray;
                    modCount = ++RangeSet.this.modCount;
                } else {
                    throw new ConcurrentModificationException();
                }
            } else {
                throw new IllegalStateException();
            }
        }
    }

    /**
     * Returns a hash value for this set of ranges. This value need not remain consistent between different
     * implementations of the same class.
     */
    @Override
    public int hashCode() {
        int code = elementClass.hashCode();
        if (array != null) {
            for (int i = Array.getLength(array); (i -= 8) >= 0; ) {
                code = code * 37 + Array.get(array, i).hashCode();
            }
        }
        return code;
    }

    /**
     * Compares the specified object with this set of ranges for equality.
     *
     * @param object The object to compare with this range.
     * @return {@code true} if the given object is equals to this range.
     */
    @Override
    public boolean equals(final Object object) {
        if (object != null && object.getClass().equals(getClass())) {
            final RangeSet that = (RangeSet) object;
            if (Utilities.equals(this.elementClass, that.elementClass)) {
                switch (arrayElementCode) {
                    case DOUBLE:
                        return Arrays.equals((double[]) this.array, (double[]) that.array);
                    case FLOAT:
                        return Arrays.equals((float[]) this.array, (float[]) that.array);
                    case LONG:
                        return Arrays.equals((long[]) this.array, (long[]) that.array);
                    case INTEGER:
                        return Arrays.equals((int[]) this.array, (int[]) that.array);
                    case SHORT:
                        return Arrays.equals((short[]) this.array, (short[]) that.array);
                    case BYTE:
                        return Arrays.equals((byte[]) this.array, (byte[]) that.array);
                    case CHARACTER:
                        return Arrays.equals((char[]) this.array, (char[]) that.array);
                    default:
                        return Arrays.equals((Object[]) this.array, (Object[]) that.array);
                }
            }
        }
        return false;
    }

    /**
     * Returns a clone of this range set.
     *
     * @return A clone of this range set.
     */
    @Override
    public RangeSet clone() {
        final RangeSet set;
        try {
            set = (RangeSet) super.clone();
        } catch (CloneNotSupportedException exception) {
            // Should not happen, since we are cloneable.
            throw new AssertionError(exception);
        }
        switch (set.arrayElementCode) {
            case DOUBLE:
                set.array = ((double[]) set.array).clone();
                break;
            case FLOAT:
                set.array = ((float[]) set.array).clone();
                break;
            case LONG:
                set.array = ((long[]) set.array).clone();
                break;
            case INTEGER:
                set.array = ((int[]) set.array).clone();
                break;
            case SHORT:
                set.array = ((short[]) set.array).clone();
                break;
            case BYTE:
                set.array = ((byte[]) set.array).clone();
                break;
            case CHARACTER:
                set.array = ((char[]) set.array).clone();
                break;
            default:
                set.array = ((Object[]) set.array).clone();
                break;
        }
        return set;
    }

    /**
     * Returns a string representation of this set of ranges. The returned string is implementation dependent. It is
     * usually provided for debugging purposes.
     */
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder(Classes.getShortClassName(this));
        buffer.append('[');
        boolean first = true;
        for (final Range range : this) {
            if (!first) {
                buffer.append(',');
            }
            buffer.append('{')
                    .append(range.getMinValue())
                    .append("..")
                    .append(range.getMaxValue())
                    .append('}');
            first = false;
        }
        return buffer.append(']').toString();
    }
}
