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

import java.awt.Color;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.measure.unit.Unit;
import javax.media.jai.JAI;

import org.geotools.referencing.operation.transform.LinearTransform1D;
import org.geotools.resources.ClassChanger;
import org.geotools.resources.Classes;
import org.geotools.resources.XArray;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.resources.image.ColorUtilities;
import org.geotools.util.NumberRange;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.Utilities;
import org.opengis.coverage.ColorInterpretation;
import org.opengis.coverage.PaletteInterpretation;
import org.opengis.coverage.SampleDimension;
import org.opengis.coverage.SampleDimensionType;
import org.opengis.referencing.operation.MathTransform1D;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.InternationalString;


/**
 * Describes the data values for a coverage as a list of {@linkplain Category categories}. For
 * a grid coverage a sample dimension is a band. Sample values in a band may be organized in
 * categories. This {@code GridSampleDimension} implementation is capable to differenciate
 * <em>qualitative</em> and <em>quantitative</em> categories. For example an image of sea surface
 * temperature (SST) could very well defines the following categories:
 *
 * <blockquote><pre>
 *   [0]       : no data
 *   [1]       : cloud
 *   [2]       : land
 *   [10..210] : temperature to be converted into Celsius degrees through a linear equation
 * </pre></blockquote>
 *
 * In this example, sample values in range {@code [10..210]} defines a quantitative category,
 * while all others categories are qualitative. The difference between those two kinds of category
 * is that the {@link Category#getSampleToGeophysics} method returns a non-null transform if and
 * only if the category is quantitative.
 * <p>
 * While this class can be used with arbitrary {@linkplain org.opengis.coverage.Coverage coverage},
 * the primary target for this implementation is {@linkplain org.opengis.coverage.grid.GridCoverage
 * grid coverage} storing their sample values as integers. This explain the "{@code Grid}" prefix
 * in the class name.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class GridSampleDimension implements SampleDimension, Serializable {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 6026936545776852758L;

    /**
     * A sample dimension wrapping the list of categories {@code CategoryList.inverse}.
     * This object is constructed and returned by {@link #geophysics}. Constructed when first
     * needed, but serialized anyway because it may be a user-supplied object.
     */
    private GridSampleDimension inverse;

    /**
     * The category list for this sample dimension, or {@code null} if this sample
     * dimension has no category. This field is read by {@code SampleTranscoder} only.
     */
    final CategoryList categories;

    /**
     * {@code true} if all categories in this sample dimension have been already scaled
     * to geophysics ranges. If {@code true}, then the {@link #getSampleToGeophysics()}
     * method should returns an identity transform. Note that the opposite do not always hold:
     * an identity transform doesn't means that all categories are geophysics. For example,
     * some qualitative categories may map to some values differents than {@code NaN}.
     * <p>
     * Assertions:
     *  <ul>
     *    <li>{@code isGeophysics} == {@code categories.isGeophysics(true)}.</li>
     *    <li>{@code isGeophysics} != {@code categories.isGeophysics(false)}, except
     *        if {@code categories.geophysics(true) == categories.geophysics(false)}</li>
     * </ul>
     */
    private final boolean isGeophysics;

    /**
     * {@code true} if this sample dimension has at least one qualitative category. An arbitrary
     * number of qualitative categories is allowed, providing their sample value ranges do not
     * overlap. A sample dimension can have both qualitative and quantitative categories.
     */
    private final boolean hasQualitative;

    /**
     * {@code true} if this sample dimension has at least one quantitative category.
     * An arbitrary number of quantitative categories is allowed, providing their sample
     * value ranges do not overlap.
     * <p>
     * If {@code sampleToGeophysics} is non-null, then {@code hasQuantitative}
     * <strong>must</strong> be true.  However, the opposite do not hold in all cases: a
     * {@code true} value doesn't means that {@code sampleToGeophysics} should be non-null.
     */
    private final boolean hasQuantitative;

    /**
     * The {@link Category#getSampleToGeophysics sampleToGeophysics} transform used by every
     * quantitative {@link Category}, or {@code null}. This field may be null for two reasons:
     *
     * <ul>
     *   <li>There is no quantitative category in this sample dimension.</li>
     *   <li>There is more than one quantitative category, and all of them don't use the same
     *       {@link Category#getSampleToGeophysics sampleToGeophysics} transform.</li>
     * </ul>
     *
     * This field is used by {@link #getOffset} and {@link #getScale}. The
     * {@link #getSampleToGeophysics} method may also returns directly this
     * value in some conditions.
     */
    private final MathTransform1D sampleToGeophysics;

    /**
     * Decription for this sample dimension. Typically used as a way to perform a band select by
     * using human comprehensible descriptions instead of just numbers. Web Coverage Service (WCS)
     * can use this feature in order to perform band subsetting as directed from a user request.
     */
    private final InternationalString description;

    /**
     * Constructs a sample dimension with specified name and no category.
     *
     * @param description
     *            The sample dimension title or description, or {@code null} if
     *            none. This is the value to be returned by {@link #getDescription}.
     *
     * @since 2.3
     */
    public GridSampleDimension(final CharSequence description) {
        this(description, (CategoryList) null);
    }

    /**
     * Constructs a sample dimension with a set of qualitative categories only. This constructor
     * expects only a sequence of category names for the values contained in a sample dimension.
     * This allows for names to be assigned to numerical values. The first entry in the sequence
     * relates to a cell value of zero. For example: [0]="Background", [1]="Water", [2]="Forest",
     * [3]="Urban". The created sample dimension will have no unit and a default set of colors.
     *
     * @param description
     *            The sample dimension title or description, or {@code null} for the default
     *            (the name of what looks like the "main" category). This is the value to be
     *            returned by {@link #getDescription}.
     * @param categoriesNames
     *            Sequence of category names for the values contained in a sample dimension,
     *            as {@link String} or {@link InternationalString} objects.
     *
     * @since 2.3
     */
    public GridSampleDimension(final CharSequence description,
                               final CharSequence[] categoriesNames)
    {
        // TODO: 'list(...)' should be inlined there if only Sun was to fix RFE #4093999
        // ("Relax constraint on placement of this()/super() call in constructors").
        this(description, list(categoriesNames));
    }

    /** Constructs a list of categories. Used by constructors only. */
    private static CategoryList list(final CharSequence[] names) {
        final int length = names.length;
        final Color[] colors = new Color[length];
        final double scale = 255.0 / length;
        for (int i=0; i<length; i++) {
            final int r = (int) Math.round(scale * i);
            colors[i] = new Color(r, r, r);
        }
        return list(names, colors);
    }

    /**
     * Constructs a sample dimension with a set of qualitative categories and colors. This
     * constructor expects a sequence of category names for the values contained in a sample
     * dimension. This allows for names to be assigned to numerical values. The first entry in
     * the sequence relates to a cell value of zero. For example: [0]="Background", [1]="Water",
     * [2]="Forest", [3]="Urban". The created sample dimension will have no unit and a default
     * set of colors.
     *
     * @param description
     *            The sample dimension title or description, or {@code null} for the default
     *            (the name of what looks like the "main" category). This is the value to be
     *            returned by {@link #getDescription}.
     * @param names
     *            Sequence of category names for the values contained in a sample dimension,
     *            as {@link String} or {@link InternationalString} objects.
     * @param colors
     *            Color to assign to each category. This array must have the
     *            same length than {@code names}.
     *
     * @since 2.3
     */
    public GridSampleDimension(final CharSequence description,
                               final CharSequence[] names,
                               final Color[] colors)
    {
        // TODO: 'list(...)' should be inlined there if only Sun was to fix RFE #4093999
        // ("Relax constraint on placement of this()/super() call in constructors").
        this(description, list(names, colors));
    }

    /** Constructs a list of categories. Used by constructors only. */
    private static CategoryList list(final CharSequence[] names, final Color[] colors) {
        if (names.length != colors.length) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.MISMATCHED_ARRAY_LENGTH));
        }
        final int length = names.length;
        final Category[] categories = new Category[length];
        for (int i=0; i<length; i++) {
            categories[i] = new Category(names[i], colors[i], i);
        }
        return list(categories, null);
    }

    /**
     * Constructs a sample dimension with the specified properties. For convenience,
     * any argument which is not a {@code double} primitive can be {@code null}, and
     * any {@linkplain CharSequence char sequence} can be either a {@link String}
     * or {@link InternationalString} object.
     * <p>
     * This constructor allows the construction of a {@code GridSampleDimension} without explicit
     * construction of {@link Category} objects. An heuristic approach is used for dispatching the
     * informations into a set of {@link Category} objects. However, this constructor still less
     * general and provides less fine-grain control than the constructor expecting an array of
     * {@link Category} objects.
     *
     * @param description
     *            The sample dimension title or description, or {@code null} for the default
     *            (the name of what looks like the "main" category). This is the value to be
     *            returned by {@link #getDescription}.
     * @param type
     *            The grid value data type (which indicate the number of bits for the data type),
     *            or {@code null} for computing it automatically from the range
     *            {@code [minimum..maximum]}. This is the value to be returned by
     *            {@link #getSampleDimensionType}.
     * @param color
     *            The color interpretation, or {@code null} for a default value (usually
     *            {@link ColorInterpretation#PALETTE_INDEX PALETTE_INDEX}). This is the
     *            value to be returned by {@link #getColorInterpretation}.
     * @param palette
     *            The color palette associated with the sample dimension, or {@code null} for a
     *            default color palette (usually grayscale). If {@code categories} is non-null,
     *            then both arrays usually have the same length. However, this constructor is
     *            tolerant on this array length. This is the value to be returned (indirectly)
     *            by {@link #getColorModel}.
     * @param categories
     *            A sequence of category names for the values contained in the
     *            sample dimension, or {@code null} if none. This is the values
     *            to be returned by {@link #getCategoryNames}.
     * @param nodata
     *            the values to indicate "no data", or {@code null} if none.
     *            This is the values to be returned by {@link #getNoDataValues}.
     * @param minimum
     *            The lower value, inclusive. The {@code [minimum..maximum]} range may or may not
     *            includes the {@code nodata} values; the range will be adjusted as needed. If
     *            {@code categories} was non-null, then {@code minimum} is usually 0. This is the
     *            value to be returned by {@link #getMinimumValue}.
     * @param maximum
     *            The upper value, <strong>inclusive</strong> as well. The {@code [minimum..maximum]}
     *            range may or may not includes the {@code nodata} values; the range will be adjusted
     *            as needed. If {@code categories} was non-null, then {@code maximum} is usually
     *            equals to {@code categories.length-1}. This is the value to be returned by
     *            {@link #getMaximumValue}.
     * @param scale
     *            The value which is multiplied to grid values, or 1 if none.
     *            This is the value to be returned by {@link #getScale}.
     * @param offset
     *            The value to add to grid values, or 0 if none. This is the
     *            value to be returned by {@link #getOffset}.
     * @param unit
     *            The unit information for this sample dimension, or {@code null} if none.
     *            This is the value to be returned by {@link #getUnits}.
     *
     * @throws IllegalArgumentException
     *             if the range {@code [minimum..maximum]} is not valid.
     */
    @SuppressWarnings("deprecation")
	public GridSampleDimension(final CharSequence  description,
                               final SampleDimensionType  type,
                               final ColorInterpretation color,
                               final Color[]           palette,
                               final CharSequence[] categories,
                               final double[]           nodata,
                               final double            minimum,
                               final double            maximum,
                               final double              scale,
                               final double             offset,
                               final Unit<?>              unit)
    {
        // TODO: 'list(...)' should be inlined there if only Sun was to fix RFE #4093999
        //       ("Relax constraint on placement of this()/super() call in constructors").
        this(description, list(description, type, color, palette, categories, nodata,
                               minimum, maximum, scale, offset, unit));
    }

    /** Constructs a list of categories. Used by constructors only. */
    @SuppressWarnings("deprecation")
	private static CategoryList list(CharSequence  description,
                                     SampleDimensionType  type,
                                     ColorInterpretation color,
                               final Color[]           palette,
                               final CharSequence[] categories,
                               final double[]           nodata,
                                     double            minimum,
                                     double            maximum,
                               final double              scale,
                               final double             offset,
                               final Unit<?>              unit)
    {
        if (description == null) {
            description = Vocabulary.formatInternational(VocabularyKeys.UNTITLED);
        }
        if (Double.isInfinite(minimum) || Double.isInfinite(maximum) || !(minimum < maximum)) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.BAD_RANGE_$2, minimum, maximum));
        }
        if (Double.isNaN(scale) || Double.isInfinite(scale) || scale == 0) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.BAD_PARAMETER_$2, "scale", scale));
        }
        if (Double.isNaN(offset) || Double.isInfinite(offset)) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.BAD_PARAMETER_$2, "offset", offset));
        }
        if (type == null) {
            type = TypeMap.getSampleDimensionType(minimum, maximum);
        }
        if (color == null) {
            color = ColorInterpretation.PALETTE_INDEX;
        }
        final int  nameCount    = (categories != null) ? categories.length : 0;
        final int  nodataCount  = (nodata     != null) ?     nodata.length : 0;
        final List<Category> categoryList = new ArrayList<Category>(nameCount + nodataCount + 2);
        /*
         * STEP 1 - Add a qualitative category for each 'nodata' value.
         *   NAME: Fetched from 'categories' if available, otherwise default to the value.
         *   COLOR: Fetched from 'palette' if available, otherwise use Category default.
         */
        for (int i=0; i<nodataCount; i++) {
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
            categoryList.add(new Category(name, colors, range, (MathTransform1D) null));
        }
        /*
         * STEP 2 - Add a qualitative category for each category name.
         *   RANGE: Fetched from the index (position) in the 'categories' array.
         *   COLOR: Fetched from 'palette' if available, otherwise use Category default.
         */
        if (nameCount != 0) {
            int lower = 0;
            final int length = categories.length;
            for (int upper=1; upper<=length; upper++) {
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
                Number min = TypeMap.wrapSample(lower,   type, false);
                Number max = TypeMap.wrapSample(upper-1, type, false);
                final Class<? extends Number> classe;
                if (min.equals(max)) {
                    min = max;
                    classe = max.getClass();
                } else {
                    classe = ClassChanger.getWidestClass(min, max);
                    min    = ClassChanger.cast(min, classe);
                    max    = ClassChanger.cast(max, classe);
                }
                final NumberRange<?> range = new NumberRange(classe, min, max);
                final Color[] colors = ColorUtilities.subarray(palette, lower, upper);
                categoryList.add(new Category(name, colors, range, (MathTransform1D) null));
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
        if (scale != 1 || offset != 0 || nodataCount != 0 || categoryList.size() <= 1) {
            needQuantitative = true;
            for (int i = categoryList.size(); --i >= 0;) {
                Category category = categoryList.get(i);
                if (!category.isQuantitative()) {
                    final NumberRange range = category.getRange();
                    final Comparable  min   = range.getMinValue();
                    final Comparable  max   = range.getMaxValue();
                    @SuppressWarnings("unchecked")
                    final int c = min.compareTo(max);
                    if (c != 0) {
                        final double xmin = ((Number) min).doubleValue();
                        final double xmax = ((Number) max).doubleValue();
                        if (!rangeContains(xmin, xmax, nodata)) {
                            final InternationalString name = category.getName();
                            final Color[] colors = category.getColors();
                            category = new Category(name, colors, range, scale, offset);
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
            for (int i = categoryList.size(); --i >= 0;) {
                final NumberRange range = categoryList.get(i).getRange();
                final double min = range.getMinimum();
                final double max = range.getMaximum();
                if (max-minimum < maximum-min) {
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
                final NumberRange range = new NumberRange(classe, min, minIncluded,
                                                                  max, maxIncluded);
                final Color[] colors = ColorUtilities.subarray(palette,
                        (int) Math.ceil(minimum), (int) Math.floor(maximum));
                categoryList.add(new Category(description, colors, range, scale, offset));
                needQuantitative = false;
            }
        }
        /*
         * STEP 5 - Now, the list of categories should be complete. Construct a
         *          sample dimension appropriate for the type of palette used.
         */
        final Category[] cl = categoryList.toArray(new Category[categoryList.size()]);
        if (ColorInterpretation.PALETTE_INDEX.equals(color) ||
            ColorInterpretation.GRAY_INDEX.equals(color))
        {
            return list(cl, unit);
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Constructs a sample dimension with an arbitrary set of categories, which may be both
     * quantitative and qualitative. It is possible to specify more than one quantitative
     * categories, providing that their sample value ranges do not overlap. Quantitative
     * categories can map sample values to geophysics values using arbitrary relation
     * (not necessarly linear).
     *
     * @param description
     *            The sample dimension title or description, or {@code null} for the default
     *            (the name of what looks like the "main" category). This is the value to be
     *            returned by {@link #getDescription}.
     * @param categories
     *            The list of categories.
     * @param units
     *            The unit information for this sample dimension. May be {@code null} if
     *            no category has units. This unit apply to values obtained after the
     *            {@link #getSampleToGeophysics sampleToGeophysics} transformation.
     * @throws IllegalArgumentException
     *             if {@code categories} contains incompatible categories. If
     *             may be the case for example if two or more categories have
     *             overlapping ranges of sample values.
     *
     * @since 2.3
     */
    public GridSampleDimension(CharSequence description, Category[] categories, Unit<?> units)
            throws IllegalArgumentException
    {
        // TODO: 'list(...)' should be inlined there if only Sun was to fix RFE #4093999
        // ("Relax constraint on placement of this()/super() call in constructors").
        this(description, list(categories, units));
    }

    /** Constructs a list of categories. Used by constructors only. */
    private static CategoryList list(final Category[] categories, final Unit<?> units) {
        if (categories == null || categories.length == 0) {
            return null;
        }
        final CategoryList list = new CategoryList(categories, units);
        if (CategoryList.isGeophysics(categories, false)) return list;
        if (CategoryList.isGeophysics(categories, true )) return list.inverse;
        throw new IllegalArgumentException(Errors.format(ErrorKeys.MIXED_CATEGORIES));
    }

    /**
     * Constructs a sample dimension with the specified list of categories.
     *
     * @param description
     *            The sample dimension title or description, or {@code null} for the default
     *            (the name of what looks like the "main" category). This is the value to be
     *            returned by {@link #getDescription}.
     * @param list
     *            The list of categories, or {@code null}.
     */
    private GridSampleDimension(final CharSequence description, final CategoryList list) {
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
        MathTransform1D main = null;
        boolean isMainValid = true;
        boolean qualitative = false;
        if (list != null) {
            for (int i=list.size(); --i >= 0;) {
                final MathTransform1D candidate = list.get(i).getSampleToGeophysics();
                if (candidate == null) {
                    qualitative = true;
                    continue;
                }
                if (main != null) {
                    isMainValid &= main.equals(candidate);
                }
                main = candidate;
            }
            this.isGeophysics = list.isGeophysics(true);
        } else {
            this.isGeophysics = false;
        }
        this.categories = list;
        this.hasQualitative = qualitative;
        this.hasQuantitative = (main != null);
        this.sampleToGeophysics = isMainValid ? main : null;
    }

    /**
     * Constructs a new sample dimension with the same categories and
     * units than the specified sample dimension.
     *
     * @param other The other sample dimension, or {@code null}.
     */
    protected GridSampleDimension(final GridSampleDimension other) {
        if (other != null) {
            inverse            = other.inverse;
            categories         = other.categories;
            isGeophysics       = other.isGeophysics;
            hasQualitative     = other.hasQualitative;
            hasQuantitative    = other.hasQuantitative;
            sampleToGeophysics = other.sampleToGeophysics;
            description        = other.description;
        } else {
            // 'inverse' will be set when needed.
            categories         = null;
            isGeophysics       = false;
            hasQualitative     = false;
            hasQuantitative    = false;
            sampleToGeophysics = null;
            description        = Vocabulary.formatInternational(VocabularyKeys.UNTITLED);
        }
    }

    /**
     * Wraps the specified OpenGIS's sample dimension into a Geotools's
     * implementation of {@code GridSampleDimension}.
     *
     * @param sd The sample dimension to wrap into a Geotools implementation.
     * @return The given sample dimension as a {@code GridSampleDimension} instance.
     */
    public static GridSampleDimension wrap(final SampleDimension sd) {
        if (sd instanceof GridSampleDimension) {
            return (GridSampleDimension) sd;
        }
        final int[][] palette = sd.getPalette();
        final Color[] colors;
        if (palette != null) {
            final int length = palette.length;
            colors = new Color[length];
            for (int i = 0; i < length; i++) {
                // Assuming RGB. It will be checked in the constructor.
                final int[] color = palette[i];
                colors[i] = new Color(color[0], color[1], color[2]);
            }
        } else {
            colors = null;
        }
        return new GridSampleDimension(
                sd.getDescription(),
                sd.getSampleDimensionType(),
                sd.getColorInterpretation(), colors,
                sd.getCategoryNames(),
                sd.getNoDataValues(),
                sd.getMinimumValue(),
                sd.getMaximumValue(),
                sd.getScale(),
                sd.getOffset(),
                sd.getUnits());
    }

    /**
     * Returns a code value indicating grid value data type.
     * This will also indicate the number of bits for the data type.
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
     * Gets the sample dimension title or description.
     * This string may be {@code null} if no description is present.
     *
     * @return The title or description of this sample dimension.
     */
    public InternationalString getDescription() {
        return description;
    }

    /**
     * Returns a sequence of category names for the values contained in this sample dimension.
     * This allows for names to be assigned to numerical values. The first entry in the sequence
     * relates to a cell value of zero. For example:
     *
     *  <blockquote><pre>
     *    [0] Background
     *    [1] Water
     *    [2] Forest
     *    [3] Urban
     *  </pre></blockquote>
     *
     * @return The sequence of category names for the values contained in this sample dimension,
     *         or {@code null} if there is no category in this sample dimension.
     * @throws IllegalStateException if a sequence can't be mapped because some category use
     *         negative or non-integer sample values.
     *
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
        for (int i=categories.size(); --i>=0;) {
            final Category category = categories.get(i);
            final int lower = (int) category.minimum;
            final int upper = (int) category.maximum;
            if (lower != category.minimum || lower < 0 ||
                upper != category.maximum || upper < 0)
            {
                throw new IllegalStateException(Errors.format(ErrorKeys.NON_INTEGER_CATEGORY));
            }
            if (names == null) {
                names = new InternationalString[upper+1];
            }
            Arrays.fill(names, lower, upper+1, category.getName());
        }
        return names;
    }

    /**
     * Returns all categories in this sample dimension. Note that a {@link Category} object may
     * apply to an arbitrary range of sample values. Consequently, the first element in this
     * collection may not be directly related to the sample value {@code 0}.
     *
     * @return The list of categories in this sample dimension, or {@code null} if none.
     *
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
     * Returns the category for the specified sample value. If this method can't maps
     * a category to the specified value, then it returns {@code null}.
     *
     * @param  sample The value (can be one of {@code NaN} values).
     * @return The category for the supplied value, or {@code null} if none.
     *
     * @see #getCategories
     * @see #getCategoryNames
     */
    public Category getCategory(final double sample) {
        return (categories != null) ? categories.getCategory(sample) : null;
    }

    /**
     * Returns a default category to use for background. A background category is used
     * when an image is <A HREF="../gp/package-summary.html#Resample">resampled</A> (for
     * example reprojected in an other coordinate system) and the resampled image do not
     * fit in a rectangular area. It can also be used in various situation where a raisonable
     * "no data" category is needed. The default implementation try to returns one
     * of the {@linkplain #getNoDataValues no data values}. If no suitable category is found,
     * then a {@linkplain Category#NODATA default} one is returned.
     *
     * @return A category to use as background for the "Resample" operation. Never {@code null}.
     */
    public Category getBackground() {
        return (categories != null) ? categories.nodata : Category.NODATA;
    }

    /**
     * Returns the values to indicate "no data" for this sample dimension.  The default
     * implementation deduces the "no data" values from the list of categories supplied
     * at construction time. The rules are:
     *
     * <ul>
     *   <li>If {@link #getSampleToGeophysics} returns {@code null}, then {@code getNoDataValues()}
     *       returns {@code null} as well. This means that this sample dimension contains no category
     *       or contains only qualitative categories (e.g. a band from a classified image).</li>
     *
     *   <li>If {@link #getSampleToGeophysics} returns an identity transform, then
     *       {@code getNoDataValues()} returns {@code null}. This means that sample value in this
     *       sample dimension are already expressed in geophysics values and that all "no data"
     *       values (if any) have already been converted into {@code NaN} values.</li>
     *
     *   <li>Otherwise, if there is at least one quantitative category, returns the sample values
     *       of all non-quantitative categories. For example if "Temperature" is a quantitative
     *       category and "Land" and "Cloud" are two qualitative categories, then sample values
     *       for "Land" and "Cloud" will be considered as "no data" values. "No data" values
     *       that are already {@code NaN} will be ignored.</li>
     * </ul>
     *
     * Together with {@link #getOffset()} and {@link #getScale()}, this method provides a limited
     * way to transform sample values into geophysics values. However, the recommended way is to
     * use the {@link #getSampleToGeophysics sampleToGeophysics} transform instead, which is more
     * general and take care of converting automatically "no data" values into {@code NaN}.
     *
     * @return The values to indicate no data values for this sample dimension,
     *         or {@code null} if not applicable.
     * @throws IllegalStateException if some qualitative categories use a range of
     *         non-integer values.
     *
     * @see #getSampleToGeophysics
     */
    public double[] getNoDataValues() throws IllegalStateException {
        if (!hasQuantitative) {
            return null;
        }
        int count = 0;
        double[] padValues = null;
        final int size = categories.size();
        for (int i=0; i<size; i++) {
            final Category category = categories.get(i);
            if (!category.isQuantitative()) {
                final double min = category.minimum;
                final double max = category.maximum;
                if (!Double.isNaN(min) || !Double.isNaN(max)) {
                    if (padValues == null) {
                        padValues = new double[size-i];
                    }
                    if (count >= padValues.length) {
                        padValues = XArray.resize(padValues, count*2);
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
                        if (lower!=min || upper!=max ||
                            !Classes.isInteger(category.getRange().getElementClass()))
                        {
                            throw new IllegalStateException(Errors.format(
                                      ErrorKeys.NON_INTEGER_CATEGORY));
                        }
                        final int requiredLength = count + (upper-lower);
                        if (requiredLength > padValues.length) {
                            padValues = XArray.resize(padValues, requiredLength*2);
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
            final double value = categories.get(0).minimum;
            if (!Double.isNaN(value)) {
                return value;
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
        if (categories != null) {
            for (int i=categories.size(); --i>=0;) {
                final double value = categories.get(i).maximum;
                if (!Double.isNaN(value)) {
                    return value;
                }
            }
        }
        return Double.POSITIVE_INFINITY;
    }

    /**
     * Returns the range of values in this sample dimension. This is the union of the range of
     * values of every categories, excluding {@code NaN} values. A {@link NumberRange} object
     * gives more informations than {@link #getMinimumValue} and {@link #getMaximumValue} methods
     * since it contains also the data type (integer, float, etc.) and inclusion/exclusion
     * informations.
     *
     * @return The range of values. May be {@code null} if this sample dimension has no
     *         quantitative category.
     *
     * @see Category#getRange
     * @see #getMinimumValue
     * @see #getMaximumValue
     *
     * @todo We should do a better job in {@code CategoryList.getRange()} when selecting the
     *       appropriate data type. {@link TypeMap#getSampleDimensionType} may be of some help.
     */
    public NumberRange<? extends Number> getRange() {
        return (categories != null) ? categories.getRange() : null;
    }

    /**
     * Returns {@code true} if at least one value of {@code values} is
     * in the range {@code lower} inclusive to {@code upper} exclusive.
     */
    private static boolean rangeContains(final double   lower,
                                         final double   upper,
                                         final double[] values)
    {
        if (values != null) {
            for (int i=0; i<values.length; i++) {
                final double v = values[i];
                if (v>=lower && v<upper) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns a string representation of a sample value. This method try to returns
     * a representation of the geophysics value; the transformation is automatically
     * applied when necessary. More specifically:
     *
     * <ul>
     *   <li>If {@code value} maps a qualitative category, then the
     *       category name is returned as of {@link Category#getName}.</li>
     *
     *   <li>Otherwise, if {@code value} maps a quantitative category, then the value is
     *       transformed into a geophysics value as with the {@link #getSampleToGeophysics()
     *       sampleToGeophysics} transform, the result is formatted as a number and the unit
     *       symbol is appened.</li>
     * </ul>
     *
     * @param  value  The sample value (can be one of {@code NaN} values).
     * @param  locale Locale to use for formatting, or {@code null} for the default locale.
     * @return A string representation of the geophysics value, or {@code null} if there is none.
     *
     * @todo What should we do when the value can't be formatted? {@code GridSampleDimension}
     *       returns {@code null} if there is no category or if an exception is thrown, but
     *       {@code CategoryList} returns "Untitled" if the value is an unknow NaN, and try
     *       to format the number anyway in other cases.
     */
    public String getLabel(final double value, final Locale locale) {
        if (categories != null) {
            if (isGeophysics) {
                return categories.format(value, locale);
            } else try {
                return categories.inverse.format(categories.transform(value), locale);
            } catch (TransformException exception) {
                // Value probably don't match a category. Ignore...
            }
        }
        return null;
    }

    /**
     * Returns the unit information for this sample dimension. May returns {@code null}
     * if this dimension has no units. This unit apply to values obtained after the
     * {@link #getSampleToGeophysics sampleToGeophysics} transformation.
     *
     * @see #getSampleToGeophysics
     */
    public Unit<?> getUnits() {
        return (categories != null) ? categories.geophysics(true).getUnits() : null;
    }

    /**
     * Returns the value to add to grid values for this sample dimension.
     * This attribute is typically used when the sample dimension represents
     * elevation data. The transformation equation is:
     *
     * <blockquote><pre>offset + scale*sample</pre></blockquote>
     *
     * Together with {@link #getScale()} and {@link #getNoDataValues()}, this method provides a
     * limited way to transform sample values into geophysics values. However, the recommended
     * way is to use the {@link #getSampleToGeophysics sampleToGeophysics} transform instead,
     * which is more general and take care of converting automatically "no data" values
     * into {@code NaN}.
     *
     * @return The offset to add to grid values.
     * @throws IllegalStateException if the transform from sample to geophysics values
     *         is not a linear relation.
     *
     * @see #getSampleToGeophysics
     * @see #rescale
     */
    public double getOffset() throws IllegalStateException {
        return getCoefficient(0);
    }

    /**
     * Returns the value which is multiplied to grid values for this sample dimension.
     * This attribute is typically used when the sample dimension represents elevation
     * data. The transformation equation is:
     *
     * <blockquote><pre>offset + scale*sample</pre></blockquote>
     *
     * Together with {@link #getOffset()} and {@link #getNoDataValues()}, this method provides a
     * limited way to transform sample values into geophysics values. However, the recommended
     * way is to use the {@link #getSampleToGeophysics sampleToGeophysics} transform instead,
     * which is more general and take care of converting automatically "no data" values
     * into {@code NaN}.
     *
     * @return The scale to multiply to grid value.
     * @throws IllegalStateException if the transform from sample to geophysics values
     *         is not a linear relation.
     *
     * @see #getSampleToGeophysics
     * @see #rescale
     */
    public double getScale() {
        return getCoefficient(1);
    }

    /**
     * Returns a coefficient of the linear transform from sample to geophysics values.
     *
     * @param  order The coefficient order (0 for the offset, or 1 for the scale factor,
     *         2 if we were going to implement quadratic relation, 3 for cubic, etc.).
     * @return The coefficient.
     * @throws IllegalStateException if the transform from sample to geophysics values
     *         is not a linear relation.
     */
    private double getCoefficient(final int order) throws IllegalStateException {
        if (!hasQuantitative) {
            // Default value for "offset" is 0; default value for "scale" is 1.
            // This is equal to the order if 0 <= order <= 1.
            return order;
        }
        Exception cause = null;
        if (sampleToGeophysics != null) try {
            final double value;
            switch (order) {
                case 0:  value = sampleToGeophysics.transform(0); break;
                case 1:  value = sampleToGeophysics.derivative(Double.NaN); break;
                default: throw new AssertionError(order); // Should not happen
            }
            if (!Double.isNaN(value)) {
                return value;
            }
        } catch (TransformException exception) {
            cause = exception;
        }
        throw new IllegalStateException(Errors.format(ErrorKeys.NON_LINEAR_RELATION), cause);
    }

    /**
     * Returns a transform from sample values to geophysics values. If this sample dimension
     * has no category, then this method returns {@code null}. If all sample values are
     * already geophysics values (including {@code NaN} for "no data" values), then this
     * method returns an identity transform. Otherwise, this method returns a transform expecting
     * sample values as input and computing geophysics value as output. This transform will take
     * care of converting all "{@linkplain #getNoDataValues() no data values}" into
     * {@code NaN} values.
     * <p>
     * The <code>sampleToGeophysics.{@linkplain MathTransform1D#inverse() inverse()}</code>
     * transform is capable to differenciate {@code NaN} values to get back the original
     * sample value.
     *
     * @return The transform from sample to geophysics values, or {@code null} if this
     *         sample dimension do not defines any transform (which is not the same that
     *         defining an identity transform).
     *
     * @see #getScale
     * @see #getOffset
     * @see #getNoDataValues
     * @see #rescale
     */
    public MathTransform1D getSampleToGeophysics() {
        if (isGeophysics) {
            return LinearTransform1D.IDENTITY;
        }
        if (!hasQualitative && sampleToGeophysics!=null) {
            // If there is only quantitative categories and they all use the same transform,
            // then we don't need the indirection level provided by CategoryList.
            return sampleToGeophysics;
        }
        // CategoryList is a MathTransform1D.
        return categories;
    }

    /**
     * Returns the {@linkplain org.geotools.coverage.grid.ViewType#GEOPHYSICS geophysics} or
     * {@linkplain org.geotools.coverage.grid.ViewType#PACKED packed} view of this sample dimension.
     * By definition, a <cite>geophysics sample dimension</cite> is a sample dimension with a
     * {@linkplain #getRange range of sample values} transformed in such a way that the
     * {@linkplain #getSampleToGeophysics sample to geophysics} transform is always the
     * {@linkplain MathTransform1D#isIdentity identity} transform, or {@code null} if no such
     * transform existed in the first place. In other words, the range of sample values in all
     * {@linkplain Category categories} maps directly the "<cite>real world</cite>" values
     * without the need for any transformation.
     * <p>
     * {@code GridSampleDimension} objects live by pair: a
     * {@linkplain org.geotools.coverage.grid.ViewType#GEOPHYSICS geophysics} one (used for
     * computation) and a {@linkplain org.geotools.coverage.grid.ViewType#PACKED packed} one
     * (used for storing data, usually as integers). The {@code geo} argument specifies which
     * object from the pair is wanted, regardless if this method is invoked on the geophysics or
     * packed instance of the pair.
     *
     * @param  geo {@code true} to get a sample dimension with an identity
     *         {@linkplain #getSampleToGeophysics transform} and a {@linkplain #getRange range of
     *         values} matching the {@linkplain org.geotools.coverage.grid.ViewType#GEOPHYSICS
     *         geophysics} values, or {@code false} to get back the
     *         {@linkplain org.geotools.coverage.grid.ViewType#PACKED packed} sample dimension.
     * @return The sample dimension. Never {@code null}, but may be {@code this}.
     *
     * @see Category#geophysics
     * @see org.geotools.coverage.grid.GridCoverage2D#view
     */
    public GridSampleDimension geophysics(final boolean geo) {
        if (geo == isGeophysics) {
            return this;
        }
        if (inverse == null) {
            if (categories != null) {
                inverse = new GridSampleDimension(description, categories.inverse);
                inverse.inverse = this;
            } else {
                /*
                 * If there is no categories, then there is no real difference between geophysics
                 * and packed sample dimensions. Both kinds of sample dimensions would be identical
                 * objects, so we are better to just returns 'this'.
                 */
                inverse = this;
            }
        }
        return inverse;
    }

    /**
     * Color palette associated with the sample dimension. A color palette can have any number of
     * colors. See palette interpretation for meaning of the palette entries. If the grid coverage
     * has no color palette, {@code null} will be returned.
     *
     * @return The color palette associated with the sample dimension.
     *
     * @see #getPaletteInterpretation
     * @see #getColorInterpretation
     * @see IndexColorModel
     *
     * @deprecated No replacement.
     */
    public int[][] getPalette() {
        final ColorModel color = getColorModel();
        if (color instanceof IndexColorModel) {
            final IndexColorModel cm = (IndexColorModel) color;
            final int[][] colors = new int[cm.getMapSize()][];
            for (int i=0; i<colors.length; i++) {
                colors[i] = new int[] {cm.getRed(i), cm.getGreen(i), cm.getBlue(i)};
            }
            return colors;
        }
        return null;
    }

    /**
     * Indicates the type of color palette entry for sample dimensions which have a
     * palette. If a sample dimension has a palette, the color interpretation must
     * be {@link ColorInterpretation#GRAY_INDEX GRAY_INDEX}
     * or {@link ColorInterpretation#PALETTE_INDEX PALETTE_INDEX}.
     * A palette entry type can be Gray, RGB, CMYK or HLS.
     *
     * @return The type of color palette entry for sample dimensions which have a palette.
     *
     */
    public PaletteInterpretation getPaletteInterpretation() {
        return PaletteInterpretation.RGB;
    }

    /**
     * Returns the color interpretation of the sample dimension.
     * A sample dimension can be an index into a color palette or be a color model
     * component. If the sample dimension is not assigned a color interpretation
     * the value is {@link ColorInterpretation#UNDEFINED}.
     *
     */
    public ColorInterpretation getColorInterpretation() {
        // The 'Grid2DSampleDimension' class overrides this method
        // with better values for 'band' and 'numBands' constants.
        final int band     = 0;
        final int numBands = 1;
        return TypeMap.getColorInterpretation(getColorModel(band, numBands), band);
    }

    /**
     * Returns a color model for this sample dimension. The default implementation create a color
     * model with 1 band using each category's colors as returned by {@link Category#getColors}.
     * The returned color model will typically use {@link DataBuffer#TYPE_FLOAT} if this sample
     * dimension is {@linkplain org.geotools.coverage.grid.ViewType#GEOPHYSICS geophysics}, or
     * an integer data type otherwise.
     * <p>
     * Note that {@link org.geotools.coverage.grid.GridCoverage2D#getSampleDimension} returns
     * special implementations of {@code GridSampleDimension}. In this particular case,
     * the color model created by this {@code getColorModel()} method will have the same number of
     * bands than the grid coverage's {@link java.awt.image.RenderedImage}.
     *
     * @return The requested color model, suitable for {@link java.awt.image.RenderedImage} objects
     *         with values in the <code>{@link #getRange}</code> range. May be {@code null} if this
     *         sample dimension has no category.
     */
    public ColorModel getColorModel() {
        // The 'Grid2DSampleDimension' class overrides this method
        // with better values for 'band' and 'numBands' constants.
        final int band     = 0;
        final int numBands = 1;
        return getColorModel(band, numBands);
    }

    /**
     * Returns a color model for this sample dimension. The default implementation create the
     * color model using each category's colors as returned by {@link Category#getColors}. The
     * returned color model will typically use {@link DataBuffer#TYPE_FLOAT} if this sample
     * dimension is {@linkplain org.geotools.coverage.grid.ViewType#GEOPHYSICS geophysics},
     * or an integer data type otherwise.
     *
     * @param  visibleBand The band to be made visible (usually 0). All other bands, if any
     *         will be ignored.
     * @param  numBands The number of bands for the color model (usually 1). The returned color
     *         model will renderer only the {@code visibleBand} and ignore the others, but
     *         the existence of all {@code numBands} will be at least tolerated. Supplemental
     *         bands, even invisible, are useful for processing with Java Advanced Imaging.
     * @return The requested color model, suitable for {@link java.awt.image.RenderedImage} objects
     *         with values in the <code>{@link #getRange}</code> range. May be {@code null} if this
     *         sample dimension has no category.
     *
     * @todo This method may be deprecated in a future version. It it strange to use
     *       only one {@code SampleDimension} object for creating a multi-bands color
     *       model. Logically, we would expect as many {@code SampleDimension}s as bands.
     */
    public ColorModel getColorModel(final int visibleBand, final int numBands) {
        if (categories != null) {
            if (isGeophysics && hasQualitative) {
                // Data likely to have NaN values, which require a floating point type.
                return categories.getColorModel(visibleBand, numBands, DataBuffer.TYPE_FLOAT);
            }
            return categories.getColorModel(visibleBand, numBands);
        }
        return null;
    }

    /**
     * Returns a color model for this sample dimension. The default implementation create the
     * color model using each category's colors as returned by {@link Category#getColors}.
     *
     * @param  visibleBand The band to be made visible (usually 0). All other bands, if any
     *         will be ignored.
     * @param  numBands The number of bands for the color model (usually 1). The returned color
     *         model will renderer only the {@code visibleBand} and ignore the others, but
     *         the existence of all {@code numBands} will be at least tolerated. Supplemental
     *         bands, even invisible, are useful for processing with Java Advanced Imaging.
     * @param  type The data type that has to be used for the sample model.
     * @return The requested color model, suitable for {@link java.awt.image.RenderedImage} objects
     *         with values in the <code>{@link #getRange}</code> range. May be {@code null} if this
     *         sample dimension has no category.
     *
     * @todo This method may be deprecated in a future version. It it strange to use
     *       only one {@code SampleDimension} object for creating a multi-bands color
     *       model. Logically, we would expect as many {@code SampleDimension}s as bands.
     */
    public ColorModel getColorModel(final int visibleBand, final int numBands, final int type) {
        if (categories != null) {
            return categories.getColorModel(visibleBand, numBands, type);
        }
        return null;
    }

    /**
     * Returns a sample dimension using new {@link #getScale scale} and {@link #getOffset offset}
     * coefficients. Other properties like the {@linkplain #getRange sample value range},
     * {@linkplain #getNoDataValues no data values} and {@linkplain #getColorModel colors}
     * are unchanged.
     *
     * @param scale  The value which is multiplied to grid values for the new sample dimension.
     * @param offset The value to add to grid values for the new sample dimension.
     * @return The scaled sample dimension.
     *
     * @see #getScale
     * @see #getOffset
     * @see Category#rescale
     */
    public GridSampleDimension rescale(final double scale, final double offset) {
        final MathTransform1D sampleToGeophysics = Category.createLinearTransform(scale, offset);
        final Category[] categories = (Category[]) getCategories().toArray();
        boolean changed = false;
        for (int i=0; i<categories.length; i++) {
            Category category = categories[i];
            if (category.isQuantitative()) {
                category = category.rescale(sampleToGeophysics);
            }
            category = category.geophysics(isGeophysics);
            if (!categories[i].equals(category)) {
                categories[i] = category;
                changed = true;
            }
        }
        return changed ? new GridSampleDimension(description, categories, getUnits()) : this;
    }

    /**
     * Returns a hash value for this sample dimension.
     * This value need not remain consistent between
     * different implementations of the same class.
     */
    @Override
    public int hashCode() {
        return (categories!=null) ? categories.hashCode() : (int) serialVersionUID;
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
            return Utilities.equals(this.categories, that.categories);
            // Since everything is deduced from CategoryList, two sample dimensions
            // should be equal if they have the same list of categories.
        }
        return false;
    }

    /**
     * Returns a string representation of this sample dimension.
     * This string is for debugging purpose only and may change
     * in future version. The default implementation format the
     * sample value range, then the list of categories. A "*"
     * mark is put in front of what seems the "main" category.
     */
    @Override
    public String toString() {
        if (categories != null) {
            return categories.toString(this, description);
        } else {
            return Classes.getShortClassName(this) + "[\"" + description + "\"]";
        }
    }

    /////////////////////////////////////////////////////////////////////////////////
    ////////                                                                 ////////
    ////////        REGISTRATION OF "SampleTranscode" IMAGE OPERATION        ////////
    ////////                                                                 ////////
    /////////////////////////////////////////////////////////////////////////////////

    /**
     * Register the "SampleTranscode" image operation.
     * Registration is done when the class is first loaded.
     *
     * @todo This static initializer will imply immediate class loading of a lot of
     *       JAI dependencies.  This is a pretty high overhead if JAI is not wanted
     *       right now. The correct approach is to declare the image operation into
     *       the {@code META-INF/registryFile.jai} file, which is automatically
     *       parsed during JAI initialization. Unfortunatly, it can't access private
     *       classes and we don't want to make our registration classes public. We
     *       can't move our registration classes into a hidden "resources" package
     *       neither because we need package-private access to {@code CategoryList}.
     *       For now, we assume that people using the GC package probably want to work
     *       with {@link org.geotools.coverage.grid.GridCoverage2D}, which make extensive
     *       use of JAI. Peoples just working with {@link org.geotools.coverage.Coverage} are
     *       stuck with the overhead. Note that we register the image operation here because
     *       the only operation's argument is of type {@code GridSampleDimension[]}.
     *       Consequently, the image operation may be invoked at any time after class
     *       loading of {@link GridSampleDimension}.
     *       <p>
     *       Additional note: moving the initialization into the
     *       {@code META-INF/registryFile.jai} file may not be the best idea neithter,
     *       since peoples using JAI without the GCS module may be stuck with the overhead
     *       of loading GC classes.
     */
    static {
        SampleTranscoder.register(JAI.getDefaultInstance());
    }
}
