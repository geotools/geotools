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
package org.geotools.coverage;

import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.RasterFormatException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import javax.measure.unit.Unit;
import javax.media.jai.iterator.WritableRectIter;

import org.opengis.referencing.operation.MathTransform1D;
import org.opengis.referencing.operation.Matrix;
import org.opengis.referencing.operation.TransformException;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.util.InternationalString;

import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.referencing.operation.matrix.Matrix1;
import org.geotools.referencing.wkt.UnformattableObjectException;
import org.geotools.resources.Classes;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.util.AbstractInternationalString;
import org.geotools.util.NumberRange;
import org.geotools.util.Utilities;


/**
 * An immutable list of categories. Categories are sorted by their sample values.
 * Overlapping ranges of sample values are not allowed. A {@code CategoryList} can
 * contains a mix of qualitative and quantitative categories.  The {@link #getCategory}
 * method is responsible for finding the right category for an arbitrary sample value.
 * <p>
 * Instances of {@link CategoryList} are immutable and thread-safe.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
class CategoryList extends AbstractList<Category>
        implements MathTransform1D, Comparator<Category>, Serializable
{
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 2647846361059903365L;

    /**
     * The inverse transform, never {@code null}.
     * The following rule must hold:
     *
     * <ul>
     *   <li>If {@code this} is an instance of {@link CategoryList}, then
     *       {@code inverse} must be an instance of {@link GeophysicsCategoryList}.</li>
     *   <li>If {@code this} is an instance of {@link GeophysicsCategoryList}, then
     *       {@code inverse} must be an instance of {@link CategoryList}.</li>
     * </ul>
     */
    final CategoryList inverse;

    /**
     * The range of values in this category list. This is the union of the range of values
     * of every categories, excluding {@code NaN} values. This field will be computed
     * only when first requested.
     */
    private transient NumberRange<?> range;

    /**
     * List of {@link Category#minimum} values for each category in {@link #categories}.
     * This array <strong>must</strong> be in increasing order. Actually, this is the
     * need to sort this array that determines the element order in {@link #categories}.
     */
    private final double[] minimums;

    /**
     * The list of categories to use for decoding samples. This list most be sorted
     * in increasing order of {@link Category#minimum}.   This {@link CategoryList}
     * object may be used as a {@link Comparator} for that.  Qualitative categories
     * (with NaN values) are last.
     */
    private final Category[] categories;

    /**
     * The "main" category, or {@code null} if there is none. The main category
     * is the quantitative category with the widest range of sample values.
     */
    private final Category main;

    /**
     * The "nodata" category (never {@code null}). The "nodata" category is a
     * category mapping the geophysics {@link Double#NaN} value.  If none has been
     * found, a default "nodata" category is used. This category is used to transform
     * geophysics values to sample values into rasters when no suitable category has
     * been found for a given geophysics value.
     */
    final Category nodata;

    /**
     * The category to use if {@link #getCategory(double)} is invoked  with  a sample value
     * greater than all sample ranges in this category list. This is usually a reference to
     * the last category to have a range of real values. A {@code null} value means that no
     * fallback should be used. By extension, a {@code null} value also means that
     * {@link #getCategory} should not try to find any fallback at all if the requested
     * sample value do not falls in a category range.
     */
    private final Category overflowFallback;

    /**
     * The last used category. We assume that this category is the most likely
     * to be requested in the next {@code transform(...)} invocation.
     */
    private transient Category last;

    /**
     * {@code true} if there is gaps between categories, or {@code false} otherwise.
     * A gap is found if for example the range of value is [-9999 .. -9999] for the first
     * category and [0 .. 1000] for the second one.
     */
    private final boolean hasGaps;

    /**
     * The name for this category list. Will be constructed only when first needed.
     * This is given to {@link GridSampleDimension} only if the user did not specified
     * explicitly a description.
     *
     * @see #getName
     */
    private transient InternationalString name;

    /**
     * Constructs a category list using the specified array of categories.
     *
     * @param  categories The list of categories.
     * @param  units The geophysics unit, or {@code null} if none.
     * @throws IllegalArgumentException if two or more categories
     *         have overlapping sample value range.
     */
    public CategoryList(final Category[] categories, final Unit<?> units)
            throws IllegalArgumentException
    {
        this(categories, units, false, null);
        assert isGeophysics(false);
    }

    /**
     * Constructs a category list using the specified array of categories.
     *
     *         <STRONG>This constructor is for internal use only</STRONG>
     *
     * It is not private only because {@link GeophysicsCategoryList} needs this constructor.
     *
     * @param  categories The list of categories.
     * @param  units The geophysics unit, or {@code null} if none.
     * @param  searchNearest The policy when {@link #getCategory} doesn't find an exact match
     *         for a sample value. {@code true} means that it should search for the nearest
     *         category, while {@code false} means that it should returns {@code null}.
     * @param  inverse The inverse transform, or {@code null} to build it automatically.
     *         <STRONG>This argument can be non-null only if invoked from
     *         {@link GeophysicsCategoryList} constructor</STRONG>.
     * @throws IllegalArgumentException if two or more categories have overlapping sample value
     *         range.
     */
    CategoryList(Category[] categories, Unit<?> units, boolean searchNearest, CategoryList inverse)
            throws IllegalArgumentException
    {
        /*
         * Checks if we are constructing a geophysics category list, then rescale all cagegories
         * according. We may loose the user intend by doing so (he may have specified explicitly
         * a list of GeophysicsCategory), but this is the SampleDimension's job to keep trace of
         * it.
         */
        final boolean geophysics = (inverse != null);
        assert geophysics == (this instanceof GeophysicsCategoryList) : geophysics;
        this.categories = categories = categories.clone();
        for (int i=0; i<categories.length; i++) {
            categories[i] = categories[i].geophysics(geophysics);
        }
        Arrays.sort(categories, this);
        assert isSorted(categories);
        assert isGeophysics(geophysics);
        /*
         * Constructs the array of Category.minimum values. During
         * the loop, we make sure there is no overlapping ranges.
         */
        boolean hasGaps = false;
        minimums = new double[categories.length];
        for (int i=0; i<categories.length; i++) {
            final double minimum = minimums[i] = categories[i].minimum;
            if (i != 0) {
                assert !(minimum < minimums[i-1]) : minimum; // Use '!' to accept NaN.
                final Category previous = categories[i-1];
                if (compare(minimum, previous.maximum) <= 0) {
                    // Two categories have overlapping range;
                    // Formats an error message.
                    final NumberRange range1 = categories[i-1].getRange();
                    final NumberRange range2 = categories[i-0].getRange();
                    final Comparable[] args = new Comparable[] {
                        range1.getMinValue(), range1.getMaxValue(),
                        range2.getMinValue(), range2.getMaxValue()
                    };
                    for (int j=0; j<args.length; j++) {
                        if (args[j] instanceof Number) {
                            final float value = ((Number) args[j]).floatValue();
                            if (Float.isNaN(value)) {
                                String hex = Integer.toHexString(Float.floatToRawIntBits(value));
                                args[j] = "NaN(" + hex + ')';
                            }
                        }
                    }
                    throw new IllegalArgumentException(Errors.format(ErrorKeys.RANGE_OVERLAP_$4, args));
                }
                // Checks if there is a gap between this category and the previous one.
                if (!Double.isNaN(minimum) && minimum!=previous.getRange().getMaximum(false)) {
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
        for (int i=categories.length; --i>=0;) {
            final Category candidate = categories[i];
            final double value = candidate.geophysics(true).minimum;
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
        for (int i=categories.length; --i>=0;) {
            final Category candidate = categories[i];
            if (candidate.isQuantitative()) {
                final Category candidatePeer = candidate.geophysics(false);
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
            for (int i=categories.length; --i>=0;) {
                final Category category = categories[i];
                if (!Double.isNaN(category.maximum)) {
                    overflowFallback = category;
                    break;
                }
            }
        }
        this.overflowFallback = overflowFallback;
        /*
         * Set the inverse transform. If no inverse transform has been explicitly specified, then
         * this is the "normal" construction call (i.e. not the special construction performed by
         * GeophysicsCategoryList) and we create our internal inverse object.
         */
        if (inverse == null) {
            inverse = new GeophysicsCategoryList(categories, units, this);
        }
        this.inverse = inverse;
        assert (this instanceof GeophysicsCategoryList) !=
            (inverse instanceof GeophysicsCategoryList);
    }

    /**
     * Compares {@link Category} objects according their {@link Category#minimum} value.
     * This is used for sorting the {@link #categories} array at construction time.
     */
    public final int compare(final Category o1, final Category o2) {
        return compare(o1.minimum, o2.minimum);
    }

    /**
     * Compares two {@code double} values. This method is similar to
     * {@link Double#compare(double,double)} except that it also order
     * NaN values.
     */
    private static int compare(final double v1, final double v2) {
        if (Double.isNaN(v1) && Double.isNaN(v2)) {
            final long bits1  = Double.doubleToRawLongBits(v1);
            final long bits2  = Double.doubleToRawLongBits(v2);
            if (bits1 < bits2) return -1;
            if (bits1 > bits2) return +1;
        }
        return Double.compare(v1, v2);
    }

    /**
     * Returns {@code true} if the specified categories are sorted. This method
     * ignores {@code NaN} values. This method is used for assertions only.
     */
    static boolean isSorted(final Category[] categories) {
        for (int i=1; i<categories.length; i++) {
            Category c;
            assert !((c=categories[i-0]).minimum > c.maximum) : c;
            assert !((c=categories[i-1]).minimum > c.maximum) : c;
            if (compare(categories[i-1].maximum, categories[i].minimum) > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Effectue une recherche bi-linéaire de la valeur spécifiée. Cette
     * méthode est semblable à {@link Arrays#binarySearch(double[],double)},
     * excepté qu'elle peut distinguer différentes valeurs de NaN.
     *
     * Note: This method is not private in order to allows testing by {@link CategoryTest}.
     */
    static int binarySearch(final double[] array, final double key) {
        int low  = 0;
        int high = array.length-1;
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
                adjustLow = (!midIsNaN || midRawBits<keyRawBits);
            } else {
                // If (mid,key)==(NaN, !NaN): mid is greater.
                // Otherwise, case for (-0.0, 0.0) and (0.0, -0.0).
                adjustLow = (!midIsNaN && midRawBits<keyRawBits);
            }
            if (adjustLow) low = mid + 1;
            else          high = mid - 1;
        }
        return -(low + 1);  // key not found.
    }

    /**
     * If {@code geo} is {@code true}, returns a list of categories scaled to
     * geophysics values. This method always returns a list of categories in which
     * <code>{@linkplain Category#geophysics(boolean) Category.geophysics}(geo)</code>
     * has been invoked for each category.
     */
    public CategoryList geophysics(final boolean geo) {
        final CategoryList scaled = geo ? inverse : this;
        assert scaled.isGeophysics(geo);
        return scaled;
    }

    /**
     * Verifies if all categories are of the specified type. {@code true} is for
     * {@linkplain org.geotools.coverage.grid.ViewType#GEOPHYSICS geophysics} and {@code false} is
     * for {@linkplain org.geotools.coverage.grid.ViewType#PACKED packed} data. This method is used
     * mostly in assertion statements.
     *
     * @param  geo The desired type for every categories.
     * @return {@code true} if all categories are in the specified type.
     */
    final boolean isGeophysics(final boolean geo) {
        return isGeophysics(categories, geo);
    }

    /**
     * Verifies if all categories are of the specified type. {@code true} is for
     * {@linkplain org.geotools.coverage.grid.ViewType#GEOPHYSICS geophysics} and {@code false} is
     * for {@linkplain org.geotools.coverage.grid.ViewType#PACKED packed} data. This method is used
     * mostly in assertion statements.
     *
     * @param  categories The categories to test.
     * @param  geo The desired type for every categories.
     * @return {@code true} if all categories are in the specified type.
     */
    static boolean isGeophysics(final Category[] categories, final boolean geo) {
        for (int i=0; i<categories.length; i++) {
            final Category category = categories[i];
            if (category.geophysics(geo) != category) {
                return false;
            }
        }
        return true;
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

    /**
     * The name for this category list. Will be created only when first needed.
     */
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
            return String.valueOf(geophysics(true).formatRange(buffer, locale));
        }

        /** Returns the name in the default locale. */
        @Override
        public String toString() {
            return toString(Locale.getDefault());
        }
    }

    /**
     * Returns the unit information for quantitative categories in this list. May returns
     * {@code null} if there is no quantitative categories in this list, or if there is no
     * unit information.
     * <p>
     * This method is to be overridden by {@link GeophysicsCategoryList}. The default implementation
     * returns {@code null} since sample values are not geophysics values as long as they have not
     * been transformed. The {@link GridSampleDimension} class will invoke
     * {@code geophysics(true).getUnits()} in order to get a non-null unit.
     */
    public Unit<?> getUnits() {
        return null;
    }

    /**
     * Returns the range of values in this category list. This is the union of the range
     * of values of every categories, excluding {@code NaN} values. A {@link NumberRange}
     * object give more informations than {@link org.opengis.CV_SampleDimension#getMinimum}
     * and {@link org.opengis.CV_SampleDimension#getMaximum} since it contains also the
     * type (integer, float, etc.) and inclusion/exclusion informations.
     *
     * @return The range of values. May be {@code null} if this category list has no
     *         quantitative category.
     *
     * @see Category#getRange
     *
     * @todo Returns an instance of {@link MeasurementRange} if we are a geophysics category list.
     */
    public final NumberRange<?> getRange() {
        if (range == null) {
            NumberRange<?> range = null;
            for (int i=0; i<categories.length; i++) {
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
     * @param  buffer The buffer where to write the range of geophysics values.
     * @param  locale The locale to use for formatting numbers.
     * @return The {@code buffer} for convenience.
     */
    private StringBuffer formatRange(StringBuffer buffer, final Locale locale) {
        final NumberRange range = getRange();
        buffer.append('[');
        if (range != null) {
            buffer = format(range.getMinimum(), false, locale, buffer);
            buffer.append(" ... ");
            buffer = format(range.getMaximum(), true,  locale, buffer);
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
     * Format the specified value using the specified locale convention. This method is to be
     * overridden by {@link GeophysicsCategoryList}. The default implementation do not format
     * the value very properly, since most invocation will be done on
     * {@code geophysics(true).format(...)} anyway.
     *
     * @param  value The value to format.
     * @param  writeUnit {@code true} if unit symbol should be formatted after the number.
     *         Ignored if this category list has no unit.
     * @param  locale The locale, or {@code null} for a default one.
     * @param  buffer The buffer where to format.
     * @return The buffer {@code buffer} for convenience.
     */
    StringBuffer format(final double value, final boolean writeUnits,
                        final Locale locale, StringBuffer buffer)
    {
        return buffer.append(value);
    }

    /**
     * Returns a color model for this category list. This method builds up the color model
     * from each category's colors (as returned by {@link Category#getColors}).
     *
     * @param  visibleBand The band to be made visible (usually 0). All other bands, if any
     *         will be ignored.
     * @param  numBands The number of bands for the color model (usually 1). The returned color
     *         model will renderer only the {@code visibleBand} and ignore the others, but
     *         the existence of all {@code numBands} will be at least tolerated. Supplemental
     *         bands, even invisible, are useful for processing with Java Advanced Imaging.
     * @return The requested color model, suitable for {@link java.awt.image.RenderedImage}
     *         objects with values in the <code>{@linkplain #getRange}</code> range.
     */
    public final ColorModel getColorModel(final int visibleBand, final int numBands) {
        int type = DataBuffer.TYPE_FLOAT;
        final NumberRange<?> range = getRange();
        final Class<?> rt = range.getElementClass();
        if (Byte.class.equals(rt) || Short.class.equals(rt) || Integer.class.equals(rt)) {
            // TODO: remove the cast when we will be allowed to compile for Java 6.
            final int min = ((Number) range.getMinValue()).intValue();
            final int max = ((Number) range.getMaxValue()).intValue();
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
     * Returns a color model for this category list. This method builds up the color model
     * from each category's colors (as returned by {@link Category#getColors}).
     *
     * @param  visibleBand The band to be made visible (usually 0). All other bands, if any
     *         will be ignored.
     * @param  numBands The number of bands for the color model (usually 1). The returned color
     *         model will renderer only the {@code visibleBand} and ignore the others, but
     *         the existence of all {@code numBands} will be at least tolerated. Supplemental
     *         bands, even invisible, are useful for processing with Java Advanced Imaging.
     * @param  type The transfer type used in the sample model.
     * @return The requested color model, suitable for {@link java.awt.image.RenderedImage}
     *         objects with values in the <code>{@link #getRange}</code> range.
     */
    public final ColorModel getColorModel(final int visibleBand, final int numBands, final int type) {
        return ColorModelFactory.getColorModel(categories, type, visibleBand, numBands);
    }

    /**
     * Returns the category of the specified sample value.
     * If no category fits, then this method returns {@code null}.
     *
     * @param  sample The value.
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
        i = ~i-1;
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
                    return (upper.minimum-sample < sample-category.maximum) ? upper : category;
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
     * Formats a sample value. If {@code value} is a real number, then the value may
     * be formatted with the appropriate number of digits and the units symbol. Otherwise,
     * if {@code value} is {@code NaN}, then the category name is returned.
     *
     * @param  value  The sample value (may be {@code NaN}).
     * @param  locale Locale to use for formatting, or {@code null} for the default locale.
     * @return A string representation of the sample value.
     */
    public final String format(final double value, final Locale locale) {
        if (Double.isNaN(value)) {
            Category category = last;
            if (!(value >= category.minimum  &&  value <= category.maximum) &&
                 Double.doubleToRawLongBits(value) != Double.doubleToRawLongBits(category.minimum))
            {
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
    /**
     * Returns the number of categories in this list.
     */
    public final int size() {
        return categories.length;
    }

    /**
     * Returns the element at the specified position in this list.
     */
    public final Category get(final int i) {
        return categories[i];
    }

    /**
     * Returns all categories in this {@code CategoryList}.
     */
    @Override
    public final Category[] toArray() {
        return categories.clone();
    }

    /**
     * Returns a string representation of this category list.
     * The returned string is implementation dependent.
     * It is usually provided for debugging purposes only.
     */
    @Override
    public final String toString() {
        return toString(this, null);
    }

    /**
     * Returns a string representation of this category list. The {@code owner}
     * argument allow for a different class name to be formatted.
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
            buffer.append("  ").append(category == main ? '\u2023' : ' ').append(' ')
                  .append(category).append(lineSeparator);
        }
        return buffer.toString();
    }

    /**
     * Compares the specified object with this category list for equality.
     * If the two objects are instances of {@link CategoryList}, then the
     * test is a little bit stricter than the default {@link AbstractList#equals}.
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
        return (overflowFallback==null) && super.equals(object);
    }

    /**
     * Reset the {@link #last} field to a non-null value after deserialization.
     */
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        last = (main != null || categories.length == 0) ? main : categories[0];
    }




    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////                                                                               ////////
    ////////    I M P L E M E N T A T I O N   O F   MathTransform1D   I N T E R F A C E    ////////
    ////////                                                                               ////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Gets the dimension of input points, which is 1.
     */
    public final int getSourceDimensions() {
        return 1;
    }

    /**
     * Gets the dimension of output points, which is 1.
     */
    public final int getTargetDimensions() {
        return 1;
    }

    /**
     * Tests whether this transform does not move any points.
     */
    public boolean isIdentity() {
        return false;
    }

    /**
     * Returns the inverse transform of this object.
     */
    public final MathTransform1D inverse() {
        return inverse;
    }

    /**
     * Ensure the specified point is one-dimensional.
     */
    private static void checkDimension(final DirectPosition point) {
        final int dim = point.getDimension();
        if (dim != 1) {
            throw new MismatchedDimensionException(Errors.format(
                    ErrorKeys.MISMATCHED_DIMENSION_$2, 1, dim));
        }
    }

    /**
     * Transforms the specified {@code ptSrc} and stores the result in {@code ptDst}.
     */
    public final DirectPosition transform(final DirectPosition ptSrc, DirectPosition ptDst)
            throws TransformException
    {
        checkDimension(ptSrc);
        if (ptDst==null) {
            ptDst = new GeneralDirectPosition(1);
        } else {
            checkDimension(ptDst);
        }
        ptDst.setOrdinate(0, transform(ptSrc.getOrdinate(0)));
        return ptDst;
    }

    /**
     * Gets the derivative of this transform at a point.
     */
    public final Matrix derivative(final DirectPosition point) throws TransformException {
        checkDimension(point);
        return new Matrix1(derivative(point.getOrdinate(0)));
    }

    /**
     * Gets the derivative of this function at a value.
     *
     * @param  value The value where to evaluate the derivative.
     * @return The derivative at the specified point.
     * @throws TransformException if the derivative can't be evaluated at the specified point.
     */
    public final double derivative(final double value) throws TransformException {
        Category category = last;
        if (!(value >= category.minimum  &&  value <= category.maximum) &&
             Double.doubleToRawLongBits(value) != Double.doubleToRawLongBits(category.minimum))
        {
            category = getCategory(value);
            if (category == null) {
                throw new TransformException(Errors.format(ErrorKeys.NO_CATEGORY_FOR_VALUE_$1, value));
            }
            last = category;
        }
        return category.transform.derivative(value);
    }

    /**
     * Transforms the specified value.
     *
     * @param value The value to transform.
     * @return the transformed value.
     * @throws TransformException if the value can't be transformed.
     */
    public final double transform(double value) throws TransformException {
        Category category = last;
        if (!(value >= category.minimum  &&  value <= category.maximum) &&
             Double.doubleToRawLongBits(value) != Double.doubleToRawLongBits(category.minimum))
        {
            category = getCategory(value);
            if (category == null) {
                throw new TransformException(Errors.format(ErrorKeys.NO_CATEGORY_FOR_VALUE_$1, value));
            }
            last = category;
        }
        value = category.transform.transform(value);
        if (overflowFallback != null) {
            if (value < category.inverse.minimum) return category.inverse.minimum;
            if (value > category.inverse.maximum) return category.inverse.maximum;
        }
        assert category == inverse.getCategory(value).inverse : category;
        return value;
    }

    /**
     * Transforms a list of coordinate point ordinal values. This implementation can work on
     * either float or double arrays, since the quasi-totality of the implementation is the
     * same. Locale variables still {@code double} because this is the type used in
     * {@link Category} objects.
     *
     * @todo We could add an optimisation after the loops checking for category change:
     *       if we were allowed to search for nearest category (overflowFallback!=null),
     *       then make sure that the category really changed. There is already a slight
     *       optimization for the most common cases, but maybe we could go a little bit
     *       further.
     */
    private void transform(final double[] srcPts, final float[] srcFloat, int srcOff,
                           final double[] dstPts, final float[] dstFloat, int dstOff,
                           int numPts, final boolean doublePrecision) throws TransformException
    {
        final int srcToDst = dstOff-srcOff;
        Category  category = last;
        double     maximum = category.maximum;
        double     minimum = category.minimum;
        long       rawBits = Double.doubleToRawLongBits(minimum);
        final int direction;
        if (srcPts!=dstPts || srcOff>=dstOff) {
            direction = +1;
        } else {
            direction = -1;
            dstOff += numPts-1;
            srcOff += numPts-1;
        }
        /*
         * Scan every points. Transforms will be performed by blocks, each time
         * the loop detects that the category has changed. The break point is near
         * the end of the loop, after we have done the transformation but before
         * to change category.
         */
        for (int peekOff=srcOff; true; peekOff += direction) {
            // NOTE: We do not need to setup 'value' since we are not going to use it if
            //       numPts<0.  Unfortunatly, the compiler flow analysis doesn't seem to
            //       be sophesticated enough to detect this case. So we have to set a dummy
            //       value in order to avoid compiler error.
            double value = 0;
            if (doublePrecision) { // Optimized loop for the 'double' version
                while (--numPts >= 0) {
                    value = srcPts[peekOff];
                    if ((value>=minimum && value<=maximum) ||
                        Double.doubleToRawLongBits(value)==rawBits)
                    {
                        peekOff += direction;
                        continue;
                    }
                    break; // The category has changed. Stop the search.
                }
            } else {
                while (--numPts >= 0) { // Optimized loop for the 'float' version
                    value = srcFloat[peekOff];
                    if ((value>=minimum && value<=maximum) ||
                        Double.doubleToRawLongBits(value)==rawBits)
                    {
                        peekOff += direction;
                        continue;
                    }
                    break; // The category has changed. Stop the search.
                }
            }
            if (overflowFallback != null) {
                // TODO: Slight optimization. We could go further by checking if 'value' is closer
                //       to this category than to the previous category or the next category.  But
                //       we may need the category index, and binarySearch is a costly operation...
                if (value > maximum && category==overflowFallback) {
                    continue;
                }
                if (value < minimum && category==categories[0]) {
                    continue;
                }
            }
            /*
             * The category has changed. Compute the start point (which depends of 'direction')
             * and performs the transformation. If 'getCategory' was allowed to search for the
             * nearest category, clamp all output values in their category range.
             */
            int count = peekOff-srcOff;  // May be negative if we are going backward.
            if (count < 0) {
                count  = -count;
                srcOff -= count-1;
            }
            if (doublePrecision) { // Optimized loop for the 'double' version.
                category.transform.transform(srcPts, srcOff, dstPts, srcOff+srcToDst, count);
                if (overflowFallback != null) {
                    dstOff  = srcOff+srcToDst;
                    final double min = category.inverse.minimum;
                    final double max = category.inverse.maximum;
                    while (--count >= 0) { // Optimized loop for the 'double' version.
                        final double check = dstPts[dstOff];
                        if (check < min) {
                            dstPts[dstOff] = min;
                        } else if (check > max) {
                            dstPts[dstOff] = max;
                        }
                        dstOff++;
                    }
                }
            } else { // Optimized loop for the 'float' version.
                category.transform.transform(srcFloat, srcOff, dstFloat, srcOff+srcToDst, count);
                if (overflowFallback != null) {
                    dstOff  = srcOff+srcToDst;
                    final float min = (float) category.inverse.minimum;
                    final float max = (float) category.inverse.maximum;
                    while (--count >= 0) { // Optimized loop for the 'double' version.
                        final float check = dstFloat[dstOff];
                        if (check < min) {
                            dstFloat[dstOff] = min;
                        } else if (check > max) {
                            dstFloat[dstOff] = max;
                        }
                        dstOff++;
                    }
                }
            }
            /*
             * Transformation is now finished for all points in the range [srcOff..peekOff]
             * (not including 'peekOff'). If there is more points to examine, gets the new
             * category for the next points.
             */
            if (numPts < 0) {
                break;
            }
            category = getCategory(value);
            if (category == null) {
                throw new TransformException(Errors.format(ErrorKeys.NO_CATEGORY_FOR_VALUE_$1, value));
            }
            maximum = category.maximum;
            minimum = category.minimum;
            rawBits = Double.doubleToRawLongBits(minimum);
            srcOff  = peekOff;
        }
        last = category;
    }

    /**
     * Transforms a list of coordinate point ordinal values.
     */
    public final void transform(double[] srcPts, int srcOff,
                                double[] dstPts, int dstOff, int numPts) throws TransformException
    {
        transform(srcPts, null, srcOff, dstPts, null, dstOff, numPts, true);
    }

    /**
     * Transforms a list of coordinate point ordinal values.
     */
    public final void transform(float[] srcPts, int srcOff,
                                float[] dstPts, int dstOff, int numPts) throws TransformException
    {
        transform(null, srcPts, srcOff, null, dstPts, dstOff, numPts, false);
    }

    /**
     * Transforms a list of coordinate point ordinal values.
     *
     * @todo Not yet implemented.
     */
    public final void transform(float[] srcPts, int srcOff,
                                double[] dstPts, int dstOff, int numPts) throws TransformException
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Transforms a list of coordinate point ordinal values.
     *
     * @todo Not yet implemented.
     */
    public final void transform(double[] srcPts, int srcOff,
                                float[] dstPts, int dstOff, int numPts) throws TransformException
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Transforms a raster. Only the current band in {@code iterator} will be transformed.
     * The transformed value are write back in the {@code iterator}. If a different
     * destination raster is wanted, a {@link org.geotools.image.TransfertRectIter}
     * may be used.
     *
     * @param  iterator An iterator to iterate among the samples to transform.
     * @throws RasterFormatException if a problem occurs during the transformation.
     */
    public final void transform(final WritableRectIter iterator) throws RasterFormatException {
        /*
         * Category of the lowest minimum and highest maximum value (not including NaN),
         * or <code>null</code in none. Will be used later for range checks.
         */
        Category categoryMin=null, categoryMax=null;
        for (int i=categories.length; --i>=0;) {
            if (!Double.isNaN(categories[i].maximum)) {
                categoryMax = categories[i];
                categoryMin = categories[0];
                break;
            }
        }
        Category category = main;
        if (main == null) {
            category = nodata;
        }
        double maximum = category.maximum;
        double minimum = category.minimum;
        long   rawBits = Double.doubleToRawLongBits(minimum);
        MathTransform1D tr = category.transform;
        double maxTr, minTr;
        if (overflowFallback == null) {
            maxTr = Double.POSITIVE_INFINITY;
            minTr = Double.NEGATIVE_INFINITY;
        } else {
            maxTr = category.inverse.maximum;
            minTr = category.inverse.minimum;
        }
        try {
            iterator.startLines();
            if (!iterator.finishedLines()) do {
                iterator.startPixels();
                if (!iterator.finishedPixels()) do {
                    double value = iterator.getSampleDouble();
                    if (!(value>=minimum && value<=maximum) &&          // 'true' if value is NaN...
                          Double.doubleToRawLongBits(value) != rawBits) // and the NaN bits changed.
                    {
                        // Category has changed. Find the new category.
                        category = getCategory(value);
                        if (category == null) {
                            category = nodata;
                        }
                        maximum = (category!=categoryMax) ? category.maximum : Double.POSITIVE_INFINITY;
                        minimum = (category!=categoryMin) ? category.minimum : Double.NEGATIVE_INFINITY;
                        rawBits = Double.doubleToRawLongBits(minimum);
                        tr      = category.transform;
                        if (overflowFallback != null) {
                            maxTr = category.inverse.maximum;
                            minTr = category.inverse.minimum;
                        }
                    }
                    /*
                     * TODO: This assertion fails in some circonstance: during conversions from
                     *       geophysics to sample values  and  when the sample value is outside
                     *       the inclusive range but inside the exclusive range... In this case
                     *       'getCategory(double)' may choose the wrong category. The fix would
                     *       be to add new fiels in Category: we should have 'minInclusive' and
                     *       'minExclusive' instead of just 'minimum',  and same for 'maximum'.
                     *       The CategoryList.minimums array would still inclusive,   but tests
                     *       for range inclusion should use the exclusive extremas.
                     */
                    assert hasGaps || (category==nodata) || // Disable assertion in those cases
                           (Double.isNaN(value) ? Double.doubleToRawLongBits(value) == rawBits
                                                : (value>=minimum && value<=maximum)) : value;
                    value = tr.transform(value);
                    if (value > maxTr) {
                        value = maxTr;
                    } else if (value < minTr) {
                        value = minTr;
                    }
                    iterator.setSample(value);
                }
                while (!iterator.nextPixelDone());
            }
            while (!iterator.nextLineDone());
        } catch (TransformException cause) {
            RasterFormatException exception = new RasterFormatException(Errors.format(
                    ErrorKeys.BAD_TRANSFORM_$1, Classes.getClass(tr)));
            exception.initCause(cause);
            throw exception;
        }
    }

    /**
     * Returns a Well Known Text</cite> (WKT) for this object. This operation
     * may fails if an object is too complex for the WKT format capability.
     *
     * @return The Well Know Text for this object.
     * @throws UnsupportedOperationException If this object can't be formatted as WKT.
     *
     * @todo Not yet implemented.
     */
    public String toWKT() throws UnsupportedOperationException {
        throw new UnformattableObjectException("Not yet implemented.", getClass());
    }
}
