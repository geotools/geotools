/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage;

import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import javax.measure.Unit;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.Errors;
import org.geotools.metadata.i18n.Vocabulary;
import org.geotools.metadata.i18n.VocabularyKeys;
import org.geotools.referencing.wkt.UnformattableObjectException;
import org.geotools.util.AbstractInternationalString;
import org.geotools.util.Classes;
import org.geotools.util.NumberRange;
import org.geotools.util.Utilities;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.util.InternationalString;

/**
 * An immutable list of categories. Categories are sorted by their sample values. Overlapping ranges
 * of sample values are not allowed. A {@code CategoryList} can contains a mix of qualitative and
 * quantitative categories. The {@link #getCategory} method is responsible for finding the right
 * category for an arbitrary sample value.
 *
 * <p>Instances of {@link CategoryList} are immutable and thread-safe.
 *
 * @since 2.1
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
class CategoryList extends AbstractList<Category> implements Comparator<Category>, Serializable {
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = 2647846361059903365L;

    /**
     * The range of values in this category list. This is the union of the range of values of every
     * categories, excluding {@code NaN} values. This field will be computed only when first
     * requested.
     */
    private transient NumberRange<?> range;

    /**
     * List of {@link Category#minimum} values for each category in {@link #categories}. This array
     * <strong>must</strong> be in increasing order. Actually, this is the need to sort this array
     * that determines the element order in {@link #categories}.
     */
    private final double[] minimums;

    /**
     * The list of categories to use for decoding samples. This list most be sorted in increasing
     * order of {@link Category#minimum}. This {@link CategoryList} object may be used as a {@link
     * Comparator} for that. Qualitative categories (with NaN values) are last.
     */
    private final Category[] categories;

    /**
     * The "main" category, or {@code null} if there is none. The main category is the quantitative
     * category with the widest range of sample values.
     */
    private final Category main;

    /**
     * The "nodata" category (never {@code null}). The "nodata" category is a category mapping the
     * {@link Double#NaN} value. If none has been found, a default "nodata" category is used.
     */
    final Category nodata;

    /**
     * The category to use if {@link #getCategory(double)} is invoked with a sample value greater
     * than all sample ranges in this category list. This is usually a reference to the last
     * category to have a range of real values. A {@code null} value means that no fallback should
     * be used. By extension, a {@code null} value also means that {@link #getCategory} should not
     * try to find any fallback at all if the requested sample value do not falls in a category
     * range.
     */
    private final Category overflowFallback;

    /**
     * The last used category. We assume that this category is the most likely to be requested in
     * the next {@code transform(...)} invocation.
     */
    private transient Category last;

    /**
     * {@code true} if there is gaps between categories, or {@code false} otherwise. A gap is found
     * if for example the range of value is [-9999 .. -9999] for the first category and [0 .. 1000]
     * for the second one.
     */
    private final boolean hasGaps;

    /**
     * The name for this category list. Will be constructed only when first needed. This is given to
     * {@link GridSampleDimension} only if the user did not specified explicitly a description.
     *
     * @see #getName
     */
    private transient InternationalString name;

    /**
     * The unit information for all quantitative categories. It may be {@code null} if no category
     * has units.
     */
    private Unit<?> unit;

    /**
     * Constructs a category list using the specified array of categories.
     *
     * @param categories The list of categories.
     * @param units The geophysics unit, or {@code null} if none.
     * @throws IllegalArgumentException if two or more categories have overlapping sample value
     *     range.
     */
    public CategoryList(final Category[] categories, final Unit<?> units)
            throws IllegalArgumentException {
        this(categories, units, false);
    }

    /**
     * Constructs a category list using the specified array of categories.
     *
     * <p><STRONG>This constructor is for internal use only</STRONG>
     *
     * @param categories The list of categories.
     * @param units The geophysics unit, or {@code null} if none.
     * @param searchNearest The policy when {@link #getCategory} doesn't find an exact match for a
     *     sample value. {@code true} means that it should search for the nearest category, while
     *     {@code false} means that it should returns {@code null}.
     * @throws IllegalArgumentException if two or more categories have overlapping sample value
     *     range.
     */
    CategoryList(Category[] categories, Unit<?> units, boolean searchNearest)
            throws IllegalArgumentException {
        this.categories = categories = categories.clone();
        Arrays.sort(categories, this);
        assert isSorted(categories);
        /*
         * Constructs the array of Category.minimum values. During
         * the loop, we make sure there is no overlapping ranges.
         */
        boolean hasGaps = false;
        minimums = new double[categories.length];
        for (int i = 0; i < categories.length; i++) {
            final double minimum = minimums[i] = categories[i].minimum;
            if (i != 0) {
                assert !(minimum < minimums[i - 1]) : minimum; // Use '!' to accept NaN.
                final Category previous = categories[i - 1];
                if (compare(minimum, previous.maximum) <= 0) {
                    // Two categories have overlapping range;
                    // Formats an error message.
                    final NumberRange range1 = categories[i - 1].getRange();
                    final NumberRange range2 = categories[i - 0].getRange();
                    final Comparable[] args =
                            new Comparable[] {
                                range1.getMinValue(), range1.getMaxValue(),
                                range2.getMinValue(), range2.getMaxValue()
                            };
                    for (int j = 0; j < args.length; j++) {
                        if (args[j] instanceof Number) {
                            final float value = ((Number) args[j]).floatValue();
                            if (Float.isNaN(value)) {
                                String hex = Integer.toHexString(Float.floatToRawIntBits(value));
                                args[j] = "NaN(" + hex + ')';
                            }
                        }
                    }
                    throw new IllegalArgumentException(
                            Errors.format(ErrorKeys.RANGE_OVERLAP_$4, args));
                }
                // Checks if there is a gap between this category and the previous one.
                if (!Double.isNaN(minimum) && minimum != previous.getRange().getMaximum(false)) {
                    hasGaps = true;
                }
            }
        }
        this.hasGaps = hasGaps;
        /*
         * Search for the "nodata" category. This loop looks
         * for a qualitative category with the NaN value.
         */
        Category nodata = Category.NODATA;
        final long nodataBits = Double.doubleToRawLongBits(Double.NaN);
        for (int i = categories.length; --i >= 0; ) {
            final Category candidate = categories[i];
            final double value = candidate.minimum;
            if (Double.isNaN(value)) {
                nodata = candidate;
                if (Double.doubleToRawLongBits(value) == nodataBits) {
                    // Give a preference for the standard Double.NaN.
                    // We should have only one such value, since the
                    // range check above prevents range overlapping.
                    break;
                }
            }
        }
        this.nodata = nodata;
        /*
         * Search for what seems to be the "main" category. This loop looks for the
         * quantitative category (if there is one) with the widest range of sample values.
         */
        double range = 0;
        Category main = null;
        for (int i = categories.length; --i >= 0; ) {
            final Category candidate = categories[i];
            if (candidate.isQuantitative()) {
                final Category candidatePeer = candidate;
                final double candidateRange = candidatePeer.maximum - candidatePeer.minimum;
                if (candidateRange >= range) {
                    range = candidateRange;
                    main = candidate;
                }
            }
        }
        this.main = main;
        this.last = (main != null || categories.length == 0) ? main : categories[0];
        /*
         * Search for the fallback if {@link #getCategory(double)} is invoked with a sample
         * value greater than all ranges of sample values. This is the last category to have
         * a range of real numbers.
         */
        Category overflowFallback = null;
        if (searchNearest) {
            for (int i = categories.length; --i >= 0; ) {
                final Category category = categories[i];
                if (!Double.isNaN(category.maximum)) {
                    overflowFallback = category;
                    break;
                }
            }
        }
        this.overflowFallback = overflowFallback;
        this.unit = units;
    }

    /**
     * Compares {@link Category} objects according their {@link Category#minimum} value. This is
     * used for sorting the {@link #categories} array at construction time.
     */
    public final int compare(final Category o1, final Category o2) {
        return compare(o1.minimum, o2.minimum);
    }

    /**
     * Compares two {@code double} values. This method is similar to {@link
     * Double#compare(double,double)} except that it also order NaN values.
     */
    private static int compare(final double v1, final double v2) {
        if (Double.isNaN(v1) && Double.isNaN(v2)) {
            final long bits1 = Double.doubleToRawLongBits(v1);
            final long bits2 = Double.doubleToRawLongBits(v2);
            if (bits1 < bits2) return -1;
            if (bits1 > bits2) return +1;
        }
        return Double.compare(v1, v2);
    }

    /**
     * Returns {@code true} if the specified categories are sorted. This method ignores {@code NaN}
     * values. This method is used for assertions only.
     */
    static boolean isSorted(final Category[] categories) {
        for (int i = 1; i < categories.length; i++) {
            Category c;
            assert !((c = categories[i - 0]).minimum > c.maximum) : c;
            assert !((c = categories[i - 1]).minimum > c.maximum) : c;
            if (compare(categories[i - 1].maximum, categories[i].minimum) > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Effectue une recherche bi-linéaire de la valeur spécifiée. Cette méthode est semblable à
     * {@code Arrays#binarySearch(double[],double)}, excepté qu'elle peut distinguer différentes
     * valeurs de NaN.
     *
     * <p>Note: This method is not private in order to allow testing by {@link CategoryTest}.
     */
    static int binarySearch(final double[] array, final double key) {
        int low = 0;
        int high = array.length - 1;
        final boolean keyIsNaN = Double.isNaN(key);
        while (low <= high) {
            final int mid = (low + high) >> 1;
            final double midVal = array[mid];
            if (midVal < key) { // Neither val is NaN, midVal is smaller
                low = mid + 1;
                continue;
            }
            if (midVal > key) { // Neither val is NaN, midVal is larger
                high = mid - 1;
                continue;
            }
            /*
             * The following is an adaptation of evaluator's comments for bug #4471414
             * (http://developer.java.sun.com/developer/bugParade/bugs/4471414.html).
             * Extract from evaluator's comment:
             *
             *     [This] code is not guaranteed to give the desired results because
             *     of laxity in IEEE 754 regarding NaN values. There are actually two
             *     types of NaNs, signaling NaNs and quiet NaNs. Java doesn't support
             *     the features necessary to reliably distinguish the two.  However,
             *     the relevant point is that copying a signaling NaN may (or may not,
             *     at the implementors discretion) yield a quiet NaN -- a NaN with a
             *     different bit pattern (IEEE 754 6.2).  Therefore, on IEEE 754 compliant
             *     platforms it may be impossible to find a signaling NaN stored in an
             *     array since a signaling NaN passed as an argument to binarySearch may
             *     get replaced by a quiet NaN.
             */
            final long midRawBits = Double.doubleToRawLongBits(midVal);
            final long keyRawBits = Double.doubleToRawLongBits(key);
            if (midRawBits == keyRawBits) {
                return mid; // key found
            }
            final boolean midIsNaN = Double.isNaN(midVal);
            final boolean adjustLow;
            if (keyIsNaN) {
                // If (mid,key)==(!NaN, NaN): mid is lower.
                // If two NaN arguments, compare NaN bits.
                adjustLow = (!midIsNaN || midRawBits < keyRawBits);
            } else {
                // If (mid,key)==(NaN, !NaN): mid is greater.
                // Otherwise, case for (-0.0, 0.0) and (0.0, -0.0).
                adjustLow = (!midIsNaN && midRawBits < keyRawBits);
            }
            if (adjustLow) low = mid + 1;
            else high = mid - 1;
        }
        return -(low + 1); // key not found.
    }

    /**
     * Returns the name of this object. This method returns the name of what seems to be the "main"
     * category (i.e. the quantitative category with the widest range of sample values) concatenated
     * with the geophysics value range. This is given to {@link GridSampleDimension} only if the
     * user did not specified explicitly a description.
     */
    public final InternationalString getName() {
        if (name == null) {
            name = new Name();
        }
        return name;
    }

    /** The name for this category list. Will be created only when first needed. */
    private final class Name extends AbstractInternationalString {
        /** Returns the name in the specified locale. */
        public String toString(final Locale locale) {
            final StringBuffer buffer = new StringBuffer(30);
            if (main != null) {
                buffer.append(main.getName().toString(locale));
            } else {
                buffer.append('(');
                buffer.append(Vocabulary.getResources(locale).getString(VocabularyKeys.UNTITLED));
                buffer.append(')');
            }
            buffer.append(' ');
            return String.valueOf(formatRange(buffer, locale));
        }

        /** Returns the name in the default locale. */
        @Override
        public String toString() {
            return toString(Locale.getDefault());
        }
    }

    /**
     * Returns the unit information for quantitative categories in this list. May returns {@code
     * null} if there is no quantitative categories in this list, or if there is no unit
     * information.
     */
    public Unit<?> getUnits() {
        return unit;
    }

    /**
     * Returns the range of values in this category list. This is the union of the range of values
     * of every categories, excluding {@code NaN} values. A {@link NumberRange} object give more
     * informations than {@link org.opengis.CV_SampleDimension#getMinimum} and {@link
     * org.opengis.CV_SampleDimension#getMaximum} since it contains also the type (integer, float,
     * etc.) and inclusion/exclusion informations.
     *
     * @return The range of values. May be {@code null} if this category list has no quantitative
     *     category.
     * @see Category#getRange
     */
    public final NumberRange<?> getRange() {
        if (range == null) {
            NumberRange<?> range = null;
            for (int i = 0; i < categories.length; i++) {
                final NumberRange extent = categories[i].getRange();
                if (!Double.isNaN(extent.getMinimum()) && !Double.isNaN(extent.getMaximum())) {
                    if (range != null) {
                        range = range.union(extent);
                    } else {
                        range = extent;
                    }
                }
            }
            this.range = range;
        }
        return range;
    }

    /**
     * Format the range of geophysics values.
     *
     * @param buffer The buffer where to write the range of geophysics values.
     * @param locale The locale to use for formatting numbers.
     * @return The {@code buffer} for convenience.
     */
    private StringBuffer formatRange(StringBuffer buffer, final Locale locale) {
        final NumberRange range = getRange();
        buffer.append('[');
        if (range != null) {
            buffer = format(range.getMinimum(), false, locale, buffer);
            buffer.append(" ... ");
            buffer = format(range.getMaximum(), true, locale, buffer);
        } else {
            final Unit<?> unit = getUnits();
            if (unit != null) {
                buffer.append(unit);
            }
        }
        buffer.append(']');
        return buffer;
    }

    /**
     * Format the specified value using the specified locale convention.
     *
     * @param value The value to format.
     * @param writeUnit {@code true} if unit symbol should be formatted after the number. Ignored if
     *     this category list has no unit.
     * @param locale The locale, or {@code null} for a default one.
     * @param buffer The buffer where to format.
     * @return The buffer {@code buffer} for convenience.
     */
    StringBuffer format(
            final double value,
            final boolean writeUnits,
            final Locale locale,
            StringBuffer buffer) {
        return buffer.append(value);
    }

    /**
     * Returns a color model for this category list. This method builds up the color model from each
     * category's colors (as returned by {@link Category#getColors}).
     *
     * @param visibleBand The band to be made visible (usually 0). All other bands, if any will be
     *     ignored.
     * @param numBands The number of bands for the color model (usually 1). The returned color model
     *     will renderer only the {@code visibleBand} and ignore the others, but the existence of
     *     all {@code numBands} will be at least tolerated. Supplemental bands, even invisible, are
     *     useful for processing with Java Advanced Imaging.
     * @return The requested color model, suitable for {@link java.awt.image.RenderedImage} objects
     *     with values in the <code>{@linkplain #getRange}</code> range.
     */
    public final ColorModel getColorModel(final int visibleBand, final int numBands) {
        int type = DataBuffer.TYPE_FLOAT;
        final NumberRange<?> range = getRange();
        final Class<?> rt = range.getElementClass();
        if (Byte.class.equals(rt) || Short.class.equals(rt) || Integer.class.equals(rt)) {
            final int min = range.getMinValue().intValue();
            final int max = range.getMaxValue().intValue();
            if (min >= 0) {
                if (max < 0x100) {
                    type = DataBuffer.TYPE_BYTE;
                } else if (max < 0x10000) {
                    type = DataBuffer.TYPE_USHORT;
                } else {
                    type = DataBuffer.TYPE_INT;
                }
            } else if (min >= Short.MIN_VALUE && max <= Short.MAX_VALUE) {
                type = DataBuffer.TYPE_SHORT;
            } else {
                type = DataBuffer.TYPE_INT;
            }
        }
        return getColorModel(visibleBand, numBands, type);
    }

    /**
     * Returns a color model for this category list. This method builds up the color model from each
     * category's colors (as returned by {@link Category#getColors}).
     *
     * @param visibleBand The band to be made visible (usually 0). All other bands, if any will be
     *     ignored.
     * @param numBands The number of bands for the color model (usually 1). The returned color model
     *     will renderer only the {@code visibleBand} and ignore the others, but the existence of
     *     all {@code numBands} will be at least tolerated. Supplemental bands, even invisible, are
     *     useful for processing with Java Advanced Imaging.
     * @param type The transfer type used in the sample model.
     * @return The requested color model, suitable for {@link java.awt.image.RenderedImage} objects
     *     with values in the <code>{@link #getRange}</code> range.
     */
    public final ColorModel getColorModel(
            final int visibleBand, final int numBands, final int type) {
        return ColorModelFactory.getColorModel(categories, type, visibleBand, numBands);
    }

    /**
     * Returns the category of the specified sample value. If no category fits, then this method
     * returns {@code null}.
     *
     * @param sample The value.
     * @return The category of the supplied value, or {@code null}.
     */
    public final Category getCategory(final double sample) {
        /*
         * Recherche à quelle catégorie pourrait appartenir la valeur.
         * Note: Les valeurs 'NaN' sont à la fin du tableau 'values'. Donc:
         *
         * 1) Si 'value' est NaN,  alors 'i' pointera forcément sur une catégorie NaN.
         * 2) Si 'value' est réel, alors 'i' peut pointer sur une des catégories de
         *    valeurs réels ou sur la première catégorie de NaN.
         */
        int i = binarySearch(minimums, sample); // Special 'binarySearch' for NaN
        if (i >= 0) {
            // The value is exactly equals to one of Category.minimum,
            // or is one of NaN values. There is nothing else to do.
            assert Double.doubleToRawLongBits(sample) == Double.doubleToRawLongBits(minimums[i]);
            return categories[i];
        }
        if (Double.isNaN(sample)) {
            // The value is NaN, but not one of the registered ones.
            // Consequently, we can't map a category to this value.
            return null;
        }
        assert i == Arrays.binarySearch(minimums, sample) : i;
        // 'binarySearch' found the index of "insertion point" (~i). This means that
        // 'sample' is lower than 'Category.minimum' at this index. Consequently, if
        // this value fits in a category's range, it fits in the previous category (~i-1).
        i = ~i - 1;
        if (i >= 0) {
            final Category category = categories[i];
            assert sample > category.minimum : sample;
            if (sample <= category.maximum) {
                return category;
            }
            if (overflowFallback != null) {
                if (++i < categories.length) {
                    final Category upper = categories[i];
                    // ASSERT: if 'upper.minimum' was smaller than 'value', it should has been
                    //         found by 'binarySearch'. We use '!' in order to accept NaN values.
                    assert !(upper.minimum <= sample) : sample;
                    return (upper.minimum - sample < sample - category.maximum) ? upper : category;
                }
                return overflowFallback;
            }
        } else if (overflowFallback != null) {
            // If the value is smaller than the smallest Category.minimum, returns
            // the first category (except if there is only NaN categories).
            if (categories.length != 0) {
                final Category category = categories[0];
                if (!Double.isNaN(category.minimum)) {
                    return category;
                }
            }
        }
        return null;
    }

    /**
     * Formats a sample value. If {@code value} is a real number, then the value may be formatted
     * with the appropriate number of digits and the units symbol. Otherwise, if {@code value} is
     * {@code NaN}, then the category name is returned.
     *
     * @param value The sample value (may be {@code NaN}).
     * @param locale Locale to use for formatting, or {@code null} for the default locale.
     * @return A string representation of the sample value.
     */
    public final String format(final double value, final Locale locale) {
        if (Double.isNaN(value)) {
            Category category = last;
            if (!(value >= category.minimum && value <= category.maximum)
                    && Double.doubleToRawLongBits(value)
                            != Double.doubleToRawLongBits(category.minimum)) {
                category = getCategory(value);
                if (category == null) {
                    return Vocabulary.getResources(locale).getString(VocabularyKeys.UNTITLED);
                }
                last = category;
            }
            return category.getName().toString(null);
        }
        return format(value, true, locale, new StringBuffer()).toString();
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    ////////                                                                          ////////
    ////////       I M P L E M E N T A T I O N   O F   List   I N T E R F A C E       ////////
    ////////                                                                          ////////
    //////////////////////////////////////////////////////////////////////////////////////////
    /** Returns the number of categories in this list. */
    public final int size() {
        return categories.length;
    }

    /** Returns the element at the specified position in this list. */
    public final Category get(final int i) {
        return categories[i];
    }

    /** Returns all categories in this {@code CategoryList}. */
    @Override
    public final Category[] toArray() {
        return categories.clone();
    }

    /**
     * Returns a string representation of this category list. The returned string is implementation
     * dependent. It is usually provided for debugging purposes only.
     */
    @Override
    public final String toString() {
        return toString(this, null);
    }

    /**
     * Returns a string representation of this category list. The {@code owner} argument allow for a
     * different class name to be formatted.
     */
    final String toString(final Object owner, final InternationalString description) {
        final String lineSeparator = System.getProperty("line.separator", "\n");
        StringBuffer buffer = new StringBuffer(Classes.getShortClassName(owner));
        buffer.append('(');
        if (description != null && description != name) {
            buffer.append('"').append(description).append("\":");
        }
        buffer = formatRange(buffer, null);
        if (hasGaps) {
            buffer.append(" with gaps");
        }
        buffer.append(')').append(lineSeparator);
        /*
         * Writes categories below the SampleDimension description.
         * The symbol used for the main category is "triangular bullet".
         */
        for (final Category category : categories) {
            buffer.append("  ")
                    .append(category == main ? '\u2023' : ' ')
                    .append(' ')
                    .append(category)
                    .append(lineSeparator);
        }
        return buffer.toString();
    }

    /**
     * Compares the specified object with this category list for equality. If the two objects are
     * instances of {@link CategoryList}, then the test is a little bit stricter than the default
     * {@link AbstractList#equals}.
     */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof CategoryList) {
            final CategoryList that = (CategoryList) object;
            if (Arrays.equals(this.categories, that.categories)) {
                assert Arrays.equals(this.minimums, that.minimums);
                return Utilities.equals(this.overflowFallback, that.overflowFallback);
            }
            return false;
        }
        return (overflowFallback == null) && super.equals(object);
    }

    /** Reset the {@link #last} field to a non-null value after deserialization. */
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        last = (main != null || categories.length == 0) ? main : categories[0];
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////                                                                               ////////
    ////////    I M P L E M E N T A T I O N   O F   MathTransform1D   I N T E R F A C E    ////////
    ////////                                                                               ////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    /** Gets the dimension of input points, which is 1. */
    public final int getSourceDimensions() {
        return 1;
    }

    /** Gets the dimension of output points, which is 1. */
    public final int getTargetDimensions() {
        return 1;
    }

    /** Tests whether this transform does not move any points. */
    public boolean isIdentity() {
        return false;
    }

    /** Ensure the specified point is one-dimensional. */
    private static void checkDimension(final DirectPosition point) {
        final int dim = point.getDimension();
        if (dim != 1) {
            throw new MismatchedDimensionException(
                    Errors.format(ErrorKeys.MISMATCHED_DIMENSION_$2, 1, dim));
        }
    }

    /**
     * Returns a Well Known Text</cite> (WKT) for this object. This operation may fails if an object
     * is too complex for the WKT format capability.
     *
     * @return The Well Know Text for this object.
     * @throws UnsupportedOperationException If this object can't be formatted as WKT.
     * @todo Not yet implemented.
     */
    public String toWKT() throws UnsupportedOperationException {
        throw new UnformattableObjectException("Not yet implemented.", getClass());
    }
}
