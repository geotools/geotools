/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2016, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Color;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import javax.measure.Unit;
import org.geotools.image.util.ColorUtilities;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.Errors;
import org.geotools.metadata.i18n.Vocabulary;
import org.geotools.metadata.i18n.VocabularyKeys;
import org.geotools.util.ClassChanger;
import org.geotools.util.Classes;
import org.geotools.util.NumberRange;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.Utilities;
import org.geotools.util.XArray;
import org.geotools.util.logging.Logging;
import org.opengis.coverage.ColorInterpretation;
import org.opengis.coverage.SampleDimension;
import org.opengis.coverage.SampleDimensionType;
import org.opengis.util.InternationalString;

/**
 * Describes the data values for a coverage as a list of {@linkplain Category categories}. For a
 * grid coverage a sample dimension is a band. Sample values in a band may be organized in
 * categories. This {@code GridSampleDimension} implementation is capable to differenciate
 * <em>qualitative</em> and <em>quantitative</em> categories. For example an image of sea surface
 * temperature (SST) could very well defines the following categories:
 *
 * <blockquote>
 *
 * <pre>
 *   [0]       : no data
 *   [1]       : cloud
 *   [2]       : land
 *   [10..210] : temperature to be converted into Celsius degrees through a linear equation
 * </pre>
 *
 * </blockquote>
 *
 * In this example, sample values in range {@code [10..210]} defines a quantitative category, while
 * all others categories are qualitative.
 *
 * <p>While this class can be used with arbitrary {@linkplain org.opengis.coverage.Coverage
 * coverage}, the primary target for this implementation is {@linkplain
 * org.opengis.coverage.grid.GridCoverage grid coverage} storing their sample values as integers.
 * This explain the "{@code Grid}" prefix in the class name.
 *
 * @since 2.1
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class GridSampleDimension implements SampleDimension, Serializable {
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = 6026936545776852758L;

    private static final double DELTA = 1E-10;

    /** The logger for grid sample dimensions. */
    public static final Logger LOGGER = Logging.getLogger(GridSampleDimension.class);

    /**
     * The category list for this sample dimension, or {@code null} if this sample dimension has no
     * category. This field is read by {@code SampleTranscoder} only.
     */
    final CategoryList categories;

    /**
     * {@code true} if this sample dimension has at least one qualitative category. An arbitrary
     * number of qualitative categories is allowed, providing their sample value ranges do not
     * overlap. A sample dimension can have both qualitative and quantitative categories.
     */
    private final boolean hasQualitative;

    /**
     * {@code true} if this sample dimension has at least one quantitative category. An arbitrary
     * number of quantitative categories is allowed, providing their sample value ranges do not
     * overlap.
     */
    private final boolean hasQuantitative;

    private double scale = 1;

    private double offset = 0;

    /**
     * Decription for this sample dimension. Typically used as a way to perform a band select by
     * using human comprehensible descriptions instead of just numbers. Web Coverage Service (WCS)
     * can use this feature in order to perform band subsetting as directed from a user request.
     */
    private final InternationalString description;

    /** Fallback unit information, used when the category list is not available */
    private Unit units;

    /**
     * Constructs a sample dimension with specified name and no category.
     *
     * @param description The sample dimension title or description, or {@code null} if none. This
     *     is the value to be returned by {@link #getDescription}.
     * @since 2.3
     */
    public GridSampleDimension(final CharSequence description) {
        this(description, (CategoryList) null, 1, 0);
    }

    /**
     * Constructs a sample dimension with a set of qualitative categories only. This constructor
     * expects only a sequence of category names for the values contained in a sample dimension.
     * This allows for names to be assigned to numerical values. The first entry in the sequence
     * relates to a cell value of zero. For example: [0]="Background", [1]="Water", [2]="Forest",
     * [3]="Urban". The created sample dimension will have no unit and a default set of colors.
     *
     * @param description The sample dimension title or description, or {@code null} for the default
     *     (the name of what looks like the "main" category). This is the value to be returned by
     *     {@link #getDescription}.
     * @param categoriesNames Sequence of category names for the values contained in a sample
     *     dimension, as {@link String} or {@link InternationalString} objects.
     * @since 2.3
     */
    public GridSampleDimension(
            final CharSequence description, final CharSequence[] categoriesNames) {
        // TODO: 'list(...)' should be inlined there if only Sun was to fix RFE #4093999
        // ("Relax constraint on placement of this()/super() call in constructors").
        this(description, list(categoriesNames), 1, 0);
    }

    /** Constructs a list of categories. Used by constructors only. */
    private static CategoryList list(final CharSequence[] names) {
        final int length = names.length;
        final Color[] colors = new Color[length];
        final double scale = 255.0 / length;
        for (int i = 0; i < length; i++) {
            final int r = (int) Math.round(scale * i);
            colors[i] = new Color(r, r, r);
        }
        return list(names, colors);
    }

    /**
     * Constructs a sample dimension with a set of qualitative categories and colors. This
     * constructor expects a sequence of category names for the values contained in a sample
     * dimension. This allows for names to be assigned to numerical values. The first entry in the
     * sequence relates to a cell value of zero. For example: [0]="Background", [1]="Water",
     * [2]="Forest", [3]="Urban". The created sample dimension will have no unit and a default set
     * of colors.
     *
     * @param description The sample dimension title or description, or {@code null} for the default
     *     (the name of what looks like the "main" category). This is the value to be returned by
     *     {@link #getDescription}.
     * @param names Sequence of category names for the values contained in a sample dimension, as
     *     {@link String} or {@link InternationalString} objects.
     * @param colors Color to assign to each category. This array must have the same length than
     *     {@code names}.
     * @since 2.3
     */
    public GridSampleDimension(
            final CharSequence description, final CharSequence[] names, final Color[] colors) {
        // TODO: 'list(...)' should be inlined there if only Sun was to fix RFE #4093999
        // ("Relax constraint on placement of this()/super() call in constructors").
        this(description, list(names, colors), 1, 0);
    }

    /** Constructs a list of categories. Used by constructors only. */
    private static CategoryList list(final CharSequence[] names, final Color[] colors) {
        if (names.length != colors.length) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.MISMATCHED_ARRAY_LENGTH));
        }
        final int length = names.length;
        final Category[] categories = new Category[length];
        for (int i = 0; i < length; i++) {
            categories[i] = new Category(names[i], colors[i], i);
        }
        return list(categories, null);
    }

    /**
     * Constructs a sample dimension with the specified properties. For convenience, any argument
     * which is not a {@code double} primitive can be {@code null}, and any {@linkplain CharSequence
     * char sequence} can be either a {@link String} or {@link InternationalString} object.
     *
     * <p>This constructor allows the construction of a {@code GridSampleDimension} without explicit
     * construction of {@link Category} objects. An heuristic approach is used for dispatching the
     * informations into a set of {@link Category} objects. However, this constructor still less
     * general and provides less fine-grain control than the constructor expecting an array of
     * {@link Category} objects.
     *
     * @param description The sample dimension title or description, or {@code null} for the default
     *     (the name of what looks like the "main" category). This is the value to be returned by
     *     {@link #getDescription}.
     * @param type The grid value data type (which indicate the number of bits for the data type),
     *     or {@code null} for computing it automatically from the range {@code [minimum..maximum]}.
     *     This is the value to be returned by {@link #getSampleDimensionType}.
     * @param palette The color palette associated with the sample dimension, or {@code null} for a
     *     default color palette (usually grayscale). If {@code categories} is non-null, then both
     *     arrays usually have the same length. However, this constructor is tolerant on this array
     *     length. This is the value to be returned (indirectly) by {@link #getColorModel}.
     * @param categories A sequence of category names for the values contained in the sample
     *     dimension, or {@code null} if none. This is the values to be returned by {@link
     *     #getCategoryNames}.
     * @param nodata the values to indicate "no data", or {@code null} if none. This is the values
     *     to be returned by {@link #getNoDataValues}.
     * @param minimum The lower value, inclusive. The {@code [minimum..maximum]} range may or may
     *     not includes the {@code nodata} values; the range will be adjusted as needed. If {@code
     *     categories} was non-null, then {@code minimum} is usually 0. This is the value to be
     *     returned by {@link #getMinimumValue}.
     * @param maximum The upper value, <strong>inclusive</strong> as well. The {@code
     *     [minimum..maximum]} range may or may not includes the {@code nodata} values; the range
     *     will be adjusted as needed. If {@code categories} was non-null, then {@code maximum} is
     *     usually equals to {@code categories.length-1}. This is the value to be returned by {@link
     *     #getMaximumValue}.
     * @param scale The value which is multiplied to grid values, or 1 if none. This is the value to
     *     be returned by {@link #getScale}.
     * @param offset The value to add to grid values, or 0 if none. This is the value to be returned
     *     by {@link #getOffset}.
     * @param unit The unit information for this sample dimension, or {@code null} if none. This is
     *     the value to be returned by {@link #getUnits}.
     * @throws IllegalArgumentException if the range {@code [minimum..maximum]} is not valid.
     */
    public GridSampleDimension(
            final CharSequence description,
            final SampleDimensionType type,
            final ColorInterpretation color,
            final Color[] palette,
            final CharSequence[] categories,
            final double[] nodata,
            final double minimum,
            final double maximum,
            final double scale,
            final double offset,
            final Unit<?> unit) {
        // TODO: 'list(...)' should be inlined there if only Sun was to fix RFE #4093999
        //       ("Relax constraint on placement of this()/super() call in constructors").
        this(
                description,
                list(description, type, color, palette, categories, nodata, minimum, maximum, unit),
                scale,
                offset);
    }

    /**
     * Constructs a sample dimension with the specified properties. For convenience, any argument
     * which is not a {@code double} primitive can be {@code null}, and any {@linkplain CharSequence
     * char sequence} can be either a {@link String} or {@link InternationalString} object.
     *
     * <p>This constructor allows the construction of a {@code GridSampleDimension} without explicit
     * construction of {@link Category} objects. An heuristic approach is used for dispatching the
     * informations into a set of {@link Category} objects. However, this constructor still less
     * general and provides less fine-grain control than the constructor expecting an array of
     * {@link Category} objects.
     *
     * @param description The sample dimension title or description, or {@code null} for the default
     *     (the name of what looks like the "main" category). This is the value to be returned by
     *     {@link #getDescription}.
     * @param type The grid value data type (which indicate the number of bits for the data type),
     *     or {@code null} for computing it automatically from the range {@code [minimum..maximum]}.
     *     This is the value to be returned by {@link #getSampleDimensionType}.
     * @param categories A sequence of category names for the values contained in the sample
     *     dimension, or {@code null} if none. This is the values to be returned by {@link
     *     #getCategoryNames}.
     * @param nodata the values to indicate "no data", or {@code null} if none. This is the values
     *     to be returned by {@link #getNoDataValues}.
     * @param minimum The lower value, inclusive. The {@code [minimum..maximum]} range may or may
     *     not includes the {@code nodata} values; the range will be adjusted as needed. If {@code
     *     categories} was non-null, then {@code minimum} is usually 0. This is the value to be
     *     returned by {@link #getMinimumValue}.
     * @param maximum The upper value, <strong>inclusive</strong> as well. The {@code
     *     [minimum..maximum]} range may or may not includes the {@code nodata} values; the range
     *     will be adjusted as needed. If {@code categories} was non-null, then {@code maximum} is
     *     usually equals to {@code categories.length-1}. This is the value to be returned by {@link
     *     #getMaximumValue}.
     * @param scale The value which is multiplied to grid values, or 1 if none. This is the value to
     *     be returned by {@link #getScale}.
     * @param offset The value to add to grid values, or 0 if none. This is the value to be returned
     *     by {@link #getOffset}.
     * @param unit The unit information for this sample dimension, or {@code null} if none. This is
     *     the value to be returned by {@link #getUnits}.
     * @throws IllegalArgumentException if the range {@code [minimum..maximum]} is not valid.
     */
    @SuppressWarnings("deprecation")
    public GridSampleDimension(
            final CharSequence description,
            final SampleDimensionType type,
            final CharSequence[] categories,
            final double[] nodata,
            final double minimum,
            final double maximum,
            final double scale,
            final double offset,
            final Unit<?> unit) {
        // TODO: 'list(...)' should be inlined there if only Sun was to fix RFE #4093999
        //       ("Relax constraint on placement of this()/super() call in constructors").
        this(
                description,
                list(description, type, null, null, categories, nodata, minimum, maximum, unit),
                scale,
                offset);
    }

    /** Constructs a list of categories. Used by constructors only. */
    @SuppressWarnings("deprecation")
    private static CategoryList list(
            CharSequence description,
            SampleDimensionType type,
            ColorInterpretation color,
            final Color[] palette,
            final CharSequence[] categories,
            final double[] nodata,
            double minimum,
            double maximum,
            final Unit<?> unit) {
        if (description == null) {
            description = Vocabulary.formatInternational(VocabularyKeys.UNTITLED);
        }
        if (Double.isInfinite(minimum) || Double.isInfinite(maximum) || !(minimum < maximum)) {
            throw new IllegalArgumentException(
                    Errors.format(ErrorKeys.BAD_RANGE_$2, minimum, maximum));
        }
        if (type == null) {
            type = TypeMap.getSampleDimensionType(minimum, maximum);
        }
        if (color == null) {
            color = ColorInterpretation.PALETTE_INDEX;
        }
        final int nameCount = (categories != null) ? categories.length : 0;
        final int nodataCount = (nodata != null) ? nodata.length : 0;
        final List<Category> categoryList = new ArrayList<Category>(nameCount + nodataCount + 2);
        /*
         * STEP 1 - Add a qualitative category for each 'nodata' value.
         *   NAME: Fetched from 'categories' if available, otherwise default to the value.
         *   COLOR: Fetched from 'palette' if available, otherwise use Category default.
         */
        for (int i = 0; i < nodataCount; i++) {
            CharSequence name = null;
            final double padValue = nodata[i];
            final int intValue = (int) Math.floor(padValue);
            if (intValue >= 0 && intValue < nameCount) {
                if (intValue == padValue) {
                    // This category will be added in step 2 below.
                    continue;
                }
                name = categories[intValue];
            }
            final Number value = TypeMap.wrapSample(padValue, type, false);
            if (name == null) {
                name = value.toString();
            }
            final NumberRange<?> range = new NumberRange(value.getClass(), value, value);
            final Color[] colors = ColorUtilities.subarray(palette, intValue, intValue + 1);
            categoryList.add(new Category(name, colors, range, false));
        }
        /*
         * STEP 2 - Add a qualitative category for each category name.
         *   RANGE: Fetched from the index (position) in the 'categories' array.
         *   COLOR: Fetched from 'palette' if available, otherwise use Category default.
         */
        if (nameCount != 0) {
            int lower = 0;
            final int length = categories.length;
            for (int upper = 1; upper <= length; upper++) {
                if (upper != length) {
                    final String nameLower = categories[lower].toString().trim();
                    final String nameUpper = categories[upper].toString().trim();
                    if (nameLower.equalsIgnoreCase(nameUpper)) {
                        /*
                         * If there is a suite of categories with identical name,  create only one
                         * category with range [lower..upper] instead of one new category for each
                         * sample value.
                         */
                        continue;
                    }
                }
                final CharSequence name = categories[lower];
                Number min = TypeMap.wrapSample(lower, type, false);
                Number max = TypeMap.wrapSample(upper - 1, type, false);
                final Class<? extends Number> classe;
                if (min.equals(max)) {
                    min = max;
                    classe = max.getClass();
                } else {
                    classe = ClassChanger.getWidestClass(min, max);
                    min = ClassChanger.cast(min, classe);
                    max = ClassChanger.cast(max, classe);
                }
                final NumberRange<?> range = new NumberRange(classe, min, max);
                final Color[] colors = ColorUtilities.subarray(palette, lower, upper);
                categoryList.add(new Category(name, colors, range, false));
                lower = upper;
            }
        }
        /*
         * STEP 3 - Changes some qualitative categories into quantitative ones.  The hard questions
         *          is: do we want to mark a category as "quantitative"?   OpenGIS has no notion of
         *          "qualitative" versus "quantitative" category. As an heuristic approach, we will
         *          look for quantitative category if:
         *
         *          - 'scale' and 'offset' do not map to an identity transform. Those
         *            coefficients can be stored in quantitative category only.
         *
         *          - 'nodata' were specified. If the user wants to declare "nodata" values,
         *            then we can reasonably assume that he have real values somewhere else.
         *
         *          - Only 1 category were created so far. A classified raster with only one
         *            category is useless. Consequently, it is probably a numeric raster instead.
         */
        boolean needQuantitative = false;
        if (nodataCount != 0 || categoryList.size() <= 1) {
            needQuantitative = true;
            for (int i = categoryList.size(); --i >= 0; ) {
                Category category = categoryList.get(i);
                if (!category.isQuantitative()) {
                    final NumberRange range = category.getRange();
                    final Comparable min = range.getMinValue();
                    final Comparable max = range.getMaxValue();
                    @SuppressWarnings("unchecked")
                    final int c = min.compareTo(max);
                    if (c != 0) {
                        final double xmin = ((Number) min).doubleValue();
                        final double xmax = ((Number) max).doubleValue();
                        if (!rangeContains(xmin, xmax, nodata)) {
                            final InternationalString name = category.getName();
                            final Color[] colors = category.getColors();
                            category = new Category(name, colors, range);
                            categoryList.set(i, category);
                            needQuantitative = false;
                        }
                    }
                }
            }
        }
        /*
         * STEP 4 - Create at most one quantitative category for the remaining sample values.
         *          The new category will range from 'minimum' to 'maximum' inclusive, minus
         *          all ranges used by previous categories.  If there is no range left, then
         *          no new category will be created.  This step will be executed only if the
         *          information provided by the user seem to be incomplete.
         *
         *          Note that substractions way break a range into many smaller ranges.
         *          The naive algorithm used here try to keep the widest range.
         */
        if (needQuantitative) {
            boolean minIncluded = true;
            boolean maxIncluded = true;
            for (int i = categoryList.size(); --i >= 0; ) {
                final NumberRange range = categoryList.get(i).getRange();
                final double min = range.getMinimum();
                final double max = range.getMaximum();
                if (max - minimum < maximum - min) {
                    if (max >= minimum) {
                        // We are loosing some sample values in
                        // the lower range because of nodata values.
                        minimum = max;
                        minIncluded = !range.isMaxIncluded();
                    }
                } else {
                    if (min <= maximum) {
                        // We are loosing some sample values in
                        // the upper range because of nodata values.
                        maximum = min;
                        maxIncluded = !range.isMinIncluded();
                    }
                }
            }
            // If the remaining range is wide enough, add the category.
            if (maximum - minimum > (minIncluded && maxIncluded ? 0 : 1)) {
                Number min = TypeMap.wrapSample(minimum, type, false);
                Number max = TypeMap.wrapSample(maximum, type, false);
                final Class<? extends Number> classe = ClassChanger.getWidestClass(min, max);
                min = ClassChanger.cast(min, classe);
                max = ClassChanger.cast(max, classe);
                final NumberRange range =
                        new NumberRange(classe, min, minIncluded, max, maxIncluded);
                final Color[] colors =
                        ColorUtilities.subarray(
                                palette, (int) Math.ceil(minimum), (int) Math.floor(maximum));
                categoryList.add(new Category(description, colors, range));
                needQuantitative = false;
            }
        }
        /*
         * STEP 5 - Now, the list of categories should be complete. Construct a
         *          sample dimension appropriate for the type of palette used.
         */
        final Category[] cl = categoryList.toArray(new Category[categoryList.size()]);
        if (ColorInterpretation.PALETTE_INDEX.equals(color)
                || ColorInterpretation.GRAY_INDEX.equals(color)) {
            return list(cl, unit);
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Constructs a sample dimension with an arbitrary set of categories, which may be both
     * quantitative and qualitative. It is possible to specify more than one quantitative
     * categories, providing that their sample value ranges do not overlap.
     *
     * @param description The sample dimension title or description, or {@code null} for the default
     *     (the name of what looks like the "main" category). This is the value to be returned by
     *     {@link #getDescription}.
     * @param categories The list of categories.
     * @param units The unit information for this sample dimension. May be {@code null} if no
     *     category has units.
     * @throws IllegalArgumentException if {@code categories} contains incompatible categories. If
     *     may be the case for example if two or more categories have overlapping ranges of sample
     *     values.
     * @since 2.3
     */
    public GridSampleDimension(CharSequence description, Category[] categories, Unit<?> units)
            throws IllegalArgumentException {
        // TODO: 'list(...)' should be inlined there if only Sun was to fix RFE #4093999
        // ("Relax constraint on placement of this()/super() call in constructors").
        this(description, list(categories, units), 1, 0);
        this.units = units;
    }

    /** Constructs a list of categories. Used by constructors only. */
    private static CategoryList list(final Category[] categories, final Unit<?> units) {
        if (categories == null || categories.length == 0) {
            return null;
        }
        final CategoryList list = new CategoryList(categories, units);
        return list;
    }

    /**
     * Constructs a sample dimension with the specified list of categories.
     *
     * @param description The sample dimension title or description, or {@code null} for the default
     *     (the name of what looks like the "main" category). This is the value to be returned by
     *     {@link #getDescription}.
     * @param list The list of categories, or {@code null}.
     */
    private GridSampleDimension(
            final CharSequence description, final CategoryList list, double scale, double offset) {
        /*
         * Checks the supplied description to see if it is null. In such a case it builds up a new
         * description by using the list of categories supplied. This second description may be less
         * human readable and it is therefore better if the user provide a meaningful name for this
         * sample dimension.
         */
        if (description != null) {
            this.description = SimpleInternationalString.wrap(description);
        } else {
            // we need to build one. Let's use the category list in
            // order to build the name of the sample dimension
            if (list != null) {
                this.description = list.getName();
            } else {
                this.description = Vocabulary.formatInternational(VocabularyKeys.UNTITLED);
            }
        }
        /*
         * Now process to the category examination.
         */
        boolean qualitative = false;
        int quantitative = 0;
        this.scale = scale;
        this.offset = offset;
        if (list != null) {
            for (int i = list.size(); --i >= 0; ) {
                Category category = list.get(i);
                if (!category.isQuantitative()) {
                    qualitative = true;
                    continue;
                } else {
                    quantitative++;
                }
            }
        }
        this.categories = list;
        this.hasQualitative = qualitative;
        this.hasQuantitative = quantitative > 0;
    }

    /**
     * Constructs a new sample dimension with the same categories and units than the specified
     * sample dimension.
     *
     * @param other The other sample dimension, or {@code null}.
     */
    protected GridSampleDimension(final GridSampleDimension other) {
        if (other != null) {
            categories = other.categories;
            hasQualitative = other.hasQualitative;
            hasQuantitative = other.hasQuantitative;
            scale = other.scale;
            offset = other.offset;
            description = other.getDescription();
            this.units = other.units;
        } else {
            categories = null;
            hasQualitative = false;
            hasQuantitative = false;
            description = Vocabulary.formatInternational(VocabularyKeys.UNTITLED);
        }
    }

    public GridSampleDimension(
            String description, Category[] categories, double scale, double offset) {
        this(description, list(categories, null), scale, offset);
    }

    /**
     * Wraps the specified OpenGIS's sample dimension into a Geotools's implementation of {@code
     * GridSampleDimension}.
     *
     * @param sd The sample dimension to wrap into a Geotools implementation.
     * @return The given sample dimension as a {@code GridSampleDimension} instance.
     */
    public static GridSampleDimension wrap(final SampleDimension sd) {
        if (sd instanceof GridSampleDimension) {
            return (GridSampleDimension) sd;
        }
        return new GridSampleDimension(
                sd.getDescription(),
                sd.getSampleDimensionType(),
                sd.getCategoryNames(),
                sd.getNoDataValues(),
                sd.getMinimumValue(),
                sd.getMaximumValue(),
                sd.getScale(),
                sd.getOffset(),
                sd.getUnits());
    }

    /**
     * Returns a code value indicating grid value data type. This will also indicate the number of
     * bits for the data type.
     *
     * @return A code value indicating grid value data type.
     */
    @SuppressWarnings("unchecked")
    public SampleDimensionType getSampleDimensionType() {
        final NumberRange range = getRange();
        if (range == null) {
            return SampleDimensionType.REAL_32BITS;
        }
        return TypeMap.getSampleDimensionType(range);
    }

    /**
     * Gets the sample dimension title or description. This string may be {@code null} if no
     * description is present.
     *
     * @return The title or description of this sample dimension.
     */
    public InternationalString getDescription() {
        return description;
    }

    /**
     * Returns a sequence of category names for the values contained in this sample dimension. This
     * allows for names to be assigned to numerical values. The first entry in the sequence relates
     * to a cell value of zero. For example:
     *
     * <blockquote>
     *
     * <pre>
     *    [0] Background
     *    [1] Water
     *    [2] Forest
     *    [3] Urban
     *  </pre>
     *
     * </blockquote>
     *
     * @return The sequence of category names for the values contained in this sample dimension, or
     *     {@code null} if there is no category in this sample dimension.
     * @throws IllegalStateException if a sequence can't be mapped because some category use
     *     negative or non-integer sample values.
     * @see #getCategories
     * @see #getCategory
     */
    public InternationalString[] getCategoryNames() throws IllegalStateException {
        if (categories == null) {
            return null;
        }
        if (categories.isEmpty()) {
            return new InternationalString[0];
        }
        InternationalString[] names = null;
        for (int i = categories.size(); --i >= 0; ) {
            final Category category = categories.get(i);
            final int lower = (int) category.minimum;
            final int upper = (int) category.maximum;
            if (lower != category.minimum || lower < 0 || upper != category.maximum || upper < 0) {
                throw new IllegalStateException(Errors.format(ErrorKeys.NON_INTEGER_CATEGORY));
            }
            if (names == null) {
                names = new InternationalString[upper + 1];
            }
            Arrays.fill(names, lower, upper + 1, category.getName());
        }
        return names;
    }

    /**
     * Returns all categories in this sample dimension. Note that a {@link Category} object may
     * apply to an arbitrary range of sample values. Consequently, the first element in this
     * collection may not be directly related to the sample value {@code 0}.
     *
     * @return The list of categories in this sample dimension, or {@code null} if none.
     * @see #getCategoryNames
     * @see #getCategory
     */
    public List<Category> getCategories() {
        if (categories == null) {
            return Collections.emptyList();
        } else {
            return categories;
        }
    }

    /**
     * Returns the category for the specified sample value. If this method can't maps a category to
     * the specified value, then it returns {@code null}.
     *
     * @param sample The value (can be one of {@code NaN} values).
     * @return The category for the supplied value, or {@code null} if none.
     * @see #getCategories
     * @see #getCategoryNames
     */
    public Category getCategory(final double sample) {
        return (categories != null) ? categories.getCategory(sample) : null;
    }

    /**
     * Returns the values to indicate "no data" for this sample dimension. The default
     * implementation deduces the "no data" values from the list of categories supplied at
     * construction time.
     *
     * @return The values to indicate no data values for this sample dimension, or {@code null} if
     *     not applicable.
     * @throws IllegalStateException if some qualitative categories use a range of non-integer
     *     values.
     */
    public double[] getNoDataValues() throws IllegalStateException {
        if (!hasQuantitative) {
            return null;
        }
        int count = 0;
        double[] padValues = null;
        final int size = categories.size();
        for (int i = 0; i < size; i++) {
            final Category category = categories.get(i);
            if (category.getName().equals(Category.NODATA.getName())) {
                final double min = category.minimum;
                final double max = category.maximum;
                if (Double.isNaN(min) && Double.isNaN(max)) {
                    return new double[] {min};
                } else if (Math.abs(min - max) < DELTA) {
                    return new double[] {min};
                } else {
                    return new double[] {min, max};
                }
            }
            if (!category.isQuantitative()) {
                final double min = category.minimum;
                final double max = category.maximum;
                if (!Double.isNaN(min) || !Double.isNaN(max)) {
                    if (padValues == null) {
                        padValues = new double[size - i];
                    }
                    if (count >= padValues.length) {
                        padValues = XArray.resize(padValues, count * 2);
                    }
                    padValues[count++] = min;
                    /*
                     * The "no data" value has been extracted. Now, check if we have a range
                     * of "no data" values instead of a single one for this category.  If we
                     * have a single value, it can be of any type. But if we have a range,
                     * then it must be a range of integers (otherwise we can't expand it).
                     */
                    if (max != min) {
                        int lower = (int) min;
                        int upper = (int) max;
                        if (lower != min
                                || upper != max
                                || !Classes.isInteger(category.getRange().getElementClass())) {
                            throw new IllegalStateException(
                                    Errors.format(ErrorKeys.NON_INTEGER_CATEGORY));
                        }
                        final int requiredLength = count + (upper - lower);
                        if (requiredLength > padValues.length) {
                            padValues = XArray.resize(padValues, requiredLength * 2);
                        }
                        while (++lower <= upper) {
                            padValues[count++] = lower;
                        }
                    }
                }
            }
        }
        if (padValues != null) {
            padValues = XArray.resize(padValues, count);
        }
        return padValues;
    }

    /**
     * Returns the minimum value occurring in this sample dimension (inclusive). The default
     * implementation fetch this value from the categories supplied at construction time. If the
     * minimum value can't be computed, then this method returns {@link Double#NEGATIVE_INFINITY}.
     *
     * @see #getRange
     */
    public double getMinimumValue() {
        if (categories != null && !categories.isEmpty()) {
            for (int i = 0; i < categories.size(); i++) {
                Category cat = categories.get(i);
                if (!Category.NODATA.getName().equals(cat.getName())) {
                    // Exclude the value of the NODATA category
                    // which can be retrieved with getNoDataValues
                    final double value = cat.minimum;
                    if (!Double.isNaN(value)) {
                        return value;
                    }
                }
            }
        }
        return Double.NEGATIVE_INFINITY;
    }

    /**
     * Returns the maximum value occurring in this sample dimension (inclusive). The default
     * implementation fetch this value from the categories supplied at construction time. If the
     * maximum value can't be computed, then this method returns {@link Double#POSITIVE_INFINITY}.
     *
     * @see #getRange
     */
    public double getMaximumValue() {
        if (categories != null && !categories.isEmpty()) {
            for (int i = categories.size(); --i >= 0; ) {
                Category cat = categories.get(i);
                if (!Category.NODATA.getName().equals(cat.getName())) {
                    // Exclude the value of the NODATA category
                    // which can be retrieved with getNoDataValues
                    final double value = cat.maximum;
                    if (!Double.isNaN(value)) {
                        return value;
                    }
                }
            }
        }
        return Double.POSITIVE_INFINITY;
    }

    /**
     * Returns the range of values in this sample dimension. This is the union of the range of
     * values of every categories, excluding {@code NaN} values. A {@link NumberRange} object gives
     * more informations than {@link #getMinimumValue} and {@link #getMaximumValue} methods since it
     * contains also the data type (integer, float, etc.) and inclusion/exclusion informations.
     *
     * @return The range of values. May be {@code null} if this sample dimension has no quantitative
     *     category.
     * @see Category#getRange
     * @see #getMinimumValue
     * @see #getMaximumValue
     * @todo We should do a better job in {@code CategoryList.getRange()} when selecting the
     *     appropriate data type. {@link TypeMap#getSampleDimensionType} may be of some help.
     */
    public NumberRange<? extends Number> getRange() {
        return (categories != null) ? categories.getRange() : null;
    }

    /**
     * Returns {@code true} if at least one value of {@code values} is in the range {@code lower}
     * inclusive to {@code upper} exclusive.
     */
    private static boolean rangeContains(
            final double lower, final double upper, final double[] values) {
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                final double v = values[i];
                if (v >= lower && v < upper) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns a string representation of a sample value. This method try to returns a
     * representation of the geophysics value; the transformation is automatically applied when
     * necessary. More specifically:
     *
     * <ul>
     *   <li>If {@code value} maps a qualitative category, then the category name is returned as of
     *       {@link Category#getName}.
     *   <li>Otherwise, if {@code value} maps a quantitative category, then the value is formatted
     *       as a number and the unit symbol is appened.
     * </ul>
     *
     * @param value The sample value (can be one of {@code NaN} values).
     * @param locale Locale to use for formatting, or {@code null} for the default locale.
     * @return A string representation of the geophysics value, or {@code null} if there is none.
     * @todo What should we do when the value can't be formatted? {@code GridSampleDimension}
     *     returns {@code null} if there is no category or if an exception is thrown, but {@code
     *     CategoryList} returns "Untitled" if the value is an unknow NaN, and try to format the
     *     number anyway in other cases.
     */
    public String getLabel(final double value, final Locale locale) {
        if (categories != null) {
            return categories.format(value, locale);
        }
        return null;
    }

    /**
     * Returns the unit information for this sample dimension. May returns {@code null} if this
     * dimension has no units.
     */
    public Unit<?> getUnits() {
        return (categories != null) ? categories.getUnits() : units;
    }

    /**
     * Returns the value to add to grid values for this sample dimension. This attribute is
     * typically used when the sample dimension represents elevation data. The transformation
     * equation is:
     *
     * <blockquote>
     *
     * <pre>offset + scale*sample</pre>
     *
     * </blockquote>
     *
     * Together with {@link #getScale()} and {@link #getNoDataValues()}, this method provides a
     * limited way to transform sample values into geophysics values.
     *
     * @return The offset to add to grid values.
     */
    public double getOffset() {
        return offset;
    }

    /**
     * Returns the value which is multiplied to grid values for this sample dimension. This
     * attribute is typically used when the sample dimension represents elevation data. The
     * transformation equation is:
     *
     * <blockquote>
     *
     * <pre>offset + scale*sample</pre>
     *
     * </blockquote>
     *
     * Together with {@link #getOffset()} and {@link #getNoDataValues()}, this method provides a
     * limited way to transform sample values into geophysics values.
     *
     * @return The scale to multiply to grid value.
     */
    public double getScale() {
        return scale;
    }

    /**
     * Returns the color interpretation of the sample dimension. A sample dimension can be an index
     * into a color palette or be a color model component. If the sample dimension is not assigned a
     * color interpretation the value is {@link ColorInterpretation#UNDEFINED}.
     */
    public ColorInterpretation getColorInterpretation() {
        // The 'Grid2DSampleDimension' class overrides this method
        // with better values for 'band' and 'numBands' constants.
        final int band = 0;
        final int numBands = 1;
        return TypeMap.getColorInterpretation(getColorModel(band, numBands), band);
    }

    /**
     * Returns a color model for this sample dimension. The default implementation create a color
     * model with 1 band using each category's colors as returned by {@link Category#getColors}.
     *
     * <p>Note that {@link org.geotools.coverage.grid.GridCoverage2D#getSampleDimension} returns
     * special implementations of {@code GridSampleDimension}. In this particular case, the color
     * model created by this {@code getColorModel()} method will have the same number of bands than
     * the grid coverage's {@link java.awt.image.RenderedImage}.
     *
     * @return The requested color model, suitable for {@link java.awt.image.RenderedImage} objects
     *     with values in the <code>{@link #getRange}</code> range. May be {@code null} if this
     *     sample dimension has no category.
     */
    public ColorModel getColorModel() {
        // The 'Grid2DSampleDimension' class overrides this method
        // with better values for 'band' and 'numBands' constants.
        final int band = 0;
        final int numBands = 1;
        return getColorModel(band, numBands);
    }

    /**
     * Returns a color model for this sample dimension. The default implementation create the color
     * model using each category's colors as returned by {@link Category#getColors}.
     *
     * @param visibleBand The band to be made visible (usually 0). All other bands, if any will be
     *     ignored.
     * @param numBands The number of bands for the color model (usually 1). The returned color model
     *     will renderer only the {@code visibleBand} and ignore the others, but the existence of
     *     all {@code numBands} will be at least tolerated. Supplemental bands, even invisible, are
     *     useful for processing with Java Advanced Imaging.
     * @return The requested color model, suitable for {@link java.awt.image.RenderedImage} objects
     *     with values in the <code>{@link #getRange}</code> range. May be {@code null} if this
     *     sample dimension has no category.
     * @todo This method may be deprecated in a future version. It it strange to use only one {@code
     *     SampleDimension} object for creating a multi-bands color model. Logically, we would
     *     expect as many {@code SampleDimension}s as bands.
     */
    public ColorModel getColorModel(final int visibleBand, final int numBands) {
        if (categories != null) {
            if (hasQualitative) {
                // Data likely to have NaN values, which require a floating point type.
                return categories.getColorModel(visibleBand, numBands, DataBuffer.TYPE_FLOAT);
            }
            return categories.getColorModel(visibleBand, numBands);
        }
        return null;
    }

    /**
     * Returns a color model for this sample dimension. The default implementation create the color
     * model using each category's colors as returned by {@link Category#getColors}.
     *
     * @param visibleBand The band to be made visible (usually 0). All other bands, if any will be
     *     ignored.
     * @param numBands The number of bands for the color model (usually 1). The returned color model
     *     will renderer only the {@code visibleBand} and ignore the others, but the existence of
     *     all {@code numBands} will be at least tolerated. Supplemental bands, even invisible, are
     *     useful for processing with Java Advanced Imaging.
     * @param type The data type that has to be used for the sample model.
     * @return The requested color model, suitable for {@link java.awt.image.RenderedImage} objects
     *     with values in the <code>{@link #getRange}</code> range. May be {@code null} if this
     *     sample dimension has no category.
     * @todo This method may be deprecated in a future version. It it strange to use only one {@code
     *     SampleDimension} object for creating a multi-bands color model. Logically, we would
     *     expect as many {@code SampleDimension}s as bands.
     */
    public ColorModel getColorModel(final int visibleBand, final int numBands, final int type) {
        if (categories != null) {
            return categories.getColorModel(visibleBand, numBands, type);
        }
        return null;
    }

    /**
     * Returns a hash value for this sample dimension. This value need not remain consistent between
     * different implementations of the same class.
     */
    @Override
    public int hashCode() {
        return (categories != null) ? categories.hashCode() : (int) serialVersionUID;
    }

    /**
     * Compares the specified object with this sample dimension for equality.
     *
     * @param object The object to compare with.
     * @return {@code true} if the given object is equals to this sample dimension.
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            // Slight optimization
            return true;
        }
        if (object instanceof GridSampleDimension) {
            final GridSampleDimension that = (GridSampleDimension) object;
            // two dimensions are equal if they have de same description (name)
            // and same categories lists
            return this.description.equals(that.description)
                    && Utilities.equals(this.categories, that.categories);
        }
        return false;
    }

    /**
     * Returns a string representation of this sample dimension. This string is for debugging
     * purpose only and may change in future version. The default implementation format the sample
     * value range, then the list of categories. A "*" mark is put in front of what seems the "main"
     * category.
     */
    @Override
    public String toString() {
        if (categories != null) {
            return categories.toString(this, description);
        } else {
            return Classes.getShortClassName(this) + "[\"" + description + "\"]";
        }
    }
}
